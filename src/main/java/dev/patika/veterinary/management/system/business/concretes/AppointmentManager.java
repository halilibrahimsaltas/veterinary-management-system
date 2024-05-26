package dev.patika.veterinary.management.system.business.concretes;

import dev.patika.veterinary.management.system.business.abstracts.AppointmentService;
import dev.patika.veterinary.management.system.core.exception.AppointmentException;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.dao.AnimalRepo;
import dev.patika.veterinary.management.system.dao.AppointmentRepo;
import dev.patika.veterinary.management.system.dao.AvailableDateRepo;
import dev.patika.veterinary.management.system.dao.DoctorRepo;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Appointment;
import dev.patika.veterinary.management.system.entities.AvailableDate;
import dev.patika.veterinary.management.system.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentManager  implements AppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final AvailableDateRepo availableDateRepo;
    private final DoctorRepo doctorRepo;
    private final AnimalRepo animalRepo;

    public AppointmentManager(AppointmentRepo appointmentRepo, AvailableDateRepo availableDateRepo, DoctorRepo doctorRepo, AnimalRepo animalRepo) {
        this.appointmentRepo = appointmentRepo;
        this.availableDateRepo = availableDateRepo;
        this.doctorRepo = doctorRepo;
        this.animalRepo = animalRepo;
    }

    @Override
    public Appointment getById(long id) {
        return this.appointmentRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override// Validate the appointment time and doctor's availability
    public Appointment save(Appointment appointment,Doctor doctor) {
        LocalDateTime appointmentDate = appointment.getAppointmentDate();


        if (appointmentDate.getMinute() != 0 || appointmentDate.getSecond() != 0) {
            throw new AppointmentException("Appointments can only be made every hour!");
        }


        if (!this.isDoctorAvailable(doctor,appointmentDate)) {
            throw new AppointmentException("The doctor is not working on this date!");
        }

        if(this.hasAppointmentAtGivenTime(appointmentDate,doctor)) {
            throw new AppointmentException("Another appointment is available at the entered time.");

        }

        return appointmentRepo.save(appointment);
    }



    @Override // Fetch appointments by date range and doctor ID
    public List<Appointment> getAppointmentsByDateRangeAndDoctor(LocalDateTime startDate, LocalDateTime endDate, long doctorId) {
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new AppointmentException("Doctor not found"));
        return appointmentRepo.findByAppointmentDateBetweenAndDoctor(startDate, endDate, doctor);
    }

    @Override // Fetch appointments by date range and animal ID
    public List<Appointment> getAppointmentsByDateRangeAndAnimal(LocalDateTime startDate, LocalDateTime endDate, long animalId) {
        Animal animal = animalRepo.findById(animalId).orElseThrow(() -> new AppointmentException("Animal not found"));
        return appointmentRepo.findByAppointmentDateBetweenAndAnimal(startDate, endDate, animal);
    }

    @Override
    public Appointment update(Appointment appointment) {
        this.getById(appointment.getId());
        return this.appointmentRepo.save(appointment);
    }

    @Override
    public Page<Appointment> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.appointmentRepo.findAll(pageable);
    }

    @Override
    public boolean delete(long id) {
        Appointment appointment= this.getById(id);
        this.appointmentRepo.delete(appointment);
        return true;
    }
    @Override// Check if doctor is available on the given date
    public boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime) {
        LocalDate appointmentDate = dateTime.toLocalDate();
        List<AvailableDate> availableDates = availableDateRepo.findByDoctorId(doctor.getId());

        return availableDates.stream()
                .anyMatch(availableDate -> availableDate.getAvailableDate().equals(appointmentDate));
    }

    @Override// Check if there is already an appointment at the given time
    public boolean hasAppointmentAtGivenTime(LocalDateTime date, Doctor doctor) {
        List<Appointment> appointments = this.appointmentRepo.findByDoctorId(doctor.getId());

        return appointments.stream().anyMatch(appointment -> appointment.getAppointmentDate().equals(date));
    }
}

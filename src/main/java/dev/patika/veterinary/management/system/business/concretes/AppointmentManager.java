package dev.patika.veterinary.management.system.business.concretes;

import dev.patika.veterinary.management.system.business.abstracts.AppointmentService;
import dev.patika.veterinary.management.system.core.exception.AppointmentException;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.dao.AppointmentRepo;
import dev.patika.veterinary.management.system.dao.AvailableDateRepo;
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

    private  final AppointmentRepo appointmentRepo;

    private final AvailableDateRepo availableDateRepo;

    public AppointmentManager(AppointmentRepo appointmentRepo, AvailableDateRepo availableDateRepo) {
        this.appointmentRepo = appointmentRepo;
        this.availableDateRepo = availableDateRepo;
    }

    @Override
    public Appointment getById(long id) {
        return this.appointmentRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Appointment save(Appointment appointment) throws AppointmentException {
        Doctor doctor = appointment.getDoctor();
        LocalDate appointmentDate = appointment.getAppointmentDate().toLocalDate();

        List<AvailableDate> availableDates = availableDateRepo.findByDoctorAndAvailableDate(doctor, appointmentDate);
        if (availableDates.isEmpty()) {
            throw new AppointmentException("The doctor is not working on this date!");
        }

        List<Appointment> doctorAppointments = appointmentRepo.findByAppointmentDateBetweenAndDoctor(
                appointmentDate.atStartOfDay(), appointmentDate.atTime(23, 59), doctor.getId());

        for (Appointment existingAppointment : doctorAppointments) {
            if (existingAppointment.getAppointmentDate().isEqual(appointment.getAppointmentDate())) {
                throw new AppointmentException("Another appointment is available at the entered time.");
            }
        }

        return appointmentRepo.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    @Override
    public List<Appointment> getAppointmentsByDateRangeAndDoctor(LocalDateTime startDate, LocalDateTime endDate, long doctorId) {
        return appointmentRepo.findByAppointmentDateBetweenAndDoctor(startDate, endDate, doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByDateRangeAndAnimal(LocalDateTime startDate, LocalDateTime endDate, long animalId) {
        return appointmentRepo.findByAppointmentDateBetweenAndAnimal(startDate, endDate, animalId);
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
    @Override
    public boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime) {
        LocalDate appointmentDate = dateTime.toLocalDate();

        // Check if the doctor is available on the given date
        List<AvailableDate> availableDates = availableDateRepo.findByDoctorAndAvailableDate(doctor, appointmentDate);
        if (availableDates.isEmpty()) {
            return false;
        }

        // Check if the doctor has another appointment at the given time
        List<Appointment> doctorAppointments = appointmentRepo.findByAppointmentDateBetweenAndDoctor(
                appointmentDate.atStartOfDay(), appointmentDate.atTime(23, 59), doctor.getId());

        for (Appointment existingAppointment : doctorAppointments) {
            if (existingAppointment.getAppointmentDate().isEqual(dateTime)) {
                return false;
            }
        }

        return true;
    }
}

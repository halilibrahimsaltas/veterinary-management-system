package dev.patika.veterinary.management.system.business.abstracts;


import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Appointment;
import dev.patika.veterinary.management.system.entities.Doctor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Appointment getById(long id);
    Appointment save(Appointment appointment);

    List<Appointment> getAppointmentsByDateRangeAndDoctor(LocalDateTime startDate, LocalDateTime endDate, long doctorId );

    List<Appointment> getAppointmentsByDateRangeAndAnimal(LocalDateTime startDate, LocalDateTime endDate, long animalId);
    Appointment update(Appointment appointment);
    Page<Appointment> cursor(int page, int pageSize);
    boolean delete(long id);

    boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime);
}

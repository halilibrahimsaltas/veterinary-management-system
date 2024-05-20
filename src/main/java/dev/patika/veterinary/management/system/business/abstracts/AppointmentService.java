package dev.patika.veterinary.management.system.business.abstracts;


import dev.patika.veterinary.management.system.entities.Appointment;
import org.springframework.data.domain.Page;

public interface AppointmentService {
    Appointment getById(long id);
    Appointment save(Appointment appointment);
    Appointment update(Appointment appointment);
    Page<Appointment> cursor(int page, int pageSize);
    boolean delete(long id);
}

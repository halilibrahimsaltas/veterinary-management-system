package dev.patika.veterinary.management.system.business.abstracts;


import dev.patika.veterinary.management.system.entities.Doctor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface DoctorService {

    Doctor getById(long id);
    Doctor save(Doctor doctor);
    Doctor update(Doctor doctor);
    Page<Doctor> cursor(int page, int pageSize);
    boolean delete(long id);
    boolean isAvailable(LocalDateTime date, Doctor doctor);
}

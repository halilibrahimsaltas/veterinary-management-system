package dev.patika.veterinary.management.system.business.abstracts;


import dev.patika.veterinary.management.system.entities.Vaccine;
import org.springframework.data.domain.Page;

public interface VaccineService {

    Vaccine getById(long id);
    Vaccine save(Vaccine vaccine);
    Vaccine update(Vaccine vaccine);
    Page<Vaccine> cursor(int page, int pageSize);
    boolean delete(long id);
}

package dev.patika.veterinary.management.system.business.abstracts;


import dev.patika.veterinary.management.system.entities.Vaccine;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface VaccineService {

    Vaccine getById(long id);
    Vaccine save(Vaccine vaccine);

    List<Vaccine> getVaccinesByAnimalId(long animalId);
    List<Vaccine> getVaccinesByProtectionFinishDateRange(LocalDate startDate, LocalDate endDate);
    Vaccine update(Vaccine vaccine);
    Page<Vaccine> cursor(int page, int pageSize);
    boolean delete(long id);
}

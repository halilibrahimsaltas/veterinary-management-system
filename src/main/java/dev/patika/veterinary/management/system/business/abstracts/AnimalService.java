package dev.patika.veterinary.management.system.business.abstracts;


import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Customer;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;


public interface AnimalService {

    Animal getById(long id);
    Animal save(Animal animal);
    Animal update(Animal animal);
    List<Animal> findByCustomerId(long customerId);
    List<Animal> filterAnimalsByName(String name);
    List<Animal> getAnimalsByVaccinationRange(LocalDate startDate, LocalDate endDate);
    Page<Animal> cursor(int page, int pageSize);
    boolean delete(long id);
}

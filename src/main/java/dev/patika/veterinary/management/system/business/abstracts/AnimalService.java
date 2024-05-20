package dev.patika.veterinary.management.system.business.abstracts;


import dev.patika.veterinary.management.system.entities.Animal;
import org.springframework.data.domain.Page;



public interface AnimalService {

    Animal getById(long id);
    Animal save(Animal animal);
    Animal update(Animal animal);
    Page<Animal> cursor(int page, int pageSize);
    boolean delete(long id);
}

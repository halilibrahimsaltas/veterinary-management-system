package dev.patika.veterinary.management.system.dao;

import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepo extends JpaRepository<Animal, Long> {
    List<Animal> findByNameContainingIgnoreCase(String name);
    List<Animal> findByCustomerId(Long customerId);
}

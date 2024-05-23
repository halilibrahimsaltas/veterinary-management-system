package dev.patika.veterinary.management.system.dao;

import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnimalRepo extends JpaRepository<Animal, Long> {
    List<Animal> findByNameContainingIgnoreCase(String name);
    List<Animal> findByCustomerId(Long customerId);
    List<Animal> findByVaccinesProtectionFinishDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Animal a LEFT JOIN FETCH a.customer WHERE a.id = :id")
    List<Animal> findByIdWithCustomer(@Param("id") long id);
}

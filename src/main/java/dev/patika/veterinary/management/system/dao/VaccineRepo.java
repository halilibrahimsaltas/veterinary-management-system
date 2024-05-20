package dev.patika.veterinary.management.system.dao;

import dev.patika.veterinary.management.system.entities.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine, Long> {
}

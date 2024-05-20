package dev.patika.veterinary.management.system.dao;

import dev.patika.veterinary.management.system.entities.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableDateRepo extends JpaRepository<AvailableDate, Long> {
}

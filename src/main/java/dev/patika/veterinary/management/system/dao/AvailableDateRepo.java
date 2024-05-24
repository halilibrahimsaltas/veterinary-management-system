package dev.patika.veterinary.management.system.dao;

import dev.patika.veterinary.management.system.entities.AvailableDate;
import dev.patika.veterinary.management.system.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailableDateRepo extends JpaRepository<AvailableDate, Long> {

    List<AvailableDate> findByDoctorId(Long doctorId);
}

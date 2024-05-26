package dev.patika.veterinary.management.system.business.concretes;

import dev.patika.veterinary.management.system.business.abstracts.AnimalService;
import dev.patika.veterinary.management.system.business.abstracts.DoctorService;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.dao.DoctorRepo;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DoctorManager  implements DoctorService {

    private  final DoctorRepo doctorRepo;

    public DoctorManager(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    @Override
    public Doctor getById(long id) {
        // Fetch doctor by ID, throw NotFoundException if not found
        return this.doctorRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Doctor save(Doctor doctor) {
        return this. doctorRepo.save(doctor);
    }

    @Override
    public Doctor update(Doctor doctor) {
        this.getById(doctor.getId());
        return this.doctorRepo.save(doctor);
    }

    @Override
    public Page<Doctor> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.doctorRepo.findAll(pageable);
    }

    @Override
    public boolean delete(long id) {
        Doctor doctor= this.getById(id);
        this.doctorRepo.delete(doctor);
        return true;
    }

    @Override
    public boolean isAvailable(LocalDateTime date, Doctor doctor) {
        LocalDate appointmentDate = date.toLocalDate();
        return doctor.getAvailableDates().contains(appointmentDate);

    }
}

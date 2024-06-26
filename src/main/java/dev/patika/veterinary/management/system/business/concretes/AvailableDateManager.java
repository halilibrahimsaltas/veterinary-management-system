package dev.patika.veterinary.management.system.business.concretes;

import dev.patika.veterinary.management.system.business.abstracts.AvailableDateService;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.dao.AvailableDateRepo;
import dev.patika.veterinary.management.system.dao.DoctorRepo;
import dev.patika.veterinary.management.system.entities.AvailableDate;
import dev.patika.veterinary.management.system.entities.Customer;
import dev.patika.veterinary.management.system.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.Doc;

@Service
public class AvailableDateManager  implements AvailableDateService {

    private  final AvailableDateRepo availableDateRepo;

    private final DoctorRepo doctorRepo;

    public AvailableDateManager(AvailableDateRepo availableDateRepo, DoctorRepo doctorRepo) {
        this.availableDateRepo = availableDateRepo;
        this.doctorRepo = doctorRepo;
    }

    @Override
    public AvailableDate getById(long id) {
        return this.availableDateRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public AvailableDate save(AvailableDate availableDate) {
        Doctor doctor = doctorRepo.findById(availableDate.getDoctor().getId())
                .orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
        availableDate.setId(null);
        availableDate.setDoctor(doctor);
        doctor.getAvailableDates().add(availableDate);
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public AvailableDate update(AvailableDate availableDate) {
        this.getById(availableDate.getId());
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public Page<AvailableDate> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.availableDateRepo.findAll(pageable);
    }

    @Override
    public boolean delete(long id) {
        AvailableDate availableDate= this.getById(id);
        this.availableDateRepo.delete(availableDate);
        return true;
    }
}

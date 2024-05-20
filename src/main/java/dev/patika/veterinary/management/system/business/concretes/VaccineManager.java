package dev.patika.veterinary.management.system.business.concretes;


import dev.patika.veterinary.management.system.business.abstracts.VaccineService;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.dao.VaccineRepo;
import dev.patika.veterinary.management.system.entities.Doctor;
import dev.patika.veterinary.management.system.entities.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VaccineManager implements VaccineService {

    private final VaccineRepo vaccineRepo;

    public VaccineManager(VaccineRepo vaccineRepo) {
        this.vaccineRepo = vaccineRepo;
    }

    @Override
    public Vaccine getById(long id) {
        return this.vaccineRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Vaccine save(Vaccine vaccine) {
        return this. vaccineRepo.save(vaccine);
    }

    @Override
    public Vaccine update(Vaccine vaccine) {
        this.getById(vaccine.getId());
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public Page<Vaccine> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.vaccineRepo.findAll(pageable);
    }

    @Override
    public boolean delete(long id) {
        Vaccine vaccine= this.getById(id);
        this.vaccineRepo.delete(vaccine);
        return true;
    }
}

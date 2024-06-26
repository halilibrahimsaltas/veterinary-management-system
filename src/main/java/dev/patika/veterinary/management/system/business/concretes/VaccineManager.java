package dev.patika.veterinary.management.system.business.concretes;


import dev.patika.veterinary.management.system.business.abstracts.VaccineService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.core.exception.VaccineException;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.dao.AnimalRepo;
import dev.patika.veterinary.management.system.dao.VaccineRepo;
import dev.patika.veterinary.management.system.dto.response.vaccine.VaccineAnimalResponse;
import dev.patika.veterinary.management.system.entities.Doctor;
import dev.patika.veterinary.management.system.entities.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VaccineManager implements VaccineService {

    private final VaccineRepo vaccineRepo;
    private  final ModelMapperService modelMapperService;

    private  final AnimalRepo animalRepo;

    public VaccineManager(VaccineRepo vaccineRepo, ModelMapperService modelMapperService, AnimalRepo animalRepo) {
        this.vaccineRepo = vaccineRepo;
        this.modelMapperService = modelMapperService;
        this.animalRepo = animalRepo;
    }

    @Override
    public Vaccine getById(long id) {
        return this.vaccineRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Vaccine save(Vaccine vaccine) throws VaccineException{
        // Check if a similar vaccine is still in effect
        List<Vaccine> existingVaccines = vaccineRepo.findByAnimalId(vaccine.getAnimal().getId());
        for (Vaccine v : existingVaccines) {
            if (v.getName().equals(vaccine.getName()) && v.getCode().equals(vaccine.getCode()) &&
                    v.getProtectionFinishDate().isAfter(LocalDate.now())) {
                throw new VaccineException("A similar vaccine is still in effect.");
            }
        }
        return vaccineRepo.save(vaccine);
    }

    @Override
    public List<Vaccine> getVaccinesByAnimalId(long animalId) {
        return vaccineRepo.findByAnimalId(animalId);
    }

    @Override
    public List<VaccineAnimalResponse> getVaccinesByProtectionFinishDateRange(LocalDate startDate, LocalDate endDate) {
        // Fetch vaccines by protection finish date range and map to response
        List<Vaccine> vaccineList = this.vaccineRepo.findByProtectionFinishDateBetween(startDate, endDate);
        return vaccineList.stream()
                .map(vaccine -> this.modelMapperService.forResponse()
                        .map(vaccine, VaccineAnimalResponse.class)).toList();
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

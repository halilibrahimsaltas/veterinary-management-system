package dev.patika.veterinary.management.system.business.concretes;

import dev.patika.veterinary.management.system.business.abstracts.AnimalService;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.dao.AnimalRepo;
import dev.patika.veterinary.management.system.dao.CustomerRepo;
import dev.patika.veterinary.management.system.dto.request.animal.AnimalSaveRequest;
import dev.patika.veterinary.management.system.dto.response.customer.CustomerResponse;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnimalManager  implements AnimalService {

    private final AnimalRepo animalRepo;

    private  final CustomerRepo customerRepo;


    public AnimalManager(AnimalRepo animalRepo, CustomerRepo customerRepo) {
        this.animalRepo = animalRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public Animal getById(long id) {
        List<Animal> animal = this.animalRepo.findByIdWithCustomer(id);
        return this.animalRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Animal save(Animal animal) {
        Customer customer = customerRepo.findById(animal.getCustomer().getId())
                .orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
        animal.setId(null);
        animal.setCustomer(customer);
        customer.getAnimals().add(animal);
        return this. animalRepo.save(animal);
    }

    @Override
    public Animal update(Animal animal) {
        this.getById(animal.getId());
        Customer customer = customerRepo.findById(animal.getCustomer().getId())
                .orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
        animal.setCustomer(customer);
        return this.animalRepo.save(animal);
    }

    @Override
    public List<Animal> findByCustomerId(long ownerId) {
        return this.animalRepo.findByCustomerId(ownerId);
    }

    @Override
    public List<Animal> filterAnimalsByName(String name) {
        return animalRepo.findByNameContainingIgnoreCase(name);
    }




    @Override
    public Page<Animal> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.animalRepo.findAll(pageable);
    }

    @Override
    public boolean delete(long id) {
        Animal animal= this.getById(id);
        this.animalRepo.delete(animal);
        return true;
    }

}

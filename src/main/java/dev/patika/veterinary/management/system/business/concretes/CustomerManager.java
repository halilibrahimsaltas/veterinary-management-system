package dev.patika.veterinary.management.system.business.concretes;

import dev.patika.veterinary.management.system.business.abstracts.CustomerService;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.dao.AnimalRepo;
import dev.patika.veterinary.management.system.dao.CustomerRepo;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Customer;
import dev.patika.veterinary.management.system.core.utils.Msg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerManager  implements CustomerService {

    private  final CustomerRepo customerRepo;

    private final AnimalRepo animalRepo;


    public CustomerManager(CustomerRepo customerRepo, AnimalRepo animalRepo) {
        this.customerRepo = customerRepo;
        this.animalRepo = animalRepo;
    }

    @Override
    public Customer getById(long id) {
        return this.customerRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Customer save(Customer customer) {
        return this.customerRepo.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        this.getById(customer.getId());
        return this.customerRepo.save(customer);
    }


    @Override
    public Optional<Customer> filterCustomersByName(String name) {
        // Filter customers by name (case insensitive)
        return customerRepo.findByNameContaining(name);
    }

    @Override
    public Page<Customer> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.customerRepo.findAll(pageable);
    }

    @Override
    public boolean delete(long id) {
        Customer customer= this.getById(id);
        this.customerRepo.delete(customer);
        return true;
    }

    @Override
    public List<Animal> getAllAnimalsByCustomerId(Long customerId) {
        Customer customer = this.customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException( customerId + "NOT FOUND" ));
        return this.animalRepo.findByCustomerId(customerId);
    }


}

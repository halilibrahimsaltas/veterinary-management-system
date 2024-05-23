package dev.patika.veterinary.management.system.business.abstracts;

import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Customer;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer getById(long id);
    Customer save(Customer customer);
    Customer update(Customer customer);
    Optional<Customer> filterCustomersByName(String name);
    Page<Customer> cursor(int page, int pageSize);
    boolean delete(long id);
    List<Animal> getAllAnimalsByCustomerId(Long customerId) ;
}

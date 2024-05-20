package dev.patika.veterinary.management.system.business.abstracts;

import dev.patika.veterinary.management.system.entities.Customer;
import org.springframework.data.domain.Page;



public interface CustomerService {
    Customer getById(long id);
    Customer save(Customer customer);
    Customer update(Customer customer);
    Page<Customer> cursor(int page, int pageSize);
    boolean delete(long id);
}

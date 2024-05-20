package dev.patika.veterinary.management.system.business.concretes;

import dev.patika.veterinary.management.system.business.abstracts.CustomerService;
import dev.patika.veterinary.management.system.dao.CustomerRepo;
import dev.patika.veterinary.management.system.entities.Customer;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CustomerManager  implements CustomerService {

    private  final CustomerRepo customerRepo;

    public CustomerManager(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Customer getId(long id) {
        return null;
                /*this.customerRepo.findById(id).orElseThrow(() -> new ChangeSetPersister.NotFoundException(Msg.NOT_FOUND));

                 */
    }



    @Override
    public Customer save(Customer customer) {
        return this.customerRepo.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public Page<Customer> cursor(int page, int pageSize) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}

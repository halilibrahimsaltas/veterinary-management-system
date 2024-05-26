package dev.patika.veterinary.management.system.controller;


import dev.patika.veterinary.management.system.business.abstracts.CustomerService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.customer.CustomerSaveRequest;
import dev.patika.veterinary.management.system.dto.request.customer.CustomerUpdateRequest;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import dev.patika.veterinary.management.system.dto.response.animal.AnimalResponse;
import dev.patika.veterinary.management.system.dto.response.customer.CustomerResponse;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Customer;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private  final ModelMapperService modelMapperService;

    private  final CustomerService customerService;

    public CustomerController(ModelMapperService modelMapperService, CustomerService customerService) {
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CustomerResponse> save (@Valid @RequestBody CustomerSaveRequest customerSaveRequest){
        Customer saveCustomer= this.modelMapperService.forRequest().map(customerSaveRequest,Customer.class);

        this.customerService.save(saveCustomer);
        return ResultHelper.created(this.modelMapperService.forResponse().map(saveCustomer,CustomerResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable ("id") long id){
        boolean isDeleted = this.customerService.delete(id);
        if (isDeleted) {
            return ResultHelper.successResult();
        } else {
            return ResultHelper.notFoundError(Msg.NOT_FOUND);
        }
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<CustomerResponse>> cursor(
            @RequestParam(name= "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "10") int pageSize
    )
    {
        Page<Customer> customerPage = this.customerService.cursor(page, pageSize);
        Page<CustomerResponse> customerResponsePage = customerPage
                .map(customer -> this.modelMapperService.forResponse().map(customer, CustomerResponse.class));

        return ResultHelper.cursor(customerResponsePage);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResultData<CustomerResponse> get (@PathVariable("id") long id){
        Customer customer= this.customerService.getById(id);
        return ResultHelper.success(this.modelMapperService.forResponse().map(customer,CustomerResponse.class));
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> update (@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest){
        Customer updateCustomer= this.modelMapperService.forRequest().map(customerUpdateRequest,Customer.class);
        this.customerService.update(updateCustomer);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateCustomer,CustomerResponse.class));
    }

    // Get animals by customer ID
    @GetMapping("/animals/{customerId}")
    public ResultData<List<AnimalResponse>> getAnimalsByCustomerId(@PathVariable ("customerId")Long customerId) {
        // Retrieve all animals associated with the given customer ID
        List<Animal> animals = customerService.getAllAnimalsByCustomerId(customerId);

        // Map animal entities to their respective response objects
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapperService.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(animalResponses);
    }


    // Filter customers by name
    @GetMapping("/filter/{name}")
    public ResultData<List<CustomerResponse>> filterCustomersByName(@PathVariable("name") String name) {
        // Retrieve customers whose name contains the provided string
        Optional<Customer> customers = customerService.filterCustomersByName(name);
        // Map customer entities to their respective response objects
        List<CustomerResponse> customerResponses = customers.stream().map(customer -> modelMapperService.forResponse().map(customer, CustomerResponse.class)).toList();
        return ResultHelper.success(customerResponses);
    }
}

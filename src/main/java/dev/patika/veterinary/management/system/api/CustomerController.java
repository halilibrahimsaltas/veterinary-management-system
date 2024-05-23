package dev.patika.veterinary.management.system.api;


import dev.patika.veterinary.management.system.business.abstracts.CustomerService;
import dev.patika.veterinary.management.system.business.concretes.CustomerManager;
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

    @GetMapping("/filter")
    public ResultData<List<CustomerResponse>> filterCustomersByName(@RequestParam String name) {
        Optional<Customer> customers = customerService.filterCustomersByName(name);
        List<CustomerResponse> customerResponses = customers.stream().map(customer -> modelMapperService.forResponse().map(customer, CustomerResponse.class)).toList();
        return ResultHelper.success(customerResponses);
    }
}

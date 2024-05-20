package dev.patika.veterinary.management.system.api;


import dev.patika.veterinary.management.system.business.abstracts.CustomerService;
import dev.patika.veterinary.management.system.business.concretes.CustomerManager;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.customer.CustomerSaveRequest;
import dev.patika.veterinary.management.system.dto.response.customer.CustomerResponse;
import dev.patika.veterinary.management.system.entities.Customer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        this.customerService.delete(id);
        return ResultHelper.successResult();
    }


}

package dev.patika.veterinary.management.system.controller;

import dev.patika.veterinary.management.system.business.abstracts.AnimalService;
import dev.patika.veterinary.management.system.business.abstracts.CustomerService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.animal.AnimalSaveRequest;
import dev.patika.veterinary.management.system.dto.request.animal.AnimalUpdateRequest;
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

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {

    private final AnimalService animalService;

    private final ModelMapperService modelMapperService;

    private CustomerService customerService;


    public AnimalController(AnimalService animalService, ModelMapperService modelMapperService, CustomerService customerService) {
        this.animalService = animalService;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
    }

    // Save Animal
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        Animal saveAnimal = this.modelMapperService.forRequest().map(animalSaveRequest, Animal.class);
        Customer customer=this.customerService.getById(animalSaveRequest.getCustomerId());
        CustomerResponse customerResponse=this.modelMapperService.forResponse().map(customer,CustomerResponse.class);
        saveAnimal.setCustomer(customer);
        this.animalService.save(saveAnimal);
        return ResultHelper.created(this.modelMapperService.forResponse().map(saveAnimal, AnimalResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") long id) {
        boolean isDeleted = this.animalService.delete(id);
        if (isDeleted) {
            return ResultHelper.successResult();
        } else {
            return ResultHelper.notFoundError(Msg.NOT_FOUND);
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AnimalResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<Animal> animalPage = this.animalService.cursor(page, pageSize);
        Page<AnimalResponse> animalResponsePage = animalPage
                .map(animal -> this.modelMapperService.forResponse().map(animal, AnimalResponse.class));

        return ResultHelper.cursor(animalResponsePage);
    }

    // Get Animal by ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> get(@PathVariable("id") long id) {
        Animal animal = this.animalService.getById(id);
        return ResultHelper.success(this.modelMapperService.forResponse().map(animal, AnimalResponse.class));
    }

    // Update Animal
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        Animal updateAnimal = this.modelMapperService.forRequest().map(animalUpdateRequest, Animal.class);
        this.animalService.update(updateAnimal);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAnimal, AnimalResponse.class));
    }


    // Find Animals by Customer ID
    @GetMapping("/byOwner/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsByOwnerId(@PathVariable("customerId") long customerId) {
        List<Animal> animals = animalService.findByCustomerId(customerId);
        return ResultHelper.success(animals.stream().map(animal -> modelMapperService.forResponse().map(animal, AnimalResponse.class)).toList());
    }

    // Filter Animals by Name
    @GetMapping("/byName/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> filterAnimalsByName(@PathVariable("name") String name) {
        List<Animal> animals = animalService.filterAnimalsByName(name);
        List<AnimalResponse> animalResponses = animals.stream().map(animal -> modelMapperService.forResponse().map(animal, AnimalResponse.class)).toList();
        return ResultHelper.success(animalResponses);
    }


}

package dev.patika.veterinary.management.system.controller;

import dev.patika.veterinary.management.system.business.abstracts.AnimalService;
import dev.patika.veterinary.management.system.business.abstracts.VaccineService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.vaccine.VaccineSaveRequest;
import dev.patika.veterinary.management.system.dto.request.vaccine.VaccineUpdateRequest;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import dev.patika.veterinary.management.system.dto.response.vaccine.VaccineAnimalResponse;
import dev.patika.veterinary.management.system.dto.response.vaccine.VaccineResponse;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Vaccine;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vaccines")
public class VaccineController {


    private  final VaccineService vaccineService;

    private  final ModelMapperService modelMapperService;

    private  final AnimalService animalService;

    public VaccineController(VaccineService vaccineService, ModelMapperService modelMapperService, AnimalService animalService) {
        this.vaccineService = vaccineService;
        this.modelMapperService = modelMapperService;
        this.animalService = animalService;
    }

    // Save a new vaccine
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save (@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest){
        // Map the request to a Vaccine entity
        Vaccine saveVaccine=this.modelMapperService.forRequest().map(vaccineSaveRequest,Vaccine.class);

        // Retrieve the animal associated with the provided ID and set it to the vaccine
        Animal animal=this.animalService.getById(vaccineSaveRequest.getAnimalId());
        saveVaccine.setAnimal(animal);

        // Save the vaccine
        this.vaccineService.save(saveVaccine);
        // Return the response with the saved vaccine details
        return ResultHelper.created(this.modelMapperService.forResponse().map(saveVaccine,VaccineResponse.class));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable ("id") long id){
        this.vaccineService.delete(id);
        return ResultHelper.successResult();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<VaccineResponse>> cursor(
            @RequestParam(name= "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "10") int pageSize
    )
    {
        Page<Vaccine> vaccinePage = this.vaccineService.cursor(page, pageSize);
        Page<VaccineResponse> vaccineResponsePage = vaccinePage
                .map(vaccine -> this.modelMapperService.forResponse().map(vaccine, VaccineResponse.class));

        return ResultHelper.cursor(vaccineResponsePage);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResultData<VaccineResponse> get (@PathVariable("id") long id){
        Vaccine vaccine= this.vaccineService.getById(id);
        return ResultHelper.success(this.modelMapperService.forResponse().map(vaccine,VaccineResponse.class));
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> update (@Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest){
        Vaccine updateVaccine= this.modelMapperService.forRequest().map(vaccineUpdateRequest,Vaccine.class);
        this.vaccineService.update(updateVaccine);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateVaccine,VaccineResponse.class));
    }


    // Retrieve a list of vaccines by the ID of the associated animal
    @GetMapping("/animal/{animalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalId(@PathVariable("animalId") long animalId) {
        // Retrieve vaccines associated with the provided animal ID
        List<Vaccine> vaccines = vaccineService.getVaccinesByAnimalId(animalId);
        // Map the vaccines to VaccineResponse objects
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> modelMapperService.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());
        // Return the response with the list of vaccines
        return ResultHelper.success(vaccineResponses);
    }

    // Retrieve a list of vaccines within a given date range
    @GetMapping("/expiring")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineAnimalResponse>> getVaccinesByProtectionFinishDateRange
            (@RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
             @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        {

            return ResultHelper.success(this.vaccineService.getVaccinesByProtectionFinishDateRange(startDate, endDate));
        }
    }
}

package dev.patika.veterinary.management.system.api;

import dev.patika.veterinary.management.system.business.abstracts.VaccineService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.doctor.DoctorSaveRequest;
import dev.patika.veterinary.management.system.dto.request.doctor.DoctorUpdateRequest;
import dev.patika.veterinary.management.system.dto.request.vaccine.VaccineSaveRequest;
import dev.patika.veterinary.management.system.dto.request.vaccine.VaccineUpdateRequest;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import dev.patika.veterinary.management.system.dto.response.animal.AnimalResponse;
import dev.patika.veterinary.management.system.dto.response.appointment.AppointmentResponse;
import dev.patika.veterinary.management.system.dto.response.doctor.DoctorResponse;
import dev.patika.veterinary.management.system.dto.response.vaccine.VaccineResponse;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Appointment;
import dev.patika.veterinary.management.system.entities.Doctor;
import dev.patika.veterinary.management.system.entities.Vaccine;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/vaccines")
public class VaccineController {


    private  final VaccineService vaccineService;

    private  final ModelMapperService modelMapperService;

    public VaccineController(VaccineService vaccineService, ModelMapperService modelMapperService) {
        this.vaccineService = vaccineService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save (@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest){
        Vaccine saveVaccine= this.modelMapperService.forRequest().map(vaccineSaveRequest,Vaccine.class);

        this.vaccineService.save(saveVaccine);
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


    @GetMapping("/animal/{animalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalId(@PathVariable long animalId) {
        List<Vaccine> vaccines = vaccineService.getVaccinesByAnimalId(animalId);
        return ResultHelper.success(vaccines.stream().map(vaccine -> modelMapperService.forResponse().map(vaccine, VaccineResponse.class)).toList());
    }

    @GetMapping("/expiring")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByProtectionFinishDateRange
            (@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Vaccine> vaccines = vaccineService.getVaccinesByProtectionFinishDateRange(startDate, endDate);
        List<VaccineResponse> vaccineResponses = vaccines.stream().map(vaccine -> modelMapperService.forResponse().map(vaccine, VaccineResponse.class)).toList();
        return ResultHelper.success(vaccineResponses);
    }
}

package dev.patika.veterinary.management.system.controller;

import dev.patika.veterinary.management.system.business.abstracts.DoctorService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.doctor.DoctorSaveRequest;
import dev.patika.veterinary.management.system.dto.request.doctor.DoctorUpdateRequest;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import dev.patika.veterinary.management.system.dto.response.doctor.DoctorResponse;
import dev.patika.veterinary.management.system.entities.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/doctors")
public class DoctorController {

    private  final DoctorService doctorService;

    private  final ModelMapperService modelMapperService;

    public DoctorController(DoctorService doctorService, ModelMapperService modelMapperService) {
        this.doctorService = doctorService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<DoctorResponse> save (@Valid @RequestBody DoctorSaveRequest doctorSaveRequest){
        Doctor saveDoctor= this.modelMapperService.forRequest().map(doctorSaveRequest,Doctor.class);

        this.doctorService.save(saveDoctor);
        return ResultHelper.created(this.modelMapperService.forResponse().map(saveDoctor,DoctorResponse.class));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable ("id") long id){
        this.doctorService.delete(id);
        return ResultHelper.successResult();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<DoctorResponse>> cursor(
            @RequestParam(name= "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "10") int pageSize
    )
    {
        Page<Doctor> doctorPage = this.doctorService.cursor(page, pageSize);
        Page<DoctorResponse> doctorResponsePage = doctorPage
                .map(doctor -> this.modelMapperService.forResponse().map(doctor, DoctorResponse.class));

        return ResultHelper.cursor(doctorResponsePage);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResultData<DoctorResponse> get (@PathVariable("id") long id){
        Doctor doctor= this.doctorService.getById(id);
        return ResultHelper.success(this.modelMapperService.forResponse().map(doctor,DoctorResponse.class));
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> update (@Valid @RequestBody DoctorUpdateRequest doctorUpdateRequest){
        Doctor updateDoctor= this.modelMapperService.forRequest().map(doctorUpdateRequest,Doctor.class);
        this.doctorService.update(updateDoctor);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateDoctor,DoctorResponse.class));
    }

}

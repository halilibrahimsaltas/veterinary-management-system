package dev.patika.veterinary.management.system.controller;


import dev.patika.veterinary.management.system.business.abstracts.AvailableDateService;
import dev.patika.veterinary.management.system.business.abstracts.DoctorService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.exception.AppointmentException;
import dev.patika.veterinary.management.system.core.exception.NotFoundException;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.availableDate.AvailableDateSaveRequest;
import dev.patika.veterinary.management.system.dto.request.availableDate.AvailableDateUpdateRequest;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import dev.patika.veterinary.management.system.dto.response.availableDate.AvailableDateResponse;
import dev.patika.veterinary.management.system.entities.AvailableDate;
import dev.patika.veterinary.management.system.entities.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/availableDates")
public class AvailableDateController {

    private  final AvailableDateService availableDateService;

    private  final ModelMapperService modelMapperService;

    private final DoctorService doctorService;

    public AvailableDateController(AvailableDateService availableDateService, ModelMapperService modelMapperService, DoctorService doctorService) {
        this.availableDateService = availableDateService;
        this.modelMapperService = modelMapperService;
        this.doctorService = doctorService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save (@Valid @RequestBody AvailableDateSaveRequest availableDateSaveRequest){
        AvailableDate saveDates = this.modelMapperService.forRequest().map(availableDateSaveRequest, AvailableDate.class);
        Doctor doctor = this.doctorService.getById(availableDateSaveRequest.getDoctorId());

        // Check if the doctor already has the same date available
        List<AvailableDate> existingDates = doctor.getAvailableDates();
        boolean isDateAvailable = existingDates.stream()
                .anyMatch(date -> date.getAvailableDate().isEqual(saveDates.getAvailableDate()));

        if (isDateAvailable) {
            // Throw an exception if the date is already available for the doctor
            throw new AppointmentException("Doctor already has this date available");
        }

        // Add the new available date to the doctor's list
        existingDates.add(saveDates);
        doctor.setAvailableDates(existingDates);
        saveDates.setDoctor(doctor);

        // Save the available date
        this.availableDateService.save(saveDates);

        // Return the response with the saved available date details
        return ResultHelper.created(this.modelMapperService.forResponse().map(saveDates, AvailableDateResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable ("id") long id){
        this.availableDateService.delete(id);
        return ResultHelper.successResult();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AvailableDateResponse>> cursor(
            @RequestParam(name= "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "10") int pageSize
    )
    {
        Page<AvailableDate> availableDatePage = this.availableDateService.cursor(page, pageSize);
        Page<AvailableDateResponse> availableDateResponsePage = availableDatePage
                .map(availableDate -> this.modelMapperService.forResponse().map(availableDate, AvailableDateResponse.class));

        return ResultHelper.cursor(availableDateResponsePage);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResultData<AvailableDateResponse> get (@PathVariable("id") long id){
        AvailableDate availableDate= this.availableDateService.getById(id);
        return ResultHelper.success(this.modelMapperService.forResponse().map(availableDate,AvailableDateResponse.class));
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update (@Valid @RequestBody AvailableDateUpdateRequest availableDateUpdateRequest){
        AvailableDate updateAvailableDate= this.modelMapperService.forRequest().map(availableDateUpdateRequest,AvailableDate.class);
        this.availableDateService.update(updateAvailableDate);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAvailableDate,AvailableDateResponse.class));
    }
}

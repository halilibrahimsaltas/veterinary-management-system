package dev.patika.veterinary.management.system.controller;

import dev.patika.veterinary.management.system.business.abstracts.AnimalService;
import dev.patika.veterinary.management.system.business.abstracts.AppointmentService;
import dev.patika.veterinary.management.system.business.abstracts.DoctorService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.appointment.AppointmentSaveRequest;
import dev.patika.veterinary.management.system.dto.request.appointment.AppointmentUpdateRequest;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import dev.patika.veterinary.management.system.dto.response.animal.AnimalResponse;
import dev.patika.veterinary.management.system.dto.response.appointment.AppointmentResponse;
import dev.patika.veterinary.management.system.dto.response.doctor.DoctorResponse;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Appointment;
import dev.patika.veterinary.management.system.entities.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private  final AppointmentService appointmentService;

    private  final ModelMapperService modelMapperService;

    private final AnimalService animalService;

    private final DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService,
                                 ModelMapperService modelMapperService,
                                 AnimalService animalService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.modelMapperService = modelMapperService;
        this.animalService = animalService;
        this.doctorService = doctorService;
    }

    // Create a new appointment
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest) {

        // Map request to appointment entity
        Appointment appointmentToSave = modelMapperService.forRequest().map(appointmentSaveRequest, Appointment.class);

        // Get animal and doctor by IDs
        Animal animal = this.animalService.getById(appointmentSaveRequest.getAnimalId());
        Doctor doctor = this.doctorService.getById(appointmentSaveRequest.getDoctorId());

        // Map animal and doctor entities to their respective response objects
        AnimalResponse animalResponse=this.modelMapperService.forResponse().map(animal,AnimalResponse.class);
        DoctorResponse doctorResponse=this.modelMapperService.forResponse().map(doctor,DoctorResponse.class);

        // Set animal and doctor for the appointment
        appointmentToSave.setAnimal(animal);
        appointmentToSave.setDoctor(doctor);

        // Save the appointment
        this.appointmentService.save(appointmentToSave, doctor);

        // Map saved appointment to response object
        AppointmentResponse response = this.modelMapperService.forResponse().map(appointmentToSave, AppointmentResponse.class);
        response.setAnimalResponse(animalResponse);
        response.setDoctorResponse(doctorResponse);
        return ResultHelper.created(response);
    }

    // Delete an appointment by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable ("id") long id){
        boolean isDeleted =this.appointmentService.delete(id);
        if (isDeleted) {
            return ResultHelper.successResult();
        } else {
            return ResultHelper.notFoundError(Msg.NOT_FOUND);
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AppointmentResponse>> cursor(
            @RequestParam(name= "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "10") int pageSize
    )
    {
        Page<Appointment> appointmentPage = this.appointmentService.cursor(page, pageSize);
        Page<AppointmentResponse> appointmentResponsePage = appointmentPage
                .map(appointment -> this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class));

        return ResultHelper.cursor(appointmentResponsePage);
    }

    // Get an appointment by ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResultData<AppointmentResponse> get (@PathVariable("id") long id){
        Appointment appointment= this.appointmentService.getById(id);
        return ResultHelper.success(this.modelMapperService.forResponse().map(appointment,AppointmentResponse.class));
    }
    // Update an appointment
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {
        Appointment appointmentToUpdate = modelMapperService.forRequest().map(appointmentUpdateRequest, Appointment.class);

        this.appointmentService.update(appointmentToUpdate);
        Appointment updatedAppointment = appointmentService.update(appointmentToUpdate);
        return ResultHelper.success(modelMapperService.forResponse().map(updatedAppointment, AppointmentResponse.class));
    }

    // Get appointments by date range and doctor
    @GetMapping("/byDateRangeAndDoctor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDateRangeAndDoctor(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam long doctorId) {
        // Get the doctor by ID
        Doctor doctor = this.doctorService.getById(doctorId);

        // Get appointments within the specified date range and doctor
        List<Appointment> appointments=this.appointmentService.getAppointmentsByDateRangeAndDoctor(startDate, endDate,doctorId);

        // Map appointments to appointment response objects
        List<AppointmentResponse> appointmentResponses=appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = new AppointmentResponse();
                    response.setId(appointment.getId());
                    response.setAppointmentDate(appointment.getAppointmentDate());

                    // Map doctor and animal entities to their respective response objects
                    DoctorResponse doctorResponse=this.modelMapperService.forResponse().map(appointment.getDoctor(),DoctorResponse.class);
                    AnimalResponse animalResponse=this.modelMapperService.forResponse().map(appointment.getAnimal(),AnimalResponse.class);
                    response.setDoctorResponse(doctorResponse);
                    response.setAnimalResponse(animalResponse);


                    return response;
                })
                .collect(Collectors.toList());
        return ResultHelper.success(appointmentResponses);
    }

    // Get appointments by date range and animal
    @GetMapping("/byDateRangeAndAnimal")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDateRangeAndAnimal(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate ,
            @RequestParam long animalId)

    {
        // Get the animal by ID
        Animal animal = this.animalService.getById(animalId);

        // Get appointments within the specified date range and animal
        List<Appointment> appointments=this.appointmentService.getAppointmentsByDateRangeAndAnimal(startDate, endDate,animalId);

        // Map appointments to appointment response objects
        List<AppointmentResponse> appointmentResponses=appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = new AppointmentResponse();
                    response.setId(appointment.getId());

                    // Map doctor and animal entities to their respective response objects
                    response.setAppointmentDate(appointment.getAppointmentDate());
                    DoctorResponse doctorResponse=this.modelMapperService.forResponse().map(appointment.getDoctor(),DoctorResponse.class);
                    AnimalResponse animalResponse=this.modelMapperService.forResponse().map(appointment.getAnimal(),AnimalResponse.class);
                    response.setDoctorResponse(doctorResponse);
                    response.setAnimalResponse(animalResponse);

                    return response;
                })
                .collect(Collectors.toList());

        return ResultHelper.success(appointmentResponses);
    }

}

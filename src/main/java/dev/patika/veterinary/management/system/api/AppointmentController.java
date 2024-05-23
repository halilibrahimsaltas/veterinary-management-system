package dev.patika.veterinary.management.system.api;

import dev.patika.veterinary.management.system.business.abstracts.AnimalService;
import dev.patika.veterinary.management.system.business.abstracts.AppointmentService;
import dev.patika.veterinary.management.system.business.abstracts.DoctorService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.exception.AppointmentException;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.Msg;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.appointment.AppointmentSaveRequest;
import dev.patika.veterinary.management.system.dto.request.appointment.AppointmentUpdateRequest;
import dev.patika.veterinary.management.system.dto.request.doctor.DoctorSaveRequest;
import dev.patika.veterinary.management.system.dto.request.doctor.DoctorUpdateRequest;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import dev.patika.veterinary.management.system.dto.response.animal.AnimalResponse;
import dev.patika.veterinary.management.system.dto.response.appointment.AppointmentResponse;
import dev.patika.veterinary.management.system.dto.response.doctor.DoctorResponse;
import dev.patika.veterinary.management.system.entities.Animal;
import dev.patika.veterinary.management.system.entities.Appointment;
import dev.patika.veterinary.management.system.entities.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest) {
        Appointment appointmentToSave = modelMapperService.forRequest().map(appointmentSaveRequest, Appointment.class);
        Animal animal = this.animalService.getById(appointmentSaveRequest.getAnimalId());
        Doctor doctor = this.doctorService.getById(appointmentSaveRequest.getDoctorId());

        AnimalResponse animalResponse=this.modelMapperService.forResponse().map(animal,AnimalResponse.class);
        DoctorResponse doctorResponse=this.modelMapperService.forResponse().map(doctor,DoctorResponse.class);

        appointmentToSave.setAnimal(animal);
        appointmentToSave.setDoctor(doctor);

        this.appointmentService.save(appointmentToSave, doctor);
        AppointmentResponse response = this.modelMapperService.forResponse().map(appointmentToSave, AppointmentResponse.class);

        // Check if the doctor is available at the requested time
        if (!appointmentService.isDoctorAvailable(appointmentToSave.getDoctor(), appointmentToSave.getAppointmentDate())) {
            throw new AppointmentException("The doctor is not available at the requested time.");
        }
        response.setAnimalResponse(animalResponse);
        response.setDoctorResponse(doctorResponse);
        return ResultHelper.created(response);
    }

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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResultData<AppointmentResponse> get (@PathVariable("id") long id){
        Appointment appointment= this.appointmentService.getById(id);
        return ResultHelper.success(this.modelMapperService.forResponse().map(appointment,AppointmentResponse.class));
    }
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {
        Appointment appointmentToUpdate = modelMapperService.forRequest().map(appointmentUpdateRequest, Appointment.class);

        // Check if the doctor is available at the requested time
        if (!appointmentService.isDoctorAvailable(appointmentToUpdate.getDoctor(), appointmentToUpdate.getAppointmentDate())) {
            throw new AppointmentException("The doctor is not available at the requested time.");
        }

        Appointment updatedAppointment = appointmentService.update(appointmentToUpdate);
        return ResultHelper.success(modelMapperService.forResponse().map(updatedAppointment, AppointmentResponse.class));
    }
    @GetMapping("/byDateRangeAndDoctor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDateRangeAndDoctor(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam long doctorId) {
        Doctor doctor = this.doctorService.getById(doctorId);

        List<Appointment> appointments=this.appointmentService.getAppointmentsByDateRangeAndDoctor(startDate, endDate,doctorId);
        List<AppointmentResponse> appointmentResponses=appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = new AppointmentResponse();
                    response.setId(appointment.getId());

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

    @GetMapping("/byDateRangeAndAnimal")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDateRangeAndAnimal(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate ,
            @RequestParam long animalId)

    {

        Animal animal = this.animalService.getById(animalId);
        List<Appointment> appointments=this.appointmentService.getAppointmentsByDateRangeAndAnimal(startDate, endDate,animalId);
        List<AppointmentResponse> appointmentResponses=appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = new AppointmentResponse();
                    response.setId(appointment.getId());


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

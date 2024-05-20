package dev.patika.veterinary.management.system.api;

import dev.patika.veterinary.management.system.business.abstracts.AppointmentService;
import dev.patika.veterinary.management.system.core.config.modelMapper.ModelMapperService;
import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.core.utils.ResultHelper;
import dev.patika.veterinary.management.system.dto.request.appointment.AppointmentSaveRequest;
import dev.patika.veterinary.management.system.dto.request.appointment.AppointmentUpdateRequest;
import dev.patika.veterinary.management.system.dto.request.doctor.DoctorSaveRequest;
import dev.patika.veterinary.management.system.dto.request.doctor.DoctorUpdateRequest;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import dev.patika.veterinary.management.system.dto.response.appointment.AppointmentResponse;
import dev.patika.veterinary.management.system.dto.response.doctor.DoctorResponse;
import dev.patika.veterinary.management.system.entities.Appointment;
import dev.patika.veterinary.management.system.entities.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private  final AppointmentService appointmentService;

    private  final ModelMapperService modelMapperService;

    public AppointmentController(AppointmentService appointmentService, ModelMapperService modelMapperService) {
        this.appointmentService = appointmentService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save (@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest){
        Appointment saveAppointment= this.modelMapperService.forRequest().map(appointmentSaveRequest,Appointment.class);

        this.appointmentService.save(saveAppointment);
        return ResultHelper.created(this.modelMapperService.forResponse().map(saveAppointment,AppointmentResponse.class));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable ("id") long id){
        this.appointmentService.delete(id);
        return ResultHelper.successResult();
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
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update (@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest){
        Appointment updateAppointment= this.modelMapperService.forRequest().map(appointmentUpdateRequest,Appointment.class);
        this.appointmentService.update(updateAppointment);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAppointment,AppointmentResponse.class));
    }

}

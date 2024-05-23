package dev.patika.veterinary.management.system.dto.request.appointment;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSaveRequest {


    @NotNull
    private LocalDate appointmentDate;


    private Long animalId;

    private Long doctorId;


}

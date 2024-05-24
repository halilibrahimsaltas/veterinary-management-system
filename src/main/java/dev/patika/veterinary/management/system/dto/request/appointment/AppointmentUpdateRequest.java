package dev.patika.veterinary.management.system.dto.request.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateRequest {
    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime appointmentDate;

    private Long animal_id;
    private Long doctorId;
}

package dev.patika.veterinary.management.system.dto.request.availableDate;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateSaveRequest {


    @NotNull
    private LocalDate availableDate;

    private Long doctorId;

}

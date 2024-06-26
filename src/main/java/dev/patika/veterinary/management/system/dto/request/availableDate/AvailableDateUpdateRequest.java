package dev.patika.veterinary.management.system.dto.request.availableDate;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateUpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private LocalDate availableDate;


}

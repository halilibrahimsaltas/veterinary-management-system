package dev.patika.veterinary.management.system.dto.response.availableDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateResponse {

    private LocalDate availableDate;
    private Long doctorId;
}

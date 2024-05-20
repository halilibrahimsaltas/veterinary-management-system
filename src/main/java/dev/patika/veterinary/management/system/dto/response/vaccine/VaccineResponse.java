package dev.patika.veterinary.management.system.dto.response.vaccine;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineResponse {

    private  String name;

    private  String code;


    private LocalDate protectionStartDate;


    private LocalDate protectionFinishDate;
}

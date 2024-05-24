package dev.patika.veterinary.management.system.dto.response.vaccine;

import dev.patika.veterinary.management.system.dto.response.animal.AnimalResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineAnimalResponse {

    private Long id;

    private  String name;

    private  String code;

    private LocalDate protectionStartDate;

    private LocalDate protectionFinishDate;

    private Long animalId;

}
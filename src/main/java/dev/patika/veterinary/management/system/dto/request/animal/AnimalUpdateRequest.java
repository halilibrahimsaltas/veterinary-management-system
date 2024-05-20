package dev.patika.veterinary.management.system.dto.request.animal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalUpdateRequest {
    @NotNull
    private  long id;

    @NotNull
    private  String name;

    private  String species;

    private  String breed;

    @NotNull
    private  String gender;

    private  String colour;

    private LocalDate dateOfBirth;

}

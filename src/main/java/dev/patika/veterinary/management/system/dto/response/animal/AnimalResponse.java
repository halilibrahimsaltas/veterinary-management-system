package dev.patika.veterinary.management.system.dto.response.animal;

import dev.patika.veterinary.management.system.entities.Customer;
import dev.patika.veterinary.management.system.entities.Doctor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalResponse {

    private  String name;

    private  String species;

    private  String breed;


    private  String gender;

    private  String colour;

    private LocalDate dateOfBirth;

    private Long customerId;


}

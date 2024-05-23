package dev.patika.veterinary.management.system.dto.response.appointment;

import lombok.AllArgsConstructor;
import dev.patika.veterinary.management.system.dto.response.animal.AnimalResponse;
import dev.patika.veterinary.management.system.dto.response.doctor.DoctorResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private  long id;

    private LocalDateTime appointmentDate;

    private AnimalResponse animalResponse;

    private DoctorResponse doctorResponse;
}

package dev.patika.veterinary.management.system.dto.request.doctor;



import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSaveRequest {

    @NotNull
    private  String name;

    private  String phone;

    private  String mail;

    private  String address;

    private  String city;

}

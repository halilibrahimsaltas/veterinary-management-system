package dev.patika.veterinary.management.system.dto.response.customer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private  Long id;
    private  String name;
    private  String phone;
    private  String mail;
    private  String address;
    private  String city;

}

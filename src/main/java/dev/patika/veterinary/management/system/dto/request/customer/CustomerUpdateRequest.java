package dev.patika.veterinary.management.system.dto.request.customer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequest {

    @NotNull
    private  Long id;

    @NotNull
    private  String name;

    @NotNull
    private  String phone;

    private  String mail;

    private  String address;

    private  String city;
}


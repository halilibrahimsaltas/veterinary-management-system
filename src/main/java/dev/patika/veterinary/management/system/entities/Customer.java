package dev.patika.veterinary.management.system.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name="customers")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id",columnDefinition = "serial")
    private Long id;

    @Column(name ="customer_name")
    @NotNull
    private String name;

    @Column(name ="customer_phone")
    @NotNull
    private String phone;

    @Column(name ="customer_mail")
    private String mail;

    @Column(name ="customer_address")
    private String address;

    @Column(name ="customer_city")
    private String city;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Animal> animals;


}

package dev.patika.veterinary.management.system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id",columnDefinition = "serial")
    private long id;

    @Column(name ="customer_name")
    @NotNull
    private  String name;
    @Column(name ="customer_phone")
    @NotNull
    private  String phone;
    @Column(name ="customer_mail")
    private  String mail;
    @Column(name ="customer_address")
    private  String address;
    @Column(name ="customer_city")
    private  String city;

    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Animal> animalList;





}

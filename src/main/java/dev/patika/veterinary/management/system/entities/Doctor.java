package dev.patika.veterinary.management.system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="doctors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="doctor_id",columnDefinition = "serial")
    private Long id;

    @Column(name ="doctor_name")
    @NotNull
    private  String name;
    @Column(name ="doctor_phone")
    @NotNull
    private  String phone;
    @Column(name ="doctor_mail")
    private  String mail;
    @Column(name ="doctor_address")
    private  String address;
    @Column(name ="doctor_city")
    private  String city;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<AvailableDate> availableDates;



}

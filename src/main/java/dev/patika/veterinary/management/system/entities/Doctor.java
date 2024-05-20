package dev.patika.veterinary.management.system.entities;

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
    private long id;

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

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "doctor2appointment",
            joinColumns = {@JoinColumn(name = "doctor2appointment_doctor_id")},
            inverseJoinColumns = {@JoinColumn (name= "doctor2appointment_appointment_id")}
    )
    private List<Appointment> appointmentsList;


    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "doctor2date",
            joinColumns = {@JoinColumn(name = "doctor2date_doctor_id")},
            inverseJoinColumns = {@JoinColumn (name= "doctor2date_availableDate_id")}
    )
    private List<AvailableDate> availableDateList;



}

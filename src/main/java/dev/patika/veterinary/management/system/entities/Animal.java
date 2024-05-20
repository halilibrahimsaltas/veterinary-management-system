package dev.patika.veterinary.management.system.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="animals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="animal_id",columnDefinition = "serial")
    private long id;

    @Column(name ="animal_name")
    @NotNull
    private  String name;
    @Column(name ="animal_species")
    private  String species;
    @Column(name ="animal_breed")
    private  String breed;
    @Column(name ="animal_gender")
    @NotNull
    private  String gender;
    @Column(name ="animal_colour")
    private  String colour;
    @Column(name ="animal_birth_date")
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name="animal_customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "animal2appointment",
            joinColumns = {@JoinColumn(name = "animal2appointment_animal_id")},
            inverseJoinColumns = {@JoinColumn (name= "animal2appointment_appointment_id")}
    )
    private List<Appointment> appointmentList;


    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "animal2vac",
            joinColumns = {@JoinColumn(name = "animal2vac_animal_id")},
            inverseJoinColumns = {@JoinColumn (name= "animal2vac_vaccine_id")}
    )
    private List<Vaccine> vaccineList;

}

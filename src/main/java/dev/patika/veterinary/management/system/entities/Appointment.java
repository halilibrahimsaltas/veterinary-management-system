package dev.patika.veterinary.management.system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="appointment_id",columnDefinition = "serial")
    private long id;

    @Column(name ="appointment_date")
    @NotNull
    private LocalDate appointmentDate;

    @ManyToMany(mappedBy = "appointmentList",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Animal> animalList;

    @ManyToMany(mappedBy = "appointmentsList",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Doctor> doctorList;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name="appointment_vaccine_id", referencedColumnName = "vaccine_id")
    private Vaccine vaccine;



}

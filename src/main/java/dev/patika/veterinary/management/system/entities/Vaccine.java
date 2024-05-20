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
@Table(name="vaccines")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vaccine_id",columnDefinition = "serial")
    private long id;

    @Column(name ="vaccine_name")
    @NotNull
    private  String name;
    @Column(name ="vaccine_code")
    @NotNull
    private  String code;
    @Column(name ="vaccine_protection_start_date")
    @NotNull
    private LocalDate protectionStartDate;
    @Column(name ="vaccine_protection_finish_date")
    @NotNull
    private LocalDate protectionFinishDate;

    @ManyToMany(mappedBy = "vaccineList",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Animal> animalList;

    @OneToMany(mappedBy = "vaccine",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Appointment> appointmentList;
}

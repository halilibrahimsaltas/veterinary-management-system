package dev.patika.veterinary.management.system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="availableDates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AvailableDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="availableDate_id",columnDefinition = "serial")
    private long id;

    @Column(name ="available_date")
    @NotNull
    private LocalDate availableDate;

    @ManyToMany(mappedBy = "availableDateList",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Doctor> doctorList;


}

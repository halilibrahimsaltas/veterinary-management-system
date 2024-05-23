package dev.patika.veterinary.management.system.business.abstracts;


import dev.patika.veterinary.management.system.entities.AvailableDate;
import org.springframework.data.domain.Page;

public interface AvailableDateService {

    AvailableDate getById(long id);
    AvailableDate save(AvailableDate availableDate);
    AvailableDate update(AvailableDate availableDate);
    Page<AvailableDate> cursor(int page, int pageSize);
    boolean delete(long id);
}

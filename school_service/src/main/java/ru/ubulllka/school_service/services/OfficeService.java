package ru.ubulllka.school_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ubulllka.school_service.models.Office;
import ru.ubulllka.school_service.repositories.OfficeRepository;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class OfficeService {
    private final OfficeRepository officeRep;
    public Iterable<Office> getAll(){
        return officeRep.findAll();
    }
    public Office getOne(int id) {
        Optional<Office> optional = officeRep.findById(id);
        if (optional.isEmpty()) return null;
        return optional.get();
    }
    public Office save(Office office){
        return officeRep.save(office);
    }
    public Office update(int id, Office office) {
        Optional<Office> optional = officeRep.findById(id);
        if (optional.isEmpty()) return null;
        office.setId(id);
        officeRep.save(office);
        return office;
    }
    public Boolean delete(int id) {
        Optional<Office> optional = officeRep.findById(id);
        if (optional.isEmpty()) return false;
        officeRep.delete(optional.get());
        return true;
    }
}

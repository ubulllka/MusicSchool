package ru.ubulllka.school_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ubulllka.school_service.models.Specialization;
import ru.ubulllka.school_service.repositories.PersonRepository;
import ru.ubulllka.school_service.repositories.SpecializationRepository;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecializationService {

    private final SpecializationRepository specializationRep;
    private final PersonRepository teacherRep;
    public Iterable<Specialization> getAll(){
        return specializationRep.findAll();
    }

    public Specialization getOne(int id) {
        Optional<Specialization> optional = specializationRep.findById(id);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    public Specialization save(Specialization specialization){
        return specializationRep.save(specialization);
    }

    public Specialization update(int id, Specialization specialization) {
        Optional<Specialization> optional = specializationRep.findById(id);
        if (optional.isEmpty()) {
            return null;
        }
        specialization.setId(id);
        specializationRep.save(specialization);
        return specialization;
    }

    public Boolean delete(int id) {
        Optional<Specialization> optional = specializationRep.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        Specialization specialization = optional.get();
        teacherRep.deleteAll(specialization.getTeachers());
        specializationRep.delete(specialization);

        return true;
    }
}



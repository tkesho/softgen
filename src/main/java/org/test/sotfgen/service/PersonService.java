package org.test.sotfgen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.PersonEntityDto;
import org.test.sotfgen.dto.PersonSearchParams;
import org.test.sotfgen.entity.PersonEntity;

@Service
public interface PersonService {

    Page<PersonEntity> getPersons(PersonSearchParams params, Pageable pageable);

    PersonEntity getPerson(Integer id);

    PersonEntity createPerson(PersonEntityDto person, Integer userId);

    PersonEntity updatePerson(PersonEntityDto person, Integer id);

    void deletePerson(Integer id);
}

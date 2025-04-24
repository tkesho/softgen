package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.PersonDto;
import org.test.sotfgen.entity.PersonEntity;
import org.test.sotfgen.dto.PersonDetailSearchParams;

@Service
public interface PersonService {

    Page<PersonEntity> getPersons(PersonDetailSearchParams params, Pageable pageable);

    PersonEntity getPerson(Integer id);

    PersonEntity createPerson(PersonDto person, Integer userId);

    PersonEntity updatePerson(PersonDto person);

    void deletePerson(Integer id);
}

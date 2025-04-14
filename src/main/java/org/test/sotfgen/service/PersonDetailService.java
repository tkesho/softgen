package org.test.sotfgen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.PersonDetailDto;
import org.test.sotfgen.dto.PersonDetailSearchParams;
import org.test.sotfgen.entity.PersonDetailEntity;

@Service
public interface PersonDetailService {

    Page<PersonDetailEntity> getPersons(PersonDetailSearchParams params, Pageable pageable);

    PersonDetailEntity getPerson(Integer id);

    PersonDetailEntity createPerson(PersonDetailDto person, Integer userId);

    PersonDetailEntity updatePerson(SecUser secUser, PersonDetailDto person, Integer id);

    void deletePerson(Integer id);
}

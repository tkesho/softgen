package org.test.sotfgen.service;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.person.PersonNotFoundException;
import org.test.sotfgen.dto.PersonEntityDto;
import org.test.sotfgen.dto.PersonSearchParams;
import org.test.sotfgen.entity.PersonEntity;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.PersonRepository;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final UserService userService;

    @Override
    public Page<PersonEntity> getPersons(PersonSearchParams params, Pageable pageable) {
        return personRepository.findAll((root, criteriaQuery, cb) -> getPredicate(params, root, cb), pageable);
    }

    @Override
    public PersonEntity getPerson(Integer id) {
        return getPersonById(id);
    }

    @Override
    @Transactional
    public PersonEntity createPerson(PersonEntityDto person, Integer userId) {
        PersonEntity personToCreate = new PersonEntity(person);
        UserEntity userToAttach = userService.getUserById(userId);
        personToCreate.setUser(userToAttach);
        return personRepository.save(personToCreate);
    }

    @Override
    @Transactional
    public PersonEntity updatePerson(PersonEntityDto person, Integer id) {
        PersonEntity personToUpdate = getPerson(id);
        updatePersonFields(person, personToUpdate);
        return personRepository.save(personToUpdate);
    }

    @Override
    @Transactional
    public void deletePerson(Integer id) {
        PersonEntity personToDelete = getPersonById(id);
        personRepository.delete(personToDelete);
    }

    private PersonEntity getPersonById(Integer id) {
        return personRepository.findByUserId(id).orElseThrow(() -> new PersonNotFoundException("person with id " + id + " not found"));
    }

    private void updatePersonFields(PersonEntityDto person, PersonEntity personToUpdate) {
        if (person.getFirstName() != null) {
            personToUpdate.setFirstName(person.getFirstName());
        }
        if (person.getLastName() != null) {
            personToUpdate.setLastName(person.getLastName());
        }
        if (person.getNationalId() != null) {
            personToUpdate.setNationalId(person.getNationalId());
        }
        if (person.getAddress() != null) {
            personToUpdate.setAddress(person.getAddress());
        }
        if (person.getPhoneNumber() != null) {
            personToUpdate.setPhoneNumber(person.getPhoneNumber());
        }
        if (person.getGender() != null) {
            personToUpdate.setGender(person.getGender());
        }
    }

    private Predicate getPredicate(PersonSearchParams params, Root<PersonEntity> root, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        if (StringUtils.isNotBlank(params.getFirstName())) {
            predicate = cb.and(predicate, cb.like(root.get("first_name"), "%" + params.getFirstName() + "%"));
        }
        if (StringUtils.isNotBlank(params.getLastName())) {
            predicate = cb.and(predicate, cb.like(root.get("last_name"), "%" + params.getLastName() + "%"));
        }
        if (StringUtils.isNotBlank(params.getNationalId())) {
            predicate = cb.and(predicate, cb.like(root.get("national_id"), "%" + params.getNationalId() + "%"));
        }
        if (StringUtils.isNotBlank(params.getAddress())) {
            predicate = cb.and(predicate, cb.like(root.get("address"), "%" + params.getAddress() + "%"));
        }
        if (StringUtils.isNotBlank(params.getPhoneNumber())) {
            predicate = cb.and(predicate, cb.like(root.get("phone_number"), "%" + params.getPhoneNumber() + "%"));
        }
        if (params.getGender() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("gender"), params.getGender()));
        }
        Join<PersonEntity, UserEntity> user = root.join("user", JoinType.LEFT);
        predicate = cb.and(predicate, cb.equal(user.get("active"), true));
        return predicate;
    }
}

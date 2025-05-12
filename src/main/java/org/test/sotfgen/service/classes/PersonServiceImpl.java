package org.test.sotfgen.service.classes;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.PersonDto;
import org.test.sotfgen.entity.PersonEntity;
import org.test.sotfgen.exceptions.PersonNotFoundException;
import org.test.sotfgen.mapper.PersonMapper;
import org.test.sotfgen.dto.PersonDetailSearchParams;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.PersonRepository;
import org.test.sotfgen.service.interfaces.PersonService;
import org.test.sotfgen.utils.UserUtil;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final UserUtil userUtil;
    private final PersonMapper personMapper;

    @Override
    public Page<PersonEntity> getPersons(PersonDetailSearchParams params, Pageable pageable) {
        return personRepository.findAll((root, criteriaQuery, cb) -> getPredicate(params, root, cb), pageable);
    }

    @Override
    public PersonEntity getPerson(Integer id) {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException("person with id " + id + " not found"));
    }

    @Override
    @Transactional
    public PersonEntity createPerson(PersonDto personDto, Integer userId) {
        PersonEntity personToCreate = personMapper.toEntity(personDto);
        UserEntity userToAttach = userUtil.getUserById(userId);
        personToCreate.setUser(userToAttach);
        return personRepository.save(personToCreate);
    }

    @Override
    @Transactional
    public PersonEntity updatePerson(PersonDto person) {
        UserEntity secUser = userUtil.getActingPrincipal();
        PersonEntity personToUpdate = getPersonByUserId(secUser.getId());
        personMapper.updateEntity(person, personToUpdate);
        return personRepository.save(personToUpdate);
    }

    @Override
    @Transactional
    public void deletePerson(Integer id) {
        PersonEntity personToDelete = getPersonByUserId(id);
        personRepository.delete(personToDelete);
    }

    private PersonEntity getPersonByUserId(Integer userId) {
        return personRepository.findByUserId(userId).orElseThrow(() -> new PersonNotFoundException("person with id " + userId + " not found"));
    }

    private Predicate getPredicate(PersonDetailSearchParams params, Root<PersonEntity> root, CriteriaBuilder cb) {
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

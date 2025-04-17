package org.test.sotfgen.service.classes;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.PersonNotFoundException;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.PersonDetailDto;
import org.test.sotfgen.dto.PersonDetailSearchParams;
import org.test.sotfgen.entity.PersonDetailEntity;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.mapper.PersonDetailMapper;
import org.test.sotfgen.repository.PersonDetailRepository;
import org.test.sotfgen.service.interfaces.PersonDetailService;
import org.test.sotfgen.utils.UserServiceUtil;

@Service
@RequiredArgsConstructor
public class PersonDetailServiceImpl implements PersonDetailService {

    private final PersonDetailRepository personDetailRepository;
    private final UserServiceUtil userServiceUtil;
    private final PersonDetailMapper personDetailMapper;

    @Override
    public Page<PersonDetailEntity> getPersons(PersonDetailSearchParams params, Pageable pageable) {
        return personDetailRepository.findAll((root, criteriaQuery, cb) -> getPredicate(params, root, cb), pageable);
    }

    @Override
    public PersonDetailEntity getPerson(Integer id) {
        return getPersonByUserId(id);
    }

    @Override
    @Transactional
    public PersonDetailEntity createPerson(PersonDetailDto personDto, Integer userId) {
        PersonDetailEntity personToCreate = personDetailMapper.personDetailDtoToPersonDetail(personDto);
        UserEntity userToAttach = userServiceUtil.getUserById(userId);
        personToCreate.setUser(userToAttach);
        return personDetailRepository.save(personToCreate);
    }

    @Override
    @Transactional
    public PersonDetailEntity updatePerson(SecUser secUser, PersonDetailDto person, Integer id) {
        PersonDetailEntity personToUpdate = getPerson(id);
        updatePersonFields(person, personToUpdate);
        return personDetailRepository.save(personToUpdate);
    }



    @Override
    @Transactional
    public void deletePerson(Integer id) {
        PersonDetailEntity personToDelete = getPersonByUserId(id);
        personDetailRepository.delete(personToDelete);
    }

    private PersonDetailEntity getPersonByUserId(Integer userId) {
        return personDetailRepository.findByUserId(userId).orElseThrow(() -> new PersonNotFoundException("person with id " + userId + " not found"));
    }

    private void updatePersonFields(PersonDetailDto person, PersonDetailEntity personToUpdate) {
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

    private Predicate getPredicate(PersonDetailSearchParams params, Root<PersonDetailEntity> root, CriteriaBuilder cb) {
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
        Join<PersonDetailEntity, UserEntity> user = root.join("user", JoinType.LEFT);
        predicate = cb.and(predicate, cb.equal(user.get("active"), true));
        return predicate;
    }
}

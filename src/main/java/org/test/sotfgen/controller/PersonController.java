package org.test.sotfgen.controller;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.PersonDetailDto;
import org.test.sotfgen.dto.PersonDetailSearchParams;
import org.test.sotfgen.entity.PersonDetailEntity;
import org.test.sotfgen.service.PersonDetailService;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonDetailService personDetailService;

    @PreAuthorize("hasAuthority('PERSON_READ')")
    @GetMapping
    public ResponseEntity<Page<PersonDetailEntity>> getPersons(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) @Max(100) Integer pageSize,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            PersonDetailSearchParams params
    ) {
        Sort sorter = Sort.by(sortDirection, sortField);
        return ResponseEntity.ok(personDetailService.getPersons(params, PageRequest.of(pageNumber, pageSize, sorter)));
    }

    @PreAuthorize("hasAuthority('PERSON_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<PersonDetailEntity> getUser(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(personDetailService.getPerson(id));
    }

    @PreAuthorize("hasAuthority('PERSON_CREATE')")
    @PostMapping
    public ResponseEntity<PersonDetailEntity> createUser(
            @RequestBody PersonDetailDto person,
            @RequestParam Integer userId
    ) {
        PersonDetailEntity createdPerson = personDetailService.createPerson(person, userId);
        var location = UriComponentsBuilder.fromPath("/persons/" + createdPerson.getId()).build().toUri();
        return ResponseEntity.created(location).body(createdPerson);
    }

    @PreAuthorize("hasAuthority('PERSON_UPDATE')")
    @PutMapping("/{personId}")
    public ResponseEntity<PersonDetailEntity> updateUser(
            @AuthenticationPrincipal SecUser secUser,
            @PathVariable Integer personId,
            @RequestBody PersonDetailDto person
    ) {
        PersonDetailEntity updatedPerson = personDetailService.updatePerson(secUser, person, personId);
        return ResponseEntity.accepted().body(updatedPerson);
    }

    @PreAuthorize("hasAuthority('PERSON_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Integer id
    ) {
        personDetailService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}

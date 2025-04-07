package org.test.sotfgen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.test.sotfgen.dto.PersonEntityDto;
import org.test.sotfgen.dto.PersonSearchParams;
import org.test.sotfgen.entity.PersonEntity;
import org.test.sotfgen.service.PersonService;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PreAuthorize("hasAuthority('PERSON_READ')")
    @GetMapping
    public ResponseEntity<Page<PersonEntity>> getPersons(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            PersonSearchParams params
    ) {
        Sort sorter = Sort.by(sortDirection, sortField);
        return ResponseEntity.ok(personService.getPersons(params, PageRequest.of(pageNumber, pageSize, sorter)));
    }

    @PreAuthorize("hasAuthority('PERSON_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<PersonEntity> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.getPerson(id));
    }

    @PreAuthorize("hasAuthority('PERSON_CREATE')")
    @PostMapping
    public ResponseEntity<PersonEntity> createUser(@RequestBody PersonEntityDto person, @RequestParam Integer userId) {
        PersonEntity createdPerson = personService.createPerson(person, userId);
        var location = UriComponentsBuilder.fromPath("/persons/" + createdPerson.getId()).build().toUri();
        return ResponseEntity.created(location).body(createdPerson);
    }

    @PreAuthorize("hasAuthority('PERSON_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<PersonEntity> updateUser(@PathVariable Integer id, @RequestBody PersonEntityDto person) {
        PersonEntity updatedPerson = personService.updatePerson(person, id);
        return ResponseEntity.accepted().body(updatedPerson);
    }

    @PreAuthorize("hasAuthority('PERSON_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}

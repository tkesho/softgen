package org.test.sotfgen.controller;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.test.sotfgen.dto.TeamSearchParams;
import org.test.sotfgen.entity.TeamEntity;
import org.test.sotfgen.service.interfaces.TeamService;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<Page<TeamEntity>> getAllTeams(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") @Max(100) int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction direction,
            TeamSearchParams params
    ) {
        Sort sort = Sort.by(direction, sortBy);
        return ResponseEntity.ok().body(teamService.getTeams(params, PageRequest.of(page, size, sort)));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamEntity> getTeam(@PathVariable Integer teamId) {
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }


    @PreAuthorize("hasRole('TEAM_LEADER')")
    @PostMapping("/{employeeId}/add/{teamId}")
    public ResponseEntity<String> assignEmployee (
            @PathVariable Integer employeeId,
            @PathVariable Integer teamId
    ) {

        try {
            teamService.addEmployeeToTeam(employeeId, teamId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error assigning employee to team: " + e.getMessage());
        }

        return ResponseEntity.ok().body("Employee with id " + employeeId + " assigned to team with id " + teamId);
    }

}

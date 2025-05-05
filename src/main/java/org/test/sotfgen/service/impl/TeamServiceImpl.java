package org.test.sotfgen.service.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.TeamSearchParams;
import org.test.sotfgen.entity.EmployeeEntity;
import org.test.sotfgen.entity.TeamEntity;
import org.test.sotfgen.repository.EmployeeRepository;
import org.test.sotfgen.repository.TeamRepository;
import org.test.sotfgen.service.interfaces.EmployeeService;
import org.test.sotfgen.service.interfaces.TeamService;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Override
    public Page<TeamEntity> getTeams(TeamSearchParams params, Pageable of) {
        return teamRepository.findAll((root, query, cb ) -> getPredicate(root, cb, params), of);
    }

    @Override
    public TeamEntity getTeam(Integer id) {
        return getTeamById(id);
    }

    @Override
    public void addEmployeeToTeam(Integer employeeId, Integer teamId) {
        TeamEntity team = getTeamById(teamId);
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
        if (team.getMembers().contains(employee)) {
            throw new IllegalArgumentException("Employee is already a member of the team");
        }

        team.getMembers().add(employee);
        employee.getTeams().add(team);

        teamRepository.save(team);
        employeeRepository.save(employee);
    }

    private Predicate getPredicate(Root<TeamEntity> root, CriteriaBuilder cb, TeamSearchParams params) {

        Predicate predicate = cb.conjunction();

        if (StringUtils.isNotBlank(params.getName())) {
            predicate = cb.and(predicate, cb.like(root.get("name"), "%" + params.getName() + "%"));
        }
        if (StringUtils.isNotBlank(params.getDescription())) {
            predicate = cb.and(predicate, cb.like(root.get("description"), "%" + params.getDescription() + "%"));
        }
        if (params.getManagerId() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("manager"), params.getManagerId()));
        }

        return predicate;
    }

    public TeamEntity getTeamById(Integer id) {
        return teamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Team with id " + id + " not found"));
    }
}
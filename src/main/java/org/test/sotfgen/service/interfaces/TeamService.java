package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.TeamSearchParams;
import org.test.sotfgen.entity.TeamEntity;

@Service
public interface TeamService {
    Page<TeamEntity> getTeams(TeamSearchParams params, Pageable of);

    TeamEntity getTeam(Integer id);

    void addEmployeeToTeam(Integer employeeId, Integer teamId);
}

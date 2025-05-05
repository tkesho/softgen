package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.test.sotfgen.audit.BaseAuditTable;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "employee", schema = "social")
public class EmployeeEntity extends BaseAuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity person;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @Column(name = "status", nullable = false)
    private EmploymentStatus employmentStatus;

    @ManyToMany
    @JoinTable(
            name = "employee_team", schema = "social",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<TeamEntity> teams;

    public enum EmploymentStatus {
        ACTIVE,
        INACTIVE,
        TERMINATED
    }

    public enum EmploymentType {
        FULL_TIME,
        PART_TIME,
        CONTRACTOR
    }
}

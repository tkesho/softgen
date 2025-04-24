package org.test.sotfgen.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ColumnTransformer;
import org.test.sotfgen.audit.BaseAuditTable;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "group", schema = "social")
public class GroupEntity extends BaseAuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;

    @Column(name = "description", length = 256)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "person_group", schema = "social",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<PersonEntity> members;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "active")
    private Boolean active = true;

    @ColumnDefault("'PUBLIC'")
    @ColumnTransformer(write = "?::privacy")
    @Enumerated(EnumType.STRING)
    @Column(name = "privacy")
    private Privacy privacy = Privacy.PUBLIC;

    public enum Privacy {
        PUBLIC, PRIVATE
    }
}
package org.test.sotfgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ColumnTransformer;
import org.test.sotfgen.dto.GroupEntityDto;
import org.test.sotfgen.model.base.Auditable;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "groups", schema = "people")
@SequenceGenerator(name = "groups_id_gen", schema = "people", sequenceName = "groups_id_seq", allocationSize = 1)
public class GroupEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groups_id_gen")
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

    @ManyToMany
    @JoinTable(
            name = "users_groups",
            schema = "people",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> usersGroups;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "active")
    private Boolean active;

    @ColumnDefault("'PUBLIC'")
    @ColumnTransformer(write = "?::privacy")
    @Enumerated(EnumType.STRING)
    @Column(name = "privacy")
    private Privacy privacy;

    public enum Privacy {
        PUBLIC, PRIVATE;
    }

    @PrePersist
    public void prePersist() {
        if (this.active == null) {
            this.active = true;
        }
        if (this.privacy == null) {
            this.privacy = Privacy.PUBLIC;
        }
    }

    public GroupEntity(GroupEntityDto group) {
        this.name = group.getName();
        this.description = group.getDescription();
        this.privacy = group.getPrivacy();
        this.active = group.getActive();
    }
}
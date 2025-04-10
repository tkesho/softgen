package org.test.sotfgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.test.sotfgen.dto.PostEntityDto;
import org.test.sotfgen.model.base.Auditable;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SequenceGenerator(name = "posts_id_gen", sequenceName = "posts_id_seq", allocationSize = 1, schema = "posts")
@Table(name = "posts", schema = "posts")
public class PostEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_id_gen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 1024)
    @NotNull
    @Column(name = "body", nullable = false, length = 1024)
    private String body;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "hidden", nullable = false)
    private Boolean hidden = false;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<FileEntity> files = new LinkedHashSet<>();

    public PostEntity(PostEntityDto postEntityDto) {
        this.title = postEntityDto.getTitle();
        this.body = postEntityDto.getBody();
    }
}
package org.test.sotfgen.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.test.sotfgen.model.base.Auditable;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "comment", schema = "content")
public class CommentEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "body", nullable = false)
    private String body;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_id")
    private CommentEntity parent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private PersonDetailEntity author;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentEntity> children = new LinkedHashSet<>();

    @Column(name = "hidden", nullable = false)
    private Boolean hidden = false;

    @PrePersist
    private void prePersist() {
        if (this.hidden == null) {
            this.hidden = false;
        }
    }
}

package org.test.sotfgen.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "comment_history", schema = "audit")
public class CommentHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @Column(name = "author_id", nullable = false)
    private Integer authorId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "hidden", nullable = false)
    private Boolean hidden = false;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;

}

package org.test.sotfgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.test.sotfgen.model.base.Auditable;

@Getter
@Setter
@Entity
@Table(name = "file", schema = "content")
public class FileEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String fileName;

    @Size(max = 255)
    @NotNull
    @Column(name = "object_key", nullable = false)
    private String objectKey;

    @Size(max = 100)
    @Column(name = "content_type", length = 100)
    private String contentType;

    @NotNull
    @Column(name = "size", nullable = false)
    private Integer size;
}
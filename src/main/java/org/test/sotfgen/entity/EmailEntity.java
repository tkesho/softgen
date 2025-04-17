package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "email", schema = "audit")
@AllArgsConstructor
@NoArgsConstructor
public class EmailEntity {


    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "from_email", nullable = false)
    private String fromEmail;

    @Column(name = "to_email", nullable = false)
    private String ToEmail;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "sent_date", nullable = false)
    private LocalDateTime sentDate;

    @PrePersist
    private void prePersist() {
        this.sentDate = LocalDateTime.now();
    }
}
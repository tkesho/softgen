package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "http_request_log", schema = "audit")
public class HttpRequestEntity {

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "method", nullable = false, updatable = false)
    private String method;

    @Column(name = "url", nullable = false, updatable = false)
    private String url;

    @Column(name = "query_params", updatable = false)
    private String queryParams;

    @Column(name = "request_body", updatable = false)
    private String requestBody;

    @Column(name = "response_status", nullable = false, updatable = false)
    private Integer responseStatus;

    @Column(name = "duration_millis", nullable = false, updatable = false)
    private Long durationMillis;

    @Column(name = "ip", nullable = false, updatable = false)
    private String ip;

    @Column(name = "user_agent", nullable = false, updatable = false)
    private String userAgent;

    @Column(name = "username", nullable = false, updatable = false)
    private String username;

    @Column(name = "request_time", nullable = false, updatable = false)
    private Date requestTime;
}
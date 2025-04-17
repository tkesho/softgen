package org.test.sotfgen.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthorityService {
    void assignToUser(Integer userId, Integer authorityId);
}

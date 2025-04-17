package org.test.sotfgen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    @Override
    public void assignToUser(Integer userId, Integer authorityId) {

    }
}

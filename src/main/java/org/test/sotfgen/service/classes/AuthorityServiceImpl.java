package org.test.sotfgen.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.sotfgen.service.interfaces.AuthorityService;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    @Override
    public void assignToUser(Integer userId, Integer authorityId) {

    }
}

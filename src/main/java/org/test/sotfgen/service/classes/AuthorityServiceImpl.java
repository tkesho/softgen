package org.test.sotfgen.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.sotfgen.entity.AuthorityEntity;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.exceptions.AuthorityNotFoundException;
import org.test.sotfgen.repository.AuthorityRepository;
import org.test.sotfgen.repository.UserRepository;
import org.test.sotfgen.service.interfaces.AuthorityService;
import org.test.sotfgen.utils.UserUtil;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final UserUtil userUtil;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Override
    public void assignToUser(Integer userId, Integer authorityId) {
        UserEntity user = userUtil.getUserById(userId);
        AuthorityEntity authority = authorityRepository.findById(authorityId).orElseThrow(() -> new AuthorityNotFoundException("Authority not found"));

        if(user.getAuthorities().contains(authority)) {
            throw new IllegalArgumentException("user already has authority");
        }

        user.getAuthorities().add(authority);

        userRepository.save(user);
    }
}
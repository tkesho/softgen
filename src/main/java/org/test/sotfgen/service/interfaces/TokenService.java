package org.test.sotfgen.service.interfaces;

import io.swagger.v3.oas.annotations.servers.Server;
import org.test.sotfgen.dto.AuthRequestDto;

@Server
public interface TokenService {
    String authenticateAndGenerateToken(AuthRequestDto authRequestDto);

    void logout();

    String refreshToken(String token);

    void forgetPassword(String emailAddress);

    void resetPassword(String token);
}

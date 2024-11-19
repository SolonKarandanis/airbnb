package com.solon.airbnb.util;

import com.solon.airbnb.shared.common.AuthorityConstants;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.domain.Authority;
import com.solon.airbnb.user.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TestUtil {

    public static Authority createTestAuthority(){
        Authority auth = new Authority();
        auth.setName(AuthorityConstants.ROLE_LANDLORD);
        return auth;
    }

    public static User createTestUser(final Long userId){
        User user = new User();
        user.setId(userId);
        user.setFirstName("Robert");
        user.setLastName("Smith");
        user.setUsername("admin1");
        user.setPassword("123");
        user.setAuthorities(Set.of(createTestAuthority()));
        return user;
    }

    public static UserDTO createTestUserDto(final Long userId){
        UserDTO userDto = new UserDTO();
        userDto.setId(userId);
        userDto.setFirstName("Robert");
        userDto.setLastName("Smith");
        userDto.setUsername("admin1");
        userDto.setPassword("123");
        userDto.setAuthorityNames(List.of(AuthorityConstants.ROLE_LANDLORD));
        return userDto;
    }

    public static Authentication getTestAuthenticationFromUserDTO(UserDTO userDto, String token) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDto, token, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return SecurityContextHolder.getContext().getAuthentication();

    }


    public static Authentication getTestAuthenticationFromUserDTO(UserDTO userDto) {
        return getTestAuthenticationFromUserDTO(userDto, TestConstants.TEST_TOKEN);
    }
}

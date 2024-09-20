package com.solon.airbnb.user.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.repository.UserRepository;




@Service
@Transactional(readOnly = true)
public class UserDetailsServiceBean extends BaseUserAccountServiceBean implements UserDetailsService,UserService{
	
	private final UserRepository userRepository;

    public UserDetailsServiceBean(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository
                .findByUsername(username)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	private UserDTO createSpringSecurityUser(User user) {
		return convertToDTO(user,true);
    }

	@Override
	public Optional<ReadUserDTO> getByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ReadUserDTO> getByPublicId(UUID publicId) {
		// TODO Auto-generated method stub
		return null;
	}

}

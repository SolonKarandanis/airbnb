package com.solon.airbnb.user.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.repository.AuthorityRepository;
import com.solon.airbnb.user.repository.UserRepository;






@Service
@Transactional(readOnly = true)
public class UserDetailsServiceBean extends BaseUserAccountServiceBean implements UserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceBean.class);
	private static final String USER_NOT_FOUND="User not found";
	
	private final UserRepository userRepository;
	private final AuthorityRepository authorityRepository;
	

    public UserDetailsServiceBean(
    		UserRepository userRepository,
    		AuthorityRepository authorityRepository
	) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository
                .findByUsername(username)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
	}
	
	private UserDTO createSpringSecurityUser(User user) {
		return convertToDTO(user,true);
    }

}

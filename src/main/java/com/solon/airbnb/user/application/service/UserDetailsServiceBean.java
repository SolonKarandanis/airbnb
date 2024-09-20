package com.solon.airbnb.user.application.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.dto.UserInputDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.repository.AuthorityRepository;
import com.solon.airbnb.user.repository.UserRepository;




@Service
@Transactional(readOnly = true)
public class UserDetailsServiceBean extends BaseUserAccountServiceBean implements UserDetailsService,UserService{
	
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceBean.class);
	
	private final UserRepository userRepository;
	private final AuthorityRepository authorityRepository;

    public UserDetailsServiceBean(UserRepository userRepository,AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
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

	@Transactional
	@Override
	public void deleteUser(String uuid) throws NotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<User> findAllUsers(UsersSearchRequestDTO searchObj, Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public User createUser(UserInputDTO dto) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public User updateUser(UserInputDTO dto) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.solon.airbnb.user.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import com.solon.airbnb.user.domain.AccountStatus;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.mapper.UserMapper;
import com.solon.airbnb.user.repository.AuthorityRepository;
import com.solon.airbnb.user.repository.UserRepository;
import com.solon.airbnb.user.repository.UsersSpecification;






@Service
@Transactional(readOnly = true)
public class UserDetailsServiceBean extends BaseUserAccountServiceBean implements UserDetailsService,UserService{
	
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceBean.class);
	private static final String USER_NOT_FOUND="User not found";
	
	private final UserRepository userRepository;
	private final AuthorityRepository authorityRepository;
	private final UserMapper userMapper;

    public UserDetailsServiceBean(
    		UserRepository userRepository,
    		AuthorityRepository authorityRepository,
    		UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userMapper = userMapper;
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
	public Optional<ReadUserDTO> getByEmail(String email) throws NotFoundException{
		Optional<User> oneByEmail = userRepository.findOneByEmail(email);
		return oneByEmail.map(userMapper::readUserDTOToUser);
	}

	@Override
	public Optional<ReadUserDTO> getByPublicId(String publicId) throws NotFoundException{
		Optional<User> oneByPublicId = userRepository.findOneByPublicId(UUID.fromString(publicId));
		return oneByPublicId.map(userMapper::readUserDTOToUser);
	}
	
	@Override
	public User findById(Long id) throws NotFoundException {
		return userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
	}
	
	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void deleteUser(String uuid) throws NotFoundException {
		Optional<User> usrOpt  = userRepository
                .findOneByPublicId(UUID.fromString(uuid));
		usrOpt.ifPresentOrElse(
				userRepository::delete,
                ()-> new NotFoundException("User with uuid:" + uuid + " Not Found!")
		);
	}

	@Override
	public Page<User> findAllUsers(UsersSearchRequestDTO searchObj, Pageable page) {
		User user = new User();
    	BeanUtils.copyProperties(searchObj, user);
    	user.setStatus(AccountStatus.valueOf(searchObj.getStatus()));
    	return  userRepository.findAll( new UsersSpecification(user),page);
	}

	@Transactional
	@Override
	public User createUser(UserInputDTO dto) throws NotFoundException {
		User user  = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new NotFoundException("User already exists with that username"));	
		user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        if(AccountStatus.ACTIVE.getValue().equals(dto.getStatus())) {
        	user.setStatus(AccountStatus.ACTIVE);
        }
        else if(AccountStatus.INACTIVE.getValue().equals(dto.getStatus())){
        	user.setStatus(AccountStatus.INACTIVE);
        }
        else {
        	user.setStatus(AccountStatus.DELETED);
        }
        UUID uuid = UUID.randomUUID();
        user.setPublicId(uuid);
        return userRepository.save(user);
	}

	@Transactional
	@Override
	public User updateUser(UserInputDTO dto) throws NotFoundException {
		User user  = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
		user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        if(AccountStatus.ACTIVE.getValue().equals(dto.getStatus())) {
        	user.setStatus(AccountStatus.ACTIVE);
        }
        else if(AccountStatus.INACTIVE.getValue().equals(dto.getStatus())){
        	user.setStatus(AccountStatus.INACTIVE);
        }
        else {
        	user.setStatus(AccountStatus.DELETED);
        }
        
        return userRepository.save(user);
	}

}

package com.solon.airbnb.user.application.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.CreateUserDTO;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UpdateUserDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.application.event.UserRegistrationCompleteEvent;
import com.solon.airbnb.user.domain.AccountStatus;
import com.solon.airbnb.user.domain.Authority;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.domain.VerificationToken;
import com.solon.airbnb.user.repository.AuthorityRepository;
import com.solon.airbnb.user.repository.UserRepository;
import com.solon.airbnb.user.repository.UsersSpecification;

@Service
@Transactional(readOnly = true)
public class UserServiceBean extends BaseUserAccountServiceBean implements UserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceBean.class);

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final VerificationTokenService verificationTokenService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceBean(
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            VerificationTokenService verificationTokenService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.verificationTokenService = verificationTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ReadUserDTO> getByEmail(String email) throws NotFoundException {
        Optional<User> oneByEmail = userRepository.findOneByEmail(email);
        return oneByEmail.map(userMapper::readUserDTOToUser);
    }

    @Override
    public Optional<User> getByPublicId(String publicId) throws NotFoundException{
        return userRepository.findOneByPublicId(UUID.fromString(publicId));
    }

    @Override
    public User findById(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String uuid) throws NotFoundException {
        Optional<User> usrOpt  = userRepository
                .findOneByPublicId(UUID.fromString(uuid));
        usrOpt.ifPresentOrElse(
                userRepository::delete,
                ()-> new NotFoundException(USER_NOT_FOUND)
        );
    }

    @Override
    public Page<User> findAllUsers(UsersSearchRequestDTO searchObj) {
        User user = new User();
        BeanUtils.copyProperties(searchObj, user);
        user.setStatus(AccountStatus.valueOf(searchObj.getStatus()));
        PageRequest pageRequest = toPageRequest(searchObj.getPaging());
        return  userRepository.findAll( new UsersSpecification(user),pageRequest);
    }

    @Override
    public Long countUsers(UsersSearchRequestDTO searchObj) throws AirbnbException {
        User user = new User();
        BeanUtils.copyProperties(searchObj, user);
        user.setStatus(AccountStatus.valueOf(searchObj.getStatus()));
        return userRepository.count(new UsersSpecification(user));
    }

    
    @Override
	public List<ReadUserDTO> findAllUsersForExport(UsersSearchRequestDTO searchObj) throws AirbnbException {
    	User user = new User();
        BeanUtils.copyProperties(searchObj, user);
        user.setStatus(AccountStatus.valueOf(searchObj.getStatus()));
        List<User> users = userRepository.findAll(new UsersSpecification(user));
        return convertToReadUserListDTO(users);
	}


    @Transactional(noRollbackFor = MailAuthenticationException.class)
    @Override
    public User registerUser(CreateUserDTO dto, String applicationUrl) throws BusinessException {
        Optional<User> userNameMaybe  = userRepository.findByUsername(dto.getUsername());

        if(userNameMaybe.isPresent()){
            throw new BusinessException("error.username.exists");
        }

        Optional<User> emailMaybe  = userRepository.findOneByEmail(dto.getEmail());
        if(emailMaybe.isPresent()){
            throw new BusinessException("error.email.exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setStatus(AccountStatus.INACTIVE);
        user.setVerified(Boolean.FALSE);
        UUID uuid = UUID.randomUUID();
        user.setPublicId(uuid);

        Authority role = authorityRepository.findByName(dto.getRole());
        user.setAuthorities(Set.of(role));
        user = userRepository.save(user);
        getPublisher().publishEvent(new UserRegistrationCompleteEvent(user, applicationUrl));
        return user;
    }

    @Transactional
    @Override
    public User updateUser(String publicId,UpdateUserDTO dto) throws NotFoundException {
        User user  = userRepository
                .findOneByPublicId(UUID.fromString(publicId))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void verifyEmail(String token) throws BusinessException {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        Boolean verificationResult = verificationTokenService.validateToken(verificationToken);
        if(verificationResult){
            User user = verificationToken.getUser();
            user.setVerified(Boolean.TRUE);
            userRepository.save(user);
        }
    }

    @Override
    public User activateUser(User user) throws BusinessException {
        if(AccountStatus.ACTIVE.equals(user.getStatus())){
            throw new BusinessException("error.user.already.active");
        }
        user.setStatus(AccountStatus.ACTIVE);
        user.setVerified(Boolean.TRUE);
        userRepository.save(user);
        return user;
    }

    @Override
    public User deactivateUser(User user) throws BusinessException {
        if(AccountStatus.INACTIVE.equals(user.getStatus())){
            throw new BusinessException("error.user.already.inactive");
        }
        user.setStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
        return user;
    }
}

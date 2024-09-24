package com.solon.airbnb.user.application.service;

import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserInputDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.application.event.UserRegistrationCompleteEvent;
import com.solon.airbnb.user.domain.AccountStatus;
import com.solon.airbnb.user.domain.Authority;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.domain.VerificationToken;
import com.solon.airbnb.user.repository.AuthorityRepository;
import com.solon.airbnb.user.repository.UserRepository;
import com.solon.airbnb.user.repository.UsersSpecification;
import com.solon.airbnb.user.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserServiceBean extends BaseUserAccountServiceBean implements UserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceBean.class);

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceBean(
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            VerificationTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ReadUserDTO> getByEmail(String email) throws NotFoundException {
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
        PageRequest pageRequest = toPageRequest(searchObj.getPaging())	;
        return  userRepository.findAll( new UsersSpecification(user),pageRequest);
    }


    @Transactional
    @Override
    public User registerUser(UserInputDTO dto, String applicationUrl) throws NotFoundException {
        Optional<User> userMaybe  = userRepository.findByUsername(dto.getUsername());

        if(userMaybe.isPresent()){
            throw new NotFoundException("error.username.exists");
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
    public User updateUser(UserInputDTO dto) throws NotFoundException {
        User user  = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void verifyEmail(String token) throws BusinessException {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if(theToken.getUser().getVerified()){
            throw new BusinessException("error.user.already.verified");
        }
    }
}

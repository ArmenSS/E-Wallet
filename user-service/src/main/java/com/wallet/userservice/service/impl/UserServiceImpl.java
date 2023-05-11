package com.wallet.userservice.service.impl;

import com.wallet.userservice.dto.LoginRequest;
import com.wallet.userservice.dto.MailVerifyDto;
import com.wallet.userservice.dto.UserAuthResponse;
import com.wallet.userservice.dto.UserDto;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import com.wallet.userservice.exception.*;
import com.wallet.userservice.feign.MailClient;
import com.wallet.userservice.feign.TransactionServiceClient;
import com.wallet.userservice.mapper.UserMapper;
import com.wallet.userservice.repository.UserRepository;
import com.wallet.userservice.security.JwtTokenUtil;
import com.wallet.userservice.service.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailClient mailClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final TransactionServiceClient transactionServiceClient;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Value("${mail.verification}")
    private boolean turnOnMailVerification;

    @Value("${mail.verify.url}")
    private String MAIL_VERIFY_URL;

    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>.%]).{8,18}$";
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";


    @Override
    public UserAuthResponse auth(LoginRequest loginRequest)
            throws EntityNotFoundException, FeignException {
        UserDto byEmail;
        try {
            byEmail = findByEmail(loginRequest.getEmail());
        } catch (FeignException e) {
            throw new EntityNotFoundException();
        }

        if (byEmail == null) {
            log.error(this.getClass().getName(), EntityNotFoundException.class);
            throw new EntityNotFoundException();
        }
        if (!byEmail.isMailVerified()) {
            log.error(this.getClass().getName(), UnVerifiedEmailUserException.class);
            throw new UnVerifiedEmailUserException();
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), byEmail.getPassword())) {
            log.error(this.getClass().getName(), IncorrectPasswordException.class);
            throw new IncorrectPasswordException();
        }
        return new UserAuthResponse(
                jwtTokenUtil.generateToken(byEmail, expiration),
                jwtTokenUtil.generateToken(byEmail, refreshExpiration),
                byEmail);
    }


    @Override
    public UserDto save(UserDto userDto) throws DuplicateUserException {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateUserException();
        }
        if (!userDto.getPassword().matches(PASSWORD_REGEX)) {
            log.error(this.getClass().getName(), IncorrectPasswordException.class);
            throw new IncorrectPasswordException();
        }
        if (!userDto.getEmail().matches(EMAIL_REGEX)) {
            log.error(this.getClass().getName(), IncorrectEmailException.class);
            throw new IncorrectEmailException();
        }
        userDto.setUserRole(UserRole.USER);
        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setMailVerifyToken(UUID.randomUUID());
        if (turnOnMailVerification){
            userEntity.setMailVerified(false);
            MailVerifyDto mailVerifyDto = MailVerifyDto.builder()
                    .name(userDto.getName())
                    .surname(userDto.getSurname())
                    .email(userDto.getEmail())
                    .mailVerificationLink(MAIL_VERIFY_URL + userEntity.getMailVerifyToken())
                    .build();
            mailClient.sendMail(mailVerifyDto);
        }else {
            userEntity.setMailVerified(true);
        }
        UserEntity save = userRepository.save(userEntity);
        transactionServiceClient.createBalance(save.getId());
        return userMapper.toDto(save);
    }


    @Override
    public UserDto findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public UserDto findById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return userMapper.toDto(userEntity);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto findByCard(UserDto userDto) {
        if (userDto.getUserRole().equals(UserRole.USER)) {

        }
        return null;
    }

    @Override
    public UserDto update(UserDto userDto) {
        userRepository.findById(userDto.getId()).orElseThrow(EntityNotFoundException::new);
        UserEntity updatedUser = userRepository.save(userMapper.toEntity(userDto));
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public String verifyUserByEmailToken(String token) {
        Optional<? extends UserEntity> userByEmailVerifyToken = userRepository
                .findByMailVerifyToken(UUID.fromString(token));
        UserEntity user = userByEmailVerifyToken.get();
        boolean isExpired = mailClient.isVerifyUrlExpired(user.getEmail());
        if (isExpired) {
            user.setMailVerifyToken(UUID.randomUUID());
            sendMail(userMapper.toDto(user));
            return "The term has expired. New email sent successfully";
        } else {
            user.setMailVerified(true);
            user.setMailVerifyToken(null);
            userRepository.save(user);
            return "email verifed successfully";
        }
    }

    private void sendMail(UserDto userDto) {
        MailVerifyDto mailVerifyDto = MailVerifyDto.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .mailVerificationLink(MAIL_VERIFY_URL + userDto.getMailVerifyToken())
                .build();
        mailClient.sendMail(mailVerifyDto);
    }
}
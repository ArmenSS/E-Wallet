package com.wallet.userservice;

import com.wallet.userservice.dto.UserDto;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.exception.EntityNotFoundException;
import com.wallet.userservice.feign.MailClient;
import com.wallet.userservice.feign.TransactionServiceClient;
import com.wallet.userservice.mapper.UserMapper;
import com.wallet.userservice.repository.UserRepository;
import com.wallet.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MailClient mailClient;
    @Mock
    private TransactionServiceClient transactionServiceClient;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);
        UserDto result = userService.findByEmail(email);
        assertEquals(userDto, result);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        UserDto userDto = new UserDto();
        userDto.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);
        UserDto result = userService.findById(id);
        assertEquals(userDto, result);
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(id));
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        userService.delete(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdate() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setId(userDto.getId());
        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setId(userDto.getId());
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(existingUserEntity));
        when(userRepository.save(updatedUserEntity)).thenReturn(updatedUserEntity);
        when(userMapper.toEntity(userDto)).thenReturn(updatedUserEntity);
        when(userMapper.toDto(updatedUserEntity)).thenReturn(userDto);
        UserDto result = userService.update(userDto);
        assertEquals(userDto, result);
    }

    @Test
    public void testUpdateNotFound() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> userService.update(userDto));
    }

    @Test
    public void testVerifyUserByEmailToken_validToken_success() {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setMailVerifyToken(uuid);
        user.setMailVerified(false);
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setMailVerifyToken(uuid);
        userDto.setMailVerified(false);
        when(userRepository.findByMailVerifyToken(uuid)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(mailClient.isVerifyUrlExpired(email)).thenReturn(false);
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        String result = userService.verifyUserByEmailToken(token);
        assertEquals("email verifed successfully", result);
        verify(userRepository, times(1)).save(user);
    }

}
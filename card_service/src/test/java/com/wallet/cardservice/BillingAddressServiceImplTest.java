package com.wallet.cardservice;

import com.wallet.cardservice.dto.BillingAddressDto;
import com.wallet.cardservice.entity.BillingAddress;
import com.wallet.cardservice.mapper.BillingAddressMapper;
import com.wallet.cardservice.repo.BillingAddressRepository;
import com.wallet.cardservice.service.BillingAddressService;
import com.wallet.cardservice.service.serviceImpl.BillingAddressServiceImpl;
import com.wallet.userservice.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BillingAddressServiceImplTest {

    private BillingAddressService billingAddressService;

    @Mock
    private BillingAddressRepository billingAddressRepository;

    @Mock
    private BillingAddressMapper billingAddressMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        billingAddressService = new BillingAddressServiceImpl(billingAddressRepository, billingAddressMapper);
    }

    @Test
    public void testSaveBillingAddress() {
        UserEntity userEntity = new UserEntity();
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(1L);

        when(billingAddressMapper.toDto(any(BillingAddress.class))).thenReturn(new BillingAddressDto());
        when(billingAddressMapper.toEntity(any(BillingAddressDto.class))).thenReturn(billingAddress);
        when(billingAddressRepository.save(any(BillingAddress.class))).thenReturn(billingAddress);

        billingAddressService.save(userEntity, billingAddress);

        verify(billingAddressRepository, times(1)).save(any(BillingAddress.class));
    }

    @Test
    public void testUpdateBillingAddress() {
        BillingAddressDto billingAddressDto = new BillingAddressDto();
        billingAddressDto.setId(1L);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(1L);

        when(billingAddressRepository.findById(anyLong())).thenReturn(Optional.of(billingAddress));
        when(billingAddressMapper.toEntity(any(BillingAddressDto.class))).thenReturn(billingAddress);
        when(billingAddressRepository.save(any(BillingAddress.class))).thenReturn(billingAddress);
        when(billingAddressMapper.toDto(any(BillingAddress.class))).thenReturn(billingAddressDto);

        BillingAddressDto updatedBillingAddress = billingAddressService.update(billingAddressDto);

        assertEquals(updatedBillingAddress.getId(), billingAddressDto.getId());
    }

    @Test
    public void testFindById() {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(1L);

        when(billingAddressRepository.findById(anyLong())).thenReturn(Optional.of(billingAddress));
        when(billingAddressMapper.toDto(any(BillingAddress.class))).thenReturn(BillingAddressDto.builder().id(1L).build());

        BillingAddressDto foundBillingAddress = billingAddressService.findById(1L);

        assertEquals(foundBillingAddress.getId(), billingAddress.getId());
    }

    @Test
    public void testDeleteById() {
        billingAddressService.delete(1L);
        verify(billingAddressRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testFindByUserId() {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(1L);

        when(billingAddressRepository.findByUserId(anyLong())).thenReturn(billingAddress);
        when(billingAddressMapper.toDto(any(BillingAddress.class))).thenReturn(BillingAddressDto.builder().id(1L).build());

        BillingAddressDto foundBillingAddress = billingAddressService.findByUserId(1L);

        assertEquals(foundBillingAddress.getId(), billingAddress.getId());
    }
}
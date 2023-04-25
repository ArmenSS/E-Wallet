package com.wallet.userservice.mapper;

import com.wallet.userservice.dto.UserDto;
import com.wallet.userservice.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, UserEntity>{
}

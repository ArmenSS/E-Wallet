package com.wallet.userservice.security;

import com.wallet.userservice.entity.UserEntity;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private final UserEntity user;

    public CurrentUser(UserEntity user) {
        super(
                user.getEmail(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getUserRole().name()));
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }
}

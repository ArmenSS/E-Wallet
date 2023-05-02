//package com.wallet.userservice.security;
//
//import com.energizeglobal.auth.service.core.entity.User;
//import com.energizeglobal.auth.service.core.mapper.UserMapper;
//import com.energizeglobal.user.service.data.transfer.response.UserResponse;
//import com.energizeglobal.user.service.feign.client.UserClient;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Log4j2
//@Service
//@RequiredArgsConstructor
//public class CurrentUserDetailServiceImpl implements UserDetailsService {
//
//  private final UserClient userClient;
//  private final UserMapper userMapper;
//
//  @Override
//  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//    UserResponse byEmail = userClient.findByEmail(email);
//    User user = userMapper.toEntity(byEmail);
//    if (user == null) {
//      log.error(this.getClass().getName(), UsernameNotFoundException.class);
//      throw new UsernameNotFoundException("Username not found");
//    }
//    return new CurrentUser(user);
//  }
//}

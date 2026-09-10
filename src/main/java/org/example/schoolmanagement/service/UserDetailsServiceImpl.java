package org.example.schoolmanagement.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.schoolmanagement.entity.Role;
import org.example.schoolmanagement.entity.User;
import org.example.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    private User findUserByEmail(String email) {
        return userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден."));
    }

    private List<GrantedAuthority> getAuthorityUser(User findUser) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        List<String> nameRoleList = findUser.getRoles()
                .stream()
                .map(Role::getNameRole)
                .toList();
        nameRoleList.forEach(nameRole -> grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + nameRole)));
        return grantedAuthorityList;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = findUserByEmail(username);
        List<GrantedAuthority> grantedAuthorities = getAuthorityUser(findUser);
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(findUser.getEmail())
                .password(findUser.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}

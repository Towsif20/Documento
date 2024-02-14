package com.towsif.Documento.service;

import com.towsif.Documento.entity.Role;
import com.towsif.Documento.entity.UserEntity;
import com.towsif.Documento.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEntityService
{
    private final UserEntityRepository userEntityRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntityService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder)
    {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserEntity user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);

        userEntityRepository.save(user);
    }
}

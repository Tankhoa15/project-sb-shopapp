package com.example.shopapp.service.impl;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.entity.Role;
import com.example.shopapp.entity.User;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.repository.RoleRepository;
import com.example.shopapp.repository.UserRepository;
import com.example.shopapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        // DTO to entity
        User newUser = User.builder()
                .fullname(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);

        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();
        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        return "";
    }
}

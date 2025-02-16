package com.example.shopapp.service;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.entity.User;
import com.example.shopapp.exception.DataNotFoundException;

public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber,String password) throws Exception;
}

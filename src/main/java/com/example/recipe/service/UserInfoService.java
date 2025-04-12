package com.example.recipe.service;

import com.example.recipe.model.UserInfo;
import com.example.recipe.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }

    public UserInfo getUserById(Long id) {
        return userInfoRepository.findById(id).orElse(null);
    }
}
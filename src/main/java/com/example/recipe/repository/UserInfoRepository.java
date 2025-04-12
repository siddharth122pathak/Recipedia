package com.example.recipe.repository;

import com.example.recipe.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    // Add custom query methods if needed
}
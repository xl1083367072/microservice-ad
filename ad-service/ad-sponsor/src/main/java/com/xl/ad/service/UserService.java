package com.xl.ad.service;

import com.xl.ad.entity.AdUser;
import com.xl.ad.exception.AdException;
import com.xl.ad.vo.CreateUserRequest;
import com.xl.ad.vo.CreateUserResponse;

public interface UserService {

    CreateUserResponse createUser(CreateUserRequest request) throws AdException;

    AdUser findByUserId(Long userId);

}

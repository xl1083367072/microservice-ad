package com.xl.ad.service.impl;

import com.xl.ad.constant.Constants;
import com.xl.ad.dao.AdUserRepository;
import com.xl.ad.entity.AdUser;
import com.xl.ad.exception.AdException;
import com.xl.ad.service.UserService;
import com.xl.ad.util.CommonUtils;
import com.xl.ad.vo.CreateUserRequest;
import com.xl.ad.vo.CreateUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private  final AdUserRepository adUserRepository;

    @Autowired
    public UserServiceImpl(AdUserRepository adUserRepository) {
        this.adUserRepository = adUserRepository;
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {
        if(!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdUser adUser = adUserRepository.findByUsername(request.getUsername());
        if(adUser!=null){
            throw new AdException(Constants.ErrorMsg.USER_EXISTS_ERROR);
        }
        adUser = adUserRepository.save(new AdUser(request.getUsername(), CommonUtils.md5(request.getUsername())));
        return new CreateUserResponse(adUser.getId(),adUser.getUsername(),adUser.getToken(),
                adUser.getCreateTime(),adUser.getUpdateTime());
    }

    @Override
    public AdUser findByUserId(Long userId) {
        return adUserRepository.findById(userId).get();
    }
}

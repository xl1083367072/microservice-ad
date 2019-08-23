package com.xl.ad.service.impl;

import com.xl.ad.dao.CreativeRepository;
import com.xl.ad.entity.Creative;
import com.xl.ad.service.CreativeService;
import com.xl.ad.vo.CreativeRequest;
import com.xl.ad.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreativeServiceImpl implements CreativeService {


    private final CreativeRepository creativeRepository;

    @Autowired
    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

//  创建创意
    @Override
    public CreativeResponse createCreative(CreativeRequest request) {
        Creative creative = creativeRepository.save(request.convert2Entity());
        return new CreativeResponse(creative.getId(),creative.getName());
    }

}

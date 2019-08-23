package com.xl.ad.service;

import com.xl.ad.exception.AdException;
import com.xl.ad.vo.*;

public interface AdUnitService {

    AdUnitResponse createAdUnit(AdUnitRequest request) throws AdException;

    AdUnitKeywordResponse createAdUnitKeyword(AdUnitKeywordRequest request) throws  AdException;

    AdUnitItResponse createAdUnitIt(AdUnitItRequest request) throws AdException;

    AdUnitDistrictResponse createAdUnitDistrict(AdUnitDistrictRequest request) throws AdException;

    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;

}

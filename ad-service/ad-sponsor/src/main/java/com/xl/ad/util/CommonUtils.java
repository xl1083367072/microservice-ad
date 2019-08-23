package com.xl.ad.util;

import com.xl.ad.constant.Constants;
import com.xl.ad.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class CommonUtils {

    private static String[] datePatterns = {"yyyy-MM-dd","yyyy.MM.dd","yyyy/MM/dd"};

    public static String md5(String val){
        return DigestUtils.md5Hex(val).toUpperCase();
    }

    public static Date convertString2Date(String dateString) throws AdException {
        try {
            Date date = DateUtils.parseDate(dateString, datePatterns);
            return date;
        }catch (Exception e){
            throw new AdException(Constants.ErrorMsg.DATE_CONVERT_ERROR);
        }
    }

}

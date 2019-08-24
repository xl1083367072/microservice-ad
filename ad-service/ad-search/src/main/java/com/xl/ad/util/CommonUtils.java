package com.xl.ad.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class CommonUtils {

    public static <K,V> V getOrCreate(K k, Map<K,V> map, Supplier<V> factory){
        return map.computeIfAbsent(k,obj -> factory.get());
    }

    public static String concat(String... args){
        StringBuffer stringBuffer = new StringBuffer();
        for (String str:args){
            stringBuffer.append(str);
            stringBuffer.append("-");
        }
        return stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
    }

    public static Date parseDateStirng(String dateString){
        try {
//            Tue Jan 01 08:00:00 CST 2019
            DateFormat dateFormat = new SimpleDateFormat("EEE MM dd HH:mm:ss zzz yyy");
            return DateUtils.addHours(dateFormat.parse(dateString),-8);
        }catch (ParseException e){
            log.error("解析日期错误 -> {}",dateString);
            return null;
        }
    }

}

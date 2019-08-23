package com.xl.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext ac;

    private static final Map<Class,Object> map = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.ac = applicationContext;
    }

//    为了不用再类中写太多的@Autowired注解
    public static <T> T of(Class<T> clazz){
        T o = (T) map.get(clazz);
        if(o!=null){
            return o;
        }
        o = bean(clazz);
        map.put(clazz,o);
        return (T) map.get(clazz);
    }

    public static <T> T bean(String beanName){
        return (T) ac.getBean(beanName);
    }

    public static <T> T bean(Class clazz){
        return (T) ac.getBean(clazz);
    }

//    最高优先级
    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }
}

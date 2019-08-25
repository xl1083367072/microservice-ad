package com.xl.ad.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

//对binlog返回的信息进行封装，包括header和data两部分
@Data
public class BinlogRowData {

    private TableTemplate tableTemplate;
//  事件类型
    private EventType eventType;

//    返回的添加修改和删除变更结果是一个数组里面又包含一个数组，
//    外层数组是一次可能修改多条记录也就是多个行，里层数组是每条记录可能修改了多个列
//    map记录修改的列和修改后的值，多个列进行修改，对应内层数组
//    list对应外层数组
//    update事件会有两个记录before即修改前的值和after即修改后的值，我们不关心before
    private List<Map<String, String>> before;

    private List<Map<String,String>> after;

}

package com.xl.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.xl.ad.mysql.TemplateHolder;
import com.xl.ad.mysql.dto.BinlogRowData;
import com.xl.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String dbName;
    private String tableName;

    @Autowired
    private TemplateHolder templateHolder;

//    数据库名:表名和对应监听器映射，对这个数据库和表感兴趣的监听器取处理
    private Map<String,BinlogListener> listenerMap = new HashMap<>();

//    对事件生成唯一标识，处理的哪个数据库哪个表
    private String genKey(String dbName,String tableName){
        return dbName + ":" + tableName;
    }

    public void register(String dbName, String tableName,BinlogListener listener){
        log.info("register {}-{}",dbName,tableName);
        listenerMap.put(genKey(dbName,tableName),listener);
    }

//  binlog事件到来调用此方法,event中包含header和data两个域
    @Override
    public void onEvent(Event event) {
//        header里面有event类型
        EventType type = event.getHeader().getEventType();
        log.debug("eventType -> {}",type);
//        如果是TABLE_MAP类型，记录下数据库名和表名，每个insert,update,delete之前都有一个TABLE_MAP事件
        if(type == EventType.TABLE_MAP){
            TableMapEventData data = event.getData();
            this.dbName = data.getDatabase();
            this.tableName = data.getTable();
//            等待下次事件继续处理
            return;
        }
//        下次事件到来
        if(StringUtils.isEmpty(dbName)||StringUtils.isEmpty(tableName)){
//            如果这次没有数据库名和表名，可能是发生了一些错误，也可能是不是我们要处理的事件
            log.error("元数据错误");
            return;
        }
        String key = genKey(dbName, tableName);
//        根据key查找对此事件感兴趣的监听器
        BinlogListener handleListener = this.listenerMap.get(key);
        if(handleListener == null){
            log.debug("跳过 {}",key);
            return;
        }
        try {
            BinlogRowData binlogRowData = convert(event.getData());
            binlogRowData.setEventType(type);
            handleListener.onEvent(binlogRowData);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
//        处理完置空
        this.dbName = "";
        this.tableName = "";
    }


    private BinlogRowData convert(EventData data){
        TableTemplate tableTemplate = templateHolder.getTableTemplate(tableName);
        if(tableTemplate==null){
            log.error("没有对应的表 -> {}",tableName);
            return null;
        }
        List<Map<String,String>> aftermapList = new ArrayList<>();
        List<Serializable[]> afterValues = getAfterValues(data);
//        这里是BinlogRowData中after的外层数组，也就是一条记录
        for (Serializable[] afterValue:afterValues){
//            一条记录有多个列-值组合
            Map<String,String> map = new HashMap<>();
            int colLen = afterValue.length;
            for (int i=0;i<colLen;i++){
                String colName = tableTemplate.getPosMap().get(i);
                if(colName==null){
                    log.debug("忽略此列 -> {}",colName);
                    continue;
                }
                String colValue = afterValue[i].toString();
                map.put(colName,colValue);
            }
            aftermapList.add(map);
        }
        BinlogRowData rowData = new BinlogRowData();
        rowData.setAfter(aftermapList);
        rowData.setTableTemplate(tableTemplate);
        return rowData;
    }

//    获得after部分，只有update有before和after之分
    private List<Serializable[]> getAfterValues(EventData eventData){
        if(eventData instanceof WriteRowsEventData){
            return ((WriteRowsEventData) eventData).getRows();
        }
//        update返回的是Entry<Serializable[], Serializable[]>，key是before,value是after
        if(eventData instanceof UpdateRowsEventData){
            return ((UpdateRowsEventData)eventData).getRows().stream()
                    .map(Map.Entry::getValue).collect(Collectors.toList());
        }
        if(eventData instanceof DeleteRowsEventData){
            return ((DeleteRowsEventData)eventData).getRows();
        }
        return Collections.emptyList();
    }

}

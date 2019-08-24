package com.xl.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.xl.ad.mysql.constant.Constant;
import com.xl.ad.mysql.constant.OpType;
import com.xl.ad.mysql.dto.BinlogRowData;
import com.xl.ad.mysql.dto.MySqlRowData;
import com.xl.ad.mysql.dto.TableTemplate;
import com.xl.ad.sender.Sender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements BinlogListener{

//  投递对象，有多种投递方式
    private Sender sender;


    private  final AggregationListener aggregationListener;

    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }


//    将所有的表和对应的处理监听器注册进AggregationListener中
    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener注册表信息");
        Constant.table2Db.forEach((dbName, tableName) -> aggregationListener.register(dbName,tableName,this));
    }

    @Override
    public void onEvent(BinlogRowData rowData) {
//        先封装成MysqlRowData再投递出去
        TableTemplate tableTemplate = rowData.getTableTemplate();
        EventType eventType = rowData.getEventType();
        MySqlRowData mySqlRowData = new MySqlRowData();
        mySqlRowData.setTableName(tableTemplate.getTableName());
        mySqlRowData.setLevel(tableTemplate.getLevel());
        OpType type = OpType.convert(eventType);
        mySqlRowData.setOpType(type);
//        什么类型操作哪些列
        List<String> fieldList = tableTemplate.getOpTypeFieldMap().get(eventType);
        if(fieldList==null){
            log.warn("不支持{}此类型处理{}",type,tableTemplate.getTableName());
            return;
        }
        for (Map<String,String> map:rowData.getAfter()){
            Map<String,String> _map = new HashMap<>();
            for (Map.Entry<String,String> entry:map.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                _map.put(key,value);
            }
            mySqlRowData.getFieldValueMap().add(_map);
        }
        sender.sender(mySqlRowData);
    }

}

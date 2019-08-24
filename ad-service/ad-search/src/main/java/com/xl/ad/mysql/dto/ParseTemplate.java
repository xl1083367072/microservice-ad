package com.xl.ad.mysql.dto;

import com.xl.ad.mysql.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

//封装了所有表的所有信息
@Data
public class ParseTemplate {

    private String database;

//    表名-封装的层级，对列的操作等
    private Map<String,TableTemplate> tableTemplateMap = new HashMap<>();

//    解析json结果
    public static ParseTemplate parse(Template template){
//        保存解析结果
        ParseTemplate parseTemplate = new ParseTemplate();
        parseTemplate.setDatabase(template.getDatabase());
        for (JsonTable table:template.getTableList()){
            String tableName = table.getTableName();
            Integer level = table.getLevel();
//            封装进TableTemplate
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(tableName);
            tableTemplate.setLevel(level.toString());
//            封装进ParseTemplate的map中
            parseTemplate.tableTemplateMap.put(tableName,tableTemplate);
//            对tableTemplateMap中的opTypeFieldMap进行填充
            Map<OpType, List<String>> opTypeFieldMap = tableTemplate.getOpTypeFieldMap();
            for (JsonTable.column column:table.getInsert()){
                getOrCreate(OpType.ADD, opTypeFieldMap, ArrayList::new).add(column.getColumn());
            }
            for (JsonTable.column column:table.getUpdate()){
                getOrCreate(OpType.UPDATE, opTypeFieldMap, ArrayList::new).add(column.getColumn());
            }
            for (JsonTable.column column:table.getDelete()){
                getOrCreate(OpType.DELETE, opTypeFieldMap, ArrayList::new).add(column.getColumn());
            }
        }
        return parseTemplate;
    }

    private static <T,R> R getOrCreate(T key, Map<T,R> map, Supplier<R> factory){
            return map.computeIfAbsent(key, K -> factory.get());
    }

}

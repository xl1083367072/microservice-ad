package com.xl.ad.dto;

import com.xl.ad.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//对应一张表的封装
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {

    private String tableName;

    private String level;

//    对这张表将操作和对应列做映射，也就是哪些列进行什么操作
    private Map<OpType, List<String>> opTypeFieldMap = new HashMap<>();
//  将返回的信息中，索引型字段映射为字段名
    private Map<Integer,String> posMap = new HashMap<>();

}

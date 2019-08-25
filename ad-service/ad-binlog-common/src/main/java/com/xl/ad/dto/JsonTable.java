package com.xl.ad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//将template.json中描述的信息封装成对象表示，这个对象封装的是tableList中的一个table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonTable {

//  操作的表名
    private String tableName;
//  操作第几层次索引
    private Integer level;
//  操作的列
    private List<column> insert;
    private List<column> update;
    private List<column> delete;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class column{
        private String column;
    }


}

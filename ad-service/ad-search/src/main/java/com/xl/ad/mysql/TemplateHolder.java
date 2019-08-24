package com.xl.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.xl.ad.mysql.constant.OpType;
import com.xl.ad.mysql.dto.ParseTemplate;
import com.xl.ad.mysql.dto.TableTemplate;
import com.xl.ad.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate parseTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String SQL_SCHEMA = "select table_schema, table_name, " +
            "column_name, ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";

//    初始化时就要加载进行解析
    @PostConstruct
    public void init(){
        loadJson("template.json");
    }

    public TableTemplate getTableTemplate(String tableName){
        return parseTemplate.getTableTemplateMap().get(tableName);
    }

    public void loadJson(String fileName){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        try {
//            加载json文件，文件中做了格式限制，封装进对象中
            Template template = JSON.parseObject(is, Charset.defaultCharset(), Template.class);
//            解析对象，结果封装进ParseTemplate中
            this.parseTemplate = ParseTemplate.parse(template);
            loadMeta();
        }catch (Exception e){
            log.error("加载json文件失败");
            throw new RuntimeException(e.getMessage());
        }
    }

//    封装TableTemplate中的posMap信息
    public void loadMeta(){
        for (Map.Entry<String, TableTemplate> entry:parseTemplate.getTableTemplateMap().entrySet()){
            TableTemplate tableTemplate = entry.getValue();
            List<String> addFields = tableTemplate.getOpTypeFieldMap().get(OpType.ADD);
            List<String> updateFields = tableTemplate.getOpTypeFieldMap().get(OpType.UPDATE);
            List<String> deleteFields = tableTemplate.getOpTypeFieldMap().get(OpType.DELETE);
            jdbcTemplate.query(SQL_SCHEMA,new Object[]{
                    parseTemplate.getDatabase(),
                    tableTemplate.getTableName()
            },(rs,i) -> {
                int index = rs.getInt("ORDINAL_POSITION");
                String columnName = rs.getString("COLUMN_NAME");
                if(addFields!=null&&addFields.contains(columnName)||
                        updateFields!=null&&updateFields.contains(columnName)||
                        deleteFields!=null&&deleteFields.contains(columnName)){
                    tableTemplate.getPosMap().put(index-1,columnName);
                }
                return null;
            });
        }
    }

}

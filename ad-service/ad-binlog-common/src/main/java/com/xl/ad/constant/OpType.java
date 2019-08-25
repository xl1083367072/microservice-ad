package com.xl.ad.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

//索引处理类型
public enum OpType {

    ADD,
    UPDATE,
    DELETE,
    OTHER;

    public static OpType convert(EventType eventType){
        switch (eventType){
            case EXT_WRITE_ROWS:
                return ADD;
            case EXT_UPDATE_ROWS:
                return UPDATE;
            case EXT_DELETE_ROWS:
                return DELETE;
            default:
                return OTHER;
        }
    }

}

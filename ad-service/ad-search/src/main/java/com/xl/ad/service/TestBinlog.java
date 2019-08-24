package com.xl.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

public class TestBinlog {

    public static void main(String[] args) throws Exception{
        BinaryLogClient client = new BinaryLogClient("192.168.31.145",3306,"root","root");
        client.registerEventListener(event -> {
            EventData data = event.getData();
            if(data instanceof UpdateRowsEventData){
                System.out.println("update");
                System.out.println(data.toString());
            }
            if(data instanceof WriteRowsEventData){
                System.out.println("write");
                System.out.println(data.toString());
            }
            if(data instanceof DeleteRowsEventData){
                System.out.println("delete");
                System.out.println(data.toString());
            }
        });
//        伪装成slave监听master的binlog
        client.connect();
    }

}

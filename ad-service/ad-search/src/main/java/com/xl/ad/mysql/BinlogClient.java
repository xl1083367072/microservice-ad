package com.xl.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.xl.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BinlogClient {

    private BinaryLogClient client;

    @Autowired
    private BinlogConfig config;

    @Autowired
    private AggregationListener aggregationListener;

    public void connect(){
        new Thread(() -> {
            client = new BinaryLogClient(config.getHost(),config.getPort(),
                    config.getUsername(),config.getPassword());
            if(!StringUtils.isEmpty(config.getBinlogName())&&!config.getPosition().equals(-1L)){
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
            client.registerEventListener(aggregationListener);
            try {
                log.info("开启连接mysql");
                client.connect();
                log.info("关闭连接mysql");
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

}

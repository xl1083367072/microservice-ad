package com.xl.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.xl.ad.mysql.dto.MySqlRowData;
import com.xl.ad.sender.Sender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaSender implements Sender {

    @Value("${adconf.kafka.topic}")
    private String topic;

    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public void sender(MySqlRowData rowData) {
        kafkaTemplate.send(topic, JSON.toJSONString(rowData));
    }

    @KafkaListener(topics = {"search.data"},groupId = "search")
    public void processMsg(ConsumerRecord record){
        Optional<Object> optional = Optional.ofNullable(record.value());
        if(optional.isPresent()){
            Object msg = optional.get();
            MySqlRowData mySqlRowData = JSON.parseObject(msg.toString(), MySqlRowData.class);
            System.out.println("收到一条消息:" + JSON.toJSONString(mySqlRowData));
        }
    }
}

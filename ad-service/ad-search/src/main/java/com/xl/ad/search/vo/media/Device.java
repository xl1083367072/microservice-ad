package com.xl.ad.search.vo.media;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

//    机器码
    private String deviceCode;

//    mac地址
    private String mac;

//    ip地址
    private String ip;

//    机型编码
    private String model;

//    分辨率尺寸
    private String displaySize;

//    屏幕尺寸
    private String screenSize;

//    设备序列号
    private String serialName;

}


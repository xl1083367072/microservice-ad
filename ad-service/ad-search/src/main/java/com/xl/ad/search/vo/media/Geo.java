package com.xl.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geo {

//  纬度
    private Float latitude;
//    经度
    private Float longitude;

    private String city;
    private String province;

}

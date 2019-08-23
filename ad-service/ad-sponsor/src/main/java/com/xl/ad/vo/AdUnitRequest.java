package com.xl.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitRequest {

    private Long planId;
    private String unitName;
    private Integer positionType;
    private Long budget;

    public boolean validate(){
        return planId!=null&& !StringUtils.isEmpty(unitName)
                &&positionType!=null&&budget!=null;
    }

}

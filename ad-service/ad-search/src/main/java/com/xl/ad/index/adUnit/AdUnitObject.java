package com.xl.ad.index.adUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {

    private Long unitId;
    private Long planId;
    private Integer unitStatus;
    private Integer positionType;

    public void update(AdUnitObject newObject){
        if(newObject.getUnitId()!=null){
            this.unitId = newObject.getUnitId();
        }
        if(newObject.getPlanId()!=null){
            this.planId = newObject.getPlanId();
        }
        if(newObject.getUnitStatus()!=null){
            this.unitStatus = newObject.getUnitStatus();
        }
        if(newObject.getPositionType()!=null){
            this.positionType = newObject.getPositionType();
        }
    }

}

package com.xl.ad.index.adUnit;

import com.xl.ad.index.adPlan.AdPlanObject;
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
    private AdPlanObject adPlanObject;

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

    public static boolean isKaiPing(int positionType){
        return (positionType & AdUnitConstants.POSITIONTYPE.KAIPING) > 0;
    }

    public static boolean isTIEPIAN(int positionType){
        return (positionType & AdUnitConstants.POSITIONTYPE.TIEPIAN) > 0;
    }

    public static boolean isTIEPIAN_MIDDLE(int positionType){
        return (positionType & AdUnitConstants.POSITIONTYPE.TIEPIAN_MIDDLE) > 0;
    }

    public static boolean isTIEPIAN_PAUSE(int positionType){
        return (positionType & AdUnitConstants.POSITIONTYPE.TIEPIAN_PAUSE) > 0;
    }

    public static boolean isTIEPIAN_POST(int positionType){
        return (positionType & AdUnitConstants.POSITIONTYPE.TIEPIAN_POST) > 0;
    }

//    判断广告位类型和推广单元类型是否一致
    public static boolean isAdSlotType(Integer adSlotType,Integer positionType){
        switch (adSlotType){
            case AdUnitConstants.POSITIONTYPE.KAIPING:
                return isKaiPing(positionType);
            case AdUnitConstants.POSITIONTYPE.TIEPIAN:
                return isTIEPIAN(positionType);
            case AdUnitConstants.POSITIONTYPE.TIEPIAN_MIDDLE:
                return isTIEPIAN_MIDDLE(positionType);
            case AdUnitConstants.POSITIONTYPE.TIEPIAN_PAUSE:
                return isTIEPIAN_PAUSE(positionType);
            case AdUnitConstants.POSITIONTYPE.TIEPIAN_POST:
                return isTIEPIAN_POST(positionType);
            default:
                return false;
        }
    }

}

package com.xl.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitRequest {

    private List<UnitItems> unitItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
   public static class UnitItems{
       private Long id;

       private Long creativeId;

       private Long unitId;
   }

}

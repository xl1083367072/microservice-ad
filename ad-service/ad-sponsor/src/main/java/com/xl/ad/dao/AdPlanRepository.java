package com.xl.ad.dao;

import com.xl.ad.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdPlanRepository extends JpaRepository<AdPlan,Long> {

    AdPlan findByIdAndUserId(Long id,Long userId);

    List<AdPlan> findAllByIdAndUserId(List<Long> ids,Long userId);

    AdPlan findByUserIdAndPlanName(Long userId,String planName);

    List<AdPlan> findAllByPlanStatus(Integer planStatus);

}

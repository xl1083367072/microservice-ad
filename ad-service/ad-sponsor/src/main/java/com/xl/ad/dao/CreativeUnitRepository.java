package com.xl.ad.dao;

import com.xl.ad.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreativeUnitRepository extends JpaRepository<AdUnit,Long> {
}

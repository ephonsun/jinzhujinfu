package com.canary.finance.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.PayBank;
import com.canary.finance.domain.RegionCode;

public interface PayBankDao {
	List<PayBank> selectByStatus(@Param("status") int status);
	PayBank selectById(int id);
	PayBank selectByBankNO(@Param("bankNO")String bankNO);
	List<RegionCode> queryForProvinces();
	List<RegionCode> queryForCities(@Param("provinceId")String provinceId);
}
package com.canary.finance.orm;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.BranchBank;

public interface BranchBankDao {
	BranchBank selectByBankNOAndCityId(@Param("bankNO")String bankNO, @Param("cityId")String cityId);
}

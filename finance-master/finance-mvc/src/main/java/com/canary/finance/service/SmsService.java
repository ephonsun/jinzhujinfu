package com.canary.finance.service;

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.enumeration.SmsPatternEnum;

public interface SmsService {
	JSONObject send(String cellphone, String type, SmsPatternEnum pattern);
}
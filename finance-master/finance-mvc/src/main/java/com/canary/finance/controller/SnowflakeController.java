package com.canary.finance.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.canary.finance.pojo.ResponseDTO;
import com.canary.finance.repo.SnowflakeDistributedIdRepository;

@RestController
public class SnowflakeController extends BaseController {
	@Autowired
	private SnowflakeDistributedIdRepository idWorker;
	
	@PostMapping("/distributed/id")
	public ResponseDTO<Long> getSnowflakeId() throws Exception {
		ResponseDTO<Long> response = new ResponseDTO<>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setData(this.idWorker.nextValue());
		
		return response;
	}
}

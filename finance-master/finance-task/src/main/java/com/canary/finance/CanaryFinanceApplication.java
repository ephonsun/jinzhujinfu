package com.canary.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.canary.finance.config.CanaryFinanceProperties;
import com.canary.finance.orm.PaymentDao;
import com.canary.finance.task.PaymentHttpsTask;

@SpringBootApplication
@EnableConfigurationProperties(CanaryFinanceProperties.class)
@MapperScan("com.canary.finance.orm")
public class CanaryFinanceApplication implements CommandLineRunner {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private CanaryFinanceProperties properties;

	public static void main(String[] args) {
		SpringApplication.run(CanaryFinanceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PaymentHttpsTask httpsTask = new PaymentHttpsTask(paymentDao, properties);
		httpsTask.execute();
	}
}
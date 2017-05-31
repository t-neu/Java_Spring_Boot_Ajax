package com.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.controller.JDBCTemplate;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

   @Scheduled(fixedRate = 5000000)
   public void scheduledTask() {
	@SuppressWarnings("resource")
	ApplicationContext contextScheduler = new ClassPathXmlApplicationContext("Beans.xml");
	JDBCTemplate JDBC_Template = (JDBCTemplate) contextScheduler.getBean("JDBC_Template");
	
		JDBC_Template.deletePasswordRecovery();
   }

}
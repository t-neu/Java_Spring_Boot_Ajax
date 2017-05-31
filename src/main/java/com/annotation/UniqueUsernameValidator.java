package com.annotation;

import javax.validation.ConstraintValidator;
import com.controller.JDBCTemplate;
import javax.validation.ConstraintValidatorContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	
	@Override
	public void initialize(UniqueUsername constraintAnnotation) {
	}

	@Override
	public boolean isValid(String UniqueUsername, ConstraintValidatorContext context){
		@SuppressWarnings("resource")
		ApplicationContext context_jdbc = new ClassPathXmlApplicationContext("Beans.xml");
		JDBCTemplate JDBC_Template = (JDBCTemplate) context_jdbc.getBean("JDBC_Template");
		int answer = JDBC_Template.testUsername(UniqueUsername);
		if(answer == 1){
			return false;
		}else{
			return true;
		}
		
	}
}

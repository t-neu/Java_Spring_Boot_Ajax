package com.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.controller.JDBCTemplate;


public class MustBeEmailValidator implements ConstraintValidator<MustBeEmail, String> {

	
	@Override
	public void initialize(MustBeEmail constraintAnnotation) {
	}

	@Override
	public boolean isValid(String MustBeEmail, ConstraintValidatorContext context){
		@SuppressWarnings("resource")
		ApplicationContext context_jdbc = new ClassPathXmlApplicationContext("Beans.xml");
		JDBCTemplate JDBC_Template = (JDBCTemplate) context_jdbc.getBean("JDBC_Template");	
		int answer = JDBC_Template.testEmail(MustBeEmail);
		if(answer == 1){
			return false;
		}else{
			return true;
		}
	}
}

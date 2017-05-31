package com.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class JDBCTemplate implements DAO {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public void createUser(String username, String email, String password) {


		String SQL = "insert into users (username, email, password, enabled) values (?, ?, ?, ?)";

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		String hashedPassword = passwordEncoder.encode(password);

		jdbcTemplateObject.update(SQL, username, email, hashedPassword, 1);

		String SQL2 = "insert into user_roles (username, role) values (?, 'ROLE_USER')";
		
		jdbcTemplateObject.update(SQL2, username);

		return;
	}

	public void deletePasswordRecovery() {

		String SQL = "DELETE FROM password_recovery WHERE creation < DATE_SUB(NOW(),INTERVAL 45 MINUTE)";
		
		jdbcTemplateObject.update(SQL);
	
	}

	public List<Dropdown> ajaxLocationDropdown(String term) {

		String SQL = "SELECT DISTINCT CONCAT(city, ', ', state) AS dropdown FROM zips where concat_ws(', ', city, state) like '"
				+ term + "%' LIMIT 10";

		List<Dropdown> dropdown = jdbcTemplateObject.query(SQL, new Object[] {}, new DropdownMapper());

		return dropdown;
	}

	public List<Dropdown> ajaxLocationZipDropdown(String term) {

		String SQL = "select zipcode as dropdown from zips where zipcode like '" + term + "%' LIMIT 10";
		
		List<Dropdown> dropdown = jdbcTemplateObject.query(SQL, new Object[] {}, new DropdownMapper());

		return dropdown;
	}
	
	public Integer testUsername(String Username) {
		
		String sql = "SELECT count(*) FROM users WHERE username = '" + Username + "'";
		
		return jdbcTemplateObject.queryForInt(sql);
	}

	public Integer testEmail(String Email) {
		
		String sql = "SELECT count(*) FROM users WHERE email = '" + Email + "'";
		
		return jdbcTemplateObject.queryForInt(sql);
	}
	
	public void resetPassword(String username, String key) {
		
		String sql = "insert into password_recovery (username, pass_key) values(?, ?)";
		
		jdbcTemplateObject.update(sql, username, key);
		
		return;
	}

	public void newPassword(String password, String username) {

		String SQL = "update users set password = ? where username = ?";
		
		jdbcTemplateObject.update(SQL, password, username);

		String SQL2 = "delete from password_recovery where username = ?";
		
		jdbcTemplateObject.update(SQL2, username);

		return;
	}

	public String checkKey(String key) {
		
		try {
			
			Timestamp timestamp = new Timestamp(new Date().getTime());

			Calendar cal = Calendar.getInstance();
			
			cal.setTimeInMillis(timestamp.getTime());
			
			cal.setTimeInMillis(timestamp.getTime());
			
			cal.add(Calendar.DAY_OF_MONTH, +1);
			
			timestamp = new Timestamp(cal.getTime().getTime());
			
			String sql = "SELECT username FROM password_recovery WHERE pass_key = '" + key + "' AND creation <= '"
					+ timestamp + "'";
			
			List<String> li = jdbcTemplateObject.queryForList(sql, String.class);
			
			return li.get(0).toString();
			
		} catch (IndexOutOfBoundsException e) {
			
			return null;
			
		}

	}

	public String getEmailFromUsername(String username) {
		
		try {
			
			String sql = "SELECT email FROM users WHERE username = '" + username + "'";
			
			List<String> li = jdbcTemplateObject.queryForList(sql, String.class);
			
			return li.get(0).toString();
			
		} catch (IndexOutOfBoundsException e) {
			
			return null;
			
		}

	}
	
	public void emailOpenedUpdate(Integer Id) {
		
		String SQL = "update email_opened set opened = 1 where id = ?";
		
		jdbcTemplateObject.update(SQL, Id);

		return;
	}
	
	public long emailOpenedInsert(String type, String username) {
		
		final String sql = "insert into email_opened (type, username) values (?, ?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				
				PreparedStatement pst = con.prepareStatement(sql, new String[] { "id" });
				
				pst.setString(1, type);
				
				pst.setString(2, username);
				
				return pst;
				
			}
			
		}, keyHolder);
		
		return (Long) keyHolder.getKey();
		
	}

	public void sendEmail(String username, String email, String type, String other) throws AmazonClientException {

		//you must verify the from address with amazon
		final String FROM = "noreply@yourwebsite.com";
		final String TO = email;
		String Username = username;
		String MainMessage = "Message";
		String ActionText = "Get Started!";
		String ActionLink = "http://www.yourwebsite.com/login";
		String SUBJECT = "yourwebsite Confirmation";
		
		long id = emailOpenedInsert(type, username);
		
		switch (type) {
		case "registration":
			MainMessage = "Thank you, for registering!";
			ActionText = "Get Started!";
			ActionLink = "http://www.yourwebsite.com/login";
			SUBJECT = "yourwebsite Registration Confirmation";

			break;
		case "pw_reset":
			MainMessage = "You have 24 hours to visit the link below and change your password.";
			ActionText = "Reset Password";
			ActionLink = "http://www.yourwebsite.com/reset/" + other;
			SUBJECT = "yourwebsite Reset";
			break;

		default:

			break;
		}

		String footer = "<h6>&copy; 2016 yourwebsite LLC. All Rights Reserved</h6>";
		
		String content = "<td colspan=\"3\" style=\"color:#000;background-color:#FFF;text-align:center; padding: 20px; border-radius: 5px;\"><h3 alink=\"green\" style=\"color:#0E9948;\">Hello, "
				+ Username + "!</h3><p style=\"font-size:14px;\">" + MainMessage + "</p></td>";

		final String BODY = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-family:Gotham, 'Helvetica Neue', Helvetica, Arial, sans-serif\"> <tbody> <tr> <td colspan=\"5\" align=\"center\" bgcolor=\"#0E9948\"> <img src=\"https://www.yourwebsite.com/email/logo/"+ id +"\" width=\"150\" height=\"150\" alt=\"yourwebsite\" style=\"display: block;\"/> </td></tr><tr bgcolor=\"#f9f9f9\"> <td colspan=\"5\" style=\"height:30px;\"> </td></tr><tr bgcolor=\"#f9f9f9\"> <td style=\"width: 10%;\"></td>"
				+ content
				+ "<td style=\"width: 10%;\"></td></tr><tr bgcolor=\"#f9f9f9\" style=\"height:100px;\"> <td style=\"width: 10%;\"></td><td colspan=\"3\" style=\"text-align:center;\"><a href=\""
				+ ActionLink
				+ "\" style=\"text-decoration: none;\"><span style=\"width: 200px;color:#fff;background-color:#0E9948;border-radius:10px;margin:20px;text-align:center;padding:20px;font-weight:bold;\">"
				+ ActionText
				+ "</span></a></td><td style=\"width: 10%;\"></td></tr><tr bgcolor=\"#f9f9f9\"> <td colspan=\"5\" style=\"height:30px;\"> </td></tr><tr bgcolor=\"#0E9948\" style=\"color: #fff; padding: 15px;\"> <td style=\"width: 10%;\"></td><td colspan=\"3\" align=\"center\" bgcolor=\"#0E9948\"> <img src=\"https://www.yourwebsite.com/email/location/png/\" width=\"280\" height=\"auto\" alt=\"Genital Genie\" style=\"display: block;\"/> </td><td style=\"width: 10%;\"></td></tr><tr bgcolor=\"#f9f9f9\"> <td style=\"width: 10%;\"></td><td colspan=\"3\" align=\"center\" style=\"color:#666;text-align:center;\"><br><h6><a style=\"color:#0E9948;\" href=\"https://www.yourwebsite.com/profile\">Click here</a> to unsubscribe or edt your email preferences.</h6><h6>Please do not respond to this email, since we are not able to respond to emails sent to this address.</h6><h6>Please visit our <a style=\"color:#0E9948;\" href=\"https://www.yourwebsite.com/faq\">Frequently Asked Questions</a> section for more information.</h6>"
				+ footer + "</td><td style=\"width: 10%;\"></td></tr></tbody></table>";

		SendEmailRequest request = new SendEmailRequest().withSource("noresponse@yourwebsite.com");

		List<String> toAddresses = new ArrayList<String>();
		
		//add email here for testing
		
		//toAddresses.add("personal-email@testing.com");
		
		toAddresses.add(TO);
		
		Destination dest = new Destination().withToAddresses(toAddresses);
		
		request.setDestination(dest);

		Content subjContent = new Content().withData(SUBJECT);
		
		Message msg = new Message().withSubject(subjContent);

		Content htmlContent = new Content().withData(BODY);
		
		Content textContent = new Content().withData("This is text");
		
		Body bodyContent = new Body().withHtml(htmlContent).withText(textContent);
		
		msg.setBody(bodyContent);

		request.setMessage(msg);

		AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(
				new BasicAWSCredentials("AKIAJ56R56DBPTDGXPMA", "h+Wqh1AHig2kHvTwoqQIWaM2tF/6XQmOcGb+PUat"));
		try {
			
			client.sendEmail(request);
			
		} catch (AmazonClientException e) {
			
			System.out.println(e.getMessage());
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}

}
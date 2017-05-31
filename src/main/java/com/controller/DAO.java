package com.controller;

import java.util.List;
import javax.sql.DataSource;

public interface DAO {

   public void setDataSource(DataSource ds);

   public void createUser(String username, String email, String password); 
   
   public void resetPassword(String username, String key);
   
   public String getEmailFromUsername(String username);
   
   public String checkKey(String key);
   
   public void newPassword(String password, String username);
   
   public void sendEmail(String username, String email, String type, String other);
   
   public Integer testUsername(String Username);
   
   public Integer testEmail(String Email);
   
   public List<Dropdown> ajaxLocationDropdown(String term);
   
   public List<Dropdown> ajaxLocationZipDropdown(String term);
   
   public long emailOpenedInsert(String type, String username);
   
   public void emailOpenedUpdate(Integer id);
   
}
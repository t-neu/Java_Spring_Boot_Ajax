package com.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ZipCodeMapper implements RowMapper<Zipcode> {
   public Zipcode mapRow(ResultSet rs, int rowNum) throws SQLException {
	   
	  Zipcode zipcode = new Zipcode();
      zipcode.setZipcode(rs.getString("zipcode"));
      zipcode.setAbr(rs.getString("abr"));
      zipcode.setLatitude(rs.getString("latitude"));
      zipcode.setLongitude(rs.getString("longitude"));
      zipcode.setCity(rs.getString("city"));
      zipcode.setState(rs.getString("state"));
      
      return zipcode;
   }
}
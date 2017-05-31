package com.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class DropdownMapper implements RowMapper<Dropdown> {
   public Dropdown mapRow(ResultSet rs, int rowNum) throws SQLException {
	   Dropdown dropdown = new Dropdown();
	   dropdown.setDropdown(rs.getString("dropdown"));
       return dropdown;
   }
}
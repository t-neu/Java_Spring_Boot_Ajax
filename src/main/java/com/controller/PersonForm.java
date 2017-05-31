package com.controller;

import javax.validation.constraints.NotNull;

import com.annotation.CheckForUsername;

public class PersonForm {

	@NotNull
	@CheckForUsername(message = "Sorry, there is no account with that username.")
    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

	@Override
	public String toString() {
		return "PersonForm [username=" + username + "]";
	}
    

}
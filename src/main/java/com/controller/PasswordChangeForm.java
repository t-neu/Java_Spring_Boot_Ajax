package com.controller;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PasswordChangeForm {

    @NotNull
    @Pattern(regexp = "(?=^.{8,30}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;&quot;:;'?/&gt;.&lt;,]).*$", message = "Invalid Password")
    private String password;
    
    @NotNull
    @Size(min=2, max=30)
    private String password2;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	@Override
	public String toString() {
		return "PasswordChangeForm [password=" + password + ", password2=" + password2 + "]";
	}

}
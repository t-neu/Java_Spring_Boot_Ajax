package com.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.annotation.MustBeEmail;
import com.annotation.UniqueUsername;

public class SignUpForm {

    private String username;
    
    @NotNull
    @Size(min=7, max=35)
    @Email(message = "Please insert an @ symbol.")
    @Pattern(regexp=".+@.+\\..+", message=". after the @ symbol, example .com")
    @MustBeEmail(message = "Sorry, this email is already being used.")
    private String email;

    @NotNull(message = "Password cannot be empty.")
    @Pattern(regexp = "(?=^.{8,30}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;&quot;:;'?/&gt;.&lt;,]).*$", message = "Invalid Password")
    private String password;

    public String getUsername() {
        return this.username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return this.email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "User(Name: " + this.username + ", Password: " + this.password + ", Email: " + this.email + ")";
    }
}

package com.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.IOUtils;

@Controller
public class WebController extends WebMvcConfigurerAdapter {
	
	ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
	JDBCTemplate JDBC_Template = (JDBCTemplate) context.getBean("JDBC_Template");
	
	// AWS INFORMATION
	AWSCredentials credentials = new BasicAWSCredentials("",
			"");

	// create a client connection based on credentials
	AmazonS3 s3client = new AmazonS3Client(credentials);


	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		registry.addViewController("/results").setViewName("results");
		
	}
	
	@GetMapping("/logout")
	public String logout(Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		redirectAttributes.addFlashAttribute("pageTitle", "Login");
		
		return "redirect:/login?logout=true";
	}
	
	@GetMapping("/login")
	public String loginForm(Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		model.addAttribute("pageTitle", "Login");
		
		if (null == principal) return "login";
		
		redirectAttributes.addFlashAttribute("pageTitle", "Homepage");
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/validate/email", method = RequestMethod.GET)
	public @ResponseBody
	
	int checkEmail(@RequestParam(value = "val") String parse) {
		
		int answer = JDBC_Template.testEmail(parse);
	    
	return answer;
	
	}
	
	@RequestMapping(value = "/dropdown/ajax", method = RequestMethod.GET)
	public @ResponseBody
	List<Dropdown> dropdownSearch(@RequestParam(value = "val") String parse) {
		
		List<Dropdown> dropdown = JDBC_Template.ajaxLocationDropdown(parse);
		
		if(dropdown.isEmpty()){
			
			dropdown = JDBC_Template.ajaxLocationZipDropdown(parse);
			
		}
		
	return dropdown;
	
	}

	@GetMapping("/")
	public String siteIndex(Model model) {
		
		model.addAttribute("pageTitle", "Homepage");
		
		return "homepage";
	}
	
	@GetMapping("/reset")
	public String resetGet(ResetPasswordForm resetPasswordForm, Model model) {
		
		model.addAttribute("pageTitle", "Reset Password");
		
		return "reset";
	}
	
	@GetMapping("/reset/{key}")
	public String checkKey(PasswordChangeForm passwordChangeForm, @PathVariable String key, Model model, RedirectAttributes redirectAttributes) {
		
		model.addAttribute("pageTitle", "Reset Password");

		String answer;

		answer = JDBC_Template.checkKey(key);
			
		if (answer == null){
			
			redirectAttributes.addFlashAttribute("message", "notfound");
			
			return "redirect:/reset";
			
		}else{
			
			model.addAttribute("success", "found");
			
			model.addAttribute("key", key);
			
			return "reset_password_completion";
		}
		
	}
	
	
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String resetPost(@Valid ResetPasswordForm resetPasswordForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		
		model.addAttribute("pageTitle", "Reset Password");
		if (bindingResult.hasErrors()) {
            return "reset";
        }
		
		//Generating 20 Characters for the reset link
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode("randomword");
		hashedPassword = hashedPassword.replace('.', 'l');
		hashedPassword = hashedPassword.replace('/', '2');
		hashedPassword = hashedPassword.replace('$', '9');
		String first20 = hashedPassword.substring(0, 20);

		redirectAttributes.addFlashAttribute("message", "success");
		
		String email = JDBC_Template.getEmailFromUsername(resetPasswordForm.getUsername());
		
		JDBC_Template.resetPassword(resetPasswordForm.getUsername(), first20);
		
		JDBC_Template.sendEmail(resetPasswordForm.getUsername(), email, "pw_reset", first20);
		
		return "redirect:/reset";
	}
	
	@RequestMapping(value = "/reset/{key}", method = RequestMethod.POST)
	public String passwordChange(@Valid PasswordChangeForm passwordChangeForm, BindingResult bindingResult, Model model, @PathVariable String key, RedirectAttributes redirectAttributes) {
		
		model.addAttribute("pageTitle", "Reset Password");
		
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("success", "found");
			
			model.addAttribute("key", key);
			
			return "reset_password_completion";
			
        }
		
		if(!(passwordChangeForm.getPassword().equals(passwordChangeForm.getPassword2()))){
			
			redirectAttributes.addFlashAttribute("error", "error");
			
			return "redirect:/reset/{key}";
			
		}else{
			String answer = JDBC_Template.checkKey(key);
			
			if (answer == null){
				
				model.addAttribute("error", "error");
				
			}else{

				redirectAttributes.addFlashAttribute("message", "success_change");
				
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				
				String hashedPassword = passwordEncoder.encode(passwordChangeForm.getPassword());
				
				JDBC_Template.newPassword(hashedPassword, answer);
				
				return "redirect:/login";
			}
		}
		
		return "reset";
	}	

	@GetMapping("/signup")
	public String showForm(SignUpForm signUpForm, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		model.addAttribute("pageTitle", "Sign Up");
		
		if (null == principal) return "signup";
		
		redirectAttributes.addFlashAttribute("pageTitle", "Profile");
		
		return "redirect:/profile";
	}

	@PostMapping("/signup")
	public String checkPersonInfo(@Valid SignUpForm signUpForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		if (bindingResult.hasErrors()) {
			
			model.addAttribute("pageTitle", "Sign Up");
			
			return "signup";
		}
		
		try {
			
			JDBC_Template.createUser(signUpForm.getEmail(), signUpForm.getEmail(), signUpForm.getPassword());
		
		} catch (Exception e) {
			
			model.addAttribute("error", "Sorry, the Username already exists. " + e);
			
			return "signup";
		}
		
		JDBC_Template.sendEmail(signUpForm.getEmail(), signUpForm.getEmail(), "registration", "");
		
		try {
			
			//auto login on sign up
			
			request.login(signUpForm.getEmail(),signUpForm.getPassword());
			
			redirectAttributes.addFlashAttribute("pageTitle", "Homepage");
			
			return "redirect:/";
			
		} catch (ServletException e) {
			
			e.printStackTrace();
			
		}
		
		redirectAttributes.addFlashAttribute("message", "signup");
		
		redirectAttributes.addFlashAttribute("pageTitle", "Sign Up");
		
		return "redirect:/login";
	}
	
	@Autowired
    ServletContext servletContext;
	
	@ResponseBody
	@RequestMapping(value = "/email/logo/{Id}", method = RequestMethod.GET, produces = MediaType.IMAGE_GIF_VALUE)
	public byte[] testphoto(@PathVariable String Id) throws IOException {  
		
		InputStream in = servletContext.getClass().getClassLoader().getResourceAsStream("static/images/logo.gif");
		
		int intId = Integer.parseInt(Id);
		
		try{
			
			JDBC_Template.emailOpenedUpdate(intId);
		
		}catch (NullPointerException e){
		
		}
		
		return IOUtils.toByteArray(in);
	}
	
	@ResponseBody
	@RequestMapping(value = "/email/{name}/{type}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] location(@PathVariable String name, @PathVariable String type) throws IOException {  
		
		InputStream in = servletContext.getClass().getClassLoader().getResourceAsStream("static/images/email/" + name + "." + type);
		
		return IOUtils.toByteArray(in);
	}

}
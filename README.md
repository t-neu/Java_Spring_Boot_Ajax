# Project Title

Java Spring Boot Login and Passowrd Reset

![alt text](https://github.com/t-neu/Java_Spring_Boot_Ajax/blob/master/homepage.jpg?raw=true)

## Sign Up Page

![alt text](https://github.com/t-neu/Java_Spring_Boot_Ajax/blob/master/signup.jpg?raw=true)

Uses both front-end and backend validation.
The username field also uses JQuery and an Ajax call to find out if the username is already been used.

## Built With

Java
Spring
Boot
AWS
JQuery
MySql
Hibernate Validation
Themeleaf
Maven
JDBC

## Instructions

Edit the AuthenticationProviderConfig.java file.
driverManagerDataSource.setUrl("Database information");
driverManagerDataSource.setUsername("admin");
driverManagerDataSource.setPassword("rootroot");

## Getting Started
Edit the Beans.xml file.
<property name="url" value="Database information"/>
<property name="username" value="username"/>
<property name="password" value="password"/>

Edit the application.properties file.
cloud.aws.credentials.accessKey = 
cloud.aws.credentials.secretKey = 

## Authors

Thomas Neumeyer

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

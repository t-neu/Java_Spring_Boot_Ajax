# Project Title

Java Spring Boot Login and Password Reset

![alt text](https://github.com/t-neu/Java_Spring_Boot_Ajax/blob/master/homepage.jpg?raw=true)

## Sign Up Page

![alt text](https://github.com/t-neu/Java_Spring_Boot_Ajax/blob/master/signup.jpg?raw=true)

Uses both front-end and backend validation.
The username field also uses JQuery and an Ajax call to find out if the username is already been used.

## Built With

Java<br />
Spring<br />
Boot<br />
AWS<br />
JQuery<br />
MySql<br />
Hibernate Validation<br />
Themeleaf<br />
Maven<br />
JDBC<br />

## Instructions

Edit the AuthenticationProviderConfig.java file.<br />
driverManagerDataSource.setUrl("Database information");<br />
driverManagerDataSource.setUsername("admin");<br />
driverManagerDataSource.setPassword("rootroot");

## Getting Started
Edit the Beans.xml file.<br />
<property name="url" value="Database information"/><br />
<property name="username" value="username"/><br />
<property name="password" value="password"/><br />

Edit the application.properties file.<br />
cloud.aws.credentials.accessKey = <br />
cloud.aws.credentials.secretKey = <br />

## Authors

Thomas Neumeyer

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

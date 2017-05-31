# Java Spring Boot Login and Password Reset

## Homepage

![alt text](https://github.com/t-neu/Java_Spring_Boot_Ajax/blob/master/homepage.jpg?raw=true)

## Sign Up Page

![alt text](https://github.com/t-neu/Java_Spring_Boot_Ajax/blob/master/signup.jpg?raw=true)

Uses both front-end and back-end validation.

## Videos

#### JQuery Autocomplete Search & Login

[![Youtube](https://i.ytimg.com/vi/Kklw_QDzCq4/1.jpg?time=1496251253919)](https://youtu.be/Kklw_QDzCq4)

#### Password Must Include

[![Youtube](https://i.ytimg.com/vi/SPjyb1dSKLk/2.jpg?time=1496251253919)](https://youtu.be/SPjyb1dSKLk)

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
REST<br />

## Instructions

### Beginner <br />

#### Edit the AuthenticationProviderConfig.java file. <br />
Go into AWS, setup RDS. (URL, USERNAME, PASSWORD)<br />
<br />
driverManagerDataSource.setUrl("Database information"); <br />
driverManagerDataSource.setUsername("username"); <br />
driverManagerDataSource.setPassword("password");

#### Edit the Beans.xml file. <br />
Go into AWS, setup RDS. (URL, USERNAME, PASSWORD) <br />
<br />
property name="url" value="Database information" <br />
property name="username" value="username" <br />
property name="password" value="password" <br />

#### Edit the application.properties file. <br />
Go into AWS, setup IAM. <br />
<br />
cloud.aws.credentials.accessKey = <br />
cloud.aws.credentials.secretKey = <br />

### Advanced <br />

#### Edit the JDBCTemplate.java <br />
Go into AWS, signup for SES and verify your email address.

AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient( <br />
				new BasicAWSCredentials("", "")); <br />
				
#### Response Body <br />

## Authors

Thomas Neumeyer

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

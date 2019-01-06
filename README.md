

## How to use ?

````
<dependency>
			<groupId>com.revengemission.commons</groupId>
			<artifactId>captcha</artifactId>
			<version>0.4</version>
</dependency>
````
````

File file = new File("F:\\verifies\\captcha.gif");
VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file), 4, VerificationCodeMode.MIXGIF);

````

### todo list 
````
Javadoc
mvn checkstyle:checkstyle
````

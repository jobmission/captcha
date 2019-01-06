

## How to use ?

````
<dependency>
			<groupId>com.revengemission.commons</groupId>
			<artifactId>captcha</artifactId>
			<version>0.4</version>
</dependency>
````
````

File file = new File("D:\\captcha.gif");
String code = VerificationCodeUtil.generateVerificationCode(4, null);
VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file), code, VerificationCodeMode.MIXGIF);

````

### todo list 
````
Javadoc
mvn checkstyle:checkstyle
````
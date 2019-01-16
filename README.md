

## How to use ?

````
<dependency>
			<groupId>com.revengemission.commons</groupId>
			<artifactId>captcha</artifactId>
			<version>0.5</version>
</dependency>
````
````

File file = new File("D:\\captcha.gif");
String code = VerificationCodeUtil.generateVerificationCode(4);
VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file), code, VerificationCodeMode.MIXGIF);

````
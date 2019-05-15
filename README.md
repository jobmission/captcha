

## How to use ?

````
<dependency>
	<groupId>com.revengemission.commons</groupId>
	<artifactId>captcha</artifactId>
	<version>0.7</version>
</dependency>
````

````
// 默认字体IBMPlexSans-Thin.ttf，可以在classpath下放入captcha.ttf进行覆盖

int w = 150, h = 48;
String code = VerificationCodeUtil.generateVerificationCode(4);

File file = new File("/tmp/captcha.gif");
VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file), code, VerificationCodeMode.MIXGIF);

File file1 = new File("/tmp/captcha1.jpg");
VerificationCodeUtil.outputImage(w, h, file1, code, VerificationCodeMode.NORMAL);
````
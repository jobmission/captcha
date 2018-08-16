package com.revengemission.commons.captcha.test;


import com.revengemission.commons.captcha.core.VerificationCodeMode;
import com.revengemission.commons.captcha.core.VerificationCodeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CaptchaTest {

    public static void main(String[] args) throws IOException {
        File dir = new File("F:\\verifies8");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        int w = 150, h = 48;
        File file1 = new File(dir, 1 + ".jpg");
        File file2 = new File(dir, 2 + ".jpg");
        File file3 = new File(dir, 3 + ".gif");
        File file4 = new File(dir, 4 + ".gif");
        File file5 = new File(dir, 5 + ".jpg");
        File file6 = new File(dir, 6 + ".gif");
        VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file1), 4, VerificationCodeMode.NORMAL);
        VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file2), 4, VerificationCodeMode.D3);
        VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file3), 4, VerificationCodeMode.GIF);
        VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file4), 4, VerificationCodeMode.GIF3D);
        VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file5), 4, VerificationCodeMode.MIX);
        VerificationCodeUtil.outputImage(w, h, new FileOutputStream(file6), 4, VerificationCodeMode.MIXGIF);


    }
}

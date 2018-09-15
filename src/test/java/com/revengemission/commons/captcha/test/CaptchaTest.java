package com.revengemission.commons.captcha.test;


import com.revengemission.commons.captcha.core.VerificationCodeMode;
import com.revengemission.commons.captcha.core.VerificationCodeUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CaptchaTest {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/tmp", "verifies8");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        int w = 150, h = 48;
        File file1 = new File(path.toString(), 1 + ".jpg");
        File file2 = new File(path.toString(), 2 + ".jpg");
        File file3 = new File(path.toString(), 3 + ".gif");
        File file4 = new File(path.toString(), 4 + ".gif");
        File file5 = new File(path.toString(), 5 + ".jpg");
        File file6 = new File(path.toString(), 6 + ".gif");
        VerificationCodeUtil.outputImage(w, h, file1, "1A2B", VerificationCodeMode.NORMAL);
        VerificationCodeUtil.outputImage(w, h, file2, "1A2B", VerificationCodeMode.D3);
        VerificationCodeUtil.outputImage(w, h, file3, "1A2B", VerificationCodeMode.GIF);
        VerificationCodeUtil.outputImage(w, h, file4, "1A2B", VerificationCodeMode.GIF3D);
        VerificationCodeUtil.outputImage(w, h, file5, "1A2B", VerificationCodeMode.MIX);
        VerificationCodeUtil.outputImage(w, h, file6, "1A2B", VerificationCodeMode.MIXGIF);


        Path path1 = Paths.get("/tmp", "verifies8");
        System.out.println(path1.toString());

    }
}

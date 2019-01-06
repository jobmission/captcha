package com.revengemission.commons.captcha.core;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * 验证码工具类：
 * 随机字体、字体样式、字体大小（验证码图片宽度 - 8 ~ 验证码图片宽度 + 10）
 * 彩色字符 每个字符的颜色随机，一定会不相同
 * 随机字符 阿拉伯数字 + 小写字母 + 大写字母
 * 3D中空自定义字体，需要单独使用，只有阿拉伯数字和大写字母
 */
public class VerificationCodeUtil {
    /**
     * 随机类
     */
    private static SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // 验证码来源范围，去掉了0,1,I,O,l,o几个容易混淆的字符
    public static final String VERIFICATION_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";

    private static Font baseFont;

    static {
        try {
            //jar中获取资源文件方式不同
            //use Spring type ClassPathResource.
            //File file = new ClassPathResource("default.ttf").getFile();
            baseFont = Font.createFont(Font.TRUETYPE_FONT, VerificationCodeUtil.class.getResource("/IBMPlexSans-Thin.ttf").openStream());
        } catch (Exception e) {
            System.out.println("get font IBMPlexSans-Thin.ttf exception:" + e.getMessage());
            try {
                baseFont = Font.createFont(Font.TRUETYPE_FONT, VerificationCodeUtil.class.getResource("/default.ttf").openStream());
            } catch (Exception e2) {
                System.out.println("get font default.ttf exception:" + e2.getMessage());
            }
        }

        if (baseFont == null) {
            baseFont = new Font("Algerian", Font.ITALIC, 14);
        }
    }

    // 字体样式
    private static int[] fontStyle =
            {
                    Font.BOLD, Font.ITALIC, Font.ROMAN_BASELINE, Font.PLAIN, Font.BOLD + Font.ITALIC
            };

    // 颜色
    private static Color[] colorRange =
            {
                    Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW, Color.GREEN, Color.BLUE,
                    Color.DARK_GRAY, Color.BLACK, Color.RED
            };

    /**
     * 使用默认字符源生成验证码
     *
     * @param verificationCodeLength 验证码长度
     * @return String VerificationCode
     */
    public static String generateVerificationCode(int verificationCodeLength) {
        return generateVerificationCode(verificationCodeLength, VERIFICATION_CODES);
    }

    /**
     * 使用指定源生成验证码
     *
     * @param verificationCodeLength 验证码长度
     * @param sources                验证码字符源
     * @return String VerificationCode
     */
    public static String generateVerificationCode(int verificationCodeLength, String sources) {
        if (sources == null || sources.trim().length() == 0) {
            sources = VERIFICATION_CODES;
        }
        int codesLen = sources.length();
        StringBuilder verificationCode = new StringBuilder(verificationCodeLength);
        for (int i = 0; i < verificationCodeLength; i++) {
            verificationCode.append(sources.charAt(random.nextInt(codesLen - 1)));
        }

        return verificationCode.toString();
    }

    /**
     * 输出指定验证码图片流
     *
     * @param w                    验证码图片的宽
     * @param h                    验证码图片的高
     * @param os                   流
     * @param verificationCode     验证码
     * @param verificationCodeMode 场景类型
     * @throws IOException if exception
     */
    public static void outputImage(int w, int h, OutputStream os, String verificationCode, VerificationCodeMode verificationCodeMode) throws IOException {
        int verificationCodeLength = verificationCode.length();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = colorRange;
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[random.nextInt(colorSpaces.length)];
            fractions[i] = random.nextFloat();
        }
        Arrays.sort(fractions);

        g2.setColor(Color.GRAY);// 设置边框色
        g2.fillRect(0, 0, w, h);

        Color c = getRandColor(200, 250);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 2, w, h - 4);

        char[] charts = verificationCode.toCharArray();
        for (int i = 0; i < charts.length; i++) {
            g2.setColor(c);// 设置背景色
            g2.setFont(getFont(h));
            g2.fillRect(0, 2, w, h - 4);
        }

        // 1.绘制干扰线
        g2.setColor(getRandColor(160, 200));// 设置线条的颜色
        int lineNumbers = 20;
        if (verificationCodeMode.equals(VerificationCodeMode.VAGUE)) {
            lineNumbers = getRandomDrawLine();
        }
        for (int i = 0; i < lineNumbers; i++) {
            int x = random.nextInt(w - 1);
            int y = random.nextInt(h - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 2.添加噪点
        float yawpRate = 0.05f;
        if (verificationCodeMode.equals(VerificationCodeMode.VAGUE)) {
            yawpRate = getRandomDrawPoint(); // 噪声率
        }
        int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        // 3.使图片扭曲
        shear(g2, w, h, c);

        char[] chars = verificationCode.toCharArray();
        Double rd = random.nextDouble();
        Boolean rb = random.nextBoolean();

        // 4.写入验证码字符串
        if (verificationCodeMode.equals(VerificationCodeMode.NORMAL)) {
            for (int i = 0; i < verificationCodeLength; i++) {
                g2.setColor(getRandColor(80, 160));
                g2.setFont(getFont(h));

                AffineTransform affine = new AffineTransform();
                affine.setToRotation(Math.PI / 4 * rd * (rb ? 1 : -1), (w / (double) verificationCodeLength) * i + (h - 4) / 2.0, h / 2.0);
                g2.setTransform(affine);
                g2.drawOval(random.nextInt(w), random.nextInt(h), 5 + random.nextInt(10), 5 + random.nextInt(10));
                g2.drawChars(chars, i, 1, ((w - 10) / verificationCodeLength) * i + 5, h / 2 + (h - 4) / 2 - 10);
            }

            g2.dispose();
            ImageIO.write(image, "jpg", os);
        } else if (verificationCodeMode.equals(VerificationCodeMode.GIF) || verificationCodeMode.equals(VerificationCodeMode.GIF3D) || verificationCodeMode.equals(VerificationCodeMode.MIXGIF)) {
            GifEncoder gifEncoder = new GifEncoder(); // gif编码类
            // 生成字符
            gifEncoder.start(os);
            gifEncoder.setQuality(180);
            gifEncoder.setDelay(150);
            gifEncoder.setRepeat(0);

            AlphaComposite ac3;
            for (int i = 0; i < verificationCodeLength; i++) {
                g2.setColor(getRandColor(100, 160));
                g2.setFont(getFont(h));
                for (int j = 0; j < verificationCodeLength; j++) {
                    AffineTransform affine = new AffineTransform();
                    affine.setToRotation(Math.PI / 4 * rd * (rb ? 1 : -1), (w / (double) verificationCodeLength) * i + (h - 4) / 2.0, h / 2.0);
                    g2.setTransform(affine);
                    g2.drawChars(chars, i, 1, ((w - 10) / verificationCodeLength) * i + 5, h / 2 + (h - 4) / 2 - 10);

                    ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(j, i, verificationCodeLength));
                    g2.setComposite(ac3);
                    g2.drawOval(random.nextInt(w), random.nextInt(h), 5 + random.nextInt(10), 5 + random.nextInt(10));
                    gifEncoder.addFrame(image);
                    image.flush();
                }
            }
            gifEncoder.finish();
            g2.dispose();
        } else {
            for (int i = 0; i < verificationCodeLength; i++) {
                g2.setColor(getRandColor(100, 160));
                g2.setFont(getFont(h));

                AffineTransform affine = new AffineTransform();
                affine.setToRotation(Math.PI / 4 * rd * (rb ? 1 : -1), (w / (double) verificationCodeLength) * i + (h - 4) / 2.0, h / 2.0);
                g2.setTransform(affine);
                g2.drawOval(random.nextInt(w), random.nextInt(h), 5 + random.nextInt(10), 5 + random.nextInt(10));
                g2.drawChars(chars, i, 1, ((w - 10) / verificationCodeLength) * i + 5, h / 2 + (h - 4) / 2 - 10);
            }

            g2.dispose();
            ImageIO.write(image, "jpg", os);
        }
    }

    public static void outputImage(int w, int h, File file, String verificationCode, VerificationCodeMode verificationCodeMode) throws IOException {
        OutputStream os = new FileOutputStream(file);
        try {
            outputImage(w, h, os, verificationCode, verificationCodeMode);
        } catch (Exception e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void outputImage(int w, int h, OutputStream os, String verificationCode) throws IOException {
        outputImage(w, h, os, verificationCode, VerificationCodeMode.NORMAL);
    }

    public static void outputImage(int w, int h, File file, String verificationCode) throws IOException {
        OutputStream os = new FileOutputStream(file);
        try {
            outputImage(w, h, os, verificationCode, VerificationCodeMode.NORMAL);
        } catch (Exception e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * 获取随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc) {
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    /**
     * 干扰线按范围获取随机数
     *
     * @return
     */
    private static int getRandomDrawLine() {
        int min = 20;
        int max = 155;
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 噪点数率按范围获取随机数
     *
     * @return
     */
    private static float getRandomDrawPoint() {
        float min = 0.05f;
        float max = 0.1f;
        return min + ((max - min) * random.nextFloat());
    }

    /**
     * 获取字体大小按范围随机
     *
     * @param height 验证码图片高
     * @return
     */
    private static int getRandomFontSize(int height) {
        int min = height - 8;
        // int max = 46;
        return random.nextInt(11) + min;
    }

    public static Font getFont(int height) {
        // 字体样式
        int style = fontStyle[random.nextInt(fontStyle.length)];
        // 字体大小
        int size = getRandomFontSize(height);
        Font font = baseFont;
        return font.deriveFont(style, size);
    }

    /**
     * 字符和干扰线扭曲
     *
     * @param g     绘制图形的java工具类
     * @param w1    验证码图片宽
     * @param h1    验证码图片高
     * @param color 颜色
     */
    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    /**
     * x轴扭曲
     *
     * @param g     绘制图形的java工具类
     * @param w1    验证码图片宽
     * @param h1    验证码图片高
     * @param color 颜色
     */
    private static void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);

        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + w1, i, w1, i);
        }
    }

    /**
     * y轴扭曲
     *
     * @param g     绘制图形的java工具类
     * @param w1    验证码图片宽
     * @param h1    验证码图片高
     * @param color 颜色
     */
    private static void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(40) + 10; // 50;

        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            g.setColor(color);
            g.drawLine(i, (int) d, i, 0);
            g.drawLine(i, (int) d + h1, i, h1);
        }
    }

    /**
     * 获取透明度,从0到1,自动计算步长
     *
     * @param i
     * @param j
     * @return float 透明度
     */
    private static float getAlpha(int i, int j, int verificationCodeLength) {
        int num = i + j;
        float r = (float) 1 / verificationCodeLength, s = (verificationCodeLength + 1) * r;
        return num > verificationCodeLength ? (num * r - s) : num * r;
    }
}
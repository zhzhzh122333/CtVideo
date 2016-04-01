package com.ctg.ctvideo.services;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PasswordUtils {

    /**
     * 16进制字符
     */
    public static final char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final String hexString = "0123456789abcdef";

    /**
     * 用以加密解密的字符串，可自定义，建议为8个字符
     */
    public static final String secretString = "CtViDeOs";

    //SecretKey 负责保存对称密钥
    private static SecretKey key;

    //DES加密，Cipher负责完成加密或解密工作
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;

    //3DES加密
    private static Cipher encryptCipher3Des;
    private static Cipher decryptCipher3Des;

    static {
        try {
            // 生成对称密钥
            byte[] srcBytes = secretString.getBytes();
            byte[] keyBytes = new byte[8];
            for (int i = 0; i < 8; i++) {
                keyBytes[i] = srcBytes.length > i ? srcBytes[i] : 0;
            }
            key = new SecretKeySpec(keyBytes, "DES");

            // 生成Cipher对象,指定其支持的DES算法
            // 加密
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            // 解密
            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);

            // 3DES
            key = new SecretKeySpec("0123456789123456".getBytes(), "DESede");
            encryptCipher3Des = Cipher.getInstance("DESede");
            encryptCipher3Des.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher3Des = Cipher.getInstance("DESede");
            decryptCipher3Des.init(Cipher.DECRYPT_MODE, key);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对字符串进行DES加密
     *
     * @param str
     * @return 返回加密后的Base64字符串，若出现异常则返回原字符串
     */
    public static String encryptDES(String str) {
        try {
            return new String(Base64.encode(encryptCipher.doFinal(str.getBytes()), Base64.DEFAULT));
        } catch(Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 对字符串进行DES解密
     *
     * @param str
     * @return 返回解密后的字符串，若出现异常则返回原字符串
     */
    public static String decryptDES(String str) {
        try {
            return new String(decryptCipher.doFinal(Base64.decode(str, Base64.DEFAULT)));
        } catch(Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 将字符串用MD5加密
     *
     * @param str
     * @return 返回加密后的16进制字符串，若出现异常则返回原字符串
     */
    public static String encryptMD5(String str) {
        try {

            // 根据MD5算法生成MessageDigest对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            // 更新摘要
            md5.update(str.getBytes());

            // 完成哈希计算，得到result
            return toHexString(md5.digest());
        } catch(Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 转换成16进制字符串
     *
     * @param byteArray
     * @return
     */
    public static String toHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(byteArray.length * 2);

        for (byte b : byteArray) {
            sb.append(hexString.charAt((b & 0xf0) >>> 4));
            sb.append(hexString.charAt(b & 0x0f));
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转byte数组
     *
     * @param str
     * @return
     */
    public static byte[] hexToByte(String str) {
        byte[] bytes = new byte[str.length() / 2];
        char[] chars = str.toCharArray();

        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = hexString.indexOf(chars[i * 2]) << 4;
            temp += hexString.indexOf(chars[i * 2 + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return bytes;
    }



    /**
     * 对字符串进行3DES加密
     *
     * @param str
     * @return 返回加密后的Base64字符串，若出现异常则返回原字符串
     */
    public static String encrypt3DES(String str) {
        try {
            return toHexString(encryptCipher3Des.doFinal(str.getBytes()));
        } catch(Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 对字符串进行3DES解密
     *
     * @param str
     * @return 返回解密后的字符串，若出现异常则返回原字符串
     */
    public static String decrypt3DES(String str) {
        try {
            return new String(decryptCipher3Des.doFinal(hexToByte(str)));
        } catch(Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    // 测试主函数
    public static void main(String args[]) {
        String s = new String("abcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()_+");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + encryptMD5(s));
        System.out.println("加密的：" + encryptDES(s));
        System.out.println("解密的：" + decryptDES(encryptDES(s)));
        System.out.println(s.equals(decryptDES(encryptDES(s))));

    }
}

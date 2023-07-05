package com.project.insure.util;

import lombok.experimental.UtilityClass;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@UtilityClass
public class AESUtil {
    /* PKCS#5와 PKCS#7 */
    public static final String PADDING = "AES/CBC/PKCS5Padding";

    /* 256비트(32바이트)의 키 */
    private static final String KEY = "aes256encrypt123aes256encrypt123";

    /* initialization vector */
    private static byte[] getIv() {
        return new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException
            , InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        /* 32byte IV initialization */
        byte[] iv = getIv();
        /* KEY String -> byte */
        byte[] keyData = KEY.getBytes();

        /* keyData를 key로 지정, AES algorithm사용 */
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");

        /* CBC PKCS5Padding 방식 사용 */
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv));

        /* 암호화 */
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException
            , InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // IOS url-encode 대응
        cipherText = cipherText.replace(" ", "+");

        /* 32byte IV initialization */
        byte[] iv = getIv();
        /* KEY String -> byte */
        byte[] keyData = KEY.getBytes();

        /* keyData를 key로 지정, AES algorithm사용 */
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");

        /* CBC PKCS5Padding 방식 사용 */
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv));

        /* 복호화 */
        byte[] decodedBytes = Base64.decodeBase64(cipherText.getBytes());
        byte[] decrypted = cipher.doFinal(decodedBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {

        String encrypt = AESUtil.encrypt("1234567890123456|1125|777");
        System.out.println(encrypt);
        System.out.println(AESUtil.decrypt(encrypt));
    }
}

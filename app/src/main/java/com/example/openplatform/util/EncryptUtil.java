package com.example.openplatform.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class EncryptUtil {

    //加解密密钥：AES加密反式: AES/ECB/PKCS5Padding
    public static final String AES = "C8BE5C77E0104378ABBEF7DA6FBF7408";// 接口加解秘
    public static final String CHARSET = "UTF-8";
    private static final String AES_Provider = "AES/CBC/PKCS5Padding";
    public static String iv="0123456789abcdef";
    private static final String KEY_ALGORITHM = "AES";

    //加密
    public static String encrypt(String content) throws Exception {
        //有加密
        /*Cipher cipher = Cipher.getInstance(AES_Provider);
        byte[] byteContent = content.getBytes(CHARSET);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(AES.getBytes(), KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        byte[] result = cipher.doFinal(byteContent);
        return Base64.encodeToString(result, Base64.DEFAULT);*/
        //去掉加密
        return content;
    }

    public static String decrypt(String content) throws Exception {
        //有解密
        /*Cipher cipher = Cipher.getInstance(AES_Provider);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(AES.getBytes(), KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        byte[] result = cipher.doFinal(Base64.decode(content, Base64.DEFAULT));
        return new String(result, CHARSET);*/
        //去掉解密
        return content;
    }

}

package com.lawrence.fatalis.util.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.util.Map;


public class RSAwithAESTest {

    private static String publicKey;   //RSA公钥
    private static String privateKey;  //RSA私钥

    public static void main(String[] args) throws Exception{
        long in = System.nanoTime();

        String inputStr = "陶学斌15528911698";

        String key = AESCoder.initKey();// 生成AES密钥
        System.out.println("原文:  " + inputStr);
        System.out.println("AES密钥:  " + key);
        byte[] inputData = inputStr.getBytes("UTF-8");
        inputData = AESCoder.encrypt(inputData, key);
        System.out.println("用AES密钥加密后的密文:  " + AESCoder.encryptBASE64(inputData));// 用AES加密后的密文

        Map<String, Object> keyMap = RSACoder.initKey();// 生成RSA的公钥和私钥
        publicKey = RSACoder.getPublicKey(keyMap);
        privateKey = RSACoder.getPrivateKey(keyMap);
        System.out.println("RSA产生的公钥: \n" + publicKey);
        System.out.println("RSA产生的私钥： \n" + privateKey);
        System.out.println("RSA公钥加密AES密钥——RSA私钥解密AES密钥");

        byte[] data = key.getBytes("UTF-8");// 将AES密钥用生成的RSA公钥加密
        byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);

        String transData = Base64.encodeBase64URLSafeString(encodedData);
        byte[] decodedData = RSACoder.decryptByPrivateKey(Base64.decodeBase64(transData), privateKey);
        String outputkey = new String(decodedData, "UTF-8");
        System.out.println("RSA公钥加密前的AES密钥: " + key + "\n" + "RSA公钥加密后的AES密钥："+transData+"\n"+"RSA私钥解密后的AES密钥: " + outputkey);

        byte[] outputData = AESCoder.decrypt(inputData, outputkey);// 用AES密钥解密密文
        String outputStr = new String(outputData, "UTF-8");
        System.out.println("用AES密钥解密后得到的明文:  " + outputStr);

        System.out.println(System.nanoTime() - in);

        String str = "Mysql@1234";
        System.out.println(AESCoder.encrypt(str, AESCoder.CONFIG_KEY));

        String code = MD5Coder.generate("lawrence", "lawrence");
        System.out.println(code);
        System.out.println(MD5Coder.verify("lawrence", code, "lawrence"));

    }
}


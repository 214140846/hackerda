package com.hackerda.platform.utils;

import com.alibaba.fastjson.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

@Slf4j
public class WXBizDataCrypt {

    private final String appId;

    private final String sessionKey;

    public WXBizDataCrypt(String appId, String sessionKey) {
        this.appId = appId;
        this.sessionKey = sessionKey;
    }

    /**
     * 检验数据的真实性，并且获取解密后的明文.
     * @param encryptedData  string 加密的用户数据
     * @param iv  string 与用户数据一同返回的初始向量
     * @return data string 解密后的原文
     * @return String 返回用户信息
     */
    public String decryptData(String encryptedData, String iv) {
        BASE64Decoder base64Decoder = new BASE64Decoder();

        /**
         * 小程序加密数据解密算法
         * https://developers.weixin.qq.com/miniprogram/dev/api/signature.html#wxchecksessionobject
         * 1.对称解密的目标密文为 Base64_Decode(encryptedData)。
         * 2.对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
         * 3.对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
         */
        try {
            byte[] dataByte = Base64.decodeFast(encryptedData);
            // 加密秘钥
            byte[] keyByte = Base64.decodeFast(sessionKey);
            // 偏移量
            byte[] ivByte = Base64.decodeFast(iv);

            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            log.error("decryptData error", e);
        }
        return "";
    }
}

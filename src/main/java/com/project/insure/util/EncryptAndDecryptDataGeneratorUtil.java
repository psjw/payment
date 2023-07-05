package com.project.insure.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@UtilityClass
public class EncryptAndDecryptDataGeneratorUtil {
    private static String POST_APPENDIX = "|";
    public String getEncryptData(String cardNo, String installmentMonth, String cvc) {
        //return AESUtil.encrypt(cardNo+"|"+)
        StringBuffer sf = new StringBuffer();
        sf.append(cardNo).append(POST_APPENDIX).append(installmentMonth).append(POST_APPENDIX).append(cvc);
        try {
            return AESUtil.encrypt(sf.toString());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("[EncryptAndDecryptDataBodyGeneratorUtil.getEncryptDataBody] Message : {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getDecryptData(String dataBody) {
        try {
            return AESUtil.decrypt(dataBody);
        } catch (NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException e) {
            log.error("[EncryptAndDecryptDataBodyGeneratorUtil.getDecryptDataBody] Message : {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

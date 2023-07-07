package com.project.insure.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class MaskingUtil {

    public String cardMasking(String cardNo){
        String regex = "^[0-9]*$";
        Matcher matcher = Pattern.compile(regex).matcher(cardNo);
        if(matcher.find()){
            int length = cardNo.length();
            String middleMasking = "";
            middleMasking = cardNo.substring(5, length-3);
            String dot = "";
            for(int i = 0; i<middleMasking.length(); i++) {
                dot += "*";
            }

            return cardNo.substring(0, 6)
                    + middleMasking.replace(middleMasking, dot)
                    + middleMasking.substring(length-3, length);
        }
        return cardNo;
    }
}

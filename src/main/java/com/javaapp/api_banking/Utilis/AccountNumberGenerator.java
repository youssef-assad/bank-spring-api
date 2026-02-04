package com.javaapp.api_banking.Utilis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Year;
@Component
@RequiredArgsConstructor
public class AccountNumberGenerator {

    private static final String BANK_CODE = "BNK";
    private static final SecureRandom secureRandom = new SecureRandom();

    public String generateAccountNumber() {
        String year = String.valueOf(Year.now().getValue());
        String randomPart = generateRandomPart();
        return BANK_CODE + year + randomPart;
    }

    private String generateRandomPart() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 8; j++) {
            sb.append(characters.charAt(
                    secureRandom.nextInt(characters.length())
            ));
        }
        return sb.toString();
    }
}

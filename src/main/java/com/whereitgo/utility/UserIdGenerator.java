package com.whereitgo.utility;

import java.security.SecureRandom;

public class UserIdGenerator {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int ID_LENGTH = 16;

    private static final SecureRandom random =
            new SecureRandom();

    public static String generateUserId() {

        StringBuilder userId =
                new StringBuilder();

        for (int i = 0; i < ID_LENGTH; i++) {

            int index =
                    random.nextInt(CHARACTERS.length());

            userId.append(
                    CHARACTERS.charAt(index)
            );
        }

        return userId.toString();
    }
}

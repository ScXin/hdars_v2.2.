package com.hlsii.util;



public class SystemUtil {
    private static final String DEFAULT_PLAIN_PWD = "666666";
    private static final String ENCRYPTED_DEFAULT_PWD = encryptPassword(DEFAULT_PLAIN_PWD);

    private SystemUtil() {}

    /**
     * Default password for new user or reset password
     *
     * @return - default password
     */
    public static String defaultPassword() {
        return ENCRYPTED_DEFAULT_PWD;
    }

    /**
     * Validate the password
     *
     * @param plainPassword - user input password in plain text
     * @param password - the password stored in database (md5 encrypted)
     * @return - true when passwords matched
     */
    public static boolean validatePassword(String plainPassword, String password) {
        return encryptPassword(plainPassword).equals(password);
    }

    /**
     * Encrypt a plain text password
     *
     * @param plainPassword - user input password in plain text
     * @return - Encrypted password
     */
    public static String encryptPassword(String plainPassword) {
        return MD5Util.md5(plainPassword);
    }
}

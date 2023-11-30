import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class PasswordVerify {

    public static boolean verifyPassword(String hashedPw, String name, String inputPw)
            throws NoSuchAlgorithmException {

        byte[] storedHash = hexToBytes(hashedPw);

        // Hash input password
        byte[] inputHash = hash(inputPw);

        // Compare hashes
        return Arrays.equals(storedHash, inputHash);
    }

    private static byte[] hash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(password.getBytes());
    }

    private static byte[] hexToBytes(String hex) {

        int len = hex.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }

        return data;

    }

}
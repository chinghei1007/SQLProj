import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateHashPw {

    private SaltGenerator saltGenerator = new SaltGenerator();

    public String register(String password) throws NoSuchAlgorithmException {

        // Generate salt
        byte[] salt = saltGenerator.generateSalt();

        // Hash password and return
        return hashPassword(password, salt);
    }

    private String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {

        // Hash password with salt
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPwd = md.digest(password.getBytes());

        // Concat salt and hashed password
        String hashedPassword = byteArrayToHex(salt) + byteArrayToHex(hashedPwd);

        return hashedPassword;
    }
    private String byteArrayToHex(byte[] arr) {

        StringBuilder sb = new StringBuilder(arr.length * 2);

        for(byte b : arr) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();

    }
}
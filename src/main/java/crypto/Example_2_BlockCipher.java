package crypto;

import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.util.Arrays;

public class Example_2_BlockCipher {

    public static void main(String[] args) throws Exception {
        encrypt();
    }

    private static void encrypt() throws GeneralSecurityException {
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem.";
        String keyHex = "140b41b22a29beb4061bda66b6747e14";
        String ivHex = "4ca00ff4c898d61e1edbf1800618fb28";

        byte[] messageBytes = message.getBytes();
        byte[] keyBytes = Util.decodeBinaryFromHex(keyHex);
        byte[] ivBytes = Util.decodeBinaryFromHex(ivHex);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherBytes = cipher.doFinal(messageBytes);
        System.out.println(ivHex + Util.encodeBinaryIntoHex(cipherBytes));
    }

    private static void decrypt() throws GeneralSecurityException {
        String ciperTextHex = "4ca00ff4c898d61e1edbf1800618fb28b1a9ae0c8a64e41364e66ce650ab36955c54340d786f52b4614eec74edde69404b6d9cdae3a9c4a9eaa1928381b0ff3ea49dea0ca52253d45ec55f6255149bac170622de6c2ec60612791c3f5da6c1c39e3c6e572ce90d686e20483a6d1a10f13b4a2879c98f23a44c4a4aca6aa84e7ca6dbc7104b37a8887e0cd882d913e795f9149549ef4caa125872332197d0ef2c";
        String keyHex = "140b41b22a29beb4061bda66b6747e14";

        byte[] cipherTextBytes = Util.decodeBinaryFromHex(ciperTextHex);
        int ivLength = 16;
        byte[] ivBytes = Arrays.copyOfRange(cipherTextBytes, 0, ivLength);
        byte[] cipherTextBytesWithoutIv = Arrays.copyOfRange(cipherTextBytes, ivLength, cipherTextBytes.length);
        byte[] keyBytes = Util.decodeBinaryFromHex(keyHex);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] messageBytes = cipher.doFinal(cipherTextBytesWithoutIv);
        System.out.println(new String(messageBytes));
    }
}



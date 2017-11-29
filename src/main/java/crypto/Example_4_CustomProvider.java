package crypto;

import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Example_4_CustomProvider {

    public static void main(String[] args) throws Exception {
        encrypt();
    }

    private static void encrypt() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());

        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem.";
        String keyHex = "140b41b22a29beb4061bda66b6747e14";
        String ivHex = "4ca00ff4c898d61e1edbf18006"; //CCM nonce must have a length from 7 to 13 bytes

        byte[] messageBytes = message.getBytes();
        byte[] keyBytes = Util.decodeBinaryFromHex(keyHex);
        byte[] ivBytes = Util.decodeBinaryFromHex(ivHex);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherBytes = cipher.doFinal(messageBytes);
        System.out.println(ivHex + Util.encodeBinaryIntoHex(cipherBytes));
    }

}

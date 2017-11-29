package crypto;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.*;

public class Example_5_BlockCipherChunks {

    public static void main(String[] args) throws Exception {
        encrypt();
    }

    private static void encrypt() throws GeneralSecurityException {
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem.";
        String ivHex = "4ca00ff4c898d61e1edbf1800618fb28";
        String keyHex = "140b41b22a29beb4061bda66b6747e14";

        byte[] messageBytes = message.getBytes();
        byte[] ivBytes = Util.decodeBinaryFromHex(ivHex);
        byte[] keyBytes = Util.decodeBinaryFromHex(keyHex);

        StringBuilder result = new StringBuilder();
        result.append(ivHex);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        int blockSize = cipher.getBlockSize();
        int blocks = messageBytes.length / blockSize;
        for (int i = 0; i < blocks; i++) {
            byte[] cipherBlock = cipher.update(messageBytes, i * blockSize, blockSize);
            String cipherBlockHex = Util.encodeBinaryIntoHex(cipherBlock);
            System.out.println("Block " + i + " encrypted: " + cipherBlockHex);
            result.append(cipherBlockHex);
        }

        byte[] finalBlock = cipher.doFinal(messageBytes, blocks * blockSize, messageBytes.length - blocks * blockSize);
        String finalBlockHex = Util.encodeBinaryIntoHex(finalBlock);
        System.out.println("Last bl encrypted: " + finalBlockHex);
        result.append(finalBlockHex);

        System.out.println(result);
    }


    private static void decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String ciperTextHex = "4ca00ff4c898d61e1edbf1800618fb28b1a9ae0c8a64e41364e66ce650ab36955c54340d786f52b4614eec74edde69404b6d9cdae3a9c4a9eaa1928381b0ff3ea49dea0ca52253d45ec55f6255149bac170622de6c2ec60612791c3f5da6c1c39e3c6e572ce90d686e20483a6d1a10f13b4a2879c98f23a44c4a4aca6aa84e7ca6dbc7104b37a8887e0cd882d913e795f9149549ef4caa125872332197d0ef2c";
        String keyHex =  "140b41b22a29beb4061bda66b6747e14";

        byte[] cipherTextBytes = Util.decodeBinaryFromHex(ciperTextHex);
        byte[] ivBytes = Arrays.copyOfRange(cipherTextBytes, 0, 16);
        byte[] cipherTextBytesWithoutIv = Arrays.copyOfRange(cipherTextBytes, 16, cipherTextBytes.length);
        byte[] keyBytes = Util.decodeBinaryFromHex(keyHex);

        StringBuilder result = new StringBuilder();

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        int blockSize = cipher.getBlockSize();
        int blocks = cipherTextBytesWithoutIv.length / blockSize;
        for (int i = 0; i < blocks; i++) {
            byte[] messageBlock = cipher.update(cipherTextBytesWithoutIv, i * blockSize, blockSize);
            String decodedBlock = new String(messageBlock);
            System.out.println("Decoded block " + i + ": " + decodedBlock);
            result.append(decodedBlock);
        }

        int lastBlockSize = cipherTextBytesWithoutIv.length - blocks * blockSize;
        byte[] remainderMessageBytes = cipher.doFinal(cipherTextBytesWithoutIv, blocks * blockSize, lastBlockSize);
        String finalBlockDecoded = new String(remainderMessageBytes);
        System.out.println("Last bl decoded: " + finalBlockDecoded);
        System.out.println("Last block size: " + lastBlockSize);
        result.append(finalBlockDecoded);

        System.out.println(result.toString());
    }
}

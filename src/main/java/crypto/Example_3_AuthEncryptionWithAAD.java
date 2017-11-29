package crypto;

import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.util.Arrays;

public class Example_3_AuthEncryptionWithAAD {

    public static void main(String[] args) throws Exception {
        encrypt();
    }

    private static void encrypt() throws GeneralSecurityException {
        String header = "192.168.100.100";
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem.";
        String keyHex = "140b41b22a29beb4061bda66b6747e14";
        String ivHex = "4ca00ff4c898d61e1edbf1800618fb28";

        byte[] messageBytes = message.getBytes();
        byte[] keyBytes = Util.decodeBinaryFromHex(keyHex);
        byte[] ivBytes = Util.decodeBinaryFromHex(ivHex);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        GCMParameterSpec iv = new GCMParameterSpec(128, ivBytes);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        cipher.updateAAD(header.getBytes()); //add unencrypted header to the MAC
        byte[] cipherBytes = cipher.doFinal(messageBytes);
        String cipherTextHex = ivHex + Util.encodeBinaryIntoHex(cipherBytes);
        System.out.println(header + "|" + cipherTextHex);
    }


    private static void decrypt() throws GeneralSecurityException {
        String ciperTextWithAAD = "192.168.100.100|4ca00ff4c898d61e1edbf1800618fb28DEB4BDBF6A1DCE2AA6B24EC95463BF060E644AD4FD6E2FC63139A71FA558CE3A2AC041B28D6E5ABEB666B5F8C8227AEF15A1DD543214C9071D8126F3D79232E4847248503BD212421E8B429411930C9718B3B92F6B85404433FD76C11BE074EF368FE08261AE05BEF54F6D20A73FD3382E8C1E084922E73B16B4AD4B9E17B673C7B885C65DFDC1079DCFCFE04ED1C48B50F85BC858D1CE";

        int headerLenght = ciperTextWithAAD.indexOf('|');
        String header = ciperTextWithAAD.substring(0, headerLenght);

        String ciperTextHex = ciperTextWithAAD.substring(headerLenght + 1);
        String keyHex = "140b41b22a29beb4061bda66b6747e14";

        byte[] cipherTextBytes = Util.decodeBinaryFromHex(ciperTextHex);
        int ivLength = 16;
        byte[] ivBytes = Arrays.copyOfRange(cipherTextBytes, 0, ivLength);
        byte[] cipherTextBytesWithoutIv = Arrays.copyOfRange(cipherTextBytes, ivLength, cipherTextBytes.length);
        byte[] keyBytes = Util.decodeBinaryFromHex(keyHex);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        GCMParameterSpec iv = new GCMParameterSpec(128, ivBytes);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        cipher.updateAAD(header.getBytes());
        byte[] plainTextBytes = cipher.doFinal(cipherTextBytesWithoutIv);

        System.out.println(header);
        System.out.println(new String(plainTextBytes));
    }
}

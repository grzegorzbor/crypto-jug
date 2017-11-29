package crypto;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

class Util {

    public static byte[] decodeBinaryFromHex(String hex) {
        try {
            return Hex.decodeHex(hex);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }

        //without Commons-Codec:
//        byte[] bytes = new byte[hex.length() / 2];
//        for (int i = 0; i < hex.length() / 2; i++) {
//            String substring = hex.substring(i*2, i*2 + 2);
//            byte b = Integer.valueOf(substring, 16).byteValue();
//            bytes[i] = b;
//        }
//        return bytes;
    }

    public static String encodeBinaryIntoHex(byte[] bytes) {
        return Hex.encodeHexString(bytes);

        //without Commons-Codec:
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bytes.length; i++) {
//            byte b = bytes[i];
//            sb.append(String.format("%02X", b));
//        }
//        return sb.toString();
    }

}

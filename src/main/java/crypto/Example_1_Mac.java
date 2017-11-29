package crypto;

import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.*;

public class Example_1_Mac {

    public static void main(String[] args) throws Exception {
        getTag();
    }

    private static void getTag() throws GeneralSecurityException {
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem.";
        String keyHex = "140b41b22a29beb4061bda66b6747e14";

        byte[] keyBytes = Util.decodeBinaryFromHex(keyHex);

        //using standard JCA:
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        mac.init(key);
        byte[] messageBytes = message.getBytes();
        byte[] tag = mac.doFinal(messageBytes);
        String tagHex = Util.encodeBinaryIntoHex(tag);
        System.out.println(tagHex);

        //or, using commons-codec:
        String tagHex2 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, keyBytes).hmacHex(message);
        System.out.println(tagHex2);
    }
}

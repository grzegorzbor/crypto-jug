package crypto;

import javax.crypto.*;
import java.security.*;
import java.io.*;
import java.util.Collections;

public class Example_6_Keys {

    static char[] storePassword = "password01234".toCharArray();

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        SecretKey secretKey = generateSecretKey();
        saveInStore(secretKey);
        SecretKey readFromStore = readFromStore();
        System.out.println("Keys equal: " + secretKey.equals(readFromStore));
    }

    private static SecretKey generateSecretKey() throws GeneralSecurityException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey secretKey = generator.generateKey();

        System.out.println("Generated key: " + Util.encodeBinaryIntoHex(secretKey.getEncoded()));
        System.out.println("Algorithm: " + secretKey.getAlgorithm());
        System.out.println("Format: " + secretKey.getFormat());

        return secretKey;
    }

    private static void saveInStore(SecretKey secretKey) throws GeneralSecurityException, IOException {
        KeyStore store = KeyStore.getInstance("PKCS12");
        store.load(null, null);
        KeyStore.SecretKeyEntry aesKey = new KeyStore.SecretKeyEntry(secretKey);
        store.setEntry("aesKey", aesKey, new KeyStore.PasswordProtection(storePassword));
        try (FileOutputStream fos = new FileOutputStream("mykeystore")) {
            store.store(fos, storePassword);
        }
    }

    private static SecretKey readFromStore() throws GeneralSecurityException, IOException {
        KeyStore store = KeyStore.getInstance("PKCS12");
        try (FileInputStream in = new FileInputStream("mykeystore")) {
            store.load(in, storePassword);
        }
        System.out.println("Keys in the store: " + Collections.list(store.aliases()));

        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry)
                store.getEntry("aesKey", new KeyStore.PasswordProtection(storePassword));
        SecretKey key = entry.getSecretKey();

        System.out.println("Retrieved key: " + Util.encodeBinaryIntoHex(key.getEncoded()));

        return key;
    }
}

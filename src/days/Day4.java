package days;

import setup.Day;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.System.exit;

public class Day4 extends Day {
    String key;
    
    @Override
    public void processInput() {
        key = input.trim();
    }

    @Override
    public void part1() {
        System.out.println(findInteger(5));
    }

    @Override
    public void part2() {
        System.out.println(findInteger(6));
    }

    private int findInteger(int n) {
        int i = 0;
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            exit(1);
        }

        String hash, toHash;
        do {
            toHash = key + (i++);
            md5.reset();
            md5.update(toHash.getBytes());
            hash = bytesToHex(md5.digest());
        } while (!hash.startsWith("0".repeat(n)));
        return i - 1;
    }

    @Override
    public int getDay() {
        return 4;
    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}

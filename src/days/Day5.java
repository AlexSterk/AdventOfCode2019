package days;

import setup.Day;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.exit;

public class Day5 extends Day {

    private String doorID;

    @Override
    public void processInput() {
        doorID = input.trim();
    }

    @Override
    public void part1() {
        int i = 0;
        MessageDigest md5 = null;
        
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            exit(1);
        }

        List<Character> password = new ArrayList<>();

        for (int p = 0; p < 8; p++) {
            Hash hash = getHash(i, md5,false);

            password.add(hash.character());
            i = hash.count();
        }

        password.forEach(System.out::print);
        System.out.println();
    }

    @Override
    public void part2() {
        int i = 0;
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            exit(1);
        }

        List<Character> password = new ArrayList<>(Collections.nCopies(8, null));

        for (int p = 0; p < 8; p++) {
            Hash hash = getHash(i, md5,true);

            i = hash.count();
            if (password.get(hash.index()) != null) {
                p--;
                continue;
            }

            password.set(hash.index(), hash.character());
        }

        password.forEach(System.out::print);
        System.out.println();
    }

    private Hash getHash(int i, MessageDigest md5, boolean partTwo) {
        String hash;
        String toHash;
        int index;
        do {
            toHash = doorID + (i++);
            md5.reset();
            md5.update(toHash.getBytes());
            hash = bytesToHex(md5.digest());
            index = Character.digit(hash.charAt(5), 16);
        } while (!hash.startsWith("00000") || (partTwo && (index < 0 || index > 7)));
        
        return new Hash(hash, i, partTwo ? hash.charAt(6) : hash.charAt(5), index);
    }

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public boolean isTest() {
        return false;
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

record Hash(String hash, int count, char character, int index) {}

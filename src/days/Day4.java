package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 extends Day {

    private List<Room> rooms;

    @Override
    public void processInput() {
        rooms = Arrays.stream(input.trim().split("\r?\n")).map(Room::stringToRoom).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        System.out.println(rooms.stream().filter(Room::isValid).mapToInt(Room::sectorId).sum());
    }

    @Override
    public void part2() {
        rooms.stream()
                .filter(r -> r.decryptName().contains("north"))
                .forEach(System.out::println);
    }

    @Override
    public int getDay() {
        return 4;
    }
}

record Room(String encryptedName, int sectorId, String checksum) {
    static final Pattern ROOM_PATTERN = Pattern.compile("([a-z-]+)-(\\d+)\\[([a-z]{5})]");
    static Room stringToRoom(String s){
        Matcher matcher = ROOM_PATTERN.matcher(s);
        matcher.matches();
        
        return new Room(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3));
    }
    
    String computeChecksum() {
        String noDashes = encryptedName.replaceAll("-", "");
        HashMap<Character, Integer> charCounts = new HashMap<>();
        char[] chars = noDashes.toCharArray();
        for (char c : chars) {
            charCounts.merge(c, 1, Integer::sum);
        }
        ArrayList<Map.Entry<Character, Integer>> entries = new ArrayList<>(charCounts.entrySet());
        entries.sort((o1, o2) -> {
            int compare = Integer.compare(o2.getValue(), o1.getValue());
            return compare == 0 ? Character.compare(o1.getKey(), o2.getKey()) : compare;
        });
        
        return entries.stream().limit(5).map(Map.Entry::getKey).map(Object::toString).collect(Collectors.joining());
    }
    
    boolean isValid() {
        String anObject = computeChecksum();
        return checksum.equals(anObject);
    }
    
    String decryptName() {
        return encryptedName.chars().map(c -> c == '-' ? ' ' : ((c - 'a' + sectorId) % 26) + 'a').mapToObj(Character::toString).collect(Collectors.joining());
    }
}

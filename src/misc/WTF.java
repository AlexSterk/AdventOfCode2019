package misc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;
import java.util.Set;

public class WTF {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Object o = new ObjectInputStream(new FileInputStream("data/buggy")).readObject();
        Set<Set<Integer>> o1 = (Set<Set<Integer>>) o;

        o1.forEach(System.out::println);
        System.out.println("--------");

        while (!o1.isEmpty()) {
            Optional<Set<Integer>> possibleSet = o1.stream().filter(s -> s.size() == 1).findAny();
            if (possibleSet.isPresent()) {
                Set<Integer> set = possibleSet.get();
                System.out.println(set);
                assert o1.contains(set);
                o1.remove(set);
                o1.forEach(s -> s.removeAll(set));
            } else {
                break;
            }
        }
    }
}

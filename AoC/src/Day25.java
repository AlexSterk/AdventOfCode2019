import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day25 {
    public static void main(String[] args) throws IOException {
        String program = Files.readString(Paths.get("AoC/resources/day25.txt"));
        ASCII_Output out = new ASCII_Output();
        ASCII_Input in = new ASCII_Input();
        IntCodeInterpreter cpu = new IntCodeInterpreter(program, in, out);

        Set<String> items = Set.of("whirled peas", "planetoid", "mouse", "klein bottle", "mutex", "dark matter", "antenna", "fuel cell");
        List<String> collectAll = List.of(
                "south",
                "take fuel cell",
                "north",
                "west",
                "take mouse",
                "north",
                "east",
                "take klein bottle",
                "west",
                "south",
                "west",
                "south",
                "take dark matter",
                "north",
                "east",
                "east",
                "north",
                "west",
                "south",
                "take planetoid",
                "west",
                "take antenna",
                "east",
                "east",
                "take mutex",
                "south",
                "take whirled peas",
                "south",
                "east",
                "inv"
        );

        String.join("\n", collectAll).chars().forEach(c -> in.add((long) c));
        in.add(10L);

        in.setTakingInput(false);
        cpu.run();

        out.setPrint(false);
        out.clear();

        Set<Set<String>> subsets = findPowerSet2(items);
        for (Set<String> subset : subsets) {
            subset.stream().map(s -> "take " + s + "\n").flatMap(s -> s.chars().boxed()).mapToLong(i -> i).forEach(in::add);
            "north\n".chars().mapToLong(i -> i).forEach(in::add);
            cpu.run();
            String string = out.getString();
            if (string.contains("Analysis complete! You may proceed.")) {
                System.out.println(string);
                System.out.println(subset);
                break;
            }
            out.clear();
            subset.stream().map(s -> "drop " + s + "\n").flatMap(s -> s.chars().boxed()).mapToLong(i -> i).forEach(in::add);
        }
    }

    public static <E> Set<Set<E>> findPowerSet2(Set<E> set){
        Set<Set<E>> result = new HashSet<>();
        result.add(new HashSet<E>());
        for(E element : set){
            Set<Set<E>> previousSets = new HashSet<>(result);
            for (Set<E> subSet : previousSets) {
                Set<E> newSubSet = new HashSet<E>(subSet);
                newSubSet.add(element);
                result.add(newSubSet);
            }
        }
        return result;
    }
}

@EqualsAndHashCode(callSuper = true)
@Data
class ASCII_Output extends ArrayList<Character> implements InterpreterOutput {
    private boolean print = true;

    @Override
    public void write(Long i) {
        if (print) System.out.print((char) i.intValue());
        this.add((char) i.intValue());
    }

    @Override
    public void clear() {
        super.clear();
    }
    
    public String getString() {
        return stream().map(Objects::toString).collect(Collectors.joining());
    }
}

@EqualsAndHashCode(callSuper = true)
@Data
class ASCII_Input extends LinkedList<Long> implements InterpreterInput {
    private boolean takingInput = true;
    
    @SneakyThrows
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && (!takingInput || System.in.read() == '\n');
    }

    @Override
    public Long getNext() throws IOException {
        return super.isEmpty() ? (long) System.in.read() : super.poll();
    }

    @Override
    public void clear() {
        super.clear();
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day25 {
    public static void main(String[] args) throws IOException {
        IntCodeInterpreter cpu = new IntCodeInterpreter(Files.readString(Paths.get("AoC/resources/day23.txt")));
    }
}

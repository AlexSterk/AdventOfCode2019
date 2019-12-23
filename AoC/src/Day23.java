import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day23 {
    public static void main(String[] args) throws IOException {
        IntCodeInterpreter[] cpus = new IntCodeInterpreter[50];
        for (int i = 0; i < cpus.length; i++) {
            IntCodeInterpreter cpu = getCpu();
            cpus[i] = cpu;
            cpu.in.offer((long) i);
            cpu.run();
        }
        
        Long Y;
        outer: while (true) {
            for (IntCodeInterpreter cpu : cpus) {
                while (!cpu.out.isEmpty()) {
                    Long address = cpu.out.poll();
                    Long x = cpu.out.poll();
                    Long y = cpu.out.poll();
                    if (address.equals(255L)) {
                        Y = y;
                        break outer;
                    }
                    IntCodeInterpreter recipient = cpus[Math.toIntExact(address)];
                    recipient.in.offer(x);
                    recipient.in.offer(y);
                }
            }
            for (IntCodeInterpreter cpu : cpus) {
                if (cpu.in.isEmpty()) cpu.in.offer(-1L);
                cpu.run();
            }
        }
        System.out.println(Y);
    }

    private static IntCodeInterpreter getCpu() throws IOException {
        return new IntCodeInterpreter(Files.readString(Paths.get("AoC/resources/day23.txt")));
    }
}

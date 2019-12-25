import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day23 {
    public static void main(String[] args) throws IOException {
        IntCodeInterpreter[] cpus = new IntCodeInterpreter[50];
        for (int i = 0; i < cpus.length; i++) {
            IntCodeInterpreter cpu = getCpu();
            cpus[i] = cpu;
            ((InterpreterQueue) cpu.in).offer((long) i);
            cpu.run();
        }

        Long X = null, Y = null;
        Long prevY = null;
        while (true) {
            boolean idle = Arrays.stream(cpus).filter(cpu -> cpu.in.isEmpty() && ((InterpreterQueue) cpu.out).isEmpty()).count() == cpus.length;
            if (idle && X != null && Y != null) {
                ((InterpreterQueue) cpus[0].in).offer(X);
                ((InterpreterQueue) cpus[0].in).offer(Y);

                if (Y.equals(prevY)) {
                    break;
                }

                prevY = Y;
            }
            for (IntCodeInterpreter cpu : cpus) {
                while (true) {
                    InterpreterQueue out = (InterpreterQueue) cpu.out;
                    if (out.isEmpty()) break;
                    Long address = out.poll();
                    Long x = out.poll();
                    Long y = out.poll();
                    if (address.equals(255L)) {
                        X = x;
                        Y = y;
                        continue;
                    }
                    IntCodeInterpreter recipient = cpus[Math.toIntExact(address)];
                    InterpreterQueue in = (InterpreterQueue) recipient.in;
                    in.offer(x);
                    in.offer(y);
                }
            }
            for (IntCodeInterpreter cpu : cpus) {
                if (cpu.in.isEmpty()) ((InterpreterQueue) cpu.in).offer(-1L);
                cpu.run();
            }
        }
        System.out.println(prevY);
    }

    private static IntCodeInterpreter getCpu() throws IOException {
        return new IntCodeInterpreter(Files.readString(Paths.get("AoC/resources/day23.txt")));
    }
}

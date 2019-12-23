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
            cpu.in.offer((long) i);
            cpu.run();
        }
        
        Long X = null, Y = null;
        Long  prevY = null;
        o: while (true) {
            boolean idle = Arrays.stream(cpus).filter(cpu -> cpu.in.isEmpty() && cpu.out.isEmpty()).count() == cpus.length;
            if (idle && X != null && Y != null) {
                cpus[0].in.offer(X);
                cpus[0].in.offer(Y);
                
                if (Y.equals(prevY)) {
                    break o;
                }
                    
                prevY = Y;
            }
            for (IntCodeInterpreter cpu : cpus) {
                while (!cpu.out.isEmpty()) {
                    Long address = cpu.out.poll();
                    Long x = cpu.out.poll();
                    Long y = cpu.out.poll();
                    if (address.equals(255L)) {
                        X = x;
                        Y = y;
                        continue;
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
        System.out.println(prevY);
    }

    private static IntCodeInterpreter getCpu() throws IOException {
        return new IntCodeInterpreter(Files.readString(Paths.get("AoC/resources/day23.txt")));
    }
}

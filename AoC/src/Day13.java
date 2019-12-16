import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day13 {
    public static void main(String[] args) throws IOException {
        partOne();
        partTwo();
    }

    private static void partOne() throws IOException {
        String inputString = Files.readString(Paths.get("AoC/resources/day13.txt"));
        List<Long> initialState = Stream.of(inputString.split("[,\n]")).map(Long::parseLong).collect(Collectors.toList());

        LinkedList<Long> in = new LinkedList<>();
        LinkedList<Long> out = new LinkedList<>();
        IntCodeInterpreter cpu = new IntCodeInterpreter(initialState, in, out);
        cpu.run();

        long width = IntStream.range(0,out.size()).filter(x -> x % 3 == 0).mapToLong(out::get).max().getAsLong() + 1;
        long height = IntStream.range(0,out.size()).filter(x -> x % 3 == 1).mapToLong(out::get).max().getAsLong() + 1;

        long[][] board = new long[(int) height][(int) width];

        for (int i = 0; i < out.size(); i+=3) {
            int x = Math.toIntExact(out.get(i));
            int y = Math.toIntExact(out.get(i + 1));
            Long v = out.get(i + 2);

            board[y][x] = v;
        }
        LongStream str = LongStream.empty();
        for (long[] row : board) {
            str = LongStream.concat(str, LongStream.of(row));
        }
        System.out.println(str.filter(x -> x == 2).count());
    }

    private static void partTwo() throws IOException {
        String inputString = Files.readString(Paths.get("AoC/resources/day13.txt"));
        List<Long> initialState = Stream.of(inputString.split("[,\n]")).map(Long::parseLong).collect(Collectors.toList());

        initialState.set(0, 2L);

        LinkedList<Long> in = new LinkedList<>();
        LinkedList<Long> out = new LinkedList<>();
        IntCodeInterpreter cpu = new IntCodeInterpreter(initialState, in, out);
        cpu.run();

//        Game game = new Game(cpu);

        long width = IntStream.range(0,cpu.out.size()).filter(x -> x % 3 == 0).mapToLong(out::get).max().getAsLong() + 1;
        long height = IntStream.range(0,cpu.out.size()).filter(x -> x % 3 == 1).mapToLong(out::get).max().getAsLong() + 1;

        long[][] board = new long[(int) height][(int) width];

        Integer score = null, px = null, bx = null;
        boolean halted;
        do {
            Integer[] update = update(cpu.out, board);
            score = update[0] == null ? score : update[0];
            bx = update[1] == null ? bx : update[1];
            px = update[2] == null ? px : update[2];
            cpu.in.offer((long) Integer.signum(bx - px));
            halted = cpu.run();
        } while (!halted);
        Integer[] update = update(cpu.out, board);
        score = update[0] == null ? score : update[0];
        System.out.println(score);
    }

    private static Integer[] update(Queue<Long> out, long[][] board) {
        Integer score = null;
        Integer bx = null;
        Integer px = null;
        while (!out.isEmpty()) {
            int x = Math.toIntExact(out.poll());
            int y = Math.toIntExact(out.poll());
            int v = Math.toIntExact(out.poll());

            if (v == 4) bx = x;
            if (v == 3) px = x;

            if (x == -1) {
                score = v;
                continue;
            }

            board[y][x] = v;
        }
        return new Integer[]{score, bx, px};
    }

    private static class Game extends JFrame {
        public static void main(String[] args) throws IOException {
            String inputString = Files.readString(Paths.get("AoC/resources/day13.txt"));
            List<Long> initialState = Stream.of(inputString.split("[,\n]")).map(Long::parseLong).collect(Collectors.toList());
            initialState.set(0, 2L);
            IntCodeInterpreter cpu = new IntCodeInterpreter(initialState, new LinkedList<>(), new LinkedList<>());
            Game game = new Game(cpu);
        }

        private Game(IntCodeInterpreter cpu) {
            cpu.run();

            long width = IntStream.range(0,cpu.out.size()).filter(x -> x % 3 == 0).mapToLong(((LinkedList<Long>) cpu.out)::get).max().getAsLong() + 1;
            long height = IntStream.range(0,cpu.out.size()).filter(x -> x % 3 == 1).mapToLong(((LinkedList<Long>) cpu.out)::get).max().getAsLong() + 1;

            long[][] board = new long[(int) height][(int) width];

            Box box = new Box(BoxLayout.Y_AXIS);
            JLabel scoreLabel = new JLabel(String.valueOf(Day13.update(cpu.out, board)[0]));
            box.add(scoreLabel);
            GamePanel panel = new GamePanel(board);
            box.add(panel);
            add(box);

            addKeyListener(new KeyAdapter() {
                private boolean halted = false;
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (!halted) {
                        long in;
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_UP:
                                in = 0L;
                                break;
                            case KeyEvent.VK_LEFT:
                                in = -1L;
                                break;
                            case KeyEvent.VK_RIGHT:
                                in = 1L;
                                break;
                            default: return;
                        }

                        cpu.in.offer(in);
                        halted = cpu.run();
                        Integer score = Day13.update(cpu.out, board)[0];
                        if (score != null) {
                            scoreLabel.setText(score.toString());
                        }
                        panel.repaint();
                    }
                }
            });

            setDefaultCloseOperation(EXIT_ON_CLOSE);
            pack();
            setVisible(true);
        }

        private static class GamePanel extends JPanel {
            private final long[][] board;
            private final int scale = 20;

            private GamePanel(long[][] board) {
                this.board = board;
            }

            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(board[0].length * scale, board.length * scale);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int y = 0; y < board.length; y++) {
                    long[] row = board[y];
                    for (int x = 0; x < row.length; x++) {
                        int v = (int) row[x];

                        Color c = Color.lightGray;

                        switch (v) {
                            case 1:
                                c = Color.BLACK; break;
                            case 2:
                                c = Color.GREEN; break;
                            case 3:
                                c = Color.YELLOW; break;
                            case 4:
                                c = Color.RED; break;
                        }
                        g.setColor(c);
                        g.fillRect(x* scale, y* scale, x* scale + scale, y* scale + scale);
                    }
                }

            }
        }
    }
}

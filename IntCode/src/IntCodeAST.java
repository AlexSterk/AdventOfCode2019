public class IntCodeAST {
    public static abstract class Argument {
        public final long value;

        protected abstract String toStringArgs();

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "(" + toStringArgs() + ")";
        }

        protected Argument(long value) {
            this.value = value;
        }

        public static class Abs extends Argument {
            @Override
            protected String toStringArgs() {
                return String.valueOf(value);
            }

            protected Abs(long value) {
                super(value);
            }
        }
        public static abstract class PositionalArgument extends Argument {
            public final long position;

            @Override
            protected String toStringArgs() {
                return position + "->" + value;
            }

            protected PositionalArgument(long value, long position) {
                super(value);
                this.position = position;
            }
        }
        public static class Pos extends PositionalArgument {

            protected Pos(long value, long position) {
                super(value, position);
            }
        }
        public static class Rel extends PositionalArgument {

            protected Rel(long value, long position) {
                super(value, position);
            }
        }
    }
    public static abstract class Instruction {
        public final long relBase;
        public final long pointer;

        protected abstract String toStringArgs();

        @Override
        public String toString() {
            return pointer + " +- " + relBase + ": " + this.getClass().getSimpleName() + "(" + toStringArgs() + ")";
        }

        protected Instruction(long relBase, long pointer) {
            this.relBase = relBase;
            this.pointer = pointer;
        }

        public static abstract class Arithmetic extends Instruction {
            public final Argument a1, a2;
            public final Argument.PositionalArgument a3;

            @Override
            protected String toStringArgs() {
                return a1 + ", " + a2 + ", " + a3;
            }

            protected Arithmetic(int relBase, int pointer, Argument a1, Argument a2, Argument.PositionalArgument a3) {
                super(relBase, pointer);
                this.a1 = a1;
                this.a2 = a2;
                this.a3 = a3;
            }
        }
        public static class Add extends Arithmetic {
            protected Add(int relBase, int pointer, Argument a1, Argument a2, Argument.PositionalArgument a3) {
                super(relBase, pointer, a1, a2, a3);
            }
        }
        public static class Mul extends Arithmetic {
            protected Mul(int relBase, int pointer, Argument a1, Argument a2, Argument.PositionalArgument a3) {
                super(relBase, pointer, a1, a2, a3);
            }
        }
        public static class LessThan extends Arithmetic {
            protected LessThan(int relBase, int pointer, Argument a1, Argument a2, Argument.PositionalArgument a3) {
                super(relBase, pointer, a1, a2, a3);
            }
        }
        public static class Equals extends Arithmetic {

            protected Equals(int relBase, int pointer, Argument a1, Argument a2, Argument.PositionalArgument a3) {
                super(relBase, pointer, a1, a2, a3);
            }
        }
        public static class In extends Instruction {
            public final Argument.PositionalArgument a;
            public final Argument.Abs value;

            protected In(int relBase, int pointer, Argument.PositionalArgument a, Argument.Abs value) {
                super(relBase, pointer);
                this.a = a;
                this.value = value;
            }

            @Override
            protected String toStringArgs() {
                return a + ", " + value.value;
            }
        }
        public static class Out extends Instruction {
            public final Argument a;

            protected Out(int relBase, int pointer, Argument a) {
                super(relBase, pointer);
                this.a = a;
            }

            @Override
            protected String toStringArgs() {
                return a.toString();
            }
        }
        public static class Halt extends Instruction {

            protected Halt(int relBase, int pointer) {
                super(relBase, pointer);
            }

            @Override
            protected String toStringArgs() {
                return "";
            }
        }
        public static abstract class Jump extends Instruction {
            public final Argument a1, a2;

            @Override
            protected String toStringArgs() {
                return a1 + ", " + a2;
            }

            protected Jump(int relBase, int pointer, Argument a1, Argument a2) {
                super(relBase, pointer);
                this.a1 = a1;
                this.a2 = a2;
            }
        }
        public static class JumpIfTrue extends Jump {

            protected JumpIfTrue(int relBase, int pointer, Argument a1, Argument a2) {
                super(relBase, pointer, a1, a2);
            }
        }
        public static class JumpIfFalse extends Jump {

            protected JumpIfFalse(int relBase, int pointer, Argument a1, Argument a2) {
                super(relBase, pointer, a1, a2);
            }
        }
        public static class AdjustBase extends Instruction {
            public final Argument a;

            protected AdjustBase(long relBase, long pointer, Argument a) {
                super(relBase, pointer);
                this.a = a;
            }

            @Override
            protected String toStringArgs() {
                return a.toString();
            }
        }
    }
}

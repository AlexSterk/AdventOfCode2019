import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.LinkedList;

interface InterpreterInput {
    boolean isEmpty();
    Long getNext() throws IOException;
    void clear();
}

interface InterpreterOutput {
    void write(Long i) throws IOException;
    void clear();
}

@EqualsAndHashCode(callSuper = true)
@Data
class InterpreterQueue extends LinkedList<Long> implements InterpreterInput, InterpreterOutput {

    @Override
    public Long getNext() {
        return this.poll();
    }

    @Override
    public void write(Long i) {
        this.offer(i);
    }
}
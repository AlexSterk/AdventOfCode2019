import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;

interface InterpreterInput {
    boolean isEmpty();
    Long getNext();
    void clear();
}

interface InterpreterOutput {
    boolean write(Long i);
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
    public boolean write(Long i) {
        return this.offer(i);
    }
}
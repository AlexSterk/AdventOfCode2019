import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

@EqualsAndHashCode(callSuper = true)
@Data
class InterpreterInputStream extends InputStream implements InterpreterInput {
    private final InputStream in;
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public Long getNext() throws IOException {
        return (long) in.read();
    }

    @Override
    public void clear() {
        try {
            in.skip(in.available());
        } catch (IOException ignored) {

        }
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }
}

@EqualsAndHashCode(callSuper = true)
@Data
class InterpreterOutputStream extends OutputStream implements InterpreterOutput {
    private final OutputStream out;
    
    @Override
    public void write(Long i) throws IOException {
        out.write(i.intValue());
    }

    @Override
    public void clear() {
        try {
            out.flush();
        } catch (IOException ignored) {
            
        }
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
}
package org.jmmo.vertx;

import io.vertx.core.buffer.Buffer;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamBuffer extends OutputStream {
    protected final Buffer buffer;

    public OutputStreamBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void write(int b) throws IOException {
        buffer.appendByte((byte) b);
    }
}

package org.jmmo.vertx;

import io.vertx.core.buffer.Buffer;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamBuffer extends InputStream {
    protected final Buffer buffer;
    protected int position;

    public InputStreamBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        if (position >= buffer.length()) {
            return -1;
        }

        return buffer.getByte(position++) & 0xff;
    }
}

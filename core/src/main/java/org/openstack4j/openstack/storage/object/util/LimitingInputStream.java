package org.openstack4j.openstack.storage.object.util;

import java.io.IOException;
import java.io.InputStream;

public class LimitingInputStream extends InputStream {
    private final InputStream delegate;
    private final int limit;
    private int readBytes = 0;

    public LimitingInputStream(InputStream delegate, int limit) {
        this.delegate = delegate;
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        if(readBytes >= limit) {
            return -1;
        }
        int read = delegate.read();
        if (read >= 0) {
            readBytes++;
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if(this.readBytes >= limit) {
            return -1;
        }
        final int read = delegate.read(b, off, Math.min(getBytesToRead(), len));
        this.readBytes += read;
        return read;
    }

    private int getBytesToRead() {
        return Math.max(0, limit - readBytes);
    }

    @Override
    public int available() throws IOException {
        return Math.min(getBytesToRead(), delegate.available());
    }
}

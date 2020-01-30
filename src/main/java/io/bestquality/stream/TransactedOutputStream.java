package io.bestquality.stream;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The {@code TransactedOutputStream} is used to implement an I/O transaction where
 * all write requests are buffered until the transaction is committed or rolled
 * back.
 * <p/>
 * Useful when generating sitemaps where there is a limit on the total size of the
 * sitemap file. This allows a sitemap entry to be buffered, so that the potential
 * number of bytes contained in the entry can be tested to see if the sitemap file
 * is within allowable limits.
 */
public class TransactedOutputStream
        extends FilterOutputStream {
    private final ByteArrayOutputStream buffer;

    private long bytesWritten;
    private boolean inProgress;

    public TransactedOutputStream(OutputStream delegate) {
        super(delegate);
        this.buffer = new ByteArrayOutputStream();
    }

    /**
     * Returns the total number of bytes written through this {@link OutputStream}
     *
     * @return The total number of bytes written
     */
    public long getBytesWritten() {
        return bytesWritten;
    }

    /**
     * Returns the total number of bytes currently buffered in the transaction.
     *
     * @return The total number of bytes currently buffered in the transaction.
     */
    public int getBytesBuffered() {
        return buffer.size();
    }

    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Begins a transaction, buffering all bytes written through this {@link OutputStream}
     * until a {@link #commit()} or a {@link #rollback()} is issued.
     *
     * @throws IOException If an error occurs while starting the transaction
     */
    public void begin()
            throws IOException {
        inProgress = true;
    }

    /**
     * If a transaction is in progress, buffers the write request until a
     * {@link #commit()} or a {@link #rollback()} is issued. If a transaction
     * is not in progress, the write request is forwarded to the decorated {@link OutputStream}
     *
     * @param b The byte to write
     * @throws IOException If an error occurs while writing the specified characters
     */
    @Override
    public void write(int b)
            throws IOException {
        if (inProgress) {
            buffer.write(b);
        } else {
            out.write(b);
            bytesWritten++;
        }
    }

    /**
     * If a transaction is in progress and there is buffered content, this operation
     * writes the buffered content to the decorated {@link OutputStream}
     *
     * @throws IOException
     */
    public void commit()
            throws IOException {
        try {
            int size = buffer.size();
            if (inProgress && size > 0) {
                buffer.writeTo(out);
                bytesWritten += size;
            }
        } finally {
            reset();
        }
    }

    /**
     * Terminates the in progress transaction and clears the buffer of any content
     */
    public void rollback() {
        reset();
    }

    private void reset() {
        inProgress = false;
        buffer.reset();
    }
}

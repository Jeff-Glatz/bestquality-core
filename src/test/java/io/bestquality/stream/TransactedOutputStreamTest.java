package io.bestquality.stream;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TransactedOutputStreamTest {
    private ByteArrayOutputStream byteArrayOutputStream;
    private TransactedOutputStream transactedOutputStream;
    private byte[] bytes;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        transactedOutputStream = new TransactedOutputStream(byteArrayOutputStream);
        bytes = new byte[]{'a', 'b', 'c'};
    }

    @Test
    public void shouldIndicateTransactionInProgress()
            throws Exception {
        assertThat(transactedOutputStream.isInProgress(), equalTo(false));
        transactedOutputStream.begin();
        assertThat(transactedOutputStream.isInProgress(), equalTo(true));
        transactedOutputStream.commit();
        assertThat(transactedOutputStream.isInProgress(), equalTo(false));
        transactedOutputStream.begin();
        assertThat(transactedOutputStream.isInProgress(), equalTo(true));
        transactedOutputStream.rollback();
        assertThat(transactedOutputStream.isInProgress(), equalTo(false));
    }

    @Test
    public void shouldWriteToDelegateWhenNoTransactionInProgress()
            throws Exception {
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.write(bytes);
        assertThat(byteArrayOutputStream.toString(), equalTo("abc"));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(3L));

        transactedOutputStream.write(bytes, 0, 2);
        assertThat(byteArrayOutputStream.toString(), equalTo("abcab"));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(5L));

        transactedOutputStream.write('d');
        assertThat(byteArrayOutputStream.toString(), equalTo("abcabd"));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(6L));
    }

    @Test
    public void shouldBufferBytesWhenTransactionInProgressAndWriteOnCommit()
            throws Exception {
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.begin();

        transactedOutputStream.write(bytes);
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(3));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.write(bytes, 0, 2);
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(5));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.write('d');
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(6));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.commit();

        assertThat(byteArrayOutputStream.toString(), equalTo("abcabd"));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(6L));
    }

    @Test
    public void shouldBufferBytesWhenTransactionInProgressAndDiscardOnRollback()
            throws Exception {
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.begin();

        transactedOutputStream.write(bytes);
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(3));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.write(bytes, 0, 2);
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(5));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.write('d');
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(6));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.rollback();

        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));
    }

    @Test
    public void shouldAccumulateBytesWritten()
            throws Exception {
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.begin();
        transactedOutputStream.write(bytes);
        transactedOutputStream.commit();
        assertThat(byteArrayOutputStream.toString(), equalTo("abc"));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(3L));

        transactedOutputStream.begin();
        transactedOutputStream.write(bytes, 0, 2);
        transactedOutputStream.commit();
        assertThat(byteArrayOutputStream.toString(), equalTo("abcab"));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(5L));

        transactedOutputStream.begin();
        transactedOutputStream.write('d');
        transactedOutputStream.commit();
        assertThat(byteArrayOutputStream.toString(), equalTo("abcabd"));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(6L));
    }

    @Test
    public void shouldNotAccumulateBytesWritten()
            throws Exception {
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.begin();
        transactedOutputStream.write(bytes);
        transactedOutputStream.rollback();
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.begin();
        transactedOutputStream.write(bytes, 0, 2);
        transactedOutputStream.rollback();
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));

        transactedOutputStream.begin();
        transactedOutputStream.write('d');
        transactedOutputStream.rollback();
        assertThat(byteArrayOutputStream.toString(), equalTo(""));
        assertThat(transactedOutputStream.getBytesBuffered(), equalTo(0));
        assertThat(transactedOutputStream.getBytesWritten(), equalTo(0L));
    }
}

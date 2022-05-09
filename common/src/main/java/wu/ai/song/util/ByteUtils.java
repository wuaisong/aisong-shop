package wu.ai.song.util;

import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author yangyj
 */
public final class ByteUtils {

    private ByteUtils() {

        throw new UnsupportedOperationException();
    }

    public static String asString(final byte[] raw) {
        return asString(raw, StandardCharsets.UTF_8);
    }

    public static String asString(final byte[] raw, Charset charset) {
        return new String(raw, charset);
    }
}

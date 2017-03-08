package ch.epfl.alpano.dem;

/**
 * @author : Jeremy Zerbib (257715)
 */

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

public class MemMapExample {
    public static void main(String[] args)
            throws IOException {
        File f = new File("N46E007.hgt");
        long l = f.length();
        try (FileInputStream s = new FileInputStream(f)) {
            ShortBuffer b = s.getChannel()
                    .map(MapMode.READ_ONLY, 0, l)
                    .asShortBuffer();

            for (int i = 0; i <= 10; ++i) {
                System.out.println(b.get(i) + " | " + i);
            }
        }
    }
}
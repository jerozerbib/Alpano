package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel {

    private final int FILE_LENGTH = 25934402;
    private final File file;
    private final Interval2D extent;
    private ShortBuffer b;
    private FileInputStream s;
    private final int startingX;
    private final int startingY;

    /**
     * Constructor
     * 
     * @param file
     */
    public HgtDiscreteElevationModel(File file) {
        String name = file.getName();
        checkArgument(checkName(name), "Le nom du fichier n'est pas le bon");
        checkArgument(file.length() == FILE_LENGTH,
                "La taille du fichier hgt n'est pas la bonne");

        try (FileInputStream s = new FileInputStream(file)) {
            this.s = s;
            b = s.getChannel()
                    .map(FileChannel.MapMode.READ_ONLY, 0, file.length())
                    .asShortBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int latitude = parseInt(name.substring(1, 3));
        int longitude = parseInt(name.substring(4, 7));

        sign(name, 0, latitude, 'S');
        sign(name, 3, longitude, 'W');

        this.file = file;
        this.startingX = longitude * SAMPLES_PER_DEGREE;
        this.startingY = latitude * SAMPLES_PER_DEGREE;
        Interval1D iX = new Interval1D(startingX,
                startingX + SAMPLES_PER_DEGREE);
        Interval1D iY = new Interval1D(startingY,
                startingY + SAMPLES_PER_DEGREE);
        this.extent = new Interval2D(iX, iY);
    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        checkArgument(this.extent().contains(x, y));
        int relY = abs(y - startingY) + 1;
        int lines = 3601 - relY;

        int relX = abs(x - startingX);

        int index = 3601 * lines + relX;
        return b.get(index);
    }

    @Override
    public void close() throws Exception {
        s.close();
        b = null;
    }

    /**
     * Checks that the file's name is acceptable.
     * 
     * @param name
     * @return boolean
     */
    private boolean checkName(String name) {
        if (name.length() != 11) {
            return false;
        }

        if (name.charAt(0) != 'N' && name.charAt(0) != 'S') {
            return false;
        }

        String latString = name.substring(1, 3);
        try {
            parseInt(latString);
        } catch (NumberFormatException e) {
            return false;
        }

        String longString = name.substring(4, 7);
        try {
            parseInt(longString);
        } catch (NumberFormatException e) {
            return false;
        }

        if (name.charAt(3) != 'W' && name.charAt(3) != 'E') {
            return false;
        }

        String extension = name.substring(7, 11);
        if (!extension.equals(".hgt")) {
            return false;
        }

        return true;

    }

    /**
     * Returns the longitude or latitude given a character.
     * 
     * @param name
     *            The name we want to check
     * @param index
     *            0 or 3
     * @param i
     *            Longitude or latitude
     * @param c1
     *            W or S
     * @return int
     */
    private int sign(String name, int index, int i, char c1) {
        if (name.charAt(index) == c1) {
            return (-1) * i;
        } else {
            return i;
        }
    }
}
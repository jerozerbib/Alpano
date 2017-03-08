package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static ch.epfl.alpano.dem.DiscreteElevationModel.sampleIndex;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel{

    private final int FILE_LENGTH = 25934402;
    private final File file;
    private final Interval2D extent;
    private ShortBuffer b;

    /**
     * @param file
     */
    public HgtDiscreteElevationModel(File file){
        try (FileInputStream s = new FileInputStream(file)) {
            String name = file.getName();
            checkArgument(name.length() == 11, "la longueur du nom du fichier n'est pas la bonne");
            int latitude = Integer.parseInt(name.substring(1, 3));
            int longitude = Integer.parseInt(name.substring(4, 8));
            checkArgument(name.charAt(0) == 'N' || name.charAt(0) == 'S', "La première lettre du fichier n'est pas la bonne");
            checkArgument(name.charAt(3) == 'E' || name.charAt(3) == 'W', "La deuxième lettre n'est pas la bonne");
            checkArgument(name.substring(8, 10).equals(".hgt"), "l'extension n'est pas la bonne");
            if (name.charAt(0) == 'S'){
                latitude = (-1) * latitude;
            }
            if (name.charAt(3) == 'W'){
                longitude = (-1) * longitude;
            }
            checkArgument(file.length() == FILE_LENGTH, "La taille du fichier hgt n'est pas la bonne");
            this.file = file;
            Interval1D iX = new Interval1D(longitude * SAMPLES_PER_DEGREE, longitude * SAMPLES_PER_DEGREE + SAMPLES_PER_DEGREE);
            Interval1D iY = new Interval1D(latitude * SAMPLES_PER_DEGREE, latitude * SAMPLES_PER_DEGREE + SAMPLES_PER_DEGREE);
            this.extent = new Interval2D(iX, iY);
            b = s.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length()).asShortBuffer();
        } catch (IOException | NumberFormatException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Interval2D extent(){
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        checkArgument(extent().contains(x, y));
        String name = file.getName();
        double line = Math.abs(sampleIndex((Integer.parseInt(name.substring(4, 8)))) - x);
        double column = Math.abs(sampleIndex((Integer.parseInt(name.substring(1, 3)))) - y);
        int index = (int) ((3601 * line) + column);
        return b.get(index);
    }

    @Override
    public void close() throws Exception {
        
    }
}
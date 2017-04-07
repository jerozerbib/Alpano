package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Math.toRadians;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public class GazetteerParser {

    private final static double SECONDS_PER_MINUTES = 60.0;
    private final static double SECONDS_PER_HOUR = SECONDS_PER_MINUTES * 60.0;

    /**
     * The Constructor of GazetteerParser
     */
    private GazetteerParser() {
    }

    /**
     * Creates a List of summits from a file.
     * 
     * @param file
     *            the file we read from
     * @return a list of summits
     * @throws IOException
     *             if there is an IOException or if a line has the wrong format
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException {
        List<Summit> summits = new ArrayList<>();
        try (BufferedReader b = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = b.readLine()) != null) {
                summits.add(correspondingSummit(line));
            }
        } catch (IOException | NumberFormatException
                | StringIndexOutOfBoundsException e) {
            throw new IOException(e);
        }
        return Collections.unmodifiableList(new ArrayList<>(summits));
    }

    /**
     * Returns the Summit of a line in the file. What we want to do in this
     * method is a decomposition of all the informations of a line.
     * 
     * @param line
     *            a line to read
     * @return the summit who has been read
     */
    private static Summit correspondingSummit(String line) {
        String longitude = line.substring(0, 9).trim();
        String latitude = line.substring(10, 18).trim();
        String elevation = line.substring(18, 24).trim();
        String name = line.substring(35).trim();
        double longitudeDouble = StringToRadians(longitude);
        double latitudeDouble = StringToRadians(latitude);
        GeoPoint p = new GeoPoint(longitudeDouble, latitudeDouble);
        return new Summit(name, p, parseInt(elevation));
    }

    /**
     * Converts a given String into radians
     * 
     * @param s
     *            the string
     * @return the radian corresponding to a string
     */
    private static double StringToRadians(String s) {
        String[] sDegrees = s.split(":");
        double sDouble = parseInt(sDegrees[0])
                + parseInt(sDegrees[1]) / SECONDS_PER_MINUTES
                + parseInt(sDegrees[2]) / SECONDS_PER_HOUR;
        return toRadians(sDouble);
    }
}

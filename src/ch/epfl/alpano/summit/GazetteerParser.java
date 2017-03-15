package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Math.toRadians;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class GazetteerParser {

    private static ArrayList<Summit> summits = new ArrayList<>();

    private GazetteerParser(){}

    public static List<Summit> readSummitsFrom(File file) {
        try (BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = b.readLine()) != null && !("".equals(line))) {
                summits.add(correspondingSummit(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return summits;
    }

    /**
     * Returns the Summit of a line in the file.
     * @param line
     * @return
     */
    private static Summit correspondingSummit(String line){
        String longitude = line.substring(0, 9).trim();
        String latitude = line.substring(10, 18).trim();
        String name = line.substring(35).trim();
        String elevation = line.substring(18, 24).trim();
        double longitudeDouble = StringToRadians(longitude);
        double latitudeDouble = StringToRadians(latitude);
        GeoPoint p = new GeoPoint(toRadians(longitudeDouble), toRadians(latitudeDouble));
        return new Summit(name, p, parseInt(elevation));
    }


    private static double StringToRadians(String s){
        String[] sDegrees = s.split(":");
        double sDouble = parseInt(sDegrees[0]) + parseInt(sDegrees[1]) / 60 + parseInt(sDegrees[2]) / 3600;
        return toRadians(sDouble);
    }
}

package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;

import java.io.File;

import static java.lang.Math.toRadians;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class ProfileTest {
    final static File HGT_FILE = new File("N46E006.hgt");
    final static int LENGTH = 100_000;
    final static double AZIMUTH = toRadians(45);
    final static double LONGITUDE = toRadians(6.00);
    final static double LATITUDE = toRadians(46.00);

    public static void main(String[] as) throws Exception {
        DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        GeoPoint o = new GeoPoint(LONGITUDE, LATITUDE);
        ElevationProfile p = new ElevationProfile(cDEM, o, AZIMUTH, LENGTH);


    }
}

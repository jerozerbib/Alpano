package ch.epfl.alpano;

import static java.lang.Math.toRadians;
import static java.lang.Math.toDegrees;

public class Main {
    public static void main(String agrs[]) {
        final int IMAGE_WIDTH = 10;
        final int IMAGE_HEIGHT = 10;

        final double ORIGIN_LON = toRadians(7.65);
        final double ORIGIN_LAT = toRadians(46.73);
        final int ELEVATION = 600;
        final double CENTER_AZIMUTH = toRadians(180);
        final double HORIZONTAL_FOV = toRadians(60);
        final int MAX_DISTANCE = 100_000;

        final PanoramaParameters PARAMS = new PanoramaParameters(
                new GeoPoint(ORIGIN_LON, ORIGIN_LAT), ELEVATION, CENTER_AZIMUTH,
                HORIZONTAL_FOV, MAX_DISTANCE, IMAGE_WIDTH, IMAGE_HEIGHT);
        System.out.println(toDegrees(PARAMS.azimuthForX(9)));

        // System.out.println(PARAMS.xForAzimuth(toRadians(150)));

        // System.out.println(toDegrees(PARAMS.altitudeForY((IMAGE_HEIGHT-1)/2.0)));

        // System.out.println(PARAMS.yForAltitude(toRadians(-29.99999)));

        // System.out.println(PARAMS.isValidSampleIndex(-2,-2));

        // System.out.println(PARAMS.linearSampleIndex(1, 8));

    }
}

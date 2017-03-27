package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

import java.util.function.DoubleUnaryOperator;

import static ch.epfl.alpano.Distance.EARTH_RADIUS;
import static ch.epfl.alpano.Math2.*;
import static java.lang.Math.cos;
import static java.lang.Math.tan;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class PanoramaComputer {
    private final ContinuousElevationModel dem;

    public PanoramaComputer(ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
    }

    public Panorama computePanorama(PanoramaParameters parameters) {
        final double maxD = parameters.maxDistance();
        final GeoPoint obsPos = parameters.observerPosition();
        final int dX = 64;
        final int epsilon = 4;
        double ray0;
        Panorama.Builder p = new Panorama.Builder(parameters);
        for (int i = 0; i < parameters.width(); i++) {
            double rayX = 0;
            ElevationProfile e = new ElevationProfile(dem, obsPos, parameters.azimuthForX(i), maxD);
            for (int j = parameters.height() - 1; j >= 0; j--) {
                double raySlope = parameters.altitudeForY(j);
                ray0 = parameters.observerElevation();
                DoubleUnaryOperator f = rayToGroundDistance(e, ray0, tan(raySlope));
                double lowerBoundFirst = firstIntervalContainingRoot(f, rayX, maxD, dX);
                System.out.println(lowerBoundFirst + " | " + " | " + i + " |" + j + " | " + maxD);
                double upperBound = lowerBoundFirst + dX;
                rayX = improveRoot(f, lowerBoundFirst, upperBound, epsilon);
                if (lowerBoundFirst != Double.POSITIVE_INFINITY) {
                    p.setDistanceAt(i, j, (float) (rayX / cos(raySlope)));
                    p.setElevationAt(i, j, (float) e.elevationAt(j));
                    p.setSlopeAt(i, j, (float) e.slopeAt(j));
                    p.setLongitudeAt(i, j, (float) e.positionAt(i).longitude());
                    p.setLatitudeAt(i, j, (float) e.positionAt(i).latitude());
                }
            }
        }
        return p.build();
    }

    public static DoubleUnaryOperator rayToGroundDistance(
            ElevationProfile profile, double ray0, double raySlope) {
        final double k = 0.13;
        return x -> ray0 + x * raySlope - (profile.elevationAt(x) - ((1 - k) / (2 * EARTH_RADIUS)) * sq(x));
        // We call the static field EARTH_RADIUS from the class Distance and the method sq() from Math2.
    }
}

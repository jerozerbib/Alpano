package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

import java.util.function.DoubleUnaryOperator;

import static ch.epfl.alpano.Distance.EARTH_RADIUS;
import static ch.epfl.alpano.Math2.*;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.cos;
import static java.lang.Math.tan;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class PanoramaComputer {
    private final ContinuousElevationModel dem;
    private final int dX = 64;
    private final int epsilon = 4;

    public PanoramaComputer(ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
    }

    public Panorama computePanorama(PanoramaParameters parameters) {
        final double maxD = parameters.maxDistance();
        final GeoPoint obsPos = parameters.observerPosition();
        double ray0 = parameters.observerElevation();
        Panorama.Builder p = new Panorama.Builder(parameters);
        for (int i = 0; i < parameters.width(); i++) {
            double rayX = 0;
            ElevationProfile e = new ElevationProfile(dem, obsPos, parameters.azimuthForX(i), maxD);
            for (int j = parameters.height() - 1; j >= 0; j--) {
                double raySlope = parameters.altitudeForY(j);
                DoubleUnaryOperator f = rayToGroundDistance(e, ray0, tan(raySlope));
                double lowerBoundFirst = firstIntervalContainingRoot(f, rayX, maxD, dX);
                double upperBound = lowerBoundFirst + dX;
                if (lowerBoundFirst != POSITIVE_INFINITY) {
                    rayX = improveRoot(f, lowerBoundFirst, upperBound, epsilon);
                    p.setDistanceAt(i, j, (float) (rayX / cos(raySlope)));
                    p.setElevationAt(i, j, (float) e.elevationAt(rayX));
                    p.setSlopeAt(i, j, (float) e.slopeAt(rayX));
                    p.setLongitudeAt(i, j, (float) e.positionAt(rayX).longitude());
                    p.setLatitudeAt(i, j, (float) e.positionAt(rayX).latitude());
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

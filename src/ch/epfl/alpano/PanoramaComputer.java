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
 * @author : Etienne Caquot (249949)
 */
public final class PanoramaComputer {
    private final ContinuousElevationModel dem;
    private final static int dX = 64;
    private final static int epsilon = 4;
    private final static double k = 0.13;
    private final static double K_RATIO = (1d - k) / (2d * EARTH_RADIUS);

    /**
     * Panorama's Constructor
     * 
     * @param dem
     *            the dem to set
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
    }

    /**
     * Computes the panorama from given parameters by calculating slopes of
     * differents rays from the obeserver's position.
     * 
     * @param parameters
     *            the parameter we use to compute the Panorama
     * @return Panorama computed with the parameters
     */
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
                    GeoPoint position = e.positionAt(rayX);
                    p.setDistanceAt(i, j, (float) (rayX / cos(raySlope)))
                            .setElevationAt(i, j, (float) dem.elevationAt(position))
                            .setSlopeAt(i, j, (float) dem.slopeAt(position))
                            .setLongitudeAt(i, j, (float) position.longitude())
                            .setLatitudeAt(i, j, (float) position.latitude());
                }
            }
        }
        return p.build();
    }

    /**
     * This method helps us build a function that calculates the ray's distance
     * to the ground.
     * 
     * @param profile
     *            the profile
     * @param ray0
     *            the initial elevation of the ray0
     * @param raySlope
     *            the slope of the ray
     * @return DoubleUnaryOperator a function that calculates the distance
     */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
        return x -> (ray0 + x * raySlope) - (profile.elevationAt(x) - K_RATIO * sq(x));
    }
}

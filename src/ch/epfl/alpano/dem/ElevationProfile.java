package ch.epfl.alpano.dem;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;

import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.lerp;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class ElevationProfile {

    private final ContinuousElevationModel elevationModel;
    private final double length;
    private final double[][] discreteElevation;
    private final static int TWO_POW_INDEX = 12;
    private final static int INDEX_EXTENSION = 2;

    /**
     * ElevationProfile's constructor
     * 
     * @param elevationModel
     *            the elevation model to set
     * @param origin
     *            the origin Geopoint to set
     * @param azimuth
     *            the azimuth to set
     * @param length
     *            the length to set
     * @throws IllegalArgumentException
     *             if the length is negative or if the azimuth is not canonical
     */
    public ElevationProfile(ContinuousElevationModel elevationModel,
            GeoPoint origin, double azimuth, double length) {

        checkArgument(Azimuth.isCanonical(azimuth));
        checkArgument(length > 0);
        this.length = length;
        this.elevationModel = requireNonNull(elevationModel);
        requireNonNull(origin);

        int upperBoundModel = (int) (scalb(length, -TWO_POW_INDEX))
                + INDEX_EXTENSION;

        discreteElevation = new double[upperBoundModel][3];
        discreteElevationCalculation(discreteElevation, origin, azimuth);

    }

    /**
     * Calculates the position of point at a given distance.
     *
     * @param x
     *            the position in the profile
     * @return the coordonates of the point at the given position
     *
     * @throws IllegalArgumentException
     *             if the position is not in the range of the profile
     */
    public GeoPoint positionAt(double x) {
        checkArgument((x >= 0) && (x <= length));

        double i = scalb(x, -TWO_POW_INDEX);
        int lowerIndex = (int) floor(i);

        double latitudeLowerBound = discreteElevation[lowerIndex][2];
        double latitudeUpperBound = discreteElevation[lowerIndex + 1][2];
        double longitudeLowerBound = discreteElevation[lowerIndex][1];
        double longitudeUpperBound = discreteElevation[lowerIndex + 1][1];

        double latitude = lerp(latitudeLowerBound, latitudeUpperBound,
                i - lowerIndex);
        double longitude = lerp(longitudeLowerBound, longitudeUpperBound,
                i - lowerIndex);

        return new GeoPoint(longitude, latitude);

    }

    /**
     * retourne la pente du terrain à la position donnée du profil
     *
     * @param x
     *            the position in the profile
     * @return the slope of the ground at the given position
     *
     * @throws IllegalArgumentException
     *             if the position is not in the range of the profile
     */
    public double slopeAt(double x) {
        checkArgument((x >= 0) && (x <= length));

        return elevationModel.slopeAt(positionAt(x));
    }

    /**
     * Calculates the elevation at a given distance.
     *
     * @param x
     *            the position in the profile
     * @return the elevation of the ground at a given distance
     *
     * @throws IllegalArgumentException
     *             if the position is not in the range of the profile
     */
    public double elevationAt(double x) {
        checkArgument((x >= 0) && (x <= length));

        return elevationModel.elevationAt(positionAt(x));

    }

    /**
     * Intermediate computation of the longitude and the latitude as well as the
     * distance of some discret points of the DEM and store them if the array
     *
     * @param discreteElevation
     *            the array in which we store the computations
     */
    private void discreteElevationCalculation(double[][] discreteElevation,
            GeoPoint origin, double azimuth) {
        for (int i = 0; i < discreteElevation.length; ++i) {

            discreteElevation[i][0] = Math.scalb(i, TWO_POW_INDEX);

            double xInRadians = Distance.toRadians(discreteElevation[i][0]);
            double originLatitude = origin.latitude();
            double originLongitude = origin.longitude();
            double MathAzimuth = toMath(azimuth);

            double pointLatitude = Math.asin(
                    sin(originLatitude) * cos(xInRadians) + cos(originLatitude)
                            * sin(xInRadians) * cos(MathAzimuth));

            double pointLongitude = (originLongitude - asin(
                    sin(MathAzimuth) * sin(xInRadians) / cos(pointLatitude))
                    + PI) % PI2 - PI;

            discreteElevation[i][1] = pointLongitude;

            discreteElevation[i][2] = pointLatitude;
        }

    }
}

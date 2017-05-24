package ch.epfl.alpano.dem;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;

import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Math2.floorMod;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public class ElevationProfile {

    private final ContinuousElevationModel elevationModel;
    private final GeoPoint origin;
    private final double azimuth;
    private final double length;
    private final double STEP = Distance.toRadians(4096);
    private final double[][] tab;

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
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length) {
        checkArgument(length > 0, "La longueur est négative");
        this.length = length;
        final int size = (int) Math.ceil(Distance.toRadians(length) / STEP);
        checkArgument(isCanonical(azimuth), "l'azimuth n'est pas canonique");
        this.elevationModel = requireNonNull(elevationModel);
        this.origin = requireNonNull(origin);
        this.azimuth = azimuth;
        tab = new double[size + 1][2];
        for (int i = 0; i < size; i += 1) {
            tab[i][0] = longitudeAt(i * STEP);
            tab[i][1] = latitudeAt(i * STEP);
        }
        tab[size][0] = origin.longitude() + toRadians(1); // In this case and
                                                          // the following we
                                                          // are using the
        tab[size][1] = origin.latitude() + toRadians(1); // static reference
                                                         // toRadians from Math.
    }

    /**
     * Calculates the elevation at a given distance.
     * 
     * @param x
     * @return double
     */
    public double elevationAt(double x) {
        checkArgument(x >= 0 && x <= length, "la valeur x n'est pas comprise dans la longueur du profil");
        return elevationModel.elevationAt(positionAt(x));
    }

    /**
     * Calculates the position of point at a given distance.
     * 
     * @param x
     * @return double
     */
    public GeoPoint positionAt(double x) {
        checkArgument(x <= length && x >= 0, "la valeur x n'est pas comprise dans la longueur du profil");
        double indexOfX = scalb(x, -12);
        int lowerBound = (int) floor(indexOfX);
        double longitude = lerp(tab[lowerBound][0], tab[lowerBound + 1][0], indexOfX - lowerBound);
        double latitude = lerp(tab[lowerBound][1], tab[lowerBound + 1][1], indexOfX - lowerBound);
        return new GeoPoint(longitude, latitude);
    }

    /**
     * Calculates the slope at a given distance.
     * 
     * @param x
     * @return double
     */
    public double slopeAt(double x) {
        checkArgument(x <= length && x >= 0,
                "la valeur x n'est pas comprise dans la longueur du profil");
        return elevationModel.slopeAt(positionAt(x));
    }

    /**
     * Calculates the value of the latitude with the given formulae.
     * 
     * @param x
     * @return double
     */
    private double latitudeAt(double x) {
        checkArgument(x <= Distance.toRadians(length),
                "la valeur x n'est pas comprise dans la longueur du profil");
        double lat = origin.latitude();
        double sinLat = sin(lat);
        double cosDist = cos(x);
        double cosLat = cos(lat);
        double sinDist = sin(x);
        double cosAz = cos(toMath(azimuth));
        return asin(sinLat * cosDist + cosLat * sinDist * cosAz);
    }

    /**
     * Calculates the value of the longitude with the given formulae.
     * 
     * @param x
     * @return double
     */
    private double longitudeAt(double x) {
        checkArgument(x <= Distance.toRadians(length),
                "la valeur x n'est pas comprise dans la longueur du profil");
        double longitude = origin.longitude();
        double sinAz = sin(toMath(azimuth));
        double sinDist = sin(x);
        double cosLatAt = cos(latitudeAt(x));
        double arcsin = asin((sinAz * sinDist) / cosLatAt);
        return floorMod(((longitude - arcsin) + PI), PI2) - PI;
    }
}
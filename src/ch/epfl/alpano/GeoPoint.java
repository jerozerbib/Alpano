package ch.epfl.alpano;

import java.util.Locale;

import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Azimuth.fromMath;
import static ch.epfl.alpano.Distance.toMeters;
import static ch.epfl.alpano.Math2.haversin;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.*;
import static java.lang.String.format;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public final class GeoPoint {
    private final double longitude, latitude;

    /**
     * GeoPoint's constructor
     * 
     * @param longitude
     *            the longitude to set
     * @param latitude
     *            the latitude to set
     * @throws IllegalArgumentException
     *             if the latitude or the longitude given is not valid
     */

    public GeoPoint(double longitude, double latitude) {
        checkArgument(longitude > -Math.PI && longitude < Math.PI);
        this.longitude = longitude;
        checkArgument(latitude > -Math.PI / 2 && latitude < Math.PI / 2);
        this.latitude = latitude;
    }

    /**
     * Returns the longitude.
     * 
     * @return the longitude
     */

    public double longitude() {
        return longitude;
    }

    /**
     * Returns the latitude.
     * 
     * @return the latitude
     */

    public double latitude() {
        return latitude;
    }

    /**
     * Calculates the distance between two points.
     * 
     * @param that
     *            the point to compare with
     * @return the distance between observator and the other point
     */

    public double distanceTo(GeoPoint that) {
        double haver1 = haversin(this.latitude() - that.latitude());
        double haver2 = haversin(this.longitude() - that.longitude());
        double cos = cos(this.latitude()) * cos(that.latitude()) * haver2;
        double sqr = sqrt(haver1 + cos);
        double a = 2 * asin(sqr);
        return toMeters(a);
    }

    /**
     * Calculates the azimuth of the other point in respect to the observator
     * 
     * @param that
     *            the point
     * @return the azimuth of the other point in respect to the observator
     */

    public double azimuthTo(GeoPoint that) {
        double sin1 = sin(this.longitude - that.longitude);
        double cos1 = cos(that.latitude);
        double num = sin1 * cos1;
        double cos2 = cos(this.latitude);
        double sin2 = sin(that.latitude);
        double sin3 = sin(this.latitude);
        double cos3 = cos(that.latitude);
        double cos4 = cos(this.longitude - that.longitude());
        double den = cos2 * sin2 - sin3 * cos3 * cos4;
        return fromMath(canonicalize(Math.atan2(num, den)));
    }

    /**
     * *overrides the toString method inherited from Object and gives back a
     * String composed of the point's coordinates (in degrees) with a 0.0001
     * precision.
     * 
     * @return the String corresponding to the GeoPoint
     */
    @Override
    public String toString() {
        Locale l = null;
        double longitudeDegree = toDegrees(this.longitude);
        double latitudeDegree = toDegrees(this.latitude);
        return format(l, "(%.4f,%.4f)", longitudeDegree, latitudeDegree);
    }
}
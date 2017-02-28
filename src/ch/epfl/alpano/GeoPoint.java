package ch.epfl.alpano;

import java.util.Locale;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

/**
 * @author etienne
 *
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
     */

    public GeoPoint(double longitude, double latitude) {
        Preconditions.checkArgument(longitude > -Math.PI && longitude < Math.PI);
        this.longitude = longitude;
        Preconditions.checkArgument(latitude > -Math.PI / 2 && latitude < Math.PI / 2);
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
        double haver1 = Math2.haversin(this.latitude() - that.latitude());
        double haver2 = Math2.haversin(this.longitude() - that.longitude());
        double cos = Math.cos(this.latitude()) * Math.cos(that.latitude()) * haver2;
        double sqr = Math.sqrt(haver1 + cos);
        double a = 2 * Math.asin(sqr);
        return Distance.toMeters(a);
    }

    /**
     * Calculates the azimuth of the other point in respect to the observator
     * 
     * @param that
     *            the point
     * @return the azimuth of the other point in respect to the observator
     */

    public double azimuthTo(GeoPoint that) {
        double sin1 = Math.sin(this.longitude - that.longitude);
        double cos1 = Math.cos(that.latitude);
        double num = sin1 * cos1;
        double cos2 = Math.cos(this.latitude);
        double sin2 = Math.sin(that.latitude);
        double sin3 = Math.sin(this.latitude);
        double cos3 = Math.cos(that.latitude);
        double cos4 = Math.cos(this.longitude - that.longitude());
        double den = cos2 * sin2 - sin3 * cos3 * cos4;
        return Azimuth.canonicalize(Math.atan2(num, den));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Locale l = null;
        double longitudeDegree = Math.toDegrees(this.longitude);
        double latitudeDegree = Math.toDegrees(this.latitude);
        return String.format(l, "(%.4f,%.4f)", longitudeDegree, latitudeDegree);
    }
}
package ch.epfl.alpano;

import java.util.Locale;

/**
 * @author Jeremy Zerbib
 * @author Etienne Caquot
 */

public final class GeoPoint {
    private final double longitude, latitude;

    /**
     * GeoPoint's constructor
     * @param longitude
     * @param latitude
     */

    public GeoPoint(double longitude, double latitude) {
        if ((longitude < (-Math.PI) || longitude > Math.PI) ||
                (latitude < (-Math.PI) / 2.0 || latitude > Math.PI / 2.0)) {
                    throw new IllegalArgumentException();
                } else {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

    /**
     * Returns the longitude.
     * @return double
     */

    public double longitude() {
        return longitude;
    }

    /**
     * Returns the latitude.
     * @return double
     */

    public double latitude() {
        return latitude;
    }

    /**
     * Calculates the distance between two points.
     * @param that
     * @return double
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
     * Calculates the azimut between two points
     * @param that
     * @return double
     */

    public double azimuthTo(GeoPoint that) {
        return Azimuth.canonicalize(
                Math.atan2(Math.sin(this.longitude - that.longitude) * Math.cos(that.latitude),
                        Math.cos(this.latitude) * Math.sin(that.latitude) -
                        Math.sin(this.latitude) * Math.cos(that.latitude) * Math.cos(this.longitude - that.longitude)
                )
        );
    }

    @Override
    public String toString() {
        Locale l = null;
        double longitudeDegree = Math.toDegrees(this.longitude);
        double latitudeDegree = Math.toDegrees(this.latitude);
        return String.format(l, "(%.4f,%.4f)", longitudeDegree, latitudeDegree);
    }
}
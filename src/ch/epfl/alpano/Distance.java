package ch.epfl.alpano;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public interface Distance {
    double EARTH_RADIUS = 6371000;

    /**
     * Convert a value in meters to radians.
     * 
     * @param distanceInMeters
     *            the distance in meters
     * @return the converted ditance in radians
     */
    static double toRadians(double distanceInMeters) {
        return distanceInMeters / EARTH_RADIUS;
    }

    /**
     * Convert a value in radians to meters.
     * 
     * @param distanceInRadians
     *            the distance in radians
     * @return the converted distance in meters
     */
    static double toMeters(double distanceInRadians) {
        return distanceInRadians * EARTH_RADIUS;
    }
}

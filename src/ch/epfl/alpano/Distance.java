package ch.epfl.alpano;

/**
 * @author Etienne Caquot
 * @author : Jeremy Zerbib  (257715)
 */

public interface Distance {
    double EARTH_RADIUS = 6371000;

    /**
     * Convert a value in meters to radians.
     * @param distanceInMeters
     * @return double
     */
    static double toRadians(double distanceInMeters){
        return distanceInMeters/EARTH_RADIUS;
    }

    /**
     * Convert a value in radians to meters.
     * @param distanceInRadians
     * @return double
     */
    static double toMeters(double distanceInRadians){
        return distanceInRadians*EARTH_RADIUS;
    }
}

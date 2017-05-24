package ch.epfl.alpano.gui;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public enum UserParameter {
    OBSERVER_LONGITUDE(60_000, 120_000),
    OBSERVER_LATITUDE(450_000, 480_000),
    OBSERVER_ELEVATION(300, 10_000),
    CENTER_AZIMUTH(0,359),
    HORIZONTAL_FIELD_OF_VIEW(1, 360),
    MAX_DISTANCE(10, 600),
    WIDTH(30, 16_000),
    HEIGHT(100, 4_000),
    SUPER_SAMPLING_EXPONENT(0, 2);

    private final int min;
    private final int max;

    /**
     * UserParameter's constructor
     * 
     * @param min
     *            the minimum value to set
     * @param max
     *            the maximum value to set
     */
    UserParameter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the closest valid value.
     * 
     * @param v
     *            the value to check
     * @return the corresponding closest valid value
     */
    public int sanitize(int v) {
        return max(min, min(v, max));
    }

}

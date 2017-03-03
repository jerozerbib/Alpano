package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public interface Azimuth {

    /**
     * Returns true if argument is a canonical azimuth, false otherwise
     * 
     * @param azimuth
     *            azimuth to check
     * @return true is the azimuth is canonical and false otherwise
     */
    static boolean isCanonical(double azimuth) {
        return azimuth >= 0 && azimuth < PI2;
    }

    /**
     * Returns the canonical azimuth equivalent to the one given
     * 
     * @param azimuth
     *            the non-canonical azimuth
     * @return the equivalent canonical azimuth
     */
    static double canonicalize(double azimuth) {
        return Math2.floorMod(azimuth, PI2);
    }

    /**
     * Transforms an azimuth in a mathematical angle, or throws the exception
     * IllegalArgumentException if the argument is not a canonical azimut
     * 
     * @param azimuth
     *            the azimuth to convert
     * @throws IllegalArgumentException
     *             the azimuth is not canonical
     * @return the corresponding mathematical angle
     */
    static double toMath(double azimuth) {
        checkArgument(isCanonical(azimuth), "The given azimuth is not canonical");
        return canonicalize(PI2 - azimuth);
    }
    /**
     * Transforms a mathematical angle in an azimuth, or throws the
     * IllegalArgumentException if the mathematical angle is not canonical
     *
     * @param angle
     *            the angle to convert
     * @throws IllegalArgumentException
     *             if the angle is not canonical
     * @return the correspinding azimuth
     */
    static double fromMath(double angle) {
        checkArgument(isCanonical(angle), "The given angle is not canonical");
        return canonicalize(PI2 - angle);
    }

    /**
     * Returns a string cooresponding to the octant in which the azimuth is,
     * throws the IllegalArgumentException if the azimuth is not canonical
     * 
     * @param azimuth
     *            the azimuth for which we want to make corresponds an octant
     * @param n
     *            the north
     * @param e
     *            the east
     * @param s
     *            the south
     * @param w
     *            the west
     * @throws IllegalArgumentException
     *             if the azimuth is not canonical
     * @return the corresponding octant
     */
    static String toOctantString(double azimuth, String n, String e, String s,
            String w) {
        String message = "";
        checkArgument(isCanonical(azimuth), "The given azimuth is not canonical");
        if ((azimuth > ((15.0 / 16.0) * PI2))
                || (azimuth <= ((1.0 / 16.0) * PI2))) {
            message = n;
        } else if ((azimuth > ((1.0 / 16.0) * PI2))
                && (azimuth <= ((3.0 / 16.0) * PI2))) {
            message = n + e;
        } else if ((azimuth > ((3.0 / 16.0) * PI2))
                && (azimuth <= ((5.0 / 16.0) * PI2))) {
            message = e;
        } else if ((azimuth > ((5.0 / 16.0) * PI2))
                && (azimuth <= ((7.0 / 16.0) * PI2))) {
            message = s + e;
        } else if ((azimuth > ((7.0 / 16.0) * PI2))
                && (azimuth <= ((9.0 / 16.0) * PI2))) {
            message = s;
        } else if ((azimuth > ((9.0 / 16.0) * PI2))
                && (azimuth <= ((11.0 / 16.0) * PI2))) {
            message = s + w;
        } else if ((azimuth > ((11.0 / 16.0) * PI2))
                && (azimuth <= ((13.0 / 16.0) * PI2))) {
            message = w;
        } else if ((azimuth > ((13.0 / 16.0) * PI2))
                && (azimuth <= ((15.0 / 16.0) * PI2))) {
            message = n + w;
        }
        return message;
    }
}
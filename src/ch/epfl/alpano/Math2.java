package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.PI;
import static java.lang.Math.floor;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public interface Math2 {
    double PI2 = PI * 2;

    /**
     * Elevates a number to the square.
     * 
     * @param x
     *            the number
     * @return the square of the number
     */
    static double sq(double x) {
        return x * x;
    }

    /**
     * Gives the rest of the default division of two numbers
     * 
     * @param x
     *            the numerator
     * @param y
     *            the denominator
     * @return the rest of the default division
     */
    static double floorMod(double x, double y) {
        return x - y * floor(x / y);
    }

    /**
     * Calculates the haversin value.
     * 
     * @param x
     *            the number
     * @return the haversin of the value
     */
    static double haversin(double x) {
        return sq(Math.sin(x / 2));
    }

    /**
     * Calculates the angular difference between two points.
     * 
     * @param a1
     *            first points
     * @param a2
     *            second point
     * @return the angular difference of the to points
     */
    static double angularDistance(double a1, double a2) {
        double n = (a2 - a1 + PI);
        return floorMod(n, PI2) - PI;
    }

    /**
     * Calculates the linear interpolation of f(x).
     * 
     * @param y0
     *            f(0)
     * @param y1
     *            f(1)
     * @param x
     *            in where we want to evaluate f
     * @return the linear interpolation
     */
    static double lerp(double y0, double y1, double x) {
        return y0 - x * (y0 - y1);
    }

    /**
     * Calculates the bilinear interpolation of f(x,y).
     * 
     * @param z00
     *            f(0,0)
     * @param z10
     *            f(1,0)
     * @param z01
     *            f(0,1)
     * @param z11
     *            f(1,1)
     * @param x
     *            in where we want to evaluate f
     * @param y
     *            in where we want to evaluate f
     * @return the bilinear interpolation
     */
    static double bilerp(double z00, double z10, double z01, double z11,
            double x, double y) {
        double z1 = lerp(z00, z10, x);
        double z2 = lerp(z01, z11, x);
        return lerp(z1, z2, y);
    }

    /**
     * Finds the lower bound of an interval of size dX in which there is a zero
     * of a function
     * 
     * @param f
     *            the function
     * @param minX
     *            the start of searching
     * @param maxX
     *            the end of searching
     * @param dX
     *            the size of the interval
     * @return the lower bound of the interval
     */

    static double firstIntervalContainingRoot(DoubleUnaryOperator f,
            double minX, double maxX, double dX) {
        double bound = 0;
        for (double i = minX; i <= maxX; i += dX) {
            if (f.applyAsDouble(i) * f.applyAsDouble(i + dX) < 0) {
                return i;
            } else {
                bound = POSITIVE_INFINITY;
            }
        }
        return bound;
    }

    /**
     * Finds the lower bound of an interval of size inferior or equal to epsilon
     * in which there is a zero of a function
     * 
     * @param f
     *            the function
     * @param x1
     *            the lower bound of searching
     * @param x2
     *            the upper bound of searching
     * @param epsilon
     *            the size of the interval
     * @return the lower bound of the interval
     */
    static double improveRoot(DoubleUnaryOperator f, double x1, double x2,
            double epsilon) {
        if (f.applyAsDouble(x1) * f.applyAsDouble(x2) > 0) {
            throw new IllegalArgumentException();
        }
        do {
            double xm = (x1 + x2) / 2;
            if (x2 - x1 == epsilon) {
                return x1;
            } else if (f.applyAsDouble(xm) == 0) {
                return xm;
            } else if (f.applyAsDouble(x1) * f.applyAsDouble(xm) < 0) {
                x2 = xm;
            } else {
                x1 = xm;
            }
        } while (x2 - x1 >= epsilon);

        return x1;
    }

}

package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.PI;
import static java.lang.Math.floor;
import static java.lang.Math.sin;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public interface Math2 {
    double PI2 = 2 * PI;

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
        return x - (y * floor(x / y));
    }

    /**
     * Calculates the haversin of a number.
     * 
     * @param x
     *            the number
     * @return the haversin of the value
     */
    static double haversin(double x) {
        return sq(sin(x / 2));
    }

    /**
     * Calculates the angular difference between two angles.
     * 
     * @param a1
     *            first angle
     * @param a2
     *            second angle
     * @return the angular difference of the two angles
     */
    static double angularDistance(double a1, double a2) {
        return floorMod((a2 - a1 + PI), PI2) - PI;
    }

    /**
     * Calculates the linear interpolation of f(x) between two points.
     * 
     * @param y0
     *            f(0), the first point
     * @param y1
     *            f(1), the second point
     * @param x
     *            point where we want to evaluate f
     * @return the linear interpolation of f(x) between two points
     */
    static double lerp(double y0, double y1, double x) {
        return y0 - x * (y0 - y1);
    }

    /**
     * Calculates the bilinear interpolation of f(x,y)between four points. Using
     * 3 times a linear interpolation.
     * 
     * @param z00
     *            f(0,0), the first point
     * @param z10
     *            f(1,0), the second point
     * @param z01
     *            f(0,1), the third point
     * @param z11
     *            f(1,1), the fourth point
     * @param x
     *            in where we want to evaluate f
     * @param y
     *            in where we want to evaluate f
     * @return the bilinear interpolation of f(x,y)
     */
    static double bilerp(double z00, double z10, double z01, double z11,
            double x, double y) {
        double z1 = lerp(z00, z10, x);
        double z2 = lerp(z01, z11, x);
        return lerp(z1, z2, y);
    }

    /**
     * Finds the lower bound of an interval of size dX in which there is a zero
     * of a function.
     * 
     * @param f
     *            the function
     * @param minX
     *            the start of searching
     * @param maxX
     *            the end of searching
     * @param dX
     *            the size of the interval
     * @throws IllegalArgumentException
     *             min is bigger than max or dx is 0 or less
     * @return the lower bound of the interval if it exists, infinity otherwise.
     */

    static double firstIntervalContainingRoot(DoubleUnaryOperator f,
            double minX, double maxX, double dX) {
        if (minX > maxX || dX <= 0) {
            throw new IllegalArgumentException(
                    "min est plus grand que max ou dX <= 0");
        }
        for (double i = minX; i <= (maxX - dX); i += dX) {
            if (f.applyAsDouble(i) * f.applyAsDouble(i + dX) <= 0) {
                return i;
            }
        }
        return POSITIVE_INFINITY;
    }

    /**
     * Finds the lower bound of an interval of size inferior to epsilon in which
     * there is a zero of a function.
     * 
     * @param f
     *            the function
     * @param x1
     *            the lower bound of searching
     * @param x2
     *            the upper bound of searching
     * @param epsilon
     *            the size of the interval
     * @throws IllegalArgumentException
     *             if there is no zero between the lower and the upper bound of
     *             searching
     * @return the lower bound of the interval
     */
    static double improveRoot(DoubleUnaryOperator f, double x1, double x2,
            double epsilon) {
        if (f.applyAsDouble(x1) * f.applyAsDouble(x2) > 0) {
            throw new IllegalArgumentException(
                    "f(x1) et f(x2) sont du même signe.");
        }
        while (x2 - x1 > epsilon) {
            double xm = (x1 + x2) / 2;
            if (f.applyAsDouble(xm) == 0) {
                return xm;
            } else if (f.applyAsDouble(x1) * f.applyAsDouble(xm) <= 0) {
                x2 = xm;
            } else {
                x1 = xm;
            }
        }
        return x1;
    }

}

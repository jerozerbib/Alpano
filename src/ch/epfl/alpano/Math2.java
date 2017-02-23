package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

/**
 * @author Etienne Caquot
 * @author : Jeremy Zerbib (257715)
 */
public interface Math2 {
    double PI2 = Math.PI * 2;

    /**
     * Elevates a number to the square.
     * @param x
     * @return double
     */
    static double sq(double x) {
        return x * x;
    }

    /**
     * Gives the rest of the division.
     * @param x
     * @param y
     * @return double
     */
    static double floorMod(double x, double y){
        return x - y * Math.floor(x/y);
    }

    /**
     * Calculates the haversin value.
     * @param x
     * @return double
     */
    static double haversin(double x){
        return sq(Math.sin(x/2));
    }

    /**
     * Calculates the angular distance between two points.
     * @param a1
     * @param a2
     * @return double
     */
    static double angularDistance(double a1, double a2){
        double n = (a2 - a1 + Math.PI);
        return floorMod(n, PI2) - Math.PI;
    }

    /**
     * Calculates the linear interpolation of f(x) with f(0) = y0 and f(1) = y1.
     * @param y0
     * @param y1
     * @param x
     * @return double
     */
    static double lerp(double y0, double y1, double x){
        return y0 - x * (y0 - y1);
    }

    /**
     * Calculates the bilinear interpolation of f(x,y) with f(0,0) = z00, f(1,0) = z10, f(0,1) = z01 and f(1,1) = z11
     * @param z00
     * @param z10
     * @param z01
     * @param z11
     * @param x
     * @param y
     * @return double
     */
    static double bilerp(double z00, double z10, double z01, double z11, double x, double y){
        double z1 = lerp(z00, z10, x);
        double z2 = lerp(z01, z11, x);
        return lerp(z1, z2, y);
    }

    /**
     * Finds the lower bound of thw zero of a function within a size of dX
     * @param f
     * @param minX
     * @param maxX
     * @param dX
     * @return double
     */

    static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX){
        double bound = 0;
        for (double i = minX; i <= maxX; i += dX){
                if (f.applyAsDouble(i) * f.applyAsDouble(i + dX) < 0){
                    return i;
                } else {
                    bound = Double.POSITIVE_INFINITY;
                }
        }
        return bound;
    }

    /**
     * Finds the lower bound of thw zero of a function within a size of epsilon
     * @param f
     * @param x1
     * @param x2
     * @param epsilon
     * @return double
     */
    static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon){
        if (f.applyAsDouble(x1) * f.applyAsDouble(x2) > 0){
            throw new IllegalArgumentException();
        }

        do {
            double xm = (x1 + x2) /2;
            if (x2 - x1 == epsilon){
                return x1;
            } else if (f.applyAsDouble(xm) == 0){
                return xm;
            } else if (f.applyAsDouble(x1) * f.applyAsDouble(xm) < 0){
                x2 = xm;
            } else {
                x1 = xm;
            }
        } while (x2 - x1 >= epsilon);
        return x1;
    }


}

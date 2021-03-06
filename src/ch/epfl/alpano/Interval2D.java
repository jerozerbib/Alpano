package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public final class Interval2D {
    private final Interval1D iX;
    private final Interval1D iY;

    /**
     * Interval2D's constructor
     * 
     * @param iX
     *            the first interval
     * @param iY
     *            the second interval
     * @throws NullPointerException
     *             if one of the two given interval is null
     */
    public Interval2D(Interval1D iX, Interval1D iY) {
        this.iX = requireNonNull(iX);
        this.iY = requireNonNull(iY);

    }

    /**
     * Gets the first interval
     * 
     * @return the first interval
     */
    public Interval1D iX() {
        return iX;
    }

    /**
     * Gets the second interval
     * 
     * @return the second interval
     */
    public Interval1D iY() {
        return iY;
    }

    /**
     * Checks if x and y are a part of the bidimensional interval
     * 
     * @param x
     *            the first parameter to check in the first interval
     * @param y
     *            the first parameter to check in the first interval
     * @return true is x is included in the first interval and if y is included
     *         in the second interval, false otherwise
     */
    public boolean contains(int x, int y) {
        return iX.contains(x) && iY.contains(y);
    }

    /**
     * Returns the size of the interval
     * 
     * @return the size of the bidimensional interval
     */
    public int size() {
        return iX().size() * iY().size();
    }

    /**
     * Gets the size of the intersection of two bidimensional intervals.
     * 
     * @param that
     *            the other bidimensional interval
     * @return the size of the intersection
     */
    public int sizeOfIntersectionWith(Interval2D that) {
        int sizeIX = this.iX().sizeOfIntersectionWith(that.iX());
        int sizeIY = this.iY().sizeOfIntersectionWith(that.iY());
        return (sizeIX * sizeIY);
    }

    /**
     * Get the bounding union of two bidimensional intervals.
     * 
     * @param that
     *            the other bidimensional interval
     * @return the bounding union
     */
    public Interval2D boundingUnion(Interval2D that) {
        Interval1D iXUnion = this.iX().boundingUnion(that.iX());
        Interval1D iYUnion = this.iY().boundingUnion(that.iY());
        return new Interval2D(iXUnion, iYUnion);
    }

    /**
     * Checks of two bidimensional intervals are unionable.
     * 
     * @param that
     *            the other bidimensional interval
     * @return true if the two intervals are unionable, false otherwise
     */
    public boolean isUnionableWith(Interval2D that) {
        int union = this.size() + that.size() - this.sizeOfIntersectionWith(that);
        return union == this.boundingUnion(that).size();
    }

    /**
     * Returns the union of two sets, otherwise it throws an error.
     * 
     * @param that
     *            the other interval
     * @return the union of the two intervals
     * @throws IllegalArgumentException
     *             if the two interval are not unionable
     */
    public Interval2D union(Interval2D that) {
        checkArgument(this.isUnionableWith(that));
        return this.boundingUnion(that);
    }


    @Override
    public boolean equals(Object thatO) {
        if (thatO instanceof Interval2D) {
            return this.iX().equals(((Interval2D) thatO).iX())
                    && this.iY().equals(((Interval2D) thatO).iY());
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return hash(iX(), iY());
    }


    @Override
    public String toString() {
        return this.iX().toString() + "x" + this.iY().toString();
    }

}

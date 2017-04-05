package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Objects.hash;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public final class Interval1D {

    private final int includedFrom, includedTo;

    /**
     * Interval1D's constructor
     * 
     * @param includedFrom
     *            lower bound of the interval
     * @param includedTo
     *            upper bound of the interval
     * @throws IllegalArgumentException
     *             if the upper bound is smaller than the lower bound
     */

    public Interval1D(int includedFrom, int includedTo) {
        checkArgument(includedFrom <= includedTo, "La borne inférieure est plus grande que la borne supérieure");
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }

    /**
     * Returns the value of includedFrom
     * 
     * @return lower bound
     */

    public int includedFrom() {
        return includedFrom;
    }

    /**
     * Returns the value of includedTo
     * 
     * @return upper bound
     */

    public int includedTo() {
        return includedTo;
    }

    /**
     * Checks if a given value is in the interval
     * 
     * @param v
     *            parameter to check
     * @return true if the parameter is int the interval, false otherwise
     */
    public boolean contains(int v) {
        return v <= includedTo() && v >= includedFrom();
    }

    /**
     * Returns the size of the interval
     * 
     * @return the size of the interval
     */
    public int size() {
        return includedTo() - includedFrom() + 1;
    }

    /**
     * Returns the size of the intersection of two intervals.
     * 
     * @param that
     *            the other interval
     * @return size of the intersection of the two intervals
     */
    public int sizeOfIntersectionWith(Interval1D that) {
        int max = max(this.includedFrom(), that.includedFrom());
        int min = min(this.includedTo(), that.includedTo());
        return max(min - max + 1, 0);
    }

    /**
     * Returns the bounding union of two intervals.
     * 
     * @param that
     *            the other interval
     * @return the bounding union of the two intervals
     */
    public Interval1D boundingUnion(Interval1D that) {
        int minIncludeFrom = min(that.includedFrom(), this.includedFrom());
        int maxIncludeTo = max(that.includedTo(), this.includedTo());
        return new Interval1D(minIncludeFrom, maxIncludeTo);
    }

    /**
     * Checks if two intervals are unionable
     * 
     * @param that
     *            the other interval
     * @return true if the tow are unionable, false otherwise
     */
    public boolean isUnionableWith(Interval1D that) {
        int sizeThis = this.size();
        int sizeThat = that.size();
        int intersection = this.sizeOfIntersectionWith(that);
        int union = sizeThis + sizeThat - intersection;
        return union == this.boundingUnion(that).size();
    }

    /**
     * Creates the interval resulting of the union of two intervals.
     * 
     * @param that
     *            the other interval
     * @return the union of the tow intervals
     * @throws IllegalArgumentException
     */
    public Interval1D union(Interval1D that) {
        checkArgument(this.isUnionableWith(that));
        return this.boundingUnion(that);
    }


    @Override
    public boolean equals(Object thatO) {
        if (thatO instanceof Interval1D) {
            return this.includedTo() == ((Interval1D) thatO).includedTo() && this.includedFrom() == ((Interval1D) thatO).includedFrom();
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return hash(includedFrom(), includedTo());
    }


    @Override
    public String toString() {
        return "[" + this.includedFrom() + ".." + this.includedTo() + "]";
    }

}

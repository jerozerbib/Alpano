package ch.epfl.alpano;

import java.util.Objects;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class Interval1D {

    private final int includedFrom, includedTo;

    /**
     * Interval1D's constructor
     * @param includedFrom
     * @param includedTo
     */

    public Interval1D(int includedFrom, int includedTo){
        if (includedFrom > includedTo){
            throw new IllegalArgumentException("La borne inférieure est plus grande que la borne supérieure");
        }
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }

    /**
     * Returns the value of includedFrom
     * @return int
     */

    public int includedFrom(){
        return includedFrom;
    }

    /**
     * Returns the value of includedTo
     * @return int
     */

    public int includedTo(){
        return includedTo;
    }

    /**
     * Checks if a given value is in the interval
     * @param v
     * @return boolean
     */
    public boolean contains(int v){
        return v <= includedTo() && v >=includedFrom();
    }

    /**
     * Returns the size of the interval
     * @return int
     */
    public int size(){
        return includedTo() - includedFrom() + 1;
    }

    /**
     * Returns the size of the intersection of two intervals.
     * @param that
     * @return int
     */
    public int sizeOfIntersectionWith(Interval1D that){
        if(this.contains(that.includedFrom()) && !this.contains(that.includedTo())){
            return new Interval1D(that.includedFrom(), this.includedTo()).size();
        } else if (that.contains(this.includedFrom()) && that.contains(this.includedTo())) {
            return this.size();
        } else if (this.contains(that.includedTo())){
            if (this.contains(that.includedFrom())){
                return that.size();
            } else {
                return new Interval1D(this.includedFrom(), that.includedTo()).size();
            }
        }  else {
            return 0;
        }
    }

    /**
     * Returns the bounding union of two intervals.
     * @param that
     * @return Interval1D
     */
    public Interval1D boundingUnion(Interval1D that){
        return new Interval1D(Math.min(that.includedFrom(), this.includedFrom()), Math.max(that.includedTo(), this.includedTo()));
    }


    /**
     * Checks if two intervals are unionable
     * @param that
     * @return boolean
     */
    public boolean isUnionableWith(Interval1D that){
        int sizeThis = this.size();
        int sizeThat = that.size();
        int intersection= this.sizeOfIntersectionWith(that);
        int union = sizeThis + sizeThat - intersection;
        return union == this.boundingUnion(that).size();
    }

    /**
     * Creates the intevals resulting of the union of two intervals.
     * @param that
     * @return Interval1D
     */
    public Interval1D union(Interval1D that){
        if (this.isUnionableWith(that)){
            return new Interval1D(this.boundingUnion(that).includedFrom(), this.boundingUnion(that).includedTo());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object thatO) {
        if (thatO instanceof Interval1D) {
            return this.union((Interval1D) thatO).size() == this.size();
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(includedFrom(), includedTo());
    }

    @Override
    public String toString(){
        return "[" + this.includedFrom() + ".." + this.includedTo() + "]";
    }



}

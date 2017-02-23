package ch.epfl.alpano;

import java.util.Objects;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot
 */
public final class Interval2D {
    private final Interval1D iX;
    private final Interval1D iY;

    /**
     * Interval2D's constructor
     * @param iX
     * @param iY
     */
    public Interval2D(Interval1D iX, Interval1D iY){
        if (iX == null || iY == null){
            throw new NullPointerException();
        }
        this.iX = iX;
        this.iY = iY;
    }

    /**
     * Gets the first interval
     * @return Interval1D
     */
    public Interval1D iX(){
        return iX;
    }

    /**
     * Gets the second interval
     * @return Interval1D
     */
    public Interval1D iY(){
        return iY;
    }

    /**
     * Checks if x and y are a part of the bidimensional interval
     * @param x
     * @param y
     * @return boolean
     */
    public boolean contains(int x, int y){
        return iX.contains(x) && iY.contains(y);
    }

    /**
     * Returns the size of the interval
     * @return int
     */
    public int size(){
        return iX().size() + iY().size();
    }


    /**
     * Gets the size of the intersection of two bidimensional intervals.
     * @param that
     * @return int
     */
    public int sizeOfIntersectionWith(Interval2D that){
        return this.iX().sizeOfIntersectionWith(that.iX()) + this.iY().sizeOfIntersectionWith(that.iY());
    }

    /**
     * Get the bounding union of two bidimensional intervals.
     * @param that
     * @return Interval2D
     */
    public Interval2D boundingUnion(Interval2D that){
        return new Interval2D(this.iX().boundingUnion(that.iX()), this.iY().boundingUnion(that.iY()));
    }

    /**
     * Checks of two bidimensional intervals are unionable.
     * @param that
     * @return boolean
     */
    public boolean isUnionableWith(Interval2D that){
        int sizeThis = this.size();
        int sizeThat = that.size();
        int intersection= this.sizeOfIntersectionWith(that);
        int union = sizeThis + sizeThat - intersection;
        return union == this.boundingUnion(that).size();
    }

    /**
     * Returns the union of two sets, otherwise it throws an error.
     * @param that
     * @return Interval2D
     * @throws IllegalArgumentException
     */
    public Interval2D union(Interval2D that){
        if (this.isUnionableWith(that)){
            return new Interval2D(this.boundingUnion(that).iX(), this.boundingUnion(that).iY());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object thatO) {
        if (thatO instanceof Interval2D) {
            return this.union((Interval2D) thatO).size() == this.size();
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(iX(), iY());
    }

    @Override
    public String toString(){
        return this.iX().toString() + "x" + this.iY().toString();
    }

}

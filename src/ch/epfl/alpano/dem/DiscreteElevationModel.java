package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.toDegrees;

/**
 * @author : Jeremy Zerbib (257715)
 */
public interface DiscreteElevationModel extends AutoCloseable {

    int SAMPLES_PER_DEGREE = 3600;
    double SAMPLES_PER_RADIAN = toDegrees(1) * SAMPLES_PER_DEGREE;

    /**
     * Returns the index corrresponding to a given angle in radian
     * @param angle
     * @return double
     */
    static double sampleIndex(double angle){
        return angle * SAMPLES_PER_RADIAN;
    }

    /**
     * Returns the scope of DEM (digital elevation model)
     * @return Interval2D
     */
    Interval2D extent();


    /**
     * Returns the elevation sample if the index is part of the DEM's scope. Otherwise, it throws an error.
     * @param x
     * @param y
     * @return double
     * @throws IllegalArgumentException
     */
    double elevationSample(int x, int y);


    /**
     * Returns the union of two DEM sample. If the two DEM sample are not unionable, it thorws an error.
     * @param that
     * @return DiscreteELevationModel
     * @throws IllegalArgumentException
     */
    default DiscreteElevationModel union(DiscreteElevationModel that){
        checkArgument(this.extent().isUnionableWith(that.extent()));
        return new CompositeDiscreteElevationModel(this, that);
    }
}

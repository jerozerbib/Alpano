package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.toDegrees;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public interface DiscreteElevationModel extends AutoCloseable {

    int SAMPLES_PER_DEGREE = 3600;
    double SAMPLES_PER_RADIAN = toDegrees(1) * SAMPLES_PER_DEGREE;

    /**
     * Returns the index corresponding to a given angle in radian
     * 
     * @param angle
     *            the angle
     * @return the index
     */
    static double sampleIndex(double angle) {
        return angle * SAMPLES_PER_RADIAN;
    }

    /**
     * Returns the scope of DEM (digital elevation model)
     * 
     * @return the scope
     */
    Interval2D extent();

    /**
     * Returns the elevation sample if the index is part of the DEM's scope.
     * Otherwise, it throws an error.
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @return the elevation at index x and y
     * @throws IllegalArgumentException
     *             one of the two indexes are not part of the DEM scope.
     */
    double elevationSample(int x, int y);

    /**
     * Returns the union of two DEM sample. If the two DEM sample are not
     * unionable, it throws an error.
     * 
     * @param that
     *            the other DEM
     * @return the union of that and this
     * @throws IllegalArgumentException
     *             if the DEMs are not unionable.
     */
    default DiscreteElevationModel union(DiscreteElevationModel that) {
        checkArgument(this.extent().isUnionableWith(that.extent()));
        return new CompositeDiscreteElevationModel(this, that);
    }
}

package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public final class CompositeDiscreteElevationModel
        implements DiscreteElevationModel {

    private DiscreteElevationModel dem1;
    private DiscreteElevationModel dem2;
    private Interval2D extent;

    /**
     * ContinuousElevationModel's constructor, union of two DEM's
     * 
     * @param dem1
     *            the fist DEM
     * @param dem2
     *            the second DEM
     * @throws NullPointerException
     *             if one of the two DEMs given in parameter is null
     */
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1,
            DiscreteElevationModel dem2) {
        this.dem1 = requireNonNull(dem1);
        this.dem2 = requireNonNull(dem2);
        extent = dem1.extent().union(dem2.extent());
    }

    /**
     * Overrides the extend method implemented from DiscreteElevationModel
     * 
     * @return the scope of this composite model, which is the union of the
     *         scope of it's two DEM's
     */
    @Override
    public Interval2D extent() {
        return extent;
    }

    /**
     * Returns the elevation sample if the index is part of the composite DEM's
     * scope. Otherwise, it throws an error.
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @return the elevation at index x and y
     * @throws IllegalArgumentException
     *             one of the two indexes are not part of the composite DEM
     *             scope.
     */
    @Override
    public double elevationSample(int x, int y) {
        checkArgument(extent.contains(x, y));
        if (dem1.extent().contains(x, y)) {
            return dem1.elevationSample(x, y);
        } else {
            return dem2.elevationSample(x, y);
        }

    }

    /**
     * Frees the resources associated with the object to which it is applied,
     * here the two DEM's of this composite DEM.
     * 
     * @throws Exception
     *             if one the two DEM's resource cannot be closed
     * 
     */
    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
    }
}

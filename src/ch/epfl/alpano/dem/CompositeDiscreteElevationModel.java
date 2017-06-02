package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

final class CompositeDiscreteElevationModel implements DiscreteElevationModel {

    private final DiscreteElevationModel dem1;
    private final DiscreteElevationModel dem2;
    private final Interval2D extent;

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

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        checkArgument(extent.contains(x, y));
        if (dem1.extent().contains(x, y)) {
            return dem1.elevationSample(x, y);
        } else {
            return dem2.elevationSample(x, y);
        }

    }

    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
    }
}

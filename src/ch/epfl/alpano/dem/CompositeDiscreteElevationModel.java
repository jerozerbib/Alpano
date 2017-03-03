package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class CompositeDiscreteElevationModel implements DiscreteElevationModel{

    private DiscreteElevationModel dem1;
    private DiscreteElevationModel dem2;
    private Interval2D extent;

    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2){
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
        if (dem1.extent().contains(x, y)){
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

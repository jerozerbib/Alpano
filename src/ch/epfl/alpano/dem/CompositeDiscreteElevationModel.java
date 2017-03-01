package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class CompositeDiscreteElevationModel implements DiscreteElevationModel{

    private DiscreteElevationModel dem1;
    private DiscreteElevationModel dem2;
    private DiscreteElevationModel union;

    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2){
        this.dem1 = requireNonNull(dem1);
        this.dem2 = requireNonNull(dem2);
        union = dem1.union(dem2);
    }

    public DiscreteElevationModel dem1() {
        return dem1;
    }

    public DiscreteElevationModel dem2() {
        return dem2;
    }

    @Override
    public Interval2D extent() {
        return union.extent();
    }

    @Override
    public double elevationSample(int x, int y) {
        Preconditions.checkArgument(union.extent().contains(x, y));
        if (dem1().extent().contains(x, y)){
            return dem1().elevationSample(x, y);
        } else {
            return dem2().elevationSample(x, y);
        }

    }

    @Override
    public void close() throws Exception {
        dem1().close();
        dem2().close();
    }
}

package ch.epfl.alpano.dem;

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.GeoPoint;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class ContinuousElevationModel {

    private DiscreteElevationModel dem;

    public ContinuousElevationModel(DiscreteElevationModel dem) {
        this.dem = requireNonNull(dem);
    }

    public DiscreteElevationModel dem() {
        return dem;
    }

    public double elevationAt(GeoPoint p) {
        return 0;
    }
    
    public double slopeAt(GeoPoint p){
        return 0;
    }
}

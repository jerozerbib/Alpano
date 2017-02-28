package ch.epfl.alpano.dem;


import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class ContinuousElevationModel {

    private DiscreteElevationModel dem;

    public ContinuousElevationModel(DiscreteElevationModel dem){
        this.dem = requireNonNull(dem);
    }


    public DiscreteElevationModel dem(){
        return dem;
    }
}

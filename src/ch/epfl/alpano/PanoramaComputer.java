package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

import java.util.function.DoubleUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class PanoramaComputer {
    private final ContinuousElevationModel dem;

    public PanoramaComputer(ContinuousElevationModel dem){
        this.dem = requireNonNull(dem);
    }

    public Panorama computePanorama(PanoramaParameters parameters){
        return new Panorama.Builder(parameters).build();
    }

    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope){
        return null;
    }
}

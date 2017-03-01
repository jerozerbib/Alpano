package ch.epfl.alpano.dem;


import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

import static ch.epfl.alpano.Distance.toMeters;
import static ch.epfl.alpano.Math2.sq;
import static ch.epfl.alpano.dem.DiscreteElevationModel.sampleIndex;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class ContinuousElevationModel {

    private DiscreteElevationModel dem;
    private static final double DNS = toMeters(1 / DiscreteElevationModel.SAMPLES_PER_RADIAN);

    public ContinuousElevationModel(DiscreteElevationModel dem){
        this.dem = requireNonNull(dem);
    }


    public DiscreteElevationModel dem(){
        return dem;
    }

    private double elevationAtDEMExtent(int x, int y){
        if (dem().extent().contains(x, y)){
            return dem().elevationSample(x, y);
        } else {
            return 0;
        }
    }

    public double elevationAt(GeoPoint p){
        double longIndex =  sampleIndex(p.longitude());
        double latIndex = sampleIndex(p.latitude());
        int lg = (int) Math.floor(longIndex); //a
        int lat = (int) Math.floor(latIndex); //b
        int neighborLg = (int) Math.ceil(longIndex); //a+1
        int neighborLat = (int) Math.ceil(latIndex); //b+1
        double z00 = elevationAtDEMExtent(lg, lat);
        double z10 = elevationAtDEMExtent(neighborLg, lat);
        double z01 = elevationAtDEMExtent(lg, neighborLat);
        double z11 = elevationAtDEMExtent(neighborLg, neighborLat);
        return Math2.bilerp(z00, z10, z01, z11, longIndex - lg, latIndex - lat);
    }

    private double slopeAtDEMExtent(int x, int y){
        double dZa = elevationAtDEMExtent(x, y) - elevationAtDEMExtent(x + 1, y);
        double dZb = elevationAtDEMExtent(x, y) - elevationAtDEMExtent(x, y + 1);
        double den = sqrt(sq(dZa)+ sq(dZb) + sq(DNS));
        return acos(DNS / den);
    }

    public double slopeAt(GeoPoint p){
        double longIndex =  sampleIndex(p.longitude());
        double latIndex = sampleIndex(p.latitude());
        int lg = (int) Math.floor(longIndex); //a
        int lat = (int) Math.floor(latIndex); //b
        int neighborLg = (int) Math.ceil(longIndex); //a+1
        int neighborLat = (int) Math.ceil(latIndex); //b+1
        double z00 = slopeAtDEMExtent(lg, lat);
        double z10 = slopeAtDEMExtent(neighborLg, lat);
        double z01 = slopeAtDEMExtent(lg, neighborLat);
        double z11 = slopeAtDEMExtent(neighborLg, neighborLat);
        return Math2.bilerp(z00, z10, z01, z11, longIndex - lg, latIndex - lat);
    }
}

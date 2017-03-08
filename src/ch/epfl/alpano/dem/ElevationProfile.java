package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;

import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Distance.toMeters;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class ElevationProfile {

    private final ContinuousElevationModel elevationModel;
    private final GeoPoint origin;
    private final double azimuth;
    private final double length;

    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length){
        checkArgument(isCanonical(azimuth), "l'azimuth n'est pas canonique");
        checkArgument(length >= 0, "La longueur est négative");
        this.elevationModel = requireNonNull(elevationModel);
        this.origin = requireNonNull(origin);
        this.azimuth = azimuth;
        this.length = length;
    }


    private double latitudeAt(double x){
        checkArgument(x <= length, "la valeur x n'est pas comprise dans la longueur du profil");
        double lat = origin.latitude();
        double sinLat = sin(lat);
        double cosDist = cos(toMeters(x));
        double cosLat = cos(lat);
        double sinDist = sin(toMeters(x));
        double cosAz = cos(toMath(azimuth));
        return asin(sinLat * cosDist + cosLat * sinDist * cosAz);
    }

    private double longitudeAt(double x){
        double longitude = origin.longitude();
        double sinAz = sin(toMath(azimuth));
        double sinDist = sin(toMeters(x));
        double cosLatAt = cos(latitudeAt(x));
        double arcsin = asin((sinAz * sinDist) / cosLatAt);
        return (((longitude - arcsin) + PI) % PI2) - PI;
    }

    public double elevationAt(double x){
        return elevationModel.elevationAt(positionAt(x));
    }

    public GeoPoint positionAt(double x){
        return new GeoPoint(longitudeAt(x), latitudeAt(x));
    }

    public double slopeAt(double x){
        return elevationModel.slopeAt(positionAt(x));
    }
}

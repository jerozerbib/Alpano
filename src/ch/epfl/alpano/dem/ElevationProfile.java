package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;

import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Distance.toMeters;
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
        double lat = origin.latitude();
        double sinLat = sin(lat);
        double cosDist = cos(toMeters(x));
        double cosLat = cos(lat);
        double sinDist = sin(toMeters(x));
        double cosAz = cos(azimuth);
        return asin(sinLat * cosDist + cosLat * sinDist * cosAz);
    }

    private double longitudeAt(double x){
        checkArgument(x <= length, "la valeur x n'est pas comprise dans la longueur du profil");
        double originLongitude = origin.longitude();
        double arcsin = asin((sin(azimuth) * sin(x)) / cos(latitudeAt(x)));
        return 0;
    }

    public double elevationAt(double x){
        return 0;
    }

    public GeoPoint positionAt(double x){
        return null;
    }

    public double slopeAt(double x){
        return 0;
    }
}

package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Distance.toRadians;
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
    private final double STEP = 4096;
    private final double[][] tab;

    public ElevationProfile(ContinuousElevationModel elevationModel,
            GeoPoint origin, double azimuth, double length) {
        final int size = (int) Math.ceil(length / STEP);
        checkArgument(isCanonical(azimuth), "l'azimuth n'est pas canonique");
        checkArgument(length >= 0, "La longueur est négative");
        this.elevationModel = requireNonNull(elevationModel);
        this.origin = requireNonNull(origin);
        this.azimuth = azimuth;
        this.length = length;
        tab = new double[size][3];
        for (int i = 0; i < size; i += STEP) {
            tab[i][0] = i;
            tab[i][1] = longitudeAt(i) + origin.longitude();
            tab[i][2] = latitudeAt(i) + origin.latitude();
        }
    }

    private double latitudeAt(double x) {
        checkArgument(x <= length, "la valeur x n'est pas comprise dans la longueur du profil");
        double lat = origin.latitude();
        double sinLat = sin(lat);
        double cosDist = cos(toRadians(x));
        double cosLat = cos(lat);
        double sinDist = sin(toRadians(x));
        double cosAz = cos(toMath(azimuth));
        return asin(sinLat * cosDist + cosLat * sinDist * cosAz);
    }

    private double longitudeAt(double x) {
        double longitude = origin.longitude();
        double sinAz = sin(toMath(azimuth));
        double sinDist = sin(toRadians(x));
        double cosLatAt = cos(latitudeAt(x));
        double arcsin = asin((sinAz * sinDist) / cosLatAt);
        return (((longitude - arcsin) + PI) % PI2) - PI;
    }

    public double elevationAt(double x) {
        System.out.print(positionAt(x).longitude());
        System.out.print(positionAt(x).latitude());
        return elevationModel.elevationAt(positionAt(x));
    }

    public GeoPoint positionAt(double x) {
        int flag = 0;
        for (int i = 0; i < tab.length - 1; ++i) {
            if (tab[i][0] <= x && tab[i + 1][0] >= x) {
                flag = i;
            }
        }
        return new GeoPoint((Math2.lerp(tab[flag][1], tab[flag + 1][1], x)),
                (Math2.lerp(tab[flag][2], tab[flag + 1][2], x)));
    }

    public double slopeAt(double x) {
        return elevationModel.slopeAt(positionAt(x));
    }
}

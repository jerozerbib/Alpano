package ch.epfl.alpano.dem;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;

import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.lerp;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;


/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

/**
 * Représente un profil altimétrique, comprenant l'atlitude, la pente et la
 * position, suivant un arc de grand cercle
 *
 */
public final class ElevationProfile{

    private final ContinuousElevationModel elevationModel;
    private final double length;
    private final double[][] discreteElevation;
    private final static int TWO_POW_INDEX = 12;
    private final static int INDEX_EXTENSION = 2;


    /**
     * construit un profil altimétrique basé sur le MNT donné et dont le tracé
     * débute au point origin, suit le grand cercle dans la direction donnée par
     * azimuth, et a une longueur de length mètres. .
     *
     * @param elevationModel
     *            MNT
     * @param origin
     *            point d'origin
     * @param azimuth
     *            direction du profil
     * @param length
     *            longueur du profil
     * @throws IllegalArgumentException
     *             si l'azimuth n'est pas canonique ou si la longueur n'est pas
     *             strictement positive
     * @throws NullPointerException
     *             si l'un des deux autres arguments est null
     */
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length) {

        checkArgument(Azimuth.isCanonical(azimuth));
        checkArgument(length > 0);
        this.length = length;
        this.elevationModel = requireNonNull(elevationModel);
        requireNonNull(origin);

        int upperBoundModel = (int) (scalb(length, -TWO_POW_INDEX)) + INDEX_EXTENSION;

        discreteElevation = new double[upperBoundModel][3];
        discreteElevationCalculation(discreteElevation,origin,azimuth);

    }

    /**
     * retourne les coordonnées du point à la position donnée du profil
     *
     * @param x
     *            position dans le profil
     * @return les coordonnées du point à la position donnée du profil
     *
     * @throws IllegalArgumentException
     *             si cette position n'est pas dans les bornes du profil
     */
    public GeoPoint positionAt(double x) {
        checkArgument((x >= 0) && (x <= length));

        double i = scalb(x, -TWO_POW_INDEX);
        int lowerIndex = (int) floor(i);

        double latitudeLowerBound = discreteElevation[lowerIndex][2];
        double latitudeUpperBound = discreteElevation[lowerIndex + 1][2];
        double longitudeLowerBound = discreteElevation[lowerIndex][1];
        double longitudeUpperBound = discreteElevation[lowerIndex + 1][1];

        double latitude = lerp(latitudeLowerBound, latitudeUpperBound, i - lowerIndex);
        double longitude = lerp(longitudeLowerBound, longitudeUpperBound, i - lowerIndex);

        return new GeoPoint(longitude, latitude);

    }

    /**
     * retourne la pente du terrain à la position donnée du profil
     *
     * @param x
     *            position dans le profil altimétrique
     * @return la pente du terrain à la position donnée du profil
     *
     * @throws IllegalArgumentException
     *             si cette position n'est pas dans les bornes du profil
     */
    public double slopeAt(double x) {
        checkArgument((x >= 0) && (x <= length));

        return elevationModel.slopeAt(positionAt(x));
    }

    /**
     * retourne l'altitude du terrain à la position donnée du profil
     *
     * @param x
     *            position dans le profil altimétrique
     * @return l'altitude du terrain à la position donnée du profil
     *
     * @throws IllegalArgumentException
     *             si cette position n'est pas dans les bornes du profil
     */
    public double elevationAt(double x) {
        checkArgument((x >= 0) && (x <= length));

        return elevationModel.elevationAt(positionAt(x));

    }

    /**
     * Calcul intermédiaire de la longitude de la latitude ainsi que la distance
     * de quelques points discrets du DEM et les stocke dans un tableau de
     * double
     *
     * @param discreteElevation
     *            tableau ou seront stockés ces valeurs discretes
     */
    private void discreteElevationCalculation(double[][] discreteElevation,GeoPoint origin, double azimuth) {
        for (int i = 0; i < discreteElevation.length; ++i) {

            discreteElevation[i][0] = Math.scalb(i, TWO_POW_INDEX);

            double xInRadians = Distance.toRadians(discreteElevation[i][0]);
            double originLatitude = origin.latitude();
            double originLongitude = origin.longitude();
            double MathAzimuth = toMath(azimuth);

            double pointLatitude = Math.asin(sin(originLatitude) * cos(xInRadians) + cos(originLatitude) * sin(xInRadians) * cos(MathAzimuth));

            double pointLongitude = (originLongitude - asin(
                    sin(MathAzimuth) * sin(xInRadians) / cos(pointLatitude))
                    + PI) % PI2 - PI;

            discreteElevation[i][1] = pointLongitude;

            discreteElevation[i][2] = pointLatitude;
        }

    }
}


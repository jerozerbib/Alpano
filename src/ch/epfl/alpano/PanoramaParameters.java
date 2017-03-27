package ch.epfl.alpano;

import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class PanoramaParameters {

    private final GeoPoint observerPosition;
    private final int observerElevation, maxDistance, width, height;
    private final double centerAzimuth, horizontalFieldOfView;
    private double delta;
    private double arcDelta;

    /**
     * PanoramaParameters' constructor
     * 
     * @param observerPosition
     * @param observerElevation
     * @param centerAzimuth
     * @param horizontalFieldOfView
     * @param maxDistance
     * @param width
     * @param height
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
            double centerAzimuth, double horizontalFieldOfView, int maxDistance,
            int width, int height) {
        this.observerElevation = observerElevation;
        this.observerPosition = requireNonNull(observerPosition);
        checkArgument(isCanonical(centerAzimuth),
                "L'azimuth n'est pas Canonique");
        this.centerAzimuth = centerAzimuth;
        checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= PI2,
                "Le champ de vision horizontal n'est pas dans les bornes");
        this.horizontalFieldOfView = horizontalFieldOfView;
        checkArgument(maxDistance > 0 && width > 0 && height > 0,
                "La valeur n'est pas strictement positive");
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
        this.delta = this.horizontalFieldOfView / (this.width - 1);
        this.arcDelta = 1.0 / delta;

    }

    /**
     * observerPosition's getter
     * 
     * @return GeoPoint
     */
    public GeoPoint observerPosition() {
        return this.observerPosition;
    }

    /**
     * observerElevation's getter
     * 
     * @return int
     */
    public int observerElevation() {
        return this.observerElevation;
    }

    /**
     * maxDistance's getter
     * 
     * @return int
     */
    public int maxDistance() {
        return this.maxDistance;
    }

    /**
     * width's getter
     * 
     * @return int
     */
    public int width() {
        return this.width;
    }

    /**
     * height's getter
     * 
     * @return int
     */
    public int height() {
        return this.height;
    }

    /**
     * horizontalFieldOfView's getter.
     * 
     * @return double
     */
    public double horizontalFieldOfView() {
        return this.horizontalFieldOfView;
    }

    /**
     * centerAzimuth's getter
     * 
     * @return double
     */
    public double centerAzimuth() {
        return this.centerAzimuth;
    }

    /**
     * verticalFieldOfView's "getter".
     * 
     * @return double
     */
    public double verticalFieldOfView() {
        return delta * (height - 1);
    }

    /**
     * Returns the azimuth for a given index.
     * 
     * @param x
     *            This index can be a double
     * @return double
     */
    public double azimuthForX(double x) {
        checkArgument(x >= 0 && x <= width - 1,
                "La largeur n'et pas dans les bornes");
        double indexForCenterAzimuth = (width() - 1) / 2.0;
        return canonicalize(
                centerAzimuth - (indexForCenterAzimuth - x) * delta);
    }

    /**
     * Returns an index for a given azimuth
     * 
     * @param a
     *            This azimuth is a double
     * @return double
     */
    public double xForAzimuth(double a) {
        checkArgument(
                (a <= centerAzimuth + horizontalFieldOfView() / 2.0)
                        && (a >= centerAzimuth - horizontalFieldOfView() / 2.0),
                "L'angle de vue est en dehors des limites");
        double indexForCenterAzimuth = (width() - 1) / 2.0;
        return indexForCenterAzimuth - Math2.angularDistance(a, centerAzimuth) * arcDelta;
    }

    /**
     * Returns the altitude for a given index.
     * 
     * @param y
     * @return double
     */
    public double altitudeForY(double y) {
        checkArgument(y >= 0 && y <= height - 1,
                "La hauteur n'est pas dans les bornes");
        double indexForAltZero = (height() - 1) / 2.0;
        return (indexForAltZero - y) * delta;
    }

    /**
     * Returns the y index for a given altitude.
     * 
     * @param a
     * @return double
     */
    public double yForAltitude(double a) {
        checkArgument(abs(a) <= verticalFieldOfView() / 2.0,
                "L'angle de vue n'est pas dans les bornes");
        double indexForAltZero = (height() - 1) / 2.0;
        return indexForAltZero - Math2.angularDistance(0, a) * arcDelta;
    }

    /**
     * Checks the validity of a sample index
     * 
     * @param x
     * @param y
     * @return boolean
     */
    protected boolean isValidSampleIndex(int x, int y) {
        if (x >= 0 && x <= width() - 1 && y >= 0 && y <= height() - 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the sample index as a linear index.
     * 
     * @param x
     * @param y
     * @return int
     */
    protected int linearSampleIndex(int x, int y) {
        return (x + 1) * (y + 1) - 1;
    }
}

package ch.epfl.alpano;

import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class PanoramaParameters {

    private final GeoPoint observerPosition;
    private final int observerElevation, maxDistance, width, height;
    private final double centerAzimuth, horizontalFieldOfView;
    private final double delta;
    private final double arcDelta;

    /**
     * PanoramaParameters' constructor
     * 
     * @param observerPosition
     *            the observerPosition to set
     * @param observerElevation
     *            the observerElevation to set
     * @param centerAzimuth
     *            the centerAzimuth to set
     * @param horizontalFieldOfView
     *            the horizontalFieldOfView to set
     * @param maxDistance
     *            the maxDistance to set
     * @param width
     *            the width to set
     * @param height
     *            the height to set
     * @throws IllegalArgumentException
     *             if the centerAzimuth is not canonical, if the
     *             horizontalFielOfView is not between 0 and 2*PI or if
     *             maxDistance, width, height are not positive
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
     * @return the observerPosition
     */
    public GeoPoint observerPosition() {
        return this.observerPosition;
    }

    /**
     * observerElevation's getter
     * 
     * @return the observerElevation
     */
    public int observerElevation() {
        return this.observerElevation;
    }

    /**
     * maxDistance's getter
     * 
     * @return the maxDistance
     */
    public int maxDistance() {
        return this.maxDistance;
    }

    /**
     * width's getter
     * 
     * @return the width
     */
    public int width() {
        return this.width;
    }

    /**
     * height's getter
     * 
     * @return the height
     */
    public int height() {
        return this.height;
    }

    /**
     * horizontalFieldOfView's getter.
     * 
     * @return the horizontalFieldOfView
     */
    public double horizontalFieldOfView() {
        return this.horizontalFieldOfView;
    }

    /**
     * centerAzimuth's getter
     * 
     * @return the centerAzimuth
     */
    public double centerAzimuth() {
        return this.centerAzimuth;
    }

    /**
     * verticalFieldOfView's "getter".
     * 
     * @return the verticalFieldOfView
     */
    public double verticalFieldOfView() {
        return delta * (height - 1);
    }

    /**
     * Returns the azimuth for a given index.
     * 
     * @param x
     *            the index (double)
     * @return the azimuth
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
     *            The azimuth
     * @return the index
     */
    public double xForAzimuth(double a) {
        checkArgument(
                (a <= centerAzimuth + horizontalFieldOfView() / 2.0)
                        && (a >= centerAzimuth - horizontalFieldOfView() / 2.0),
                "L'angle de vue est en dehors des limites");
        double indexForCenterAzimuth = (width() - 1) / 2.0;
        return indexForCenterAzimuth
                - angularDistance(a, centerAzimuth) * arcDelta;
    }

    /**
     * Returns the altitude for a given index.
     * 
     * @param y
     *            the index (double)
     * @return the altitude
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
     *            the azimuth
     * @return the altitude
     */
    public double yForAltitude(double a) {
        checkArgument(abs(a) <= verticalFieldOfView() / 2.0,
                "L'angle de vue n'est pas dans les bornes");
        double indexForAltZero = (height() - 1) / 2.0;
        return indexForAltZero - angularDistance(0, a) * arcDelta;
    }

    /**
     * Checks the validity of a sample index
     * 
     * @param x
     *            the first index
     * @param y
     *            the second index
     * @return true is the first index and the second are positive or null and
     *         respectively inferior to width and height
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
     *            the first index
     * @param y
     *            the second index
     * @return the linearIndex of x and y
     */
    protected int linearSampleIndex(int x, int y) {
        return width() * y + x;
    }
}

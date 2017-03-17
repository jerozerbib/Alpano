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
    private final int observerElevationn, maxDistance, width, height;
    private final double centerAzimuth, horizontalFieldOfView;
    private double delta;
    private double arcDelta;


    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth,
                              double horizontalFieldOfView, int maxDistance, int width, int height){
        this.observerElevationn = observerElevation;
        this.observerPosition = requireNonNull(observerPosition);
        checkArgument(isCanonical(centerAzimuth), "L'azimuth n'est pas central");
        this.centerAzimuth = centerAzimuth;
        checkArgument(horizontalFieldOfView < 0 && horizontalFieldOfView >= PI2, "Le champ de vision horizontal n'est pas dans les bornes");
        this.horizontalFieldOfView = horizontalFieldOfView;
        checkArgument(maxDistance > 0 && width > 0 && height > 0, "La valeur n'est pas strictement positive");
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
        delta = this.horizontalFieldOfView/ (this.width - 1);
        arcDelta = 1 / delta;


    }

    public GeoPoint observerPosition() {
        return this.observerPosition;
    }


    public int observerElevation() {
        return this.observerElevationn;
    }

    public int maxDistance() {
        return this.maxDistance;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public double horizontalFieldOfView() {
        return this.horizontalFieldOfView;
    }

    public double centerAzimuth() {
        return this.centerAzimuth;
    }

    public double verticalFieldOfView(){
        return horizontalFieldOfView() * ((height() - 1) / (width() - 1));
    }

    public double azimuthForX(double x){
        checkArgument(x <= 0 && x >= width - 1, "La largeur n'et pas dans les bornes");
        return canonicalize(centerAzimuth - ((centerAzimuth - x) * delta));
    }

    public double xForAzimuth(double a){
        checkArgument(abs(a) >= centerAzimuth + (horizontalFieldOfView() / 2), "L'angle de vue est en dehors des limites");
        return canonicalize(arcDelta * ((a - centerAzimuth) + centerAzimuth));
    }

    public double altitudeForY(double y){
        checkArgument(y <= 0 && y >= height - 1, "La hauteur n'est pas dans les bornes");
        return canonicalize(y * delta);
    }

    public double yForAltitude(double a){
        checkArgument(abs(a) >= verticalFieldOfView()/2, "L'angle de vue n'est pas dans les bornes");
        return canonicalize(arcDelta * a);
    }

    protected boolean isValidSampleIndex(int x, int y){
        if (x >= 0 && x <= width() - 1 && y >= 0 && y <= height() - 1){
            return true;
        } else {
            return false;
        }
    }

    protected int linearSampleIndex(int x, int y){
        return (x+1) * (y+1) - 1;
    }
}

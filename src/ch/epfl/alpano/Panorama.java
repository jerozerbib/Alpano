package ch.epfl.alpano;

import java.util.Arrays;

import static java.lang.Float.POSITIVE_INFINITY;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class Panorama {
    private final PanoramaParameters p;
    private final float[] distance, longitude, latitude, altitude, slope;

    private Panorama(PanoramaParameters p, float[] distance, float[] longitude, float[] latitude, float[] altitude, float[] slope) {
        this.p = p;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.slope = slope;
    }

    public PanoramaParameters parameters() {
        return this.p;
    }

    public float distanceAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return distance[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        }
    }

    public float distanceAt(int x, int y, float d) {
        if (p.isValidSampleIndex(x, y)) {
            return distance[p.linearSampleIndex(x, y)];
        } else {
            return d;
        }
    }

    public float longitudeAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return longitude[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        }
    }

    public float latitudeAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return latitude[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        }
    }

    public float elevationAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return altitude[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        }
    }

    public float slopeAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return slope[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        }
    }

    public static final class Builder {
        final PanoramaParameters parameters;
        final int size;
        final float[] tabDistance;
        final float[] tabLongitude;
        final float[] tabLatitude;
        final float[] tabAltitude;
        final float[] tabSlope;
        boolean ticker;

        /**
         * Builder's constructor
         * @param parameters
         */
        public Builder(PanoramaParameters parameters) {
            this.parameters = requireNonNull(parameters);
            size = parameters.height() * parameters.width();
            tabDistance = new float[size];
            tabLongitude = new float[size];
            tabLatitude = new float[size];
            tabAltitude = new float[size];
            tabSlope = new float[size];
            Arrays.fill(this.tabDistance, POSITIVE_INFINITY);
            Arrays.fill(this.tabLongitude, 0);
            Arrays.fill(this.tabLatitude, 0);
            Arrays.fill(this.tabAltitude, 0);
            Arrays.fill(this.tabSlope, 0);
            ticker = false;
        }

        /**
         * Sets the distance at a calculated position from x and y via linearSampleINdex
         * @param x
         * @param y
         * @param distance
         * @return Builder
         * @throws IndexOutOfBoundsException
         *          If the index is not valid.
         * @throws IllegalStateException
         *          If build() has been invoked already
         */
        public Builder setDistanceAt(int x, int y, float distance) {
            if (ticker) {
                throw new IllegalStateException("Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)){
                    throw new IndexOutOfBoundsException("L'index linaire n'est pas valide");
                } else {
                    tabDistance[parameters.linearSampleIndex(x, y)] = distance;
                    return this;
                }
            }
        }

        /**
         * Sets the longitude at a position from x and y via linearSampleINdex
         * @param x
         * @param y
         * @param distance
         * @return Builder
         * @throws IndexOutOfBoundsException
         *          If the index is not valid.
         * @throws IllegalStateException
         *          If build() has been invoked already
         */
        public Builder setLongitudeAt(int x, int y, float longitude) {
            if (ticker) {
                throw new IllegalStateException("Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)){
                    throw new IndexOutOfBoundsException("L'index linaire n'est pas valide");
                } else {
                    this.tabLongitude[parameters.linearSampleIndex(x, y)] = longitude;
                    return this;
                }
            }
        }

        /**
         * Sets the latitude at a position from x and y via linearSampleINdex
         * @param x
         * @param y
         * @param distance
         * @return Builder
         * @throws IndexOutOfBoundsException
         *          If the index is not valid.
         * @throws IllegalStateException
         *          If build() has been invoked already
         */
        public Builder setLatitudeAt(int x, int y, float latitude) {
            if (ticker) {
                throw new IllegalStateException("Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)){
                    throw new IndexOutOfBoundsException("L'index linaire n'est pas valide");
                } else {
                    this.tabLatitude[parameters.linearSampleIndex(x, y)] = latitude;
                    return this;
                }
            }
        }

        /**
         * Sets the elevation at a position from x and y via linearSampleINdex
         * @param x
         * @param y
         * @param distance
         * @return Builder
         * @throws IndexOutOfBoundsException
         *          If the index is not valid.
         * @throws IllegalStateException
         *          If build() has been invoked already
         */
        public Builder setElevationAt(int x, int y, float elevation) {
            if (ticker) {
                throw new IllegalStateException("Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)){
                    throw new IndexOutOfBoundsException("L'index linaire n'est pas valide");
                } else {
                    this.tabAltitude[parameters.linearSampleIndex(x, y)] = elevation;
                    return this;
                }
            }
        }

        /**
         * Sets the slope at a position from x and y via linearSampleINdex
         * @param x
         * @param y
         * @param distance
         * @return Builder
         * @throws IndexOutOfBoundsException
         *          If the index is not valid.
         * @throws IllegalStateException
         *          If build() has been invoked already
         */
        public Builder setSlopeAt(int x, int y, float slope) {
            if (ticker) {
                throw new IllegalStateException("Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)){
                    throw new IndexOutOfBoundsException("L'index linaire n'est pas valide");
                } else {
                    this.tabSlope[parameters.linearSampleIndex(x, y)] = slope;
                    return this;
                }
            }
        }

        /**
         * Builds a panorama and sets the ticker at true. If the has already has been set to true we cannot build once more.
         * @return Panorama
         * @throws IllegalStateException
         *          If build() has been invoked already
         */
        public Panorama build() {
            if (!ticker) {
                ticker = true;
                return new Panorama(parameters, tabDistance, tabLongitude, tabLatitude, tabAltitude, tabSlope);
            } else {
                throw new IllegalStateException("Une construction a déjà été faite");
            }
        }

    }

}

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

    /**
     * Panorama's constructor
     * 
     * @param p
     *            The parameters to set
     * @param distance
     *            The array of distance to set
     * @param longitude
     *            The array of longitude to set
     * @param latitude
     *            The array of latitude to set
     * @param altitude
     *            The array of altitude to set
     * @param slope
     *            The array of slope to set
     */
    private Panorama(PanoramaParameters p, float[] distance, float[] longitude,
            float[] latitude, float[] altitude, float[] slope) {
        this.p = p;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.slope = slope;
    }

    /**
     * Getter of the parameters
     * 
     * @return the parameters of the Panorama
     */
    public PanoramaParameters parameters() {
        return this.p;
    }

    /**
     * Return the distance at the given indexes
     * 
     * @param x
     *            the first index
     * @param y
     *            the second index
     * @throws IndexOutOfBoundsException
     *             if the indexes is not valid
     * @return the distance at the given indexes
     */
    public float distanceAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return distance[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        }
    }

    /**
     * Return the distance at the given indexes or a default value
     * 
     * @param x
     *            the first index
     * @param y
     *            the second index
     * @param d
     *            the default value
     * @return the distance at the given indexes if the index is valid or the
     *         default value
     */
    public float distanceAt(int x, int y, float d) {
        if (p.isValidSampleIndex(x, y)) {
            return distance[p.linearSampleIndex(x, y)];
        } else {
            return d;
        }
    }

    /**
     * Return the longitude at the given indexes
     * 
     * @param x
     *            the first index
     * @param y
     *            the second index
     * @throws IndexOutOfBoundsException
     *             if the indexes is not valid
     * @return the longitude at the given indexes
     */
    public float longitudeAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return longitude[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        }
    }

    /**
     * Return the latitude at the given indexes
     * 
     * @param x
     *            the first index
     * @param y
     *            the second index
     * @throws IndexOutOfBoundsException
     *             if the indexes is not valid
     * @return the latitude at the given indexes
     */
    public float latitudeAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return latitude[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        }
    }

    /**
     * Return the elevation at the given indexes
     * 
     * @param x
     *            the first index
     * @param y
     *            the second index
     * @throws IndexOutOfBoundsException
     *             if the indexes is not valid
     * @return the elevation at the given indexes
     */
    public float elevationAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return altitude[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        }
    }

    /**
     * Return the slope at the given indexes
     * 
     * @param x
     *            the first index
     * @param y
     *            the second index
     * @throws IndexOutOfBoundsException
     *             if the indexes is not valid
     * @return the slope at the given indexes
     */
    public float slopeAt(int x, int y) {
        if (p.isValidSampleIndex(x, y)) {
            return slope[p.linearSampleIndex(x, y)];
        } else {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
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
         * Builder's constructor create and fill with default values the 5
         * arrays of the Panorama
         * 
         * @param parameters
         *            the parameters used to set the length of the arrays
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
         * Sets the distance at a calculated position from x and y via
         * linearSampleIndex
         * 
         * @param x
         *            the first index
         * @param y
         *            the second index
         * @param distance
         *            the distance
         * @return this with a new value in tabDistance if the index was valid
         * @throws IndexOutOfBoundsException
         *             If the index is not valid.
         * @throws IllegalStateException
         *             If build() has been invoked already
         */
        public Builder setDistanceAt(int x, int y, float distance) {
            if (ticker) {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)) {
                    throw new IndexOutOfBoundsException(
                            "L'index linaire n'est pas valide");
                } else {
                    tabDistance[parameters.linearSampleIndex(x, y)] = distance;
                    return this;
                }
            }
        }

        /**
         * Sets the longitude at a position from x and y via linearSampleIndex
         * 
         * @param x
         *            the first index
         * @param y
         *            the second index
         * @param distance
         *            the distance
         * @return this with a new value in tabLongitude if the index was valid
         * @throws IndexOutOfBoundsException
         *             If the index is not valid.
         * @throws IllegalStateException
         *             If build() has been invoked already
         */
        public Builder setLongitudeAt(int x, int y, float longitude) {
            if (ticker) {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)) {
                    throw new IndexOutOfBoundsException(
                            "L'index linaire n'est pas valide");
                } else {
                    this.tabLongitude[parameters.linearSampleIndex(x,
                            y)] = longitude;
                    return this;
                }
            }
        }

        /**
         * Sets the latitude at a position from x and y via linearSampleIndex
         * 
         * @param x
         *            the first index
         * @param y
         *            the second index
         * @param distance
         *            the distance
         * @return this with a new value in tabLatitude if the index was valid
         * @throws IndexOutOfBoundsException
         *             If the index is not valid.
         * @throws IllegalStateException
         *             If build() has been invoked already
         */
        public Builder setLatitudeAt(int x, int y, float latitude) {
            if (ticker) {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)) {
                    throw new IndexOutOfBoundsException(
                            "L'index linaire n'est pas valide");
                } else {
                    this.tabLatitude[parameters.linearSampleIndex(x,
                            y)] = latitude;
                    return this;
                }
            }
        }

        /**
         * Sets the elevation at a position from x and y via linearSampleIndex
         * 
         * @param x
         *            the first index
         * @param y
         *            the second index
         * @param distance
         *            the distance
         * @return this with a new value in tabAltitude if the index was valid
         * @throws IndexOutOfBoundsException
         *             If the index is not valid.
         * @throws IllegalStateException
         *             If build() has been invoked already
         */
        public Builder setElevationAt(int x, int y, float elevation) {
            if (ticker) {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)) {
                    throw new IndexOutOfBoundsException(
                            "L'index linaire n'est pas valide");

                } else {
                    this.tabAltitude[parameters.linearSampleIndex(x,
                            y)] = elevation;
                    return this;
                }

            }
        }

        /**
         * Sets the slope at a position from x and y via linearSampleIndex
         * 
         * @param x
         *            the first index
         * @param y
         *            the second index
         * @param distance
         *            the distance
         * @return this with a new value in tabSlope if the index was valid
         * @throws IndexOutOfBoundsException
         *             If the index is not valid.
         * @throws IllegalStateException
         *             If build() has been invoked already
         */
        public Builder setSlopeAt(int x, int y, float slope) {
            if (ticker) {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            } else {
                if (!parameters.isValidSampleIndex(x, y)) {
                    throw new IndexOutOfBoundsException(
                            "L'index linaire n'est pas valide");
                } else {
                    this.tabSlope[parameters.linearSampleIndex(x, y)] = slope;
                    return this;
                }
            }
        }

        /**
         * Builds a Panorama and sets the ticker at true. If the has already has
         * been set to true we cannot build once more.
         * 
         * @return Panorama built
         * @throws IllegalStateException
         *             If build() has been invoked already
         */
        public Panorama build() {

            if (ticker) {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            } else {
                ticker = true;
                return new Panorama(parameters, tabDistance, tabLongitude,
                        tabLatitude, tabAltitude, tabSlope);
            }
        }

    }

}

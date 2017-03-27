package ch.epfl.alpano;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class Panorama {
    private PanoramaParameters p;
    private final int size;
    private final float[] distance, longitude, latitude, altitude, slope;

    private Panorama(PanoramaParameters p, float[] distance, float[] longitude,
            float[] latitude, float[] altitude, float[] slope) {
        this.p = p;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.slope = slope;
        size = p.width() * p.height();
    }

    public PanoramaParameters parameters() {
        return this.p;
    }

    public float distanceAt(int x, int y) {
        if (!p.isValidSampleIndex(x, y)) {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        } else {
            return distance[p.linearSampleIndex(x, y)];
        }
    }

    public float distanceAt(int x, int y, float d) {
        if (!p.isValidSampleIndex(x, y)) {
            return d;
        } else {
            return distance[p.linearSampleIndex(x, y)];
        }
    }

    public float longitudeAt(int x, int y) {
        if (!p.isValidSampleIndex(x, y)) {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        } else {
            return longitude[p.linearSampleIndex(x, y)];
        }
    }

    public float latitudeAt(int x, int y) {
        if (!p.isValidSampleIndex(x, y)) {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        } else {
            return latitude[p.linearSampleIndex(x, y)];
        }
    }

    public float elevationAt(int x, int y) {
        if (!p.isValidSampleIndex(x, y)) {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        } else {
            return altitude[p.linearSampleIndex(x, y)];
        }
    }

    public float slopeAt(int x, int y) {
        if (!p.isValidSampleIndex(x, y)) {
            throw new IndexOutOfBoundsException(
                    "L'index est plus grand que la taille du tableau");
        } else {
            return slope[p.linearSampleIndex(x, y)];
        }
    }

    public static final class Builder {
        PanoramaParameters parameters;
        int size;
        float[] tabDistance;
        float[] tabLongitude;
        float[] tabLatitude;
        float[] tabAltitude;
        float[] tabSlope;
        boolean ticker = false;

        public Builder(PanoramaParameters parameters) {
            this.parameters = requireNonNull(parameters);
            size = parameters.height() * parameters.width();
            tabDistance = new float[size];
            tabLongitude = new float[size];
            tabLatitude = new float[size];
            tabAltitude = new float[size];
            tabSlope = new float[size];
            Arrays.fill(this.tabDistance, Float.POSITIVE_INFINITY);
            Arrays.fill(this.tabLongitude, 0);
            Arrays.fill(this.tabLatitude, 0);
            Arrays.fill(this.tabAltitude, 0);
            Arrays.fill(this.tabSlope, 0);
        }

        public Builder setDistanceAt(int x, int y, float distance) {
            if (!ticker) {
                tabDistance[parameters.linearSampleIndex(x, y)] = distance;
                return this;
            } else {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            }
        }

        public Builder setLongitudeAt(int x, int y, float longitude) {
            if (!ticker) {
                this.tabLongitude[parameters.linearSampleIndex(x,
                        y)] = longitude;
                return this;
            } else {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            }
        }

        public Builder setLatitudeAt(int x, int y, float latitude) {
            if (!ticker) {
                this.tabLatitude[parameters.linearSampleIndex(x, y)] = latitude;
                return this;
            } else {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            }
        }

        public Builder setElevationAt(int x, int y, float elevation) {
            if (!ticker) {
                this.tabAltitude[parameters.linearSampleIndex(x,
                        y)] = elevation;
                return this;
            } else {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            }
        }

        public Builder setSlopeAt(int x, int y, float slope) {
            if (!ticker) {
                this.tabSlope[parameters.linearSampleIndex(x, y)] = slope;
                return this;
            } else {
                throw new IllegalStateException(
                        "Une construction a déjà été faite");
            }
        }

        public Panorama build() {
            ticker = true;
            return new Panorama(parameters, tabDistance, tabLongitude,
                    tabLatitude, tabAltitude, tabSlope);
        }

    }

}

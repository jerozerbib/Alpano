package ch.epfl.alpano;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class Panorama {
    PanoramaParameters p;
    int size = p.width() * p.height();
    float[] distance, longitude, latitude, altitude, slope;

    private Panorama(PanoramaParameters p, float[] distance, float[] longitude, float[] latitude, float[] altitude, float[] slope){
        this.p = p;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.slope = slope;
    }

    public PanoramaParameters parameters(){
        return this.p;
    }

    public float distanceAt(int x, int y){
        if (p.linearSampleIndex(x, y) > size){
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        } else {
            return distance[p.linearSampleIndex(x, y)];
        }
    }

    public float distanceAt(int x, int y, float d){
        if (p.linearSampleIndex(x, y) > size){
            return d;
        } else {
            return distance[p.linearSampleIndex(x, y)];
        }
    }

    public float longitudeAt(int x, int y){
        if (p.linearSampleIndex(x, y) > size){
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        } else {
            return longitude[p.linearSampleIndex(x, y)];
        }
    }

    public float latitudeAt(int x, int y){
        if (p.linearSampleIndex(x, y) > size){
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        } else {
            return latitude[p.linearSampleIndex(x, y)];
        }
    }


    public float elevationAt(int x, int y){
        if (p.linearSampleIndex(x, y) > size){
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        } else {
            return altitude[p.linearSampleIndex(x, y)];
        }
    }

    public float slopeAt(int x, int y){
        if (p.linearSampleIndex(x, y) > size){
            throw new IndexOutOfBoundsException("L'index est plus grand que la taille du tableau");
        } else {
            return slope[p.linearSampleIndex(x, y)];
        }
    }


    public static final class Builder{
        PanoramaParameters parameters;
        int size = parameters.height() * parameters.width();
        float[] distance = new float[size];
        float[] longitude = new float[size];
        float[] latitude = new float[size];
        float[] altitude = new float[size];
        float[] slope= new float[size];
        boolean ticker = false;




        public Builder(PanoramaParameters parameters){
            this.parameters = requireNonNull(parameters);
            Arrays.fill(this.distance, Float.POSITIVE_INFINITY);
            Arrays.fill(this.longitude, 0);
            Arrays.fill(this.latitude, 0);
            Arrays.fill(this.altitude, 0);
            Arrays.fill(this.slope, 0);
        }


        public Builder setDistanceAt(int x, int y, float distance){
           if (ticker){
               this.distance[parameters.linearSampleIndex(x, y)] = distance;
               return this;
           } else {
               throw new IllegalStateException("Une construction a déjà été faite");
           }
        }

        public Builder setLongitudeAt(int x, int y, float longitude){
            if (ticker){
                this.longitude[parameters.linearSampleIndex(x, y)] = longitude;
                return this;
            } else {
                throw new IllegalStateException("Une construction a déjà été faite");
            }
        }

        public Builder setLatitudeAt(int x, int y, float latitude){
            if (ticker){
                this.latitude[parameters.linearSampleIndex(x, y)] = latitude;
                return this;
            } else {
                throw new IllegalStateException("Une construction a déjà été faite");
            }
        }

        public Builder setElevationAt(int x, int y, float elevation){
            if (ticker){
                this.altitude[parameters.linearSampleIndex(x, y)] = elevation;
                return this;
            } else {
                throw new IllegalStateException("Une construction a déjà été faite");
            }
        }

        public Builder setSlopeAt(int x, int y, float slope){
            if (ticker){
                this.slope[parameters.linearSampleIndex(x, y)] = slope;
                return this;
            } else {
                throw new IllegalStateException("Une construction a déjà été faite");
            }
        }

        public Panorama build(){
            ticker = true;
            return new Panorama(parameters, distance, longitude, latitude, altitude, slope);
        }

    }

}

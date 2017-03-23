package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class Summit {
    private final String name;
    private final GeoPoint position;
    private final int elevation;

    public Summit(String name, GeoPoint position, int elevation) {
        this.name = requireNonNull(name);
        this.position = requireNonNull(position);
        this.elevation = elevation;
    }

    /**
     * name's getter
     * 
     * @return String
     */
    public String name() {
        return this.name;
    }

    /**
     * position's getter
     * 
     * @return GeoPoint
     */
    public GeoPoint position() {
        return this.position;
    }

    /**
     * elevation's getter
     * 
     * @return int
     */
    public int elevation() {
        return elevation;
    }

    @Override
    public String toString() {
        String name = this.name();
        return name + " " + this.position().toString() + " " + this.elevation();
    }
}

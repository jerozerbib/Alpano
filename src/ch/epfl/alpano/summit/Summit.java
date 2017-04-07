package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class Summit {
    private final String name;
    private final GeoPoint position;
    private final int elevation;

    /**
     * Summit's Constructor
     * 
     * @param name
     *            the name to set
     * @param position
     *            the position to set
     * @param elevation
     *            the elevation to set
     */
    public Summit(String name, GeoPoint position, int elevation) {
        this.name = requireNonNull(name);
        this.position = requireNonNull(position);
        this.elevation = elevation;
    }

    /**
     * name's getter
     * 
     * @return the name of the summit
     */
    public String name() {
        return this.name;
    }

    /**
     * position's getter
     * 
     * @return the position of the summit
     */
    public GeoPoint position() {
        return this.position;
    }

    /**
     * elevation's getter
     * 
     * @return the elevation of the summit
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

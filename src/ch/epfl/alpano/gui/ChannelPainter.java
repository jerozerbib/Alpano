package ch.epfl.alpano.gui;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public interface ChannelPainter {

    /**
     * Returns the value of a channel at a given point.
     * 
     * @param x
     *            the x index
     * @param y
     *            the y index
     * @return the value of the channel a this point
     */
    float valueAt(int x, int y);

    /**
     * Returns a ChannelPainter for which the value for a point which is the
     * distance difference between the furthest of its neighbours and this
     * point.
     * 
     * @param p
     *            the panorama
     * @return a ChannelPainter for which the value for a point which is the
     *         distance difference between the furthest of its neighbours and
     *         this point.
     */
    static ChannelPainter maxDistanceToNeighbors(Panorama p) {
        return (x, y) -> max(
                max(max(p.distanceAt(x - 1, y, 0), p.distanceAt(x, y + 1, 0)),
                        p.distanceAt(x + 1, y, 0)),
                p.distanceAt(x, y - 1, 0)) - p.distanceAt(x, y);
    }

    /**
     * Adds a value to the value produced by the ChannelPainter
     * 
     * @param i
     *            the value to add
     * @return ChannelPainter with the added value
     */
    default ChannelPainter add(float i) {
        return (x, y) -> i + valueAt(x, y);
    }

    /**
     * Subtract a value to the value produced by the ChannelPainter
     * 
     * @param i
     *            the value to subtract
     * @return ChannelPainter with the subtracted value
     */
    default ChannelPainter sub(float i) {
        return (x, y) -> valueAt(x, y) - i;
    }

    /**
     * Multiplies a value to the value produced by the ChannelPainter
     * 
     * @param i
     *            the value by which we multiply
     * @return ChannelPainter with a multiplied value
     */
    default ChannelPainter mul(float i) {
        return (x, y) -> i * valueAt(x, y);
    }

    /**
     * Divides a value to the value produced by the ChannelPainter
     * 
     * @param i
     *            the value by which we divide
     * @return ChannelPainter with the divided value
     */
    default ChannelPainter div(float i) {
        return (x, y) -> valueAt(x, y) / i;
    }

    /**
     * Apply the given DoubleUnaryOperator to the value produced by the
     * ChannelPainter
     * 
     * @param d
     *            the DoubleUnaryOperators
     * @return the DoubleUnaryOperator applied to the value
     */
    default ChannelPainter map(DoubleUnaryOperator d) {
        return (x, y) -> (float) d.applyAsDouble(valueAt(x, y));
    }

    /**
     * Inverts the value of the ChannelPainter
     * 
     * @return ChannelPainter with its inverted value
     */
    default ChannelPainter inverted() {
        return (x, y) -> 1 - valueAt(x, y);
    }

    /**
     * Clamps the value of the ChannelPainter
     * 
     * @return ChannelPainter with its clamped value
     */
    default ChannelPainter clamped() {
        return (x, y) -> max(0, min(1, valueAt(x, y)));
    }

    /**
     * Cycles the value of the ChannelPainter
     * 
     * @return ChannelPainter with its cycled value
     */
    default ChannelPainter cycling() {
        return (x, y) -> (float) Math2.floorMod(valueAt(x, y), 1);
    }

}
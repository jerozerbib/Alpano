package ch.epfl.alpano.gui;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author : Jeremy Zerbib (257715)
 */
public interface ChannelPainter {

    /**
     * Returns the value of a channel from a given point.
     * @param x
     * @param y
     * @return float
     */
    float valueAt(int x, int y);

    /**
     * Returns the max distance between two points of the
     * @param p
     * @return ChannelPainter
     */
    static ChannelPainter maxDistanceToNeighbors(Panorama p){
        return (x, y) -> max(
                max(
                        max(p.distanceAt(x -1, y, 0), p.distanceAt(x, y + 1, 0)),
                        p.distanceAt(x + 1, y, 0)),
                p.distanceAt(x, y - 1, 0)) - p.distanceAt(x, y);
    }

    /**
     * Adds a value to the value produced by the ChannelPainter
     * @param i
     * @return ChannelPainter
     */
    default ChannelPainter add(float i){
        return (x, y) -> i + valueAt(x, y);
    }

    /**
     * Subs a value to the value produced by the ChannelPainter
     * @param i
     * @return ChannelPainter
     */
    default ChannelPainter sub(float i) {
        return (x, y) -> valueAt(x, y) - i;
    }

    /**
     * Multiplies a value to the value produced by the ChannelPainter
     * @param i
     * @return ChannelPainter
     */
    default ChannelPainter mul(float i) {
        return (x, y) -> i * valueAt(x, y);
    }

    /**
     * Divides a value to the value produced by the ChannelPainter
     * @param i
     * @return ChannelPainter
     */
    default ChannelPainter div(float i) {
        return (x, y) -> valueAt(x, y) / i;
    }

    /**
     * Maps the value of the ChannelPainter
     * @param d
     * @return
     */
    default ChannelPainter map(DoubleUnaryOperator d){
        return (x,y) -> (float) d.applyAsDouble(valueAt(x, y));
    }

    /**
     * Inverts the value of the ChannelPainter
     * @return ChannelPainter
     */
    default ChannelPainter inverted(){
        return (x, y) -> 1 - valueAt(x,y);
    }

    /**
     * Clamps the value of the ChannelPainter
     * @return ChannelPainter
     */
    default ChannelPainter clamped(){
        return (x, y) -> max(0, min(1, valueAt(x, y)));
    }

    /**
     * Cycles the value of the ChannelPainter
     * @return ChannelPainter
     */
    default ChannelPainter cycling(){
        return (x, y) -> (float) Math2.floorMod(valueAt(x, y), 1);
    }

}

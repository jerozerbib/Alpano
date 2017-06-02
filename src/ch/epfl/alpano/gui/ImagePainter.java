package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

@FunctionalInterface
public interface ImagePainter {

    /**
     * Returns the Color at a given position
     * 
     * @param x
     *            the x index
     * @param y
     *            the y index
     * @return Color at a given position
     */
    Color colorAt(int x, int y);

    /**
     * Returns a HSB ImagePainter corresponding to the given ones
     * 
     * @param h
     *            the hue ChannelPainter
     * @param s
     *            the saturation ChannelPainter
     * @param b
     *            the brightness ChannelPainter
     * @param op
     *            the opacity ChannelPainter
     * @return ImagePainter from several ChannelPainter
     */
    static ImagePainter hsb(ChannelPainter h, ChannelPainter s,
            ChannelPainter b, ChannelPainter op) {
        return (x, y) -> Color.hsb(h.valueAt(x, y), s.valueAt(x, y),
                b.valueAt(x, y), op.valueAt(x, y));
    }

    /**
     * Returns gray ImagePainter corresponding to the given ones
     * 
     * @param g
     *            the gray ChannelPainter
     * @param op
     *            the opacity ChannelPainter
     * @return ImagePainter
     */
    static ImagePainter gray(ChannelPainter g, ChannelPainter op) {
        return (x, y) -> Color.gray(g.valueAt(x, y), op.valueAt(x, y));
    }

}
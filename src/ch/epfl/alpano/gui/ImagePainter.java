package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

/**
 * @author : Jeremy Zerbib (257715)
 */
public interface ImagePainter {

    /**
     * Returns the Color at a given position
     * @param x
     * @param y
     * @return Color
     */
    Color colorAt(int x, int y);

    /**
     * Returns ImagePainter of a value given a hab Color
     * @param t
     * @param sat
     * @param lum
     * @param op
     * @return ImagePainter
     */
    static ImagePainter hsb(ChannelPainter h, ChannelPainter s, ChannelPainter b, ChannelPainter op){
        return (x, y) -> Color.hsb(h.valueAt(x, y), s.valueAt(x, y), b.valueAt(x, y), op.valueAt(x, y));
    }

    /**
     * Returns ImagePainter of a value given a gray Color
     * @param t
     * @param sat
     * @param lum
     * @param op
     * @return ImagePainter
     */
    static ImagePainter gray(ChannelPainter t, ChannelPainter op){
        return (x, y) -> Color.gray(t.valueAt(x, y), op.valueAt(x, y));
    }

}

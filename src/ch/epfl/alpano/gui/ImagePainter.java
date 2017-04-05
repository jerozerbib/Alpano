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
    static ImagePainter hsb(ChannelPainter t, ChannelPainter sat, ChannelPainter lum, ChannelPainter op){
        return (x, y) -> Color.hsb(t.valueAt(x, y), sat.valueAt(x, y), lum.valueAt(x, y), op.valueAt(x, y));
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

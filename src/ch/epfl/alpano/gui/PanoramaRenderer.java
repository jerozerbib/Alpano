package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.WritableImage;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public interface PanoramaRenderer {

    /**
     * Renders a Panorama
     * @param p
     *          the panorama to render
     * @param im
     *          the different channels that paint the panorama
     * @return WritableImage
     *          returns an image with all the right colorization.
     */
    static WritableImage renderPanorama(Panorama p, ImagePainter im){
        int width = p.parameters().width();
        int height = p.parameters().height();
        WritableImage pix = new WritableImage(width, height);
        int i,j;
        for (i = 0; i < width; ++i){
            for (j = 0; j < height; ++j){
                pix.getPixelWriter().setColor(i, j, im.colorAt(i, j));
            }
        }
        return pix;
    }
}

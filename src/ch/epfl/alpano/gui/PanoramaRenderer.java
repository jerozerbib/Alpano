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
        WritableImage pix = new WritableImage(p.parameters().width(), p.parameters().height());
        for (int i = 0; i < p.parameters().width(); ++i){
            for (int j = 0; j < p.parameters().height(); ++j){
                pix.getPixelWriter().setColor(i, j, im.colorAt(i, j));
            }
        }
        return pix;
    }
}

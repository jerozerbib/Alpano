package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * @author : Jeremy Zerbib (257715)
 */
public interface PanoramaRenderer {

    static WritableImage rendererPanorama(Panorama p, ImagePainter im){
        PixelWriter pix = new WritableImage(p.parameters().width(), p.parameters().height()).getPixelWriter();
    }
}

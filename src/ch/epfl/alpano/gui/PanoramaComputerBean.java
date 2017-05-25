package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.util.List;

import static ch.epfl.alpano.gui.PanoramaRenderer.renderPanorama;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public class PanoramaComputerBean {
    private final int HUE_DIV = 100_000, HUE_MUL = 360, SAT_DIV = 200_000, BR_MUL1 = 2;
    private final float BR_MUL2 = 0.7f, BR_ADD = 0.3f;
    private final ObjectProperty<Panorama> panorama;
    private final ObjectProperty<PanoramaUserParameters> pUserParameters;
    private final ObjectProperty<Image> image;
    private final Labelizer lab;
    private final PanoramaComputer panoramaComputer;
    private final ObservableList<Node> labels;

    /**
     * PanoramaComputerBean's constructor
     * @param list
     * @param cDEM
     */
    public PanoramaComputerBean(List<Summit> list, ContinuousElevationModel cDEM){
        lab = new Labelizer(cDEM, list);
        labels = FXCollections.observableArrayList();
        panoramaComputer = new PanoramaComputer(cDEM);
        panorama = new SimpleObjectProperty<>();
        image = new SimpleObjectProperty<>();
        pUserParameters = new SimpleObjectProperty<>();
        pUserParameters.addListener((b, o, n) -> {
            Panorama p = panoramaComputer.computePanorama(n.panoramaParameters());
            ChannelPainter distance = p::distanceAt;
            ChannelPainter slope = p::slopeAt;
            ChannelPainter h = distance.div(HUE_DIV).cycling().mul(HUE_MUL);
            ChannelPainter s = distance.div(SAT_DIV).clamped().inverted();
            ChannelPainter br = slope.mul(BR_MUL1).div((float) Math.PI).inverted().mul(BR_MUL2).add(BR_ADD);
            ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
            List<Node> newNodeList = lab.labels(n.panoramaDisplayParameters());
            ImagePainter l = ImagePainter.hsb(h, s, br, opacity);

            labels.setAll(newNodeList);
            image.set(renderPanorama(p, l));
        });
    }

    /**
     * Gets the PanoramaUserParameters as an ObjectProperty.
     * @return ObjectProperty<PanoramaUserParameters>
     */
    public ObjectProperty<PanoramaUserParameters> parametersProperty(){
        return pUserParameters;
    }

    /**
     * Gets the PanoramaUserParameters as themselves
     * @return PanoramaUserParameters
     */
    public PanoramaUserParameters getParamaters(){
        return parametersProperty().get();
    }

    /**
     * Sets the ParoramaUserParameters with new ones
     * @param newParameters
     */
    public void setParameters(PanoramaUserParameters newParameters){
        parametersProperty().set(newParameters);
    }

    /**
     * Gets the Panorama as a ReadOnlyObjectProperty
     * @return ReadOnlyObjectProperty<Panorama>
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProperty(){
        return panorama;
    }

    /**
     * Gets the Panoramt as himself
     * @return Panorama
     */
    public Panorama getPanorama(){
        return panoramaProperty().get();
    }

    /**
     * Gets the Image as a ReadOnlyObjectProperty
     * @return ReadOnlyObjectProperty<Image>
     */
    public ReadOnlyObjectProperty<Image> imageProperty(){
        return image;
    }

    /**
     * Gets the Image as itself
     * @return Image
     */
    public Image getImage(){
        return imageProperty().get();
    }

    /**
     * Gets the labels as a non modifiable list
     * @return ObservableList<Node>
     */
    public ObservableList<Node> getLabels(){return labels;}

}

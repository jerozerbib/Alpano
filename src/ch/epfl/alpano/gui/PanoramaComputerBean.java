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
import static java.lang.Float.POSITIVE_INFINITY;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class PanoramaComputerBean {
    private final int HUE_DIV = 100_000, HUE_MUL = 360, SAT_DIV = 200_000, BR_MUL1 = 2;
    private final float BR_MUL2 = 0.7f, BR_ADD = 0.3f;
    private final ObjectProperty<Panorama> panorama;
    private final ObjectProperty<PanoramaUserParameters> pUserParameters;
    private final ObjectProperty<Image> image;
    private final ObservableList<Node> labels;
    private final List<Summit> summits;
    private final ContinuousElevationModel cDEM;

    /**
     * PanoramaComputerBean's constructor
     * 
     * @param summits
     *            the list of summits to set
     * @param cDEM
     *            the ContiuoursElevationModel to set
     * @throws IllegalArgumentException
     *             if the Summits or the cDEM is null
     */
    public PanoramaComputerBean(List<Summit> summits,
            ContinuousElevationModel cDEM) {
        this.cDEM = requireNonNull(cDEM);
        this.summits = requireNonNull(summits);

        panorama = new SimpleObjectProperty<>();
        labels = FXCollections.observableArrayList();
        image = new SimpleObjectProperty<>();
        pUserParameters = new SimpleObjectProperty<>();
        pUserParameters.addListener((b, o, n) -> synchronizeAllProps());
    }

    /**
     * Gets the PanoramaUserParameters as an ObjectProperty.
     * 
     * @return the PanoramaUserParameters Property
     */
    public ObjectProperty<PanoramaUserParameters> parametersProperty() {
        return pUserParameters;
    }

    /**
     * Gets the PanoramaUserParameters as themselves
     * 
     * @return the PanoramaUserParameters
     */
    public PanoramaUserParameters getParamaters() {
        return parametersProperty().get();
    }

    /**
     * Sets the ParoramaUserParameters with new ones
     * 
     * @param newParameters
     *            the new parameters to set
     */
    public void setParameters(PanoramaUserParameters newParameters) {
        parametersProperty().set(newParameters);
    }

    /**
     * Gets the Panorama as a ReadOnlyObjectProperty
     * 
     * @return the Panorama Property
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProperty() {
        return panorama;
    }

    /**
     * Gets the Panoramt as himself
     * 
     * @return the Panorama
     */
    public Panorama getPanorama() {
        return panoramaProperty().get();
    }

    /**
     * Gets the Image as a ReadOnlyObjectProperty
     * 
     * @return the Image Property
     */
    public ReadOnlyObjectProperty<Image> imageProperty() {
        return image;
    }

    /**
     * Gets the Image as itself
     * 
     * @return the Image
     */
    public Image getImage() {
        return imageProperty().get();
    }

    /**
     * Gets the labels as a non modifiable list
     * 
     * @return the Node Property
     */
    public ObservableList<Node> getLabels() {
        return labels;
    }

    /**
     * Synchronized all the properties if the pUserParameters changed
     */
    private void synchronizeAllProps() {
        panorama.set(new PanoramaComputer(cDEM).computePanorama(getParamaters().panoramaParameters()));
        List<Node> newNodeList = new Labelizer(cDEM, summits).labels(getParamaters().panoramaDisplayParameters());
        labels.setAll(newNodeList);

        ChannelPainter distance = getPanorama()::distanceAt;
        ChannelPainter slope = getPanorama()::slopeAt;
        ChannelPainter h = distance.div(HUE_DIV).cycling().mul(HUE_MUL);
        ChannelPainter s = distance.div(SAT_DIV).clamped().inverted();
        ChannelPainter br = slope.mul(BR_MUL1).div((float) Math.PI).inverted().mul(BR_MUL2).add(BR_ADD);
        ChannelPainter opacity = distance.map(d -> d == POSITIVE_INFINITY ? 0 : 1);
        ImagePainter painter = ImagePainter.hsb(h, s, br, opacity);

        image.set(renderPanorama(getPanorama(), painter));
    }

}
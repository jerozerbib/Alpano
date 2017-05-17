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

/**
 * @author : Jeremy Zerbib (257715)
 */


public class PanoramaComputerBean {

    private final ObjectProperty<Panorama> panorama;
    private final ObjectProperty<PanoramaUserParameters> pUserParameters;
    private final ObjectProperty<Image> image;
    private final ObjectProperty<Labelizer> labelizer;
    private final Labelizer lab;
    private final PanoramaComputer panoramaComputer;
    private final ObservableList<Node> labels;
    private final ObjectProperty<ObservableList<Node>> objectLabels;

    public PanoramaComputerBean(List<Summit> list, ContinuousElevationModel cDEM){
        lab = new Labelizer(cDEM, list);
        labels = FXCollections.observableArrayList();
        objectLabels = new SimpleObjectProperty<>(FXCollections.unmodifiableObservableList(labels));
        panoramaComputer = new PanoramaComputer(cDEM);
        panorama = new SimpleObjectProperty<>();
        image = new SimpleObjectProperty<>();
        labelizer = new SimpleObjectProperty<>();
        pUserParameters = new SimpleObjectProperty<>();
        pUserParameters.addListener((b, o, n) -> {
            Panorama p = panoramaComputer.computePanorama(n.panoramaParameters());
            ChannelPainter distance = p::distanceAt;
            ChannelPainter slope = p::slopeAt;

            //TODO : Magic Number
            ChannelPainter h = distance.div(100_000).cycling().mul(360);
            ChannelPainter s = distance.div(200_000).clamped().inverted();
            ChannelPainter br = slope.mul(2).div((float) Math.PI).inverted().mul(0.7f).add(0.3f);
            ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
            List<Node> newNodeList = lab.labels(n.panoramaParameters());
            ImagePainter l = ImagePainter.hsb(h, s, br, opacity);

            labels.setAll(newNodeList);
            image.set(PanoramaRenderer.renderPanorama(p, l));
        });
    }

    public ObjectProperty<PanoramaUserParameters> parametersProperty(){
        return pUserParameters;
    }

    public PanoramaUserParameters getParamaters(){
        return parametersProperty().get();
    }

    public void setParameters(PanoramaUserParameters newParameters){
        parametersProperty().set(newParameters);
    }

    public ReadOnlyObjectProperty<Panorama> panoramaProperty(){
        return panorama;
    }

    public Panorama getPanorama(){
        return panoramaProperty().get();
    }

    public ReadOnlyObjectProperty<Image> imageProperty(){
        return image;
    }

    public Image getImage(){
        return imageProperty().get();
    }

    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty(){
        return objectLabels;
    }

    public ObservableList<Node> getLabels(){
        return labelsProperty().get();
    }


}

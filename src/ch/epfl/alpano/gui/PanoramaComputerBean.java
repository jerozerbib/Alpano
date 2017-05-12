package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * @author : Jeremy Zerbib (257715)
 */


public class PanoramaComputerBean {

    private final ObjectProperty<Panorama> panorama;
    private final ObjectProperty<PanoramaUserParameters> pUserParameters;
    private final ObjectProperty<ImagePainter> imagePainter;
    private final ObjectProperty<Labelizer> labelizer;

    public PanoramaComputerBean(Panorama panorama,
                                PanoramaUserParameters pUserParameters,
                                ImagePainter imagePainter,
                                Labelizer labelizer){
        this.panorama = new SimpleObjectProperty<>(panorama);
        this.pUserParameters = new SimpleObjectProperty<>(pUserParameters);
        this.imagePainter = new SimpleObjectProperty<>(imagePainter);
        this.labelizer = new SimpleObjectProperty<>(labelizer);
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

    public ReadOnlyObjectProperty<ImagePainter> imageProperty(){
        return imagePainter;
    }

    public ImagePainter getImage(){
        return imageProperty().get();
    }

    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty(){
        ObservableList<Node> list = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(labelizer.get().labels(panoramaProperty().get().parameters())));
        return new SimpleObjectProperty<>(list);
    }

    //TODO : demander à l'assistant l'utilité de l'appel à setAll !
    public ObservableList<Node> getLabels(){
        return labelsProperty().get();
    }


}

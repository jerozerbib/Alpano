package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.List;

/**
 * @author : Jeremy Zerbib (257715)
 */

//TODO : Réponse est que changer constructeur !
public class PanoramaComputerBean {

    private ObjectProperty<Panorama> panorama;
    private ObjectProperty<PanoramaUserParameters> pUserParameters;
    private ObjectProperty<ImagePainter> imagePainter;
    private ObjectProperty<Labelizer> labelizer;

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

    public ReadOnlyObjectProperty<Labelizer> labelsProperty(){
        return labelizer;
    }

    //TODO : demander à l'assistant l'utilité de l'appel à setAll !
    public ObservableList<Node> getLabels(){
        List<Node> list = labelsProperty().get().labels(getPanorama().parameters());
        return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(list));
    }


}

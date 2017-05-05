package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.List;

/**
 * @author : Jeremy Zerbib (257715)
 */

//TODO : Demander si appel à ObjectProperty ou changement dans le constructeur ?
public class PanoramaComputerBean {

    private ObjectProperty<Panorama> panorama;
    private ObjectProperty<PanoramaUserParameters> pUserParameters;
    private ObjectProperty<ImagePainter> imagePainter;
    private ObjectProperty<Labelizer> labelizer;

    public PanoramaComputerBean(ObjectProperty<Panorama> panorama,
                                ObjectProperty<PanoramaUserParameters> pUserParameters,
                                ObjectProperty<ImagePainter> imagePainter,
                                ObjectProperty<Labelizer> labelizer){
        this.panorama = panorama;
        this.pUserParameters = pUserParameters;
        this.imagePainter = imagePainter;
        this.labelizer = labelizer;
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

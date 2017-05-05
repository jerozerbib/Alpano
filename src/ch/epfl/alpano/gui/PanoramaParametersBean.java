package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author : Jeremy Zerbib (257715)
 */

//TODO : Même question que dans la classe ComputerBean;

//TODO : Demander en quoi consiste la syncronisation.
public class PanoramaParametersBean {

    private final ObjectProperty<PanoramaUserParameters> panoramaUserParameters;

    public PanoramaParametersBean(ObjectProperty<PanoramaUserParameters> panoramaUserParameters){
        this.panoramaUserParameters = panoramaUserParameters;
    }

    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
        return panoramaUserParameters;
    }

    public ObjectProperty<Integer> observerLongitudeProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().observerLon());
    }

    public ObjectProperty<Integer> observerLatitudeProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().observerLat());
    }

    public ObjectProperty<Integer> observerElevationProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().observerEl());
    }

    public ObjectProperty<Integer> centerAzimuthProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().az());
    }

    public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().hfv());
    }

    public ObjectProperty<Integer> maxDistanceProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().maxD());
    }

    public ObjectProperty<Integer> widthProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().w());
    }

    public ObjectProperty<Integer> heightProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().h());
    }

    public ObjectProperty<Integer> superSamplingExponentProperty(){
        return new SimpleObjectProperty<>(panoramaUserParameters.get().exp());
    }
}

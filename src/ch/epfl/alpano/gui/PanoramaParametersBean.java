package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.EnumMap;
import java.util.Map;

import static ch.epfl.alpano.gui.UserParameter.*;
import static javafx.application.Platform.runLater;

/**
 * @author : Jeremy Zerbib (257715)
 */


public class PanoramaParametersBean {

    private final ObjectProperty<PanoramaUserParameters> syncronisedProps;
    private final Map<UserParameter, ObjectProperty<Integer>> mapPanoramaUserParameters = new EnumMap<>(UserParameter.class);

    public PanoramaParametersBean(PanoramaUserParameters panoramaUserParameters){
        this.syncronisedProps = new SimpleObjectProperty<>(panoramaUserParameters);
        for (Map.Entry<UserParameter, ObjectProperty<Integer>> e : mapPanoramaUserParameters.entrySet()){
            ObjectProperty<Integer> i = e.getValue();
            this.mapPanoramaUserParameters.put(e.getKey(), new SimpleObjectProperty<>(panoramaUserParameters.get(e.getKey())));
            i.addListener((b, o, n) -> runLater(this :: synchronisedParameters));
        }
    }

    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
        return syncronisedProps;
    }

    public ObjectProperty<Integer> observerLongitudeProperty(){
        return mapPanoramaUserParameters.get(OBSERVER_LONGITUDE);
    }

    public ObjectProperty<Integer> observerLatitudeProperty(){
        return mapPanoramaUserParameters.get(OBSERVER_LATITUDE);
    }

    public ObjectProperty<Integer> observerElevationProperty(){
        return mapPanoramaUserParameters.get(OBSERVER_ELEVATION);
    }

    public ObjectProperty<Integer> centerAzimuthProperty(){
        return mapPanoramaUserParameters.get(CENTER_AZIMUTH);
    }

    public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
        return mapPanoramaUserParameters.get(HORIZONTAL_FIELD_OF_VIEW);
    }

    public ObjectProperty<Integer> maxDistanceProperty(){
        return mapPanoramaUserParameters.get(MAX_DISTANCE);
    }

    public ObjectProperty<Integer> widthProperty(){
        return mapPanoramaUserParameters.get(WIDTH);
    }

    public ObjectProperty<Integer> heightProperty(){
        return mapPanoramaUserParameters.get(HEIGHT);
    }

    public ObjectProperty<Integer> superSamplingExponentProperty(){
        return mapPanoramaUserParameters.get(SUPER_SAMPLING_EXPONENT);
    }

    private PanoramaUserParameters synchronisedParameters(){
        Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
        for (Map.Entry<UserParameter, ObjectProperty<Integer>> e : mapPanoramaUserParameters.entrySet()){
            map.replace(e.getKey(), e.getValue().getValue());
        }

        return new PanoramaUserParameters(map);
    }
}

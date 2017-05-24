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

    /**
     * PanoramaParametersBean's Constructor
     * 
     * @param panoramaUserParameters
     *            the panoramaUserParameters to set
     */
    public PanoramaParametersBean(PanoramaUserParameters panoramaUserParameters) {
        this.syncronisedProps = new SimpleObjectProperty<>(panoramaUserParameters);


        for (UserParameter u : UserParameter.values()) {
            this.mapPanoramaUserParameters.put(u, new SimpleObjectProperty<>(panoramaUserParameters.get(u)));
            mapPanoramaUserParameters.get(u).addListener((b, o, n) -> runLater(this::synchronisedParameters));
        }

    }

    /**
     * Getter for the property containing the parameters
     * 
     * @return the property containing the parameters
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
        return syncronisedProps;
    }

    /**
     * Getter for the property containing the observerLongitude
     * 
     * @return the property containing the observerLongitude
     */
    public ObjectProperty<Integer> observerLongitudeProperty() {
        return mapPanoramaUserParameters.get(OBSERVER_LONGITUDE);
    }

    /**
     * Getter for the property containing the observerLatitude
     * 
     * @return the property containing the observerLatitude
     */
    public ObjectProperty<Integer> observerLatitudeProperty() {
        return mapPanoramaUserParameters.get(OBSERVER_LATITUDE);
    }

    /**
     * Getter for the property containing the observerElevation
     * 
     * @return the property containing the observerElevation
     */
    public ObjectProperty<Integer> observerElevationProperty() {
        return mapPanoramaUserParameters.get(OBSERVER_ELEVATION);
    }

    /**
     * Getter for the property containing the centerAzimuth
     * 
     * @return the property containing the centerAzimuth
     */
    public ObjectProperty<Integer> centerAzimuthProperty() {
        return mapPanoramaUserParameters.get(CENTER_AZIMUTH);
    }

    /**
     * Getter for the property containing the horizontalFieldOfView
     * 
     * @return the property containing the horizontalFieldOfView
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
        return mapPanoramaUserParameters.get(HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * Getter for the property containing the maxDistance
     * 
     * @return the property containing the maxDistance
     */
    public ObjectProperty<Integer> maxDistanceProperty() {
        return mapPanoramaUserParameters.get(MAX_DISTANCE);
    }

    /**
     * Getter for the property containing the widthProperty
     * 
     * @return the property containing the widthProperty
     */
    public ObjectProperty<Integer> widthProperty() {
        return mapPanoramaUserParameters.get(WIDTH);
    }

    /**
     * Getter for the property containing the heightProperty
     * 
     * @return the property containing the heightProperty
     */
    public ObjectProperty<Integer> heightProperty() {
        return mapPanoramaUserParameters.get(HEIGHT);
    }

    /**
     * Getter for the property containing the superSamplingExponent
     * 
     * @return the property containing the superSamplingExponent
     */
    public ObjectProperty<Integer> superSamplingExponentProperty() {
        return mapPanoramaUserParameters.get(SUPER_SAMPLING_EXPONENT);
    }

    /**
     * Makes sure the parameters of the individual property and the one in the
     * instance of PanoramaUserParameters are sychronized.
     * 
     * @return the sychonized PanoramaUserParameters
     */
    private PanoramaUserParameters synchronisedParameters() {
        Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
        for (Map.Entry<UserParameter, ObjectProperty<Integer>> e : mapPanoramaUserParameters.entrySet()) {
            map.replace(e.getKey(), e.getValue().getValue());
        }

        return new PanoramaUserParameters(map);
    }
}

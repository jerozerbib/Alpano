package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static javafx.application.Platform.runLater;

public final class PanoramaParametersBean {

    private final Map<UserParameter, ObjectProperty<Integer>> objectsProperty = new EnumMap<>(UserParameter.class);
    private final ObjectProperty<PanoramaUserParameters> parametersProperty;

    /**
     * Bean JavaFX contenant les paramètres utilisateurs du panorama
     * 
     * @param PanoramaUserParameters
     * @throws NullPointerException
     *             if the PanoramaUserParameters is null
     */
    public PanoramaParametersBean(PanoramaUserParameters parameters) {
        parametersProperty = new SimpleObjectProperty<>(requireNonNull(parameters));
        for (UserParameter userParam : UserParameter.values()) {
            ObjectProperty<Integer> property = new SimpleObjectProperty<>(parameters.get(userParam));
            objectsProperty.put(userParam, property);
            property.addListener((b, o, n) -> runLater(this::synchronizeParameters));
        }
    }

    /**
     * Retourne la propriété non modifiable associée aux PanoramaUserParameters
     * @return ReadOnlyObjectProperty<PanoramaUserParameters>
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
        return parametersProperty;
    }

    /**
     * Retourne la propriété associée à la Longitude
     * 
     * @return ObjectProperty<Integer> longitudeProperty
     */
    public ObjectProperty<Integer> observerLongitudeProperty() {
        return objectsProperty.get(UserParameter.OBSERVER_LONGITUDE);
    }

    /**
     * Retourne la propriété associée à la Latitude
     * 
     * @return ObjectProperty<Integer> latitudeProperty
     */
    public ObjectProperty<Integer> observerLatitudeProperty() {
        return objectsProperty.get(UserParameter.OBSERVER_LATITUDE);
    }

    /**
     * Retourne la propriété associée à l'élévation
     * 
     * @return ObjectProperty<Integer> elevationProperty
     */

    public ObjectProperty<Integer> observerElevationProperty() {
        return objectsProperty.get(UserParameter.OBSERVER_ELEVATION);
    }

    /**
     * Retourne la propriété associée à l'azimuth central
     * 
     * @return ObjectProperty<Integer> centerAzimuthProperty
     */
    public ObjectProperty<Integer> centerAzimuthProperty() {
        return objectsProperty.get(UserParameter.CENTER_AZIMUTH);
    }

    /**
     * Retourne la propriété associée à l'angle de vue horizontal
     * 
     * @return ObjectProperty<Integer> horizontalFieldOfViewProperty
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
        return objectsProperty.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * Retourne la propriété associée à la distance maximale de visibilité
     * 
     * @return ObjectProperty<Integer> maxDistanceProperty
     */
    public ObjectProperty<Integer> maxDistanceProperty() {
        return objectsProperty.get(UserParameter.MAX_DISTANCE);
    }

    /**
     * Retourne la propriété associée à la largeur du panorama
     * 
     * @return ObjectProperty<Integer> widthProperty
     */
    public ObjectProperty<Integer> widthProperty() {
        return objectsProperty.get(UserParameter.WIDTH);
    }

    /**
     * Retourne la propriété associée à la hauteur du panorama
     * 
     * @return ObjectProperty<Integer> heightProperty
     */
    public ObjectProperty<Integer> heightProperty() {
        return objectsProperty.get(UserParameter.HEIGHT);
    }

    /**
     * Retourne la propriété associée au superSampling
     * 
     * @return ObjectProperty<Integer> superSamplingExponentProperty
     */
    public ObjectProperty<Integer> superSamplingExponentProperty() {
        return objectsProperty.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }

    /**
     * Permet de synchroniser les paramètres utilisateurs du panorama en
     * validant les valeurs entrées dans les champs de l'interface graphique par
     * l'utilisateur
     */
    private void synchronizeParameters() {
        Map<UserParameter, Integer> paramMap = new EnumMap<>(UserParameter.class);

        for (UserParameter up : UserParameter.values()) {
            paramMap.put(up, objectsProperty.get(up).get());
        }
        PanoramaUserParameters pup = new PanoramaUserParameters(paramMap);

        parametersProperty.set(pup);

        for (UserParameter up : UserParameter.values()) {
            objectsProperty.get(up).set(pup.get(up));
        }

    }
}
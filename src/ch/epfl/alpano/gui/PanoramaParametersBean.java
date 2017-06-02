package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static javafx.application.Platform.runLater;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class PanoramaParametersBean {

    private final Map<UserParameter, ObjectProperty<Integer>> objectsProperty = new EnumMap<>(UserParameter.class);
    private final ObjectProperty<PanoramaUserParameters> parametersProperty;

    /**
     * PanoramaParametersBean's Constructor
     * 
     * @param parameters
     *            the PanoramaUserParameters to set
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
     * Returns the unmodifiable property corresponding to the
     * PanoramaUserParameters
     * 
     * @return the PanoramaUserParameters property
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
        return parametersProperty;
    }

    /**
     * Returns property corresponding to the longitude
     * 
     * @return the longitude property
     */
    public ObjectProperty<Integer> observerLongitudeProperty() {
        return objectsProperty.get(UserParameter.OBSERVER_LONGITUDE);
    }

    /**
     * Returns property corresponding to the latitude
     * 
     * @return the latitude property
     */
    public ObjectProperty<Integer> observerLatitudeProperty() {
        return objectsProperty.get(UserParameter.OBSERVER_LATITUDE);
    }

    /**
     * Returns property corresponding to the observer elevation
     * 
     * @return the elevation property
     */

    public ObjectProperty<Integer> observerElevationProperty() {
        return objectsProperty.get(UserParameter.OBSERVER_ELEVATION);
    }

    /**
     * Returns property corresponding to the center azimuth
     * 
     * @return the centerAzimuth property
     */
    public ObjectProperty<Integer> centerAzimuthProperty() {
        return objectsProperty.get(UserParameter.CENTER_AZIMUTH);
    }

    /**
     * Returns property corresponding to the horizontal field of view
     * 
     * @return the horizontalFieldOfView property
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
        return objectsProperty.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * Returns property corresponding to the max distance
     * 
     * @return the maxDistance property
     */
    public ObjectProperty<Integer> maxDistanceProperty() {
        return objectsProperty.get(UserParameter.MAX_DISTANCE);
    }

    /**
     * Returns property corresponding to the width
     * 
     * @return the width property
     */
    public ObjectProperty<Integer> widthProperty() {
        return objectsProperty.get(UserParameter.WIDTH);
    }

    /**
     * Returns property corresponding to the height
     * 
     * @return the height property
     */
    public ObjectProperty<Integer> heightProperty() {
        return objectsProperty.get(UserParameter.HEIGHT);
    }

    /**
     * Returns property corresponding to the super sampling exponent
     * 
     * @return the superSamplingExponent property
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
        Map<UserParameter, Integer> paramMap = new EnumMap<>(
                UserParameter.class);

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
package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static ch.epfl.alpano.gui.UserParameter.*;
import static ch.epfl.alpano.Preconditions.checkArgument;

import static java.lang.Math.pow;
import static java.lang.Math.toRadians;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class PanoramaUserParameters {

    private Map<UserParameter, Integer> map = new EnumMap<>(
            UserParameter.class);
    private final int MAX_HEIGHT = 4000;

    /**
     * PanoramaUserParameters's constructor, check that all parameters all valid
     * with sanitize and computes the right value for the height with the given
     * formula.
     * 
     * @param map
     *            the map to set
     * @throws IllegalArgumentException
     *             if the map is empty
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> map) {
        checkArgument(!map.isEmpty(), "La map est vide");
        Map<UserParameter, Integer> map1 = new EnumMap<>(UserParameter.class);

        for (Map.Entry<UserParameter, Integer> e : map.entrySet()) {
            map1.put(e.getKey(), e.getKey().sanitize(e.getValue()));
        }

        int h = map1.get(UserParameter.HEIGHT);
        int hfv = map1.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
        int w = map1.get(UserParameter.WIDTH);

        if (!(h <= (170 * (w - 1) / hfv) + 1)) {
            map1.replace(UserParameter.HEIGHT, h, MAX_HEIGHT);
        }

        this.map = Collections.unmodifiableMap(map1);
    }

    /**
     * PanoramaUserParameters constructor's
     * 
     * @param observerLon
     *            the observer longitude to set
     * @param observerLat
     *            the observer latitude to set
     * @param observerEl
     *            the observer elevation to set
     * @param az
     *            the central azimuth to set
     * @param hfv
     *            the horizontal field of view to set
     * @param maxD
     *            the max distance to set
     * @param w
     *            the width to set
     * @param h
     *            the height to set
     * @param exp
     *            the supersampling exponent to set
     */
    public PanoramaUserParameters(int observerLon, int observerLat,
            int observerEl, int az, int hfv, int maxD, int w, int h, int exp) {
        this(fillMap(observerLon, observerLat, observerEl, az, hfv, maxD, w, h,
                exp));
    }

    /**
     * Fills the map with the given arguments
     * 
     * @param list
     *            the arguments, a list a integer to fill in the map
     * @return the filled map
     */
    private static Map<UserParameter, Integer> fillMap(int... list) {
        Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
        for (int i = 0; i < UserParameter.values().length; i++) {
            map.put(UserParameter.values()[i], list[i]);
        }
        return map;
    }

    /**
     * Returns the value corresponding to the given enum value
     * 
     * @param userParameter
     *            the given value
     * @return the value that corresponds
     */
    public int get(UserParameter userParameter) {
        return map.get(userParameter);
    }

    /**
     * Returns the corresponding value to the observer's longitude
     * 
     * @return the observer's longitude
     */
    public int observerLon() {
        return map.get(OBSERVER_LONGITUDE);
    }

    /**
     * Returns the corresponding value to the observer's latitude
     * 
     * @return the observer's latitude
     */
    public int observerLat() {
        return map.get(OBSERVER_LATITUDE);
    }

    /**
     * Returns the corresponding value to the observer's elevation
     * 
     * @return the observer's elevation
     */
    public int observerEl() {
        return map.get(OBSERVER_ELEVATION);
    }

    /**
     * Returns the corresponding value to the central azimuth
     * 
     * @return the central azimuth
     */
    public int az() {
        return map.get(CENTER_AZIMUTH);
    }

    /**
     * Returns the corresponding value to the horizontal field of view
     * 
     * @return the horizontal field of view
     */
    public int hfv() {
        return map.get(HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * Returns the corresponding value to the max distance
     * 
     * @return the max distance
     */
    public int maxD() {
        return map.get(MAX_DISTANCE);
    }

    /**
     * Returns the corresponding value to the width
     * 
     * @return the width
     */
    public int w() {
        return map.get(WIDTH);
    }

    /**
     * Returns the corresponding value to the height
     * 
     * @return the height
     */
    public int h() {
        return map.get(HEIGHT);
    }

    /**
     * Returns the corresponding value to the supersampling exponent
     * 
     * @return the supersampling exponent
     */
    public int exp() {
        return map.get(SUPER_SAMPLING_EXPONENT);
    }

    /**
     * Returns the parameters of the panorama as they will be computed (taking
     * into account the supersampling exponent)
     * 
     * @return the parameters of the panorama as they will computed
     */
    public PanoramaParameters panoramaParameters() {
        double convertedLon = toRadians(observerLon() / 10_000.0);
        double convertedLat = toRadians(observerLat() / 10_000.0);
        double convertedAz = toRadians(az());
        double convertedHFV = toRadians(hfv());
        int wp = (int) (w() * pow(2, exp()));
        int hp = (int) (h() * pow(2, exp()));
        int convertedmaxD = maxD() * 1000;
        return new PanoramaParameters(new GeoPoint(convertedLon, convertedLat),
                observerEl(), convertedAz, convertedHFV, convertedmaxD, wp, hp);
    }

    /**
     * Returns the parameters of the panorama as they will be displayed
     * (regardless of the supersampling exponent)
     * 
     * @return the parameters of the panorama as they will be displayed
     */
    public PanoramaParameters panoramaDisplayParameters() {
        double convertedLon = toRadians(observerLon() / 10_000.0);
        double convertedLat = toRadians(observerLat() / 10_000.0);
        double convertedAz = toRadians(az());
        double convertedHFV = toRadians(hfv());
        int convertedmaxD = maxD() * 1000;
        return new PanoramaParameters(new GeoPoint(convertedLon, convertedLat),
                observerEl(), convertedAz, convertedHFV, convertedmaxD, w(),
                h());
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof PanoramaUserParameters) {
            return this.map.equals(((PanoramaUserParameters) that).map);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}
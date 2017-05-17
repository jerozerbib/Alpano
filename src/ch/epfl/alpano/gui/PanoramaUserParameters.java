package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static ch.epfl.alpano.gui.UserParameter.*;
import static java.lang.Math.pow;
import static java.lang.Math.toRadians;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class PanoramaUserParameters {

    private Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
    private final int MAX_HEIGHT = 14690; // From the instructions given by the teacher

    public PanoramaUserParameters(Map<UserParameter, Integer> map) {
        for (Map.Entry<UserParameter, Integer> e : map.entrySet()) {
            e.getKey().sanitize(e.getValue());
        }

        int h = map.get(UserParameter.HEIGHT);
        int hfv = map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
        int w = map.get(UserParameter.WIDTH);

        if (!(h <= (170 * (w - 1) / hfv) + 1)) {
            map.replace(UserParameter.HEIGHT, h, MAX_HEIGHT);
        }

        this.map = Collections.unmodifiableMap(new EnumMap<>(map));
    }

    public PanoramaUserParameters(int observerLon, int observerLat,
            int observerEl, int az, int hfv, int maxD, int w, int h, int exp) {
        this(fillMap(observerLon, observerLat, observerEl, az, hfv, maxD, w, h, exp));
    }

    private static Map<UserParameter, Integer> fillMap(int... list) {
        Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
        for (int i = 0; i < UserParameter.values().length; i++) {
            map.put(UserParameter.values()[i], list[i]);
        }
        return map;
    }

    public int get(UserParameter userParameter) {
        return this.map.get(userParameter);
    }

    public int observerLon() {
        return map.get(OBSERVER_LONGITUDE);
    }

    public int observerLat() {
        return map.get(OBSERVER_LATITUDE);
    }

    public int observerEl() {
        return map.get(OBSERVER_ELEVATION);
    }

    public int az() {
        return map.get(CENTER_AZIMUTH);
    }

    public int hfv() {
        return map.get(HORIZONTAL_FIELD_OF_VIEW);
    }

    public int maxD() {
        return map.get(MAX_DISTANCE);
    }

    public int w() {
        return map.get(WIDTH);
    }

    public int h() {
        return map.get(HEIGHT);
    }

    public int exp() {
        return map.get(SUPER_SAMPLING_EXPONENT);
    }

    public PanoramaParameters panoramaParameters() {
        double convertedLon = toRadians(observerLon() / 10_000.0);
        double convertedLat = toRadians(observerLat() / 10_000.0);
        double convertedAz = toRadians(az());
        double convertedHFV = toRadians(hfv());
        int wp = (int) (w() * pow(2, exp()));
        int hp = (int) (h() * pow(2, exp()));
        int convertedmaxD = maxD() * 1000;
        return new PanoramaParameters(new GeoPoint(convertedLon, convertedLat), observerEl(), convertedAz, convertedHFV, convertedmaxD, wp, hp);
    }

    public PanoramaParameters panoramaDisplayParameters() {
        double convertedLon = toRadians(observerLon() / 10_000);
        double convertedLat = toRadians(observerLat() / 10_000);
        double convertedAz = toRadians(az());
        double convertedHFV = toRadians(hfv());
        int convertedmaxD = maxD() * 1000;
        return new PanoramaParameters(new GeoPoint(convertedLon, convertedLat), observerEl(), convertedAz, convertedHFV, convertedmaxD, w(), h());
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof PanoramaUserParameters) {
            return this.map.equals(((PanoramaUserParameters) that).map);
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}

package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class PanoramaUserParameters {

    private Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
    private int observerLon, observerLat, observerEl, az, hfv, maxD, w, h, exp;


    public PanoramaUserParameters(Map<UserParameter, Integer> map){
        for (Map.Entry<UserParameter, Integer> e : map.entrySet()){
            e.getKey().sanitize(e.getValue());
        }
        this.map = Collections.unmodifiableMap(new EnumMap<>(map));
    }

    public PanoramaUserParameters(int observerLon, int observerLat,
                                  int observerEl, int az,
                                  int hfv, int maxD,
                                  int w, int h, int exp){
        this(new EnumMap<>(UserParameter.class));
    }

    public int get(UserParameter userParameter){
        return this.map.get(userParameter);
    }

    public int observerLon(){
        return observerLon;
    }

    public int observerLat() {
        return observerLat;
    }

    public int observerEl() {
        return observerEl;
    }

    public int az() {
        return az;
    }

    public int hfv() {
        return hfv;
    }

    public int maxD() {
        return maxD;
    }

    public int w() {
        return w;
    }

    public int h() {
        return h;
    }

    public int exp() {
        return exp;
    }
}

package ch.epfl.alpano.gui;

/**
 * @author : Jeremy Zerbib (257715)
 */
public enum  UserParameter {
    OBSERVER_LONGITUDE(60000, 120000),
    OBSERVER_LATITUDE(450000, 480000),
    OBSERVER_ELEVATION(300, 10_000),
    CENTER_AZIMUTH(0, 359),
    HORIZONTAL_FIELD_OF_VIEW(1, 360),
    MAX_DISTANCE(10, 600),
    WIDTH(30, 16_000),
    HEIGHT(100, 4_000),
    SUPER_SAMPLING_EXPONENT(0, 2);

    private int min;
    private int max;

    UserParameter(int min, int max){
        this.min = min;
        this.max = max;
    }

    public int sanitize(int v) {
        for (UserParameter u : values()) {
            if (v < u.min){
                return min;
            } else if(v > u.max){
                return max;
            }
        }
        return v;
    }

}

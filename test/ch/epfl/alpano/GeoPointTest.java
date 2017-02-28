package ch.epfl.alpano;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class GeoPointTest {

    @Test(expected = IllegalArgumentException.class)
    public void isLongitudeThrowingException(){
        new GeoPoint(-12, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isLatitudeThrowingException(){
        new GeoPoint(1, -12);
    }

    @Test
    public void longitudeIsOk(){
        GeoPoint test = new GeoPoint(1, 0);
        assertEquals(1, test.longitude(), 0);
    }

    @Test
    public void latitudeIsOk(){
        GeoPoint test = new GeoPoint(1, 0);
        assertEquals(0, test.latitude(), 0);
    }

    @Test
    public void distanceTo() throws Exception {
        GeoPoint c1 = new GeoPoint(Math.toRadians(8.01700), Math.toRadians(46.57306));
        GeoPoint c2 = new GeoPoint(Math.toRadians(8.017000), Math.toRadians(47.566044));
        assertEquals(110.4e3, c1.distanceTo(c2), 1e3);
    }


    @Test
    public void azimuthTo() throws Exception {
        GeoPoint c1 = new GeoPoint(Math.toRadians(8.01700), Math.toRadians(46.57306));
        GeoPoint c2 = new GeoPoint(Math.toRadians(8.017000), Math.toRadians(47.566044));
        assertEquals(0, c1.azimuthTo(c2), 1e-2);
    }

    @Test
    public void toStringWorks(){
        GeoPoint c1 = new GeoPoint(Math.toRadians(-7.6543), Math.toRadians(54.3210));
        assertEquals("(-7.6543,54.3210)", c1.toString());
    }


}
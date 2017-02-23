package ch.epfl.alpano;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class GeoPointTest {

    @Test(expected = IllegalArgumentException.class)
    public void isLongitudeThrowingException(){
        new GeoPoint(-Math.PI-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isLatitudeThrowingException(){
        new GeoPoint(1, -Math.PI-1);
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
        GeoPoint c1 = new GeoPoint(Math.toRadians(46.57306), Math.toRadians(8.01700));
        GeoPoint c2 = new GeoPoint(Math.toRadians(46.50660), Math.toRadians(6.53430));
        assertEquals(110.49e3, c1.distanceTo(c2), 5e-5);
    }


    @Test
    public void azimuthTo() throws Exception {
        GeoPoint c1 = new GeoPoint(Math.toRadians(6.631), Math.toRadians(46.521));
        GeoPoint c2 = new GeoPoint(Math.toRadians(37.623), Math.toRadians(55.753));
        assertEquals(5.3590334671706436609, c1.azimuthTo(c2), 1e-2);
    }

    @Test
    public void toStringWorks(){
        GeoPoint c1 = new GeoPoint(Math.toRadians(-7.6543), Math.toRadians(54.3210));
        assertEquals("(-7.6543,54.3210)", c1.toString());
    }


}
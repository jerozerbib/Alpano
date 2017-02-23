package ch.epfl.alpano;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class Interval2DTest {

    @Test(expected = NullPointerException.class)
    public void throwTheRightError(){
        new Interval2D(null, new Interval1D(0, 0));
        new Interval2D(new Interval1D(0, 0), null);
        new Interval2D(null, null);
    }

    @Test
    public void iX(){
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        assertEquals(new Interval1D(4, 5), test.iX());
        assertEquals(4, test.iX().includedFrom());
        assertEquals(5, test.iX().includedTo());
    }

    @Test
    public void iY() {
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        assertEquals(new Interval1D(4, 5), test.iX());
        assertEquals(0, test.iY().includedFrom());
        assertEquals(3, test.iY().includedTo());
    }

    @Test
    public void doesContain(){
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        assertTrue(test.contains(4, 2));
    }

    @Test
    public void doesNotContain(){
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        assertFalse(test.contains(1, 5));
        assertFalse(test.contains(1, 2));
        assertFalse(test.contains(4, 5));
    }

    @Test
    public void size(){

    }

    @Test
    public void sizeOfIntersectionWith(){

    }

    @Test
    public void boundingUnion(){

    }

    @Test
    public void isUnionableWith(){

    }

    @Test
    public void union(){

    }

    @Test
    public void equals(){

    }

    @Test
    public void toStringTest(){

    }

}
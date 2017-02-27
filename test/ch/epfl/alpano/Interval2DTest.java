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
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        assertEquals(6, test.size());
    }

    @Test
    public void sizeOfIntersectionWith(){
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        Interval2D test1 = new Interval2D(new Interval1D(2, 5), new Interval1D(0, 3));
        Interval2D test2 = new Interval2D(new Interval1D(0,6),new Interval1D(1,2));
        assertEquals(6, test.sizeOfIntersectionWith(test1));
        assertEquals(4, test.sizeOfIntersectionWith(test2));
    }

    @Test
    public void boundingUnion(){
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        Interval2D test1 = new Interval2D(new Interval1D(2, 5), new Interval1D(0, 3));
        Interval2D test2 = new Interval2D(new Interval1D(2, 6), new Interval1D(2, 4));
        assertEquals(test1, test.boundingUnion(test1));
        assertEquals(new Interval2D(new Interval1D(2, 6), new Interval1D(0, 4)), test.boundingUnion(test2));
    }

    @Test
    public void isUnionableWith(){
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        Interval2D test1 = new Interval2D(new Interval1D(2, 5), new Interval1D(0, 3));
        Interval2D test2 = new Interval2D(new Interval1D(7, 7), new Interval1D(1, 9));
        assertTrue(test.isUnionableWith(test1));
        assertFalse(test.isUnionableWith(test2));
    }

    @Test
    public void union(){
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        Interval2D test1 = new Interval2D(new Interval1D(2, 5), new Interval1D(0, 3));
        assertEquals(test1, test.union(test1));
    }

    @Test
    public void equals(){
        Interval2D test = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        Interval2D test1 = new Interval2D(new Interval1D(4, 5), new Interval1D(0, 3));
        Interval2D test2 = new Interval2D(new Interval1D(2, 5), new Interval1D(0, 3));
        assertTrue(test.equals(test1));
        assertFalse(test.equals(test2));
    }

    @Test
    public void toStringTest(){
        Interval2D test = new Interval2D(new Interval1D(12, 34), new Interval1D(56, 78));
        assertTrue(test.toString().equals("[12..34]x[56..78]"));
    }

}

package ch.epfl.alpano;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author : Jeremy Zerbib (257715)
 * @author Etienne Caquot
 */

public class Interval1DTest {

    @Test(expected = IllegalArgumentException.class)
    public void areBoundsOK(){new Interval1D(-1, -2);}

    @Test
    public void includedFrom(){
        Interval1D test = new Interval1D(0, 1);
        assertEquals(0, test.includedFrom());
    }

    @Test
    public void includedTo(){
        Interval1D test = new Interval1D(0, 1);
        assertEquals(1, test.includedTo());

    }

    @Test
    public void containsTest(){
        Interval1D test = new Interval1D(0, 2);
        assertTrue(test.contains(1));
    }

    @Test
    public void doesNotContainTest(){
        Interval1D test = new Interval1D(0, 2);
        assertFalse(test.contains(3));
    }

    @Test
    public void boundIsContained(){
        Interval1D test = new Interval1D(0, 2);
        assertTrue(test.contains(0));
        assertTrue(test.contains(2));
    }


    @Test
    public void sizeBaseCase(){
        Interval1D test = new Interval1D(0, 2);
        assertEquals(3, test.size());
    }

    @Test
    public void sizeTrivialInterval(){
        Interval1D test = new Interval1D(0, 0);
        assertEquals(1, test.size());
    }

    @Test
    public void sizeOfIntersectionWithOnSameInterval(){
        Interval1D test = new Interval1D(0, 2);
        assertEquals(3, test.sizeOfIntersectionWith(test));
    }

    @Test
    public void sizeOfIntersection(){
        Interval1D int1 = new Interval1D(0, 6);
        Interval1D int2 = new Interval1D(5, 8);
        Interval1D int3 = new Interval1D(0, 8);
        Interval1D int4 = new Interval1D(2, 5);
        assertEquals(2, int1.sizeOfIntersectionWith(int2));
        assertEquals(4, int3.sizeOfIntersectionWith(int4));
        assertEquals(2, int2.sizeOfIntersectionWith(int1));
        assertEquals(4, int4.sizeOfIntersectionWith(int3));
        assertEquals(1, int4.sizeOfIntersectionWith(int2));
        assertEquals(1, int2.sizeOfIntersectionWith(int4));
    }

    @Test
    public void sizerOfintersectionOfDisjointintervals(){
        Interval1D test = new Interval1D(0, 2);
        Interval1D test1 = new Interval1D(3, 5);
        assertEquals(0, test.sizeOfIntersectionWith(test1));
        assertEquals(0, test1.sizeOfIntersectionWith(test));
    }

    @Test
    public void boundingUnion(){
        Interval1D test = new Interval1D(0, 2);
        Interval1D test1 = new Interval1D(3, 5);
        assertEquals(0, test.boundingUnion(test1).includedFrom());
        assertEquals(5, test.boundingUnion(test1).includedTo());
    }

    @Test
    public void isNotUnionableWithDisjointIntervals(){
        Interval1D test = new Interval1D(0, 1);
        Interval1D test1 = new Interval1D(3, 5);
        assertFalse(test.isUnionableWith(test1));
    }

    @Test
    public void isUnionableWith(){
        Interval1D test = new Interval1D(0, 2);
        Interval1D test1 = new Interval1D(1, 5);
        assertTrue(test.isUnionableWith(test1));
    }

    @Test
    public void isUnionableWithHimself(){
        Interval1D test = new Interval1D(0, 2);
        assertTrue(test.isUnionableWith(test));
    }

    @Test
    public void union(){
        Interval1D test = new Interval1D(0, 2);
        Interval1D test1 = new Interval1D(3, 5);
        assertEquals(0, test.union(test1).includedFrom());
        assertEquals(5, test.union(test1).includedTo());
    }

    @Test
    public void equalsTest(){
        Interval1D test = new Interval1D(0, 2);
        assertTrue(test.equals(new Interval1D(0, 2)));
        assertFalse(test.equals(new Interval1D(0, 3)));
    }

    @Test
    public void toStringTest(){
        Interval1D test = new Interval1D(12, 34);
        assertTrue(test.toString().equals("[12..34]"));
    }

}
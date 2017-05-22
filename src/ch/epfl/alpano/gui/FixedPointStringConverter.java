package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public class FixedPointStringConverter extends StringConverter<Integer> {

    private final int fixedDecimal;

    /**
     * FixedPointStringConverter's constructor
     * 
     * @param fixedDecimal
     *            the number of decimal to set
     */
    public FixedPointStringConverter(int fixedDecimal) {
        this.fixedDecimal = fixedDecimal;
    }

    @Override
    public String toString(Integer object) {
        BigDecimal b = new BigDecimal(object);
        return b.movePointLeft(fixedDecimal).stripTrailingZeros()
                .toPlainString();
    }

    @Override
    public Integer fromString(String string) {
        BigDecimal b = new BigDecimal(string);
        return b.setScale(fixedDecimal, HALF_UP).movePointRight(fixedDecimal)
                .stripTrailingZeros().intValueExact();
    }
}

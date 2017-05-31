package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

import static ch.epfl.alpano.Preconditions.checkArgument;

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
        checkArgument(fixedDecimal >= 0, "FixedDecimal ne peut pas etre un nombre negatif");
        this.fixedDecimal = fixedDecimal;
    }

    @Override
    public String toString(Integer integer) {
        if (integer == null){
            return "";
        }
        BigDecimal b = new BigDecimal(integer);
        return b.movePointLeft(fixedDecimal).toPlainString();
    }

    @Override
    public Integer fromString(String string) {
        BigDecimal b = new BigDecimal(string);
        return b.setScale(fixedDecimal, HALF_UP).movePointRight(fixedDecimal)
                .stripTrailingZeros().intValueExact();
    }
}
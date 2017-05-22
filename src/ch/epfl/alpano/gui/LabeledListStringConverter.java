package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class LabeledListStringConverter extends StringConverter<Integer> {

    private final String[] list;

    /**
     * LabeledListStringConverter's constructor
     * 
     * @param list
     *            the list of string to set
     */
    public LabeledListStringConverter(String... list) {
        this.list = list;
    }

    @Override
    public String toString(Integer object) {
        return list[object];
    }

    @Override
    public Integer fromString(String string) {
        for (int i = 0; i < list.length; i++) {
            if (string.equals(list[i])) {
                return i;
            }
        }
        throw new IllegalArgumentException(
                "La chaine n'est pas dans le tableau initial");
    }
}

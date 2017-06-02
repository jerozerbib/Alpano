package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class LabeledListStringConverter extends StringConverter<Integer> {

    private final String[] list;

    /**
     * LabeledListStringConverter's constructor
     * 
     * @param list
     *            the list of string to set
     * @throws IllegalArgumentException
     *             if the list si empty
     */
    public LabeledListStringConverter(String... list) {
        checkArgument(list.length > 0, "La liste ne peut pas etre vide");
        this.list = list;
    }

    @Override
    public String toString(Integer integer) {
        checkArgument(integer >= 0 && integer < list.length, "L'index n'est pas dans les bornes.");
        return list[integer];
    }

    @Override
    public Integer fromString(String string) {
        for (int i = 0; i < list.length; i++) {
            if (string.equals(list[i])) {
                return i;
            }
        }
        throw new IllegalArgumentException("La chaine n'est pas dans le tableau initial");
    }
}
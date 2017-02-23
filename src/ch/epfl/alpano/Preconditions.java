package ch.epfl.alpano;

/**
 * @author Etienne Caquot
 * @author : Jeremy Zerbib (257715)
 */

public interface Preconditions {

    /**
     * Checks if a given argument is valid without throwing a message.
     * @param b
     * @throws IllegalArgumentException;
     */
    static void checkArgument(boolean b){
        if (!b){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if a given argument is valid and throws a message.
     * @param b
     * @param message
     * @throws IllegalArgumentException
     */
    static void checkArgument(boolean b, String message){
        if (!b){
            throw new IllegalArgumentException(message);
        }
    }
}

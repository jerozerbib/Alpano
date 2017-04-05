package ch.epfl.alpano;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public interface Preconditions {

    /**
     * Checks if a given argument is valid without throwing a message.
     * 
     * @param b
     *            the argument to check
     * @throws IllegalArgumentException
     *             if the argument is not valid
     */
    static void checkArgument(boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if a given argument is valid and throws a message.
     * 
     * @param b
     *            the argument to check
     * @param message
     *            the message to give if the argument is false
     * @throws IllegalArgumentException
     *             if the argument is not valid
     */
    static void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }
}

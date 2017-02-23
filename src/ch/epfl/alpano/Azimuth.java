package ch.epfl.alpano;

/**
 * @author Etienne Caquot
 * @author Jeremy Zerbib (257715)
 *
 */

public interface Azimuth {

    /**
     * Retourne vrai si son argument est un azimut canonique et faux sinon
     * @param azimuth
     *      l'azimuth à vérifier
     * @return vrai si l'azimuth est canonique et faux sinon
     */
    static boolean isCanonical(double azimuth){
        return azimuth >= 0 && azimuth < Math2.PI2;
    }

    /**
     * Retourne l'azimut canonique équivalent à celui passé en argument
     * @param azimuth
     *      l'azimuth qui n'est pas canonique
     * @return l'azimut canomique equivalent
     */
    static double canonicalize(double azimuth){
        return Math2.floorMod(azimuth, Math2.PI2);
    }

    /**
     *  transforme un azimut en angle mathématique, ou lève l'exception IllegalArgumentException
     *  si son argument n'est pas un azimut canonique,
     * @param azimuth
     *      l'azimuth que l'on veut convertir
     * @throws IllegalArgumentException si l'argument n'est pas canonique
     * @return l'angle mathématiqe correspondant
     */
    static double toMath(double azimuth){
        if(isCanonical(azimuth)){
            return canonicalize(Math2.PI2 - azimuth);
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * transforme un angle mathématique en azimut, ou lève l'exception IllegalArgumentException
     * si l'argument n'est pas canonique
     * @param angle
     *      l'angle que l'on veut convertir
     * @throws IllegalArgumentException si l'argument n'est pas canonique
     * @return azimut correspondant à l'angle mathématique
     */
    static double fromMath(double angle){
        if(isCanonical(angle)){
            return canonicalize(Math2.PI2 - angle);
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * retourne une chaîne correspondant à l'octant dans lequel se trouve l'azimut donné,
     * formée en combinant les chaînes n, e, s et w correspondant aux quatre points cardinaux
     * lève l'exception IllegalArgumentException si l'azimut donnée n'est pas canonique.
     * @param azimuth
     *      l'azimuth auquel on veut faire correspondre l'octant
     * @param n
     *      le nord
     * @param e
     *      l'est
     * @param s
     *      le sud
     * @param w
     *      l'ouest
     * @throws IllegalArgumentException si l'azimuth n'est pas canonique
     * @return l'octant correspondant à l'azimuth
     */
    static String toOctantString(double azimuth, String n, String e, String s, String w){
        String message = "";
        if(isCanonical(azimuth)){
            if((azimuth > ((15.0 / 16.0) * Math2.PI2)) || (azimuth <= ((1.0 / 16.0) * Math2.PI2))){
                message = n;
            } else if((azimuth > ((1.0 / 16.0) * Math2.PI2)) && (azimuth <= ((3.0 / 16.0) * Math2.PI2))){
                message = n+e;
            } else if((azimuth > ((3.0 / 16.0) * Math2.PI2)) && (azimuth <= ((5.0 / 16.0) * Math2.PI2))) {
                message = e;
            } else if((azimuth > ((5.0 / 16.0) * Math2.PI2)) && (azimuth <= ((7.0 / 16.0) * Math2.PI2))){
                message = s+e;
            } else if((azimuth > ((7.0 / 16.0) * Math2.PI2)) && (azimuth <= ((9.0 / 16.0) * Math2.PI2))){
                message = s;
            } else if((azimuth > ((9.0 / 16.0) * Math2.PI2)) && (azimuth <= ((11.0 / 16.0) * Math2.PI2))){
                message = s+w;
            } else if((azimuth > ((11.0 / 16.0) * Math2.PI2)) && (azimuth <= ((13.0 / 16.0) * Math2.PI2))){
                message = w;
            } else if((azimuth > ((13.0 / 16.0) * Math2.PI2)) && (azimuth <= ((15.0 / 16.0) * Math2.PI2))){
                message = n+w;
            }
        } else {
            throw new IllegalArgumentException();
        }
        return message;
    }
}
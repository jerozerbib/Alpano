package ch.epfl.alpano.gui;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */


public interface PredefinedPanoramas {

    int MAX_DISTANCE = 300, WIDTH = 2_500, HEIGHT = 800, EXP = 0;


    /**
     * Predefined UserParameters for NIESEN
     */
    PanoramaUserParameters NIESEN = new PanoramaUserParameters(76_500, 467_300, 600, 180, 110, MAX_DISTANCE, WIDTH, HEIGHT, EXP);

    /**
     * Predefined UserParameters for JURA
     */
    PanoramaUserParameters JURA = new PanoramaUserParameters(68_087, 470_085, 1_380, 162, 27, MAX_DISTANCE, WIDTH, HEIGHT, EXP);

    /**
     * Predefined UserParameters for RACINE
     */
    PanoramaUserParameters RACINE = new PanoramaUserParameters(68_200, 470_200, 1_500, 135, 45, MAX_DISTANCE, WIDTH, HEIGHT, EXP);

    /**
     * Predefined UserParameters for FINSTERAAHORN
     */
    PanoramaUserParameters FINSTERAAHORN = new PanoramaUserParameters(81_260, 465_374, 4_300, 205, 20, MAX_DISTANCE, WIDTH, HEIGHT, EXP);

    /**
     * Predefined UserParameters for SAUVABELIN
     */
    PanoramaUserParameters SAUVABELIN = new PanoramaUserParameters(66_385, 465_353, 700, 135, 100, MAX_DISTANCE, WIDTH, HEIGHT, EXP);

    /**
     * Predefined UserParameters for PELICAN
     */
    PanoramaUserParameters PELICAN = new PanoramaUserParameters(65_728, 465_132, 380, 135, 60, MAX_DISTANCE, WIDTH, HEIGHT, EXP);
}

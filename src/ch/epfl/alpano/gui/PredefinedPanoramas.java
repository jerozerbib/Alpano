package ch.epfl.alpano.gui;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public interface PredefinedPanoramas {
    int maxD = 300;
    int w = 2500;
    int h = 800;
    int exp = 0;


public interface PredefinedPanoramas {

    /**
     * Predefined UserParameters for NIESEN
     */
    static PanoramaUserParameters NIESEN = new PanoramaUserParameters(76_500,
            467_300, 600, 180, 110, 300, 2_500, 800, 0);

    /**
     * Predefined UserParameters for JURA
     */
    static PanoramaUserParameters JURA = new PanoramaUserParameters(68_087,
            470_085, 1_380, 162, 27, 300, 2_500, 800, 0);

    /**
     * Predefined UserParameters for RACINE
     */
    static PanoramaUserParameters RACINE = new PanoramaUserParameters(68_200,
            470_200, 1_500, 135, 45, 300, 2_500, 800, 0);

    /**
     * Predefined UserParameters for FINSTERAAHORN
     */
    static PanoramaUserParameters FINSTERAAHORN = new PanoramaUserParameters(
            81_260, 465_374, 4_300, 205, 20, 300, 2_500, 800, 0);

    /**
     * Predefined UserParameters for SAUVABELIN
     */
    static PanoramaUserParameters SAUVABELIN = new PanoramaUserParameters(
            66_385, 465_353, 700, 135, 100, 300, 2_500, 800, 0);

    /**
     * Predefined UserParameters for PELICAN
     */
    static PanoramaUserParameters PELICAN = new PanoramaUserParameters(65_728,
            465_132, 380, 135, 60, 300, 2_500, 800, 0);
}

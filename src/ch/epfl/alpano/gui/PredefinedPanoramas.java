package ch.epfl.alpano.gui;

/**
 * @author : Jeremy Zerbib (257715)
 */
public interface PredefinedPanoramas {
    int maxD = 300;
    int w = 2500;
    int h = 800;
    int exp = 0;


    //TODO : Magic NUm
    PanoramaUserParameters NIESEN = new PanoramaUserParameters(76_500, 467_300, 600, 180, 110, maxD, w, h, exp);
    PanoramaUserParameters JURA = new PanoramaUserParameters(68_087, 470_085, 1_380, 162, 27, maxD, w, h, exp);
    PanoramaUserParameters RACINE = new PanoramaUserParameters(68_200, 470_200, 1_500, 135, 45, maxD, w, h, exp);
    PanoramaUserParameters FINSTERAAHORN = new PanoramaUserParameters(81_260, 465_374, 4_300, 205, 20, maxD, w, h, exp);
    PanoramaUserParameters SAUVABELIN = new PanoramaUserParameters(66_385, 465_353, 700, 135, 100, maxD, w, h, exp);
    PanoramaUserParameters PELICAN = new PanoramaUserParameters(65_728, 465_132, 380, 135, 60, maxD, w, h, exp);
}

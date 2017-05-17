package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;

import static java.lang.Math.toRadians;

/**
 * @author : Jeremy Zerbib (257715)
 */
public class DrawColor {

    final static File HGT_FILE = new File("N46E007.hgt");

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(60);
    final static int MAX_DISTANCE = 100_000;

    final static PanoramaParameters PARAMS = new PanoramaParameters(
            new GeoPoint(ORIGIN_LON, ORIGIN_LAT), ELEVATION, CENTER_AZIMUTH,
            HORIZONTAL_FOV, MAX_DISTANCE, IMAGE_WIDTH, IMAGE_HEIGHT);

    public static void main(String[] args){
        try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE)) {
            ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
            Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);

            ChannelPainter distance = p::distanceAt;
            ChannelPainter slope = p::slopeAt;

            ChannelPainter h = distance.div(100_000).cycling().mul(360);
            ChannelPainter s = distance.div(200_000).clamped().inverted();
            ChannelPainter b = slope.mul(2).div((float) Math.PI).inverted().mul(0.7f).add(0.3f);
            ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

            ImagePainter l = ImagePainter.hsb(h, s, b, opacity);

            Image i = PanoramaRenderer.renderPanorama(p, l);

            ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", new File("niesen-profile-color.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

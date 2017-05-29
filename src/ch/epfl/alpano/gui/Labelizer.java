package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static java.lang.Double.compare;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */

public final class Labelizer {

    private final ContinuousElevationModel cDEM;
    private final int STEP = 64;
    private final int TOLERANCE = 200;
    private final int LINE_SIZE = 20;
    private final int SPACE = 2;
    private final int MARGIN = 20;
    private final int VERTICAL_LIMIT = 170;
    private final int ANGLE = -60;
    private List<Summit> summits = new ArrayList<>();

    /**
     * Labilizer's constructor
     *
     * @param cDEM
     *            the continuous elevation model to set
     * @param summits
     *            the list of summit to set
     */
    public Labelizer(ContinuousElevationModel cDEM, List<Summit> summits) {
        this.cDEM = requireNonNull(cDEM);
        this.summits = requireNonNull(summits);
    }

    /**
     * Returns the rounded y index for the position of the summit on a panorama.
     *
     * @param s
     *            the summit
     * @param p
     *            the parameters of the panorama
     * @return the rounded y index for the position of the summit on a panorama
     */

    private int yRounded(Summit s, PanoramaParameters p) {
        GeoPoint obsPos = p.observerPosition();
        double distanceToSummit = obsPos.distanceTo(s.position());
        double azimuthToSummit = obsPos.azimuthTo(s.position());
        ElevationProfile e = new ElevationProfile(cDEM, obsPos, azimuthToSummit, distanceToSummit);
        DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(e, p.observerElevation(), 0);
        double summitElevation = -f.applyAsDouble(distanceToSummit);
        double slope = atan2(summitElevation, distanceToSummit);
        return (int) round(p.yForAltitude(slope));
    }

    /**
     * Returns the rounded x index for the position of the summit on a panorama
     *
     * @param s
     *            the summits
     * @param p
     *            the parameters of the panorama
     * @return the rounded x index for the position of the summit on a panorama
     */
    private int xRounded(Summit s, PanoramaParameters p) {
        GeoPoint obsPos = p.observerPosition();
        double azimuthToSummit = obsPos.azimuthTo(s.position());
        return (int) round(p.xForAzimuth(azimuthToSummit));
    }

    /**
     * Label's the visible summits of the panorama.
     *
     * @param p
     *            the parameters of the panorama we want to label form the
     * @return the list of javafx nodes representing the labels to attach on the
     *         panorama.
     */
    public List<Node> labels(PanoramaParameters p) {
        List<Summit> visibleSummits = visibleSummits(p);
        List<Node> listTag = new ArrayList<>();
        if (visibleSummits.isEmpty()) {
            return listTag;
        }

        int yHighestRounded = p.width();

        BitSet bitSet = new BitSet(p.width());
        bitSet.set(0,MARGIN);
        bitSet.set(p.width() - MARGIN , p.width());

        for (Summit s : visibleSummits) {
            int roundedY = yRounded(s, p);

            if (roundedY >= VERTICAL_LIMIT) {

                if(roundedY < yHighestRounded){
                    yHighestRounded = roundedY;
                }

                int roundedX = xRounded(s, p);
                BitSet subBitSet = bitSet.get(roundedX, (roundedX + MARGIN));

                if (subBitSet.isEmpty()) {
                    bitSet.set(roundedX, roundedX + MARGIN, true);

                    Line line = new Line();
                    line.setStartY(yHighestRounded - LINE_SIZE);
                    line.setStartX(roundedX);
                    line.setEndY(roundedY);
                    line.setEndX(roundedX);

                    Text name = new Text(0, 0 , s.name() + " (" + s.elevation() + " m)");
                    name.getTransforms().addAll(new Rotate(ANGLE, 0, 0));
                    name.setTranslateX(roundedX);
                    name.setTranslateY(yHighestRounded - LINE_SIZE -SPACE);

                    listTag.add(name);
                    listTag.add(line);
                }
            }

        }
        return listTag;
    }

    /**
     * Returns the list of summits which are visible on the panorama
     *
     * @param list
     *            the list of all summits
     * @param p
     *            the parameters of the panorama
     * @return the list of visible summits
     */
    public List<Summit> visibleSummits(PanoramaParameters p) {
        ArrayList<Summit> visibleSummits = new ArrayList<>();
        for (Summit s : summits) {
            GeoPoint obsPos = p.observerPosition();
            double distanceToSummit = obsPos.distanceTo(s.position());
            double azimuthToSummit = obsPos.azimuthTo(s.position());
            double angularDistanceToSummit = angularDistance(azimuthToSummit, p.centerAzimuth());
            double maxD = p.maxDistance();

            if (distanceToSummit <= maxD && abs(angularDistanceToSummit) <= p.horizontalFieldOfView() / 2.0) {
                ElevationProfile e = new ElevationProfile(cDEM, obsPos, azimuthToSummit, distanceToSummit);
                DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(e, p.observerElevation(), 0);
                double summitElevation = -f.applyAsDouble(distanceToSummit);
                double slope = atan2(summitElevation, distanceToSummit);
                if (abs(slope) <= p.verticalFieldOfView() / 2.0) {
    
                    DoubleUnaryOperator f1 = PanoramaComputer.rayToGroundDistance(e, p.observerElevation(), summitElevation / distanceToSummit);
                    double rayToGround = firstIntervalContainingRoot(f1, 0, distanceToSummit, STEP);
                    if (rayToGround >= distanceToSummit - TOLERANCE) {
                        visibleSummits.add(s);
                    }
                }
            }
        }

        visibleSummits.sort((a, b) -> {
            int yARounded = yRounded(a, p);
            int yBRounded = yRounded(b, p);

            if (yARounded == yBRounded) {
                return compare(a.elevation(), b.elevation());
            }

            return compare(yARounded, yBRounded);
        });

        return visibleSummits;
    }

}
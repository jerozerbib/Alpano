package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static ch.epfl.alpano.PanoramaComputer.rayToGroundDistance;
import static java.lang.Integer.compare;
import static java.lang.Math.*;

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
    private final int ANGLE = 60;
    private List<Summit> summits = new ArrayList<>();

    /**
     * Labilizer's constructor
     *
     * @param cDEM    the continuous elevation model to set
     * @param summits the list of summit to set
     */
    public Labelizer(ContinuousElevationModel cDEM, List<Summit> summits) {
        this.cDEM = cDEM;
        this.summits = summits;
    }

    /**
     * Returns the rounded y index for the position of the summit on a panorama.
     *
     * @param s the summit
     * @param p the parameters of the panorama
     * @return the rounded y index for the position of the summit on a panorama
     */

    private int yRounded(Summit s, PanoramaParameters p) {
        GeoPoint obsPos = p.observerPosition();
        double distanceToSummit = obsPos.distanceTo(s.position());
        double azimuthToSummit = obsPos.azimuthTo(s.position());
        ElevationProfile e = new ElevationProfile(cDEM, obsPos, azimuthToSummit, distanceToSummit);
        DoubleUnaryOperator f = rayToGroundDistance(e, p.observerElevation(), 0);
        double positionOnSummit = f.applyAsDouble(distanceToSummit);
        double altitudeInRadians = toRadians(atan2(positionOnSummit, distanceToSummit));
        return (int) round(p.yForAltitude(altitudeInRadians));
    }

    /**
     * Returns the rounded x index for the position of the summit on a panorama
     *
     * @param s the summits
     * @param p the parameters of the panorama
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
     * @param p the parameters of the panorama we want to label form the
     * @return the list of javafx nodes representing the labels to attach on the
     * panorama.
     */
    public List<Node> labels(PanoramaParameters p) {
        List<Summit> visibleSummits = visibleSummits(summits, p);
        System.out.println(visibleSummits.size());
        List<Node> listTag = new ArrayList<>();
        if (visibleSummits.isEmpty()) {
            return listTag;
        }

        int yHighestRounded = yRounded(visibleSummits.get(0), p);

        BitSet bitSet = new BitSet(p.width());
        bitSet.set(MARGIN, p.width() - MARGIN - 1);
        BitSet subBitSet;

        for (Summit s : visibleSummits) {
            int roundedX = xRounded(s, p);

            int roundedY = yRounded(s, p);
            subBitSet = bitSet.get(roundedX, roundedX + MARGIN);

            if (roundedY > VERTICAL_LIMIT) {
                int counter = 0;
                for (int i = 0; i < subBitSet.size(); ++i) {
                    if (subBitSet.get(i)) {
                        counter++;
                    }
                }
                if (counter == 0) {
                    Line line = new Line();
                    line.setStartY(roundedY);
                    line.setEndY(yHighestRounded - roundedY + LINE_SIZE);
                    line.setStartX(roundedX);
                    Text name = new Text(roundedX, yHighestRounded - roundedY + LINE_SIZE, s.name() + " (" + s.elevation() + " m)");
                    name.getTransforms().addAll(new Rotate(ANGLE, 0, 0), new Translate(0, SPACE, 0));
                    bitSet.set(roundedX, roundedX + MARGIN, false);
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
     * @param list the list of all summits
     * @param p    the parameters of the panorama
     * @return the list of visible summits
     */
    private List<Summit> visibleSummits(List<Summit> list, PanoramaParameters p) {
        ArrayList<Summit> visibleSummits = new ArrayList<>();
        for (Summit s : list) {

            GeoPoint obsPos = p.observerPosition();
            double maxD = p.maxDistance();
            double distanceToSummit = obsPos.distanceTo(s.position());

            if (distanceToSummit > maxD){
                continue;
            }

            double azimuthToSummit = obsPos.azimuthTo(s.position());
            double angularDistanceToSummit = angularDistance(azimuthToSummit, p.centerAzimuth());


            if (angularDistanceToSummit >= p.horizontalFieldOfView() /2){
                continue;
            }

            ElevationProfile e = new ElevationProfile(cDEM, obsPos, azimuthToSummit, distanceToSummit);

            DoubleUnaryOperator f2 = rayToGroundDistance(e, p.observerElevation(), 0);
            double rayToSummit = -f2.applyAsDouble(distanceToSummit);
            double slope = atan2(s.elevation() - rayToSummit, distanceToSummit);

            if (abs(slope) >= p.verticalFieldOfView() / 2){
                continue;
            }

            DoubleUnaryOperator f = rayToGroundDistance(e, p.observerElevation(), slope);
            double rayToGround = firstIntervalContainingRoot(f, 0, distanceToSummit, STEP);

            if (s.name().equals("NIESEN")){
                System.out.println(xRounded(s, p));
                System.out.println(yRounded(s, p));
                System.out.println(rayToGround);
                System.out.println(distanceToSummit - TOLERANCE);
            }

            if (rayToGround >= distanceToSummit - TOLERANCE){
                visibleSummits.add(s);
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

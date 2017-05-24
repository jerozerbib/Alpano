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
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static ch.epfl.alpano.Math2.firstIntervalContainingRootNiesen;
import static ch.epfl.alpano.PanoramaComputer.rayToGroundDistance;
import static java.lang.Double.compare;
import static java.lang.Math.*;

/**
 * @author : Jeremy Zerbib (257715)
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

    public Labelizer(ContinuousElevationModel cDEM, List<Summit> summits) {
        this.cDEM = cDEM;
        this.summits = summits;
    }

    private static int yRounded(Summit s, PanoramaParameters p) {
        double elevation = s.elevation() - p.observerElevation();
        double distanceToSummit = p.observerPosition().distanceTo(s.position());
        double altitudeInRAdians = atan2(elevation, distanceToSummit);
        return (int) round(p.yForAltitude(altitudeInRAdians));
    }

    private static int xRounded(Summit s, PanoramaParameters p) {
        double elevation = s.elevation() - p.observerElevation();
        double distanceToSummit = p.observerPosition().distanceTo(s.position());
        double altitudeInRAdians = atan2(elevation, distanceToSummit);
        return (int) round(p.xForAzimuth(altitudeInRAdians));
    }

    public List<Node> labels(PanoramaParameters p) {
        List<Summit> visibleSummits = visibleSummits(summits, p);
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
                    listTag.add(line);
                    listTag.add(name);
                }
            }

        }
        return listTag;
    }


    private List<Summit> visibleSummits(List<Summit> list, PanoramaParameters p) {
        ArrayList<Summit> visibleSummits = new ArrayList<>();
        for (Summit s : list) {
            GeoPoint obsPos = p.observerPosition();
            double distanceToSummit = obsPos.distanceTo(s.position());
            double azimuthToSummit = obsPos.azimuthTo(s.position());
            double angularDistanceToSummit = angularDistance(azimuthToSummit, p.centerAzimuth());
            double maxD = p.maxDistance();

            if (distanceToSummit <= maxD && abs(angularDistanceToSummit) < p.horizontalFieldOfView()/2) {
                ElevationProfile e = new ElevationProfile(cDEM, obsPos, azimuthToSummit, maxD);
                DoubleUnaryOperator f2 = rayToGroundDistance(e, p.observerElevation(), 0);
                double rayToSummit = -f2.applyAsDouble(distanceToSummit);
                DoubleUnaryOperator f = rayToGroundDistance(e, p.observerElevation(), rayToSummit / distanceToSummit);
                double rayToGround = firstIntervalContainingRoot(f, 0, distanceToSummit, STEP);

                if (Objects.equals(s.name(), "NIESEN")){
                    firstIntervalContainingRootNiesen(f, 0, distanceToSummit + 100000, STEP);
//                    System.out.println(rayToSummit);
//                    System.out.println(rayToGround);
//                    System.out.println(distanceToSummit - TOLERANCE);
//                    System.out.println(s.elevation());
//                    System.out.println(s.position());
//                    System.out.println(distanceToSummit);
//                    System.out.println(toDegrees(p.verticalFieldOfView() / 2));
//                    System.out.println(Math.toDegrees(atan2(distanceToSummit, rayToSummit)));
//                    System.out.println(rayToGround / distanceToSummit);
                }
                if (abs(atan2(rayToSummit, distanceToSummit)) > p.verticalFieldOfView() / 2){
                    if (rayToGround >= distanceToSummit - TOLERANCE) {
                    	System.out.println("test1");
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

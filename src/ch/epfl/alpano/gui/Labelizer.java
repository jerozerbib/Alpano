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
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.compare;
import static java.lang.Math.round;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class Labelizer {

    private final ContinuousElevationModel cDEM;
    private List<Summit> summits = new ArrayList<>();
    private final int STEP = 64;
    private final int TOLERANCE = 200;
    private final int LINE_SIZE = 20;
    private final int SPACE = 2;
    private final int MARGIN = 20;
    private final int VERTICAL_LIMIT = 170;

    public Labelizer(ContinuousElevationModel cDEM, List<Summit> summits){
        this.cDEM = cDEM;
        this.summits = summits;
    }

    public List<Node> labels(PanoramaParameters p){
        List<Summit> visibleSummits = visibleSummits(summits, p);
        List<Node> listTag = new ArrayList<>();
        if (visibleSummits.isEmpty()){
            return listTag;
        }

        int yHighestRounded = yRounded(visibleSummits.get(0), p);

        BitSet bitSet = new BitSet(p.width());
        bitSet.set(MARGIN, p.width() - MARGIN - 1);
        BitSet subBitSet;

        for (Summit s : visibleSummits){
            int roundedX = xRounded(s, p);

            int roundedY = yRounded(s, p);
            subBitSet = bitSet.get(roundedX, roundedX + MARGIN);

            if (roundedY > VERTICAL_LIMIT) {
                int counter = 0;
                for (int i = 0; i < subBitSet.size(); ++i){
                    if (subBitSet.get(i)){
                        counter++;
                    }
                }
                if (counter == 0){
                    Line line = new Line();
                    line.setStartY(roundedY);
                    line.setEndY(yHighestRounded - roundedY + LINE_SIZE);
                    line.setStartX(roundedX);
                    Text name = new Text(roundedX, yHighestRounded - roundedY + LINE_SIZE + SPACE, s.name() + " (" + s.elevation() + " m)");
                    //TODO : Demander si Translate utile ou non ?
                    name.getTransforms().addAll(new Rotate(60, 0, 0), new Translate(0, 2, 0));
                    bitSet.set(roundedX, roundedX + 20, false);
                    listTag.add(line);
                    listTag.add(name);
                }
            }

        }
        return listTag;
    }

    private List<Summit> visibleSummits(List<Summit> list, PanoramaParameters p){
        ArrayList<Summit> visibleSummits = new ArrayList<>();
        for (Summit s : list){
            double distanceToSummit = p.observerPosition().distanceTo(s.position());
            double angularDistanceToSummit = angularDistance(p.centerAzimuth(), p.observerPosition().azimuthTo(s.position()));
            double azimuthToSummit = p.observerPosition().azimuthTo(s.position());
            double maxD = p.maxDistance();
            GeoPoint obsPos = p.observerPosition();
            if (distanceToSummit < maxD && angularDistanceToSummit < p.horizontalFieldOfView()){
                ElevationProfile e = new ElevationProfile(cDEM, obsPos, azimuthToSummit, distanceToSummit);
                DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(e, distanceToSummit, angularDistanceToSummit);
                double rayToGround = firstIntervalContainingRoot(f, 0, distanceToSummit + TOLERANCE, STEP);
                if (rayToGround != POSITIVE_INFINITY){
                    visibleSummits.add(s);
                }
            }
        }
        visibleSummits.sort((a, b) -> {
            int yARounded = yRounded(a, p);
            int yBRounded = yRounded(b, p);

            if (yARounded == yBRounded){
                return compare(a.elevation(), b.elevation());
            }

            return compare(yARounded, yBRounded);
        });
        return visibleSummits;
    }

    private int yRounded(Summit s, PanoramaParameters p){
        double elevation = s.elevation() - p.observerElevation();
        double distanceToSummit = p.observerPosition().distanceTo(s.position());
        double altitudeInRAdians = Math.atan2(elevation, distanceToSummit);
        return (int) round(p.yForAltitude(altitudeInRAdians));
    }

    private int xRounded(Summit s, PanoramaParameters p){
        double elevation = s.elevation() - p.observerElevation();
        double distanceToSummit = p.observerPosition().distanceTo(s.position());
        double altitudeInRAdians = Math.atan2(elevation, distanceToSummit);
        return (int) round(p.xForAzimuth(altitudeInRAdians));
    }

}

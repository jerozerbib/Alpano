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
import static java.lang.Double.POSITIVE_INFINITY;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class Labelizer {

    private ContinuousElevationModel cDEM;
    private List<Summit> summits = new ArrayList<>();
    private final int STEP = 64;
    private final int TOLERANCE = 200;
    private final int LINE_SIZE = 22;

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
        Summit highestSummit = visibleSummits.get(0);
        Line l = new Line();
        double elevation = highestSummit.elevation() - p.observerElevation();
        double distanceToSummit = p.observerPosition().distanceTo(highestSummit.position());
        double altitudeInRAdians = Math.atan2(elevation, distanceToSummit);
        l.setStartY(p.yForAltitude(altitudeInRAdians));
        l.setEndY(p.xForAzimuth(altitudeInRAdians) + LINE_SIZE);
        l.setStartX(p.xForAzimuth(altitudeInRAdians));
        for (Summit s : visibleSummits){
            double relElevation = s.elevation() - p.observerElevation();
            double relDistanceToSummit = p.observerPosition().distanceTo(s.position());
            double relAltitudeInRAdians = Math.atan2(relElevation, relDistanceToSummit);
            BitSet bitSet = new BitSet(p.width());
            bitSet.set(20, p.width() - 19);
            if (bitSet.get((int) p.xForAzimuth(relAltitudeInRAdians))){
                Text name = new Text(s.position().longitude(), s.position().latitude() + LINE_SIZE, s.name());
                name.getTransforms().addAll(new Rotate(30, 0, 0));
                bitSet.set((int) p.xForAzimuth(relAltitudeInRAdians), (int) p.xForAzimuth(relAltitudeInRAdians) + 20, false);
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
            double halfHFV = p.horizontalFieldOfView() / 2;
            double maxD = p.maxDistance();
            GeoPoint obsPos = p.observerPosition();
            if (distanceToSummit < maxD && angularDistanceToSummit < halfHFV){
                ElevationProfile e = new ElevationProfile(cDEM, obsPos, azimuthToSummit, distanceToSummit);
                DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(e, distanceToSummit, angularDistanceToSummit);
                double rayToGround = firstIntervalContainingRoot(f, 0, distanceToSummit, STEP);
                if (rayToGround != POSITIVE_INFINITY){
                    visibleSummits.add(s);
                }
            }
        }
        visibleSummits.sort((a, b) -> Integer.compare(a.elevation(), b.elevation()));
        return visibleSummits;
    }
}

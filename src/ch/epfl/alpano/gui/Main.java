package ch.epfl.alpano.gui;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Main {
   public static void main(String[] args) throws IOException {

        ContinuousElevationModel cDEM = new ContinuousElevationModel(
                new HgtDiscreteElevationModel(new File("N46E007.hgt")));
        List<Summit> listSummits = GazetteerParser.readSummitsFrom(new File("alps.txt"));

        Labelizer labelizer = new Labelizer(cDEM, listSummits);

        PanoramaParameters p = PredefinedPanoramas.JURA.panoramaParameters();

        List<Summit> list = labelizer.visibleSummits(p);

        System.out.println(list.get(25).name() + " " + list.get(25).elevation()  + " " + labelizer.yRounded(list.get(25),p));
        System.out.println(list.get(26).name() + " " + list.get(26).elevation()  + " " + labelizer.yRounded(list.get(26),p));
        System.out.println(list.get(27).name() + " " + list.get(27).elevation()  + " " + labelizer.yRounded(list.get(27),p));
        System.out.println(list.get(28).name() + " " + list.get(28).elevation()  + " " + labelizer.yRounded(list.get(28),p));
       

    }
}
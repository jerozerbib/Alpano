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

    public static void main(String[] args) throws IOException{
        ContinuousElevationModel cDEM = new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N46E007.hgt")));
        List<Summit> listSummits = GazetteerParser.readSummitsFrom(new File("alps.txt"));

        Labelizer labelizer = new Labelizer(cDEM,listSummits);

        PanoramaParameters p = PredefinedPanoramas.NIESEN.panoramaParameters();

        List<Node> list = labelizer.labels(p);

        for (int i = 0; i < 10; ++i){
            if (list.get(i) instanceof Text){
                System.out.println("Text[ text = ' " + list.get(i).toString() + "', x=" + ((Text) list.get(i)).getX() + ", y=" + ((Text) list.get(i)).getY() + "]");
            } else {
                System.out.println("Line[ startX = "
                        + ((Line) list.get(i)).getStartX()
                        + "startY = "
                        + ((Line) list.get(i)).getStartY()
                        + "endX = "
                        + ((Line) list.get(i)).getEndX()
                        + "endY ="
                        + ((Line) list.get(i)).getEndY() );
            }
        }

    }
}
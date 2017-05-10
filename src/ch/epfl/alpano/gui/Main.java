package ch.epfl.alpano.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

public final class Main {
    public static void main(String[] args) throws IOException{
        ContinuousElevationModel cDEM = new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N46E007.hgt")));
        List<Summit> listSummits = GazetteerParser.readSummitsFrom(new File("alps.txt"));
        
        Labelizer labelizer = new Labelizer(cDEM,listSummits);
        
        PanoramaParameters p = PredefinedPanoramas.NIESEN.panoramaDisplayParameters();
        
        List<Summit> list = labelizer.visibleSummits(listSummits, p);
        
        for(Summit s : list){
            System.out.println(s.name() + "(" + Labelizer.xRounded(s,p) + Labelizer.yRounded(s,p) + ")");
        }
    }
}

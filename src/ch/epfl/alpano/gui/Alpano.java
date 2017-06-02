package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import static ch.epfl.alpano.Azimuth.toOctantString;
import static ch.epfl.alpano.summit.GazetteerParser.readSummitsFrom;
import static java.awt.Desktop.getDesktop;
import static java.lang.Math.toDegrees;
import static javafx.beans.binding.Bindings.bindContent;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static javafx.scene.layout.GridPane.setHalignment;
import static javafx.scene.paint.Color.rgb;
import static javafx.scene.text.TextAlignment.CENTER;

/**
 * @author : Jeremy Zerbib (257715)
 * @author : Etienne Caquot (249949)
 */
public final class Alpano extends Application {

    private final static HgtDiscreteElevationModel HGT1 = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
    private final static HgtDiscreteElevationModel HGT2 = new HgtDiscreteElevationModel(new File("N45E007.hgt"));
    private final static HgtDiscreteElevationModel HGT3 = new HgtDiscreteElevationModel(new File("N45E008.hgt"));
    private final static HgtDiscreteElevationModel HGT4 = new HgtDiscreteElevationModel(new File("N45E009.hgt"));
    private final static HgtDiscreteElevationModel HGT5 = new HgtDiscreteElevationModel(new File("N46E006.hgt"));
    private final static HgtDiscreteElevationModel HGT6 = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
    private final static HgtDiscreteElevationModel HGT7 = new HgtDiscreteElevationModel(new File("N46E008.hgt"));
    private final static HgtDiscreteElevationModel HGT8 = new HgtDiscreteElevationModel(new File("N46E009.hgt"));
    private final static ContinuousElevationModel cDEM = createHGT();


    private final static StringConverter<Integer> FixedPointstringConverter = new FixedPointStringConverter(4);
    private final static StringConverter<Integer> IntegerstringConverter = new IntegerStringConverter();
    
    private final static Color WHITE_BG = rgb(255, 255, 255, 0.90);
    private final static int FONT_SIZE = 40;
    private final static BackgroundFill fill = new BackgroundFill(WHITE_BG, CornerRadii.EMPTY, Insets.EMPTY);
    
    private final static PanoramaUserParameters JURA = PredefinedPanoramas.JURA;


    /**
     * Creates the graphical interface for the Panorama.
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final List<Summit> summitList = readSummitsFrom(new File("alps.txt"));
        final PanoramaComputerBean computPano = new PanoramaComputerBean(summitList, cDEM);
        final Labelizer labels = new Labelizer(cDEM, summitList);
        final PanoramaParametersBean paramsPano = new PanoramaParametersBean(JURA);

        //We take care of the image part here.
        ImageView panoView = createPanoView(computPano, paramsPano);

        //Here is the labels part
        Pane labelsPane = createLabelsPane(labels, paramsPano, computPano);

        //Now we want to create the update panel
        StackPane updateNotice = createUpdateNotice(computPano, paramsPano);


        //What we do here is simply grouping the main Pane.
        StackPane panoGroup = new StackPane(panoView, labelsPane);
        ScrollPane panoScrollPane = new ScrollPane(panoGroup);
        StackPane panoPane = new StackPane(panoScrollPane, updateNotice);
        
        TextArea text = createTextArea();

        //Here we create two methods that deal with the mouse events.
        mouseClickOnPointEventHandler(panoView, computPano);
        mouseMoveEventHandler(panoView, computPano, text);

        //Here we arrange the panorama so that it can be in the right layout
        //Here we want to create the area that embeds the info on the Panorama
        GridPane paramsGrid = createParamsGrid(paramsPano,text);

        //Again we assemble the main part of the window in a scene.
        BorderPane root = new BorderPane();
        root.setCenter(panoPane);
        root.setBottom(paramsGrid);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static ContinuousElevationModel createHGT(){
        DiscreteElevationModel line1 = HGT1.union(HGT2).union(HGT3).union(HGT4);
        DiscreteElevationModel line2 = HGT5.union(HGT6).union(HGT7).union(HGT8);
        return new ContinuousElevationModel(line1.union(line2));
    }

    /**
     * We create the grid for the Panorama
     * @param paramsPano
     * @return GridPano
     */
    private GridPane createParamsGrid(PanoramaParametersBean paramsPano, TextArea text){
        Label lat = new Label("Latitude (°) : ");
        TextField latT = createField(paramsPano.observerLatitudeProperty(), 7, FixedPointstringConverter);
        
        Label lon = new Label("Longitude (°) : ");
        TextField lonT = createField(paramsPano.observerLongitudeProperty(), 4, FixedPointstringConverter);
        
        Label alt = new Label("Altitude (m) : ");
        TextField altT = createField(paramsPano.observerElevationProperty(), 4, IntegerstringConverter);
        
        Label az = new Label("Azimuth (°) : ");
        TextField azT = createField(paramsPano.centerAzimuthProperty(), 3, IntegerstringConverter);
        
        Label hfv = new Label("Angle de vue (°) : ");
        TextField hfvT = createField(paramsPano.horizontalFieldOfViewProperty(), 3, IntegerstringConverter);
        
        Label visibility = new Label("Visibilité (km) : ");
        TextField visibilityT = createField(paramsPano.maxDistanceProperty(), 3, IntegerstringConverter);
        
        Label w = new Label("Largeur (px) : ");
        TextField wT = createField(paramsPano.widthProperty(), 4, IntegerstringConverter);
        
        Label h = new Label("Hauteur (px) : ");
        TextField hT = createField(paramsPano.heightProperty(), 4, IntegerstringConverter);
        
        Label samplingIndex = new Label("Suréchantillonage : ");
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(0, 1, 2);
        choiceBox.getSelectionModel().selectFirst();
        StringConverter<Integer> stringConverter = new LabeledListStringConverter("non", "2x", "4x");
        choiceBox.setConverter(stringConverter);
        choiceBox.valueProperty().bindBidirectional(paramsPano.superSamplingExponentProperty());
        
        GridPane paramsGrid = new GridPane();
        paramsGrid.setAlignment(Pos.CENTER);
        paramsGrid.setHgap(10);
        paramsGrid.setVgap(3);
        paramsGrid.setPadding(new Insets(7, 5, 5, 5));
        paramsGrid.addRow(0, lat, latT, lon, lonT, alt, altT);
        paramsGrid.addRow(1, az, azT, hfv, hfvT, visibility, visibilityT);
        paramsGrid.addRow(2, w, wT, h, hT, samplingIndex, choiceBox);
        paramsGrid.add(text, 6, 0, 1, 3);
        setHalignment(lat, HPos.RIGHT);
        setHalignment(lon, HPos.RIGHT);
        setHalignment(alt, HPos.RIGHT);
        setHalignment(az, HPos.RIGHT);
        setHalignment(hfv, HPos.RIGHT);
        setHalignment(visibility, HPos.RIGHT);
        setHalignment(w, HPos.RIGHT);
        setHalignment(h, HPos.RIGHT);
        setHalignment(samplingIndex, HPos.RIGHT);
        return paramsGrid;
    }

    /**
     * Creates the text area that updates the parameters as the mous moves
     * @return TextArea
     */
    private TextArea createTextArea(){
        TextArea text = new TextArea();
        text.setPrefRowCount(2);
        text.setEditable(false);
        return text;
    }

    /**
     * Creates the pane that prompts the info that the panorama has changed parameters
     * @param computPano
     * @param paramsPano
     * @return StackPane
     */
    private StackPane createUpdateNotice(PanoramaComputerBean computPano, PanoramaParametersBean paramsPano){
        Text updateText = new Text("Les paramètres du panorama ont changé.\n Cliquez ici pour mettre le dessin à jour.");
        updateText.setFont(new Font(FONT_SIZE));
        updateText.setTextAlignment(CENTER);

        StackPane updateNotice = new StackPane(updateText);
        updateNotice.setBackground(new Background(fill));
        updateNotice.visibleProperty().bind(computPano.parametersProperty().isNotEqualTo(paramsPano.parametersProperty()));
        updateNotice.setOnMouseClicked(e -> computPano.setParameters(paramsPano.parametersProperty().get()));
        return updateNotice;
    }

    /**
     * Creates the labels Pane
     * @param labels
     * @param paramsPano
     * @param computPano
     * @return Pane
     */
    private Pane createLabelsPane(Labelizer labels, PanoramaParametersBean paramsPano, PanoramaComputerBean computPano){
        Pane labelsPane = new Pane();
        labelsPane.getChildren().addAll(labels.labels(paramsPano.parametersProperty().get().panoramaParameters()));
        labelsPane.prefWidthProperty().bind(paramsPano.widthProperty());
        labelsPane.prefHeightProperty().bind(paramsPano.heightProperty());
        bindContent(labelsPane.getChildren(), computPano.getLabels());
        labelsPane.setMouseTransparent(true);
        return labelsPane;
    }

    /**
     * Creates the image of the panorama
     * @param computPano
     * @param paramsPano
     * @return ImageView
     */
    private ImageView createPanoView(PanoramaComputerBean computPano, PanoramaParametersBean paramsPano){
        ImageView panoView = new ImageView(computPano.getImage());
        panoView.fitWidthProperty().bind(paramsPano.widthProperty());
        panoView.imageProperty().bind(computPano.imageProperty());
        panoView.preserveRatioProperty().setValue(true);
        panoView.smoothProperty().setValue(true);
        return panoView;
    }

    /**
     * Creates a TextField for the infos about the panorama
     * @param ObjectProperty<Integer> object
     * @param int col
     * @param StringConverter<Integer> stringConverter
     * @return TextField
     */
    private TextField createField(ObjectProperty<Integer> object, int col, StringConverter<Integer> stringConverter) {
        TextField textField = new TextField();
        TextFormatter<Integer> formater = new TextFormatter<>(stringConverter);
        textField.setTextFormatter(formater);
        textField.setAlignment(CENTER_RIGHT);
        textField.setPrefColumnCount(col);
        formater.valueProperty().bindBidirectional(object);
        return textField;
    }

    /**
     * If the mouse moves this method takes care of the change in the textArea field.
     * @param imageView
     * @param computPano
     * @param text
     */
    private void mouseMoveEventHandler(ImageView imageView,
            PanoramaComputerBean computPano, TextArea text) {
        imageView.setOnMouseMoved(e -> {
            int x;
            int y;

            x =(int) (Math.scalb(e.getX(), computPano.parametersProperty().get().exp()));
            y =(int) (Math.scalb(e.getY(), computPano.parametersProperty().get().exp()));

            Panorama p = computPano.getPanorama();
            double az = p.parameters().azimuthForX(x);
            double alt = p.elevationAt(x, y);
            double dist = p.distanceAt(x, y);
            double lon = p.longitudeAt(x, y);
            double lat = p.latitudeAt(x, y);
            double el = p.parameters().altitudeForY(y);

            String lonFormat = String.format((Locale) null, "%.4f", toDegrees(lon));
            String latFormat = String.format((Locale) null, "%.4f", toDegrees(lat));


            String s = "Position : " + latFormat + "°N " + lonFormat + "°E" +
                    "\n" +
                    "Distance : " + dist/1000.0 + " km" +
                    "\n" +
                    "Altitude : " + alt + " m" +
                    "\n" +
                    "Azimut : " + toDegrees(az) + " °" + toOctantString(az, "N", "E", "S", "W") + "\t" + "Elévation : " + toDegrees(el) + "°";
            text.setText(s);
        });
    }

    /**
     * This method takes care of the click event and redirects the user to his default web browser.
     * @param imageView
     * @param computPano
     */
    private void mouseClickOnPointEventHandler(ImageView imageView, PanoramaComputerBean computPano) {
        imageView.setOnMouseClicked(e -> {
            int x;
            int y;

            x =(int) (Math.scalb(e.getX(), computPano.parametersProperty().get().exp()));
            y =(int) (Math.scalb(e.getY(), computPano.parametersProperty().get().exp()));

            Panorama p = computPano.getPanorama();

            double lon = p.longitudeAt(x, y);
            double lat = p.latitudeAt(x, y);
            String lonFormat = String.format((Locale) null, "%.4f", toDegrees(lon));
            String latFormat = String.format((Locale) null, "%.4f", toDegrees(lat));

            String qy = "mlat=" + latFormat + "&mlon=" + lonFormat;
            String fg = "map=15/" + latFormat + "/" + lonFormat;

            try {

                URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
                getDesktop().browse(osmURI);
            } catch (URISyntaxException | IOException e2) {
                throw new Error(e2);
            }
        });
    }

}
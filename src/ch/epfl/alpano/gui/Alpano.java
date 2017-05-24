package ch.epfl.alpano.gui;


import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import static ch.epfl.alpano.Azimuth.toOctantString;
import static ch.epfl.alpano.summit.GazetteerParser.readSummitsFrom;
import static javafx.beans.binding.Bindings.bindContent;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static javafx.scene.paint.Color.rgb;
import static javafx.scene.text.TextAlignment.CENTER;

/**
 * @author : Jeremy Zerbib (257715)
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
    private final static Color WHITE_BG = rgb(255, 255, 255, 0.90);
    private final static DiscreteElevationModel line1 = HGT1.union(HGT2).union(HGT3).union(HGT4);
    private final static DiscreteElevationModel line2 = HGT5.union(HGT6).union(HGT7).union(HGT8);
    private final static ContinuousElevationModel cDEM1 = new ContinuousElevationModel(line1.union(line2));
    private final static PanoramaUserParameters JURA = PredefinedPanoramas.JURA;
    private final static int FONT_SIZE = 40;

    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        final List<Summit> summitList = readSummitsFrom(new File("alps.txt"));
        final PanoramaComputerBean computPano = new PanoramaComputerBean(summitList, cDEM1);
        final Labelizer labels = new Labelizer(cDEM1, summitList);
        final PanoramaParametersBean paramsPano = new PanoramaParametersBean(JURA);

        ImageView panoView = new ImageView();
        Pane labelsPane = new Pane();

        Text updateText = new Text();
        BackgroundFill fill = new BackgroundFill(WHITE_BG, CornerRadii.EMPTY, Insets.EMPTY);
        updateText.setFont(new Font(FONT_SIZE));
        updateText.setTextAlignment(CENTER);

        StackPane updateNotice = new StackPane(updateText);
        Background background = new Background(fill);
        updateNotice.setBackground(background);

        GridPane paramsGrid = new GridPane();

        panoView.fitWidthProperty().bind(paramsPano.widthProperty());
        panoView.imageProperty().bind(computPano.imageProperty());
        panoView.preserveRatioProperty().setValue(true);
        panoView.smoothProperty().setValue(true);

        TextArea textArea = mouseMoveEventHandler(panoView, computPano);
        mouseClickOnPointEventHandler(panoView, computPano);

        labelsPane.getChildren().addAll(labels.labels(computPano.getPanorama().parameters()));
        labelsPane.prefWidthProperty().bind(panoView.fitWidthProperty());
        labelsPane.prefHeightProperty().bind(panoView.fitHeightProperty());
        bindContent(labels.labels(computPano.getPanorama().parameters()), computPano.getLabels());
        labelsPane.setMouseTransparent(true);


        boolean isNotEqual = computPano.parametersProperty().isNotEqualTo(paramsPano.parametersProperty()).get();
        updateNotice.visibleProperty().setValue(isNotEqual);
        mouseClickOnRefresh(updateNotice, computPano, paramsPano.parametersProperty().get());

        Label lat = new Label("Latitude (°) : ");
        TextField latT = createField(paramsPano.observerLongitudeProperty(), 4, 7);
        Label lon = new Label("Longitude (°) : ");
        TextField lonT = createField(paramsPano.observerLatitudeProperty(), 4, 7);
        Label alt = new Label("Altitude (m) : ");
        TextField altT = createField(paramsPano.observerElevationProperty(), 0, 4);
        Label az = new Label("Azimuth (°) : ");
        TextField azT = createField(paramsPano.centerAzimuthProperty(), 0, 3);
        Label hfv = new Label("Angle de vue (°) : ");
        TextField hfvT = createField(paramsPano.horizontalFieldOfViewProperty(), 0, 3);
        Label visibility = new Label("Visibilité (km) : ");
        TextField visibilityT = createField(paramsPano.maxDistanceProperty(), 0, 3);
        Label w = new Label("Largeur (px) : ");
        TextField wT = createField(paramsPano.widthProperty(), 0, 4);
        Label h = new Label("Hauteur (px) : ");
        TextField hT = createField(paramsPano.heightProperty(), 0, 4);
        Label samplingIndex = new Label("Suréchantillonage : ");
        ChoiceBox choiceBox = new ChoiceBox();
        StringConverter<Integer> stringConverter = new LabeledListStringConverter("non", "2*", "4*");
        choiceBox.setConverter(stringConverter);


        paramsGrid.addRow(0, lat, latT, lon, lonT, alt, altT);
        paramsGrid.addRow(1, az, azT, hfv, hfvT, visibility, visibilityT);
        paramsGrid.addRow(2, w, wT, h, hT, samplingIndex, choiceBox);
        paramsGrid.add(textArea, 6, 0, 1, 3);


        StackPane panoGroup = new StackPane(panoView, labelsPane);
        ScrollPane panoScrollPane = new ScrollPane(panoGroup);
        StackPane panoPane = new StackPane(panoScrollPane, updateNotice);

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        root.setCenter(panoPane);
        root.setBottom(paramsGrid);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private TextField createField(ObjectProperty<Integer> object, int fixedPoint, int col) {
        TextField textField = new TextField();
        StringConverter<Integer> stringConverter = new FixedPointStringConverter(fixedPoint);
        TextFormatter<Integer> formater = new TextFormatter<>(stringConverter);
        textField.setTextFormatter(formater);
        textField.setAlignment(CENTER_RIGHT);
        textField.setPrefColumnCount(col);
        formater.valueProperty().bindBidirectional(object);
        return textField;
    }

    private TextArea mouseMoveEventHandler(ImageView imageView, PanoramaComputerBean computPano) {
        TextArea text = new TextArea();
        text.setPrefRowCount(2);
        imageView.setOnMouseMoved(e -> {
            int x = (int) e.getX();
            int y = (int) e.getY();
            Panorama p = computPano.getPanorama();
            double az = p.parameters().azimuthForX(x);
            double alt = p.parameters().altitudeForY(y);
            double dist = p.distanceAt(x, y);
            double lon = p.longitudeAt(x, y);
            double lat = p.latitudeAt(x, y);
            double el = p.elevationAt(x, y);
            text.setText("Position : " + Math.toDegrees(lon) + "°N " + lat + "°E");
            text.setText("Distance : " + dist + "km");
            text.setText("Altitude : " + alt + "m");
            text.setText("Azimut : " + az + toOctantString(az, "N", "E", "S", "W") + "  " + "Elévation : " + el);
        });
        return text;
    }

    private void mouseClickOnPointEventHandler(ImageView imageView, PanoramaComputerBean computPano) {
        imageView.setOnMouseClicked(e -> {
            int x = (int) e.getX();
            int y = (int) e.getY();
            Panorama p = computPano.getPanorama();
            double lon = p.longitudeAt(x, y);
            double lat = p.latitudeAt(x, y);
            String lonFormat = String.format((Locale) null, "%.2f", lon);
            String latFormat = String.format((Locale) null, "%.2f", lat);

            String qy = "?mlat=" + latFormat + "&mlon=" + lonFormat;  // "query" : partie après le ?
            String fg = "#map=15/" + latFormat + "/" + lonFormat;  // "fragment" : partie après le #
            try {
                URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
                java.awt.Desktop.getDesktop().browse(osmURI);
            } catch (URISyntaxException | IOException e2) {
                throw new Error(e2);
            }
        });
    }

    private void mouseClickOnRefresh(StackPane stackPane, PanoramaComputerBean computPano, PanoramaUserParameters newParams) {
        stackPane.setOnMouseClicked(e2 -> computPano.setParameters(newParams));
    }
}

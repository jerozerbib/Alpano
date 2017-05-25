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

import static javafx.beans.binding.Bindings.bindContent;
import static javafx.scene.paint.Color.rgb;
import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.geometry.Pos.CENTER_RIGHT;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class Alpano extends Application {

    private final static HgtDiscreteElevationModel HGT1 = new HgtDiscreteElevationModel(
            new File("N45E006.hgt"));
    private final static HgtDiscreteElevationModel HGT2 = new HgtDiscreteElevationModel(
            new File("N45E007.hgt"));
    private final static HgtDiscreteElevationModel HGT3 = new HgtDiscreteElevationModel(
            new File("N45E008.hgt"));
    private final static HgtDiscreteElevationModel HGT4 = new HgtDiscreteElevationModel(
            new File("N45E009.hgt"));
    private final static HgtDiscreteElevationModel HGT5 = new HgtDiscreteElevationModel(
            new File("N46E006.hgt"));
    private final static HgtDiscreteElevationModel HGT6 = new HgtDiscreteElevationModel(
            new File("N46E007.hgt"));
    private final static HgtDiscreteElevationModel HGT7 = new HgtDiscreteElevationModel(
            new File("N46E008.hgt"));
    private final static HgtDiscreteElevationModel HGT8 = new HgtDiscreteElevationModel(
            new File("N46E009.hgt"));
    private final static Color WHITE_BG = rgb(255, 255, 255, 0.90);
    private final static DiscreteElevationModel line1 = HGT1.union(HGT2)
            .union(HGT3).union(HGT4);
    private final static DiscreteElevationModel line2 = HGT5.union(HGT6)
            .union(HGT7).union(HGT8);
    private final static ContinuousElevationModel cDEM1 = new ContinuousElevationModel(
            line1.union(line2));
    private final static PanoramaUserParameters JURA = PredefinedPanoramas.JURA;
    private final static int FONT_SIZE = 40;
    private final static BackgroundFill fill = new BackgroundFill(WHITE_BG,
            CornerRadii.EMPTY, Insets.EMPTY);

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final List<Summit> summitList = readSummitsFrom(new File("alps.txt"));
        final PanoramaComputerBean computPano = new PanoramaComputerBean(
                summitList, cDEM1);
        final Labelizer labels = new Labelizer(cDEM1, summitList);
        final PanoramaParametersBean paramsPano = new PanoramaParametersBean(
                JURA);

        ImageView panoView = new ImageView(computPano.getImage());
        panoView.fitWidthProperty().bind(paramsPano.widthProperty());
        panoView.imageProperty().bind(computPano.imageProperty());
        panoView.preserveRatioProperty().setValue(true);
        panoView.smoothProperty().setValue(true);

        Pane labelsPane = new Pane();
        labelsPane.getChildren().addAll(labels.labels(
                paramsPano.parametersProperty().get().panoramaParameters()));
        labelsPane.prefWidthProperty().bind(paramsPano.widthProperty());
        labelsPane.prefHeightProperty().bind(paramsPano.heightProperty());
        bindContent(labelsPane.getChildren(), computPano.getLabels());
        labelsPane.setMouseTransparent(true);

        Text updateText = new Text(
                "Les paramètres du panorama ont changé.\nCliquez ici pour mettre le dessin à jour.");
        updateText.setFont(new Font(FONT_SIZE));
        updateText.setTextAlignment(CENTER);

        StackPane updateNotice = new StackPane(updateText);
        updateNotice.setBackground(new Background(fill));
        updateNotice.visibleProperty().setValue(computPano.parametersProperty()
                .isNotEqualTo(paramsPano.parametersProperty()).get());
        updateNotice.setOnMouseClicked(e -> computPano
                .setParameters(paramsPano.parametersProperty().get()));

        StackPane panoGroup = new StackPane(panoView, labelsPane);

        ScrollPane panoScrollPane = new ScrollPane(panoGroup);

        StackPane panoPane = new StackPane(panoScrollPane, updateNotice);

        StringConverter<Integer> FixedPointstringConverter = new FixedPointStringConverter(
                4);
        StringConverter<Integer> IntegerstringConverter = new IntegerStringConverter();

        Label lat = new Label("Latitude (°) : ");
        TextField latT = createField(paramsPano.observerLatitudeProperty(), 7,
                FixedPointstringConverter);
        Label lon = new Label("Longitude (°) : ");
        TextField lonT = createField(paramsPano.observerLongitudeProperty(), 4,
                FixedPointstringConverter);
        Label alt = new Label("Altitude (m) : ");
        TextField altT = createField(paramsPano.observerElevationProperty(), 4,
                IntegerstringConverter);
        Label az = new Label("Azimuth (°) : ");
        TextField azT = createField(paramsPano.centerAzimuthProperty(), 3,
                IntegerstringConverter);
        Label hfv = new Label("Angle de vue (°) : ");
        TextField hfvT = createField(paramsPano.horizontalFieldOfViewProperty(),
                3, IntegerstringConverter);
        Label visibility = new Label("Visibilité (km) : ");
        TextField visibilityT = createField(paramsPano.maxDistanceProperty(), 3,
                IntegerstringConverter);
        Label w = new Label("Largeur (px) : ");
        TextField wT = createField(paramsPano.widthProperty(), 4,
                IntegerstringConverter);
        Label h = new Label("Hauteur (px) : ");
        TextField hT = createField(paramsPano.heightProperty(), 4,
                IntegerstringConverter);
        Label samplingIndex = new Label("Suréchantillonage : ");

        ChoiceBox<Integer> choiceBox = new ChoiceBox<Integer>();
        choiceBox.getItems().addAll(0, 1, 2);
        choiceBox.getSelectionModel().selectFirst();
        StringConverter<Integer> stringConverter = new LabeledListStringConverter(
                "non", "2x", "4x");
        choiceBox.setConverter(stringConverter);

        TextArea text = new TextArea();
        text.setPrefRowCount(2);
        text.setEditable(false);

        mouseClickOnPointEventHandler(panoView, computPano);
        mouseMoveEventHandler(panoView, computPano, text);

        GridPane paramsGrid = new GridPane();
        paramsGrid.setAlignment(Pos.CENTER);
        paramsGrid.setHgap(10);
        paramsGrid.setVgap(5);
        paramsGrid.addRow(0, lat, latT, lon, lonT, alt, altT);
        paramsGrid.addRow(1, az, azT, hfv, hfvT, visibility, visibilityT);
        paramsGrid.addRow(2, w, wT, h, hT, samplingIndex, choiceBox);
        paramsGrid.add(text, 6, 0, 1, 3);

        GridPane.setHalignment(lat, HPos.RIGHT);
        GridPane.setHalignment(lon, HPos.RIGHT);
        GridPane.setHalignment(alt, HPos.RIGHT);
        GridPane.setHalignment(az, HPos.RIGHT);
        GridPane.setHalignment(hfv, HPos.RIGHT);
        GridPane.setHalignment(visibility, HPos.RIGHT);
        GridPane.setHalignment(w, HPos.RIGHT);
        GridPane.setHalignment(h, HPos.RIGHT);
        GridPane.setHalignment(samplingIndex, HPos.RIGHT);

        BorderPane root = new BorderPane();
        root.setCenter(panoPane);
        root.setBottom(paramsGrid);

        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField createField(ObjectProperty<Integer> object, int col,
            StringConverter<Integer> stringConverter) {
        TextField textField = new TextField();
        TextFormatter<Integer> formater = new TextFormatter<>(stringConverter);
        textField.setTextFormatter(formater);
        textField.setAlignment(CENTER_RIGHT);
        textField.setPrefColumnCount(col);
        formater.valueProperty().bindBidirectional(object);
        return textField;
    }

    private void mouseMoveEventHandler(ImageView imageView,
            PanoramaComputerBean computPano, TextArea text) {
        imageView.setOnMouseMoved(e -> {
            int x;
            int y;
            // TODO : Demander par rapport au suréchantillonage !
            if (computPano.getParamaters().exp() == 0) {
                x = (int) e.getX();
                y = (int) e.getY();
            } else {
                x = (int) e.getX() * computPano.getParamaters().exp();
                y = (int) e.getY() * computPano.getParamaters().exp();
            }
            Panorama p = computPano.getPanorama();
            double az = p.parameters().azimuthForX(x);
            double alt = p.parameters().altitudeForY(y);
            double dist = p.distanceAt(x, y);
            double lon = p.longitudeAt(x, y);
            double lat = p.latitudeAt(x, y);
            double el = p.elevationAt(x, y);
            // TODO : Demander par rapport à l'aligmement
            text.setText("Position : " + Math.toDegrees(lon) + "°N "
                    + Math.toDegrees(lat) + "°E");
            text.setText("Distance : " + dist + "km");
            text.setText("Altitude : " + alt + "m");
            text.setText(
                    "Azimut : " + az + toOctantString(az, "N", "E", "S", "W")
                            + "  " + "Elévation : " + el);
        });
    }

    private void mouseClickOnPointEventHandler(ImageView imageView,
            PanoramaComputerBean computPano) {
        imageView.setOnMouseClicked(e -> {
            int x;
            int y;
            // TODO : Demander par rapport au suréchantillonage !
            if (computPano.getParamaters().exp() == 0) {
                x = (int) e.getX();
                y = (int) e.getY();
            } else {
                x = (int) e.getX() * computPano.getParamaters().exp();
                y = (int) e.getY() * computPano.getParamaters().exp();
            }
            Panorama p = computPano.getPanorama();
            double lon = p.longitudeAt(x, y);
            double lat = p.latitudeAt(x, y);
            String lonFormat = String.format((Locale) null, "%.2f", lon);
            String latFormat = String.format((Locale) null, "%.2f", lat);

            String qy = "?mlat=" + latFormat + "&mlon=" + lonFormat; // "query"
                                                                     // : partie
                                                                     // après le
                                                                     // ?
            String fg = "#map=15/" + latFormat + "/" + lonFormat; // "fragment"
                                                                  // : partie
                                                                  // après le #
            try {
                URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy,
                        fg);
                java.awt.Desktop.getDesktop().browse(osmURI);
            } catch (URISyntaxException | IOException e2) {
                throw new Error(e2);
            }
        });
    }

}

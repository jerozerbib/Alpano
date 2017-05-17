package ch.epfl.alpano.gui;


import ch.epfl.alpano.Azimuth;
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

import static ch.epfl.alpano.summit.GazetteerParser.readSummitsFrom;
import static javafx.beans.binding.Bindings.bindContent;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static javafx.scene.text.TextAlignment.CENTER;

/**
 * @author : Jeremy Zerbib (257715)
 */
public final class Alpano extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Summit> summitList = readSummitsFrom(new File("alps.txt"));

        HgtDiscreteElevationModel HGT1 = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        HgtDiscreteElevationModel HGT2 = new HgtDiscreteElevationModel(new File("N45E007.hgt"));
        HgtDiscreteElevationModel HGT3 = new HgtDiscreteElevationModel(new File("N45E008.hgt"));
        HgtDiscreteElevationModel HGT4 = new HgtDiscreteElevationModel(new File("N45E009.hgt"));
        HgtDiscreteElevationModel HGT5 = new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        HgtDiscreteElevationModel HGT6 = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
        HgtDiscreteElevationModel HGT7 = new HgtDiscreteElevationModel(new File("N46E008.hgt"));
        HgtDiscreteElevationModel HGT8 = new HgtDiscreteElevationModel(new File("N46E009.hgt"));


        DiscreteElevationModel line1 = HGT1.union(HGT2).union(HGT3).union(HGT4);
        DiscreteElevationModel line2 = HGT5.union(HGT6).union(HGT7).union(HGT8);
        ContinuousElevationModel cDEM1 = new ContinuousElevationModel(line1.union(line2));


        PanoramaComputerBean computPano = new PanoramaComputerBean(summitList, cDEM1);
        Labelizer labels = new Labelizer(cDEM1, summitList);
        PanoramaParametersBean paramsPano = new PanoramaParametersBean(computPano.getParamaters());


        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        ImageView panoView = new ImageView();
        panoView.fitWidthProperty().bind(paramsPano.widthProperty());
        panoView.imageProperty().bind(computPano.imageProperty());
        //TODO : setValue ou setPreserveRAtio ?????????????
        panoView.preserveRatioProperty().setValue(true);
        panoView.smoothProperty().setValue(true);
        TextArea textArea = mouseMoveEventHandler(panoView, computPano);
        mouseClickOnPointEventHandler(panoView, computPano);

        //TODO : mettre la liste de Node retournée par labels de Labelizer après correction de la méthode et import fichiers HGT.
        Pane labelsPane = new Pane();
        labelsPane.getChildren().addAll(labels.labels(computPano.getPanorama().parameters()));
        labelsPane.prefWidthProperty().bind(panoView.fitWidthProperty());
        labelsPane.prefHeightProperty().bind(panoView.fitHeightProperty());
        bindContent(labels.labels(computPano.getPanorama().parameters()), computPano.getLabels());
        labelsPane.setMouseTransparent(true);


        Text updateText = new Text();
        StackPane panoGroup = new StackPane(panoView, labelsPane);
        ScrollPane panoScrollPane = new ScrollPane(panoGroup);
        StackPane updateNotice = new StackPane(updateText);
        //TODO : Magic Number !!
        Color color = Color.rgb(255, 255, 255, 0.90);
        BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        updateNotice.setBackground(background);
        updateText.setFont(new Font(40));
        updateText.setTextAlignment(CENTER);
        boolean isNotEqual = computPano.parametersProperty().isNotEqualTo(paramsPano.parametersProperty()).get();
        updateNotice.visibleProperty().setValue(isNotEqual);
        mouseClickOnRefresh(updateNotice, computPano, paramsPano.parametersProperty().get());

        StackPane panoPane = new StackPane(panoScrollPane, updateNotice);
        //TODO : mettre les Nodes relatives à chaque param
        GridPane paramsGrid = new GridPane();
        //TODO : Valeur par défaut ?!
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
            text.setText("Azimut : " + az + Azimuth.toOctantString(az, "N", "E", "S", "W"));
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
        //TODO : Recalcul Pano ?
        stackPane.setOnMouseClicked(e2 -> computPano.setParameters(newParams));
    }
}

package uk.gov.dvla.osg.printhub.viewer.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import uk.gov.dvla.osg.printhub.core.config.NetworkConfig;
import uk.gov.dvla.osg.printhub.core.services.VolumeService;
import uk.gov.dvla.osg.printhub.viewer.controllers.VolumeController;
import uk.gov.dvla.osg.printhub.viewer.controllers.VolumeDisplayController;
import uk.gov.dvla.osg.printhub.viewer.volume.VolumeDeserializer;
import uk.gov.dvla.osg.printhub.viewer.volume.VolumeQuery;

public class Main extends Application {

    private static final Logger LOG = LogManager.getLogger();
    private static String outputFileName;
    private static AppData appConfig = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/VolumeDisplay.fxml"));

        // NETWORK CONFIG
        NetworkConfig networkConfig = null;
        try {
            File networkConfigFile = appConfig.getNetworkConfigFile();
            networkConfig = new ObjectMapper(new YAMLFactory()).readValue(networkConfigFile, NetworkConfig.class);
        } catch (Exception e) {
            LOG.fatal("Unable to load network config file [{}] : {}", appConfig.getNetworkConfigFile().getAbsoluteFile(), e.getMessage());
            System.exit(1);
        }

        VolumeService volumeService = new VolumeService(networkConfig);
        VolumeDeserializer volumeDeserializer = new VolumeDeserializer();
        VolumeQuery getVolumeQuery = new VolumeQuery(volumeService, volumeDeserializer);
        VolumeController volumeController = VolumeController.getInstance(getVolumeQuery);
        VolumeDisplayController controller = new VolumeDisplayController(volumeController, outputFileName);

        loader.setController(controller);

        Parent root = loader.load();
        primaryStage.setTitle("Work in PrintHub");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Icons/main_icon.png")));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            LOG.error("Incorrect number of arguments supplied.");
            System.exit(1);
        }

        String appConfigFile = args[0];
        outputFileName = args[1];

        File configFile = new File(appConfigFile);

        if (!configFile.exists()) {
            LOG.error("Application Configuration File [{}] does not exist on the filepath.", configFile.getAbsolutePath());
            System.exit(1);
        }

        try {
            appConfig = new ObjectMapper(new YAMLFactory()).readValue(configFile, AppData.class);
        } catch (IOException ex) {
            LOG.error("Unable to load application configuration file {} : {}", appConfigFile, ex.getMessage());
            System.exit(1);
        }

        ImmutablePair<Boolean, String> appConfigValidity = appConfig.checkValidity();

        if (!appConfigValidity.getLeft()) {
            LOG.error("Missing Configuration File(s) : " + appConfigValidity.getRight());
            System.exit(1);
        }

        launch(args);
    }

}

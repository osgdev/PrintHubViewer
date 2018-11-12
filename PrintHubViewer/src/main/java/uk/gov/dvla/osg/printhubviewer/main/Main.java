package uk.gov.dvla.osg.printhubviewer.main;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.gov.dvla.osg.printhub.client.INetworkClient;
import uk.gov.dvla.osg.printhub.client.PrintHubClient;
import uk.gov.dvla.osg.printhub.clientservices.Service;
import uk.gov.dvla.osg.printhub.clientservices.VolumeService;
import uk.gov.dvla.osg.printhubviewer.controllers.VolumeDisplayController;

public class Main extends Application {

    static final Logger LOGGER = LogManager.getLogger();
	private static Service service;
	private static String jarCommand;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/VolumeDisplay.fxml"));
        Parent root = loader.load();
        // Get the Controller from the FXMLLoader
        VolumeDisplayController controller = loader.getController();
        // Set data in the controller
        controller.setClient(service);
        controller.setJarCmd(jarCommand);
        primaryStage.setTitle("Work Pending in PrintHub");
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("")));
        //primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {   
		if (args.length != 2) {
			LOGGER.error("Incorrect number of arguments supplied.");
			return;
		}
		
		String appConfigFile = args[0];
		jarCommand = args[1];
		
		File configFile = new File(appConfigFile);

		if (!configFile.exists()) {
			LOGGER.error("Application Configuration File [{}] does not exist on the filepath.",
					configFile.getAbsolutePath());
			return;
		}

		AppData appConfig = null;

		try {
			appConfig = new ObjectMapper(new YAMLFactory()).readValue(configFile, AppData.class);
		} catch (IOException ex) {
			LOGGER.error("Unable to load application configuration file {} : {}", appConfigFile, ex.getMessage());
			return;
		}

		if (!appConfig.checkValidity().getLeft()) {
			LOGGER.error("Missing Configuration File(s) : " + appConfig.checkValidity().getRight());
			return;
		}

		File networkConfigFile = appConfig.getNetworkConfigFile();
		INetworkClient client = new PrintHubClient(networkConfigFile);
		
        service = VolumeService.getInstance(client);
        launch(args);
    }
    
}

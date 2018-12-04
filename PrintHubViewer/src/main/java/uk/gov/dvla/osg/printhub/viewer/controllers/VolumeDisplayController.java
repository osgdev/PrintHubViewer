package uk.gov.dvla.osg.printhub.viewer.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uk.gov.dvla.osg.printhub.core.clientservices.Service;
import uk.gov.dvla.osg.printhub.core.clientservices.ServiceResult;
import uk.gov.dvla.osg.printhub.core.volumedata.Count;

public class VolumeDisplayController {
	
	static final Logger LOGGER = LogManager.getLogger();
	
	@FXML private TableView<Count> table_Volume= new TableView<Count>();;
	@FXML private TableColumn<Count, String> column_Application;
	@FXML private TableColumn<Count, String> column_Volume;
	@FXML private Button button_Retrieve;
	@FXML private Button button_Update;
	@FXML private Label label_Message;
	
	private Service volumeService;
	private String outputFileName;

	public VolumeDisplayController(Service volumeService, String outputFileName) {
        this.volumeService = volumeService;
        this.outputFileName = outputFileName;
	}
	
	@FXML
	private void initialize() {
		column_Application.setCellValueFactory(new PropertyValueFactory<Count, String>("jobType"));
		column_Volume.setCellValueFactory(new PropertyValueFactory<Count, String>("volume"));
		button_Update.fire();
	}

	@FXML
	private void updateData() {
		ServiceResult result = volumeService.execute(ServiceResult.success());
		if (result.isError()) {
			label_Message.setText(result.getMessage());
		} else {
			table_Volume.setItems(FXCollections.observableArrayList(result.getVolume().getAll()));
			label_Message.setText("");
		}
	}
	
	@FXML
	private void retrieveData() {

		if (table_Volume.getSelectionModel().isEmpty()) {
			label_Message.setText("No items selected");
			return;
		}
		
		String selectedJobType = table_Volume.getSelectionModel().getSelectedItem().getJobType();

        try {
            FileUtils.writeStringToFile(new File(outputFileName), selectedJobType, StandardCharsets.UTF_8, false);
            label_Message.setText(String.format("Data requested for %s", selectedJobType));
        } catch (IOException ex) {
            LOGGER.error("Unable to write data to output file {} : {}", outputFileName, ex.getMessage());
            label_Message.setText(ex.getMessage());
        }

	}

}

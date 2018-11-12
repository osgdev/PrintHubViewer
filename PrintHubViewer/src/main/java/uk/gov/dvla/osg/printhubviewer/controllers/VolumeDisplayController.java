package uk.gov.dvla.osg.printhubviewer.controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uk.gov.dvla.osg.printhub.clientservices.Service;
import uk.gov.dvla.osg.printhub.clientservices.ServiceResult;
import uk.gov.dvla.osg.printhub.volumedata.Count;

public class VolumeDisplayController {
	
	static final Logger LOGGER = LogManager.getLogger();
	
	@FXML private TableView<Count> table_Volume= new TableView<Count>();;
	@FXML private TableColumn<Count, String> column_Application;
	@FXML private TableColumn<Count, String> column_Volume;
	@FXML private Button button_Retrieve;
	@FXML private Button button_Update;
	@FXML private Label label_Message;
	
	private Service volumeService;
	private String jarCommand;

	public VolumeDisplayController(Service volumeService, String jarCommand) {
        this.volumeService = volumeService;
        this.jarCommand = jarCommand;
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
		jarCommand = StringUtils.replace(jarCommand, "<JOBTYPE>", "\""+selectedJobType+"\"");

		try {
		    System.out.println(jarCommand);
			Runtime.getRuntime().exec("cmd.exe /C " + jarCommand);
			label_Message.setText(String.format("Data requested for %s", selectedJobType));
		} catch (Exception ex) {
		    LOGGER.debug(ex.getMessage());
			label_Message.setText(ex.getMessage());
		}
	}

	public void setClient(Service volumeService) {
		this.volumeService = volumeService;
	}

	public void setJarCmd(String jarCommand) {
		this.jarCommand = jarCommand;
	}
}

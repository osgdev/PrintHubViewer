package uk.gov.dvla.osg.printhub.viewer.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.text.TextStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import uk.gov.dvla.osg.printhub.viewer.volume.Count;

public class VolumeDisplayController {
	

    private static final String TIMESTAMP_FORMAT = "ddMMyyyy_hhmmss";
    private static final String FILENAME_PREFIX = "PRINTHUB.REQUEST.";
    private static final String FILENAME_SUFFIX = ".DAT";

    static final Logger LOGGER = LogManager.getLogger();
	
	@FXML private TableView<Count> table_Volume = new TableView<Count>();;
	@FXML private TableColumn<Count, String> column_Application;
	@FXML private TableColumn<Count, String> column_Volume;
	@FXML private Button button_Retrieve;
	@FXML private Button button_Update;
	@FXML private Label label_Message;
	
	private VolumeController volumeController;
	private String hotFolder;

	public VolumeDisplayController(VolumeController volumeController, String hotFolder) {
        this.volumeController = volumeController;
        this.hotFolder = hotFolder;
	}
	
	@FXML
	private void initialize() {
		column_Application.setCellValueFactory(new PropertyValueFactory<Count, String>("jobType"));
		column_Volume.setCellValueFactory(new PropertyValueFactory<Count, String>("volume"));
		table_Volume.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_Volume.setPlaceholder(new Label("No data available in PrintHub!"));
		button_Update.fire();
	}

	@FXML
	private void updateData() {
	    ControllerResult result = volumeController.execute();
	    
		if (result.isError()) {
			label_Message.setText(result.getMessage());
		} else {
			table_Volume.setItems(FXCollections.observableArrayList(result.getVolume()));
			label_Message.setText("");
		}
	}
	
	@FXML
	private void retrieveData() {

		if (table_Volume.getSelectionModel().isEmpty()) {
			label_Message.setText("No items selected");
			return;
		}
		
		TableViewSelectionModel<Count> selectionModel = table_Volume.getSelectionModel();
		ObservableList<Count> selectedItems = selectionModel.getSelectedItems();
		TextStringBuilder selectedJobTypes = new TextStringBuilder();
		
		for (Count item : selectedItems) {
		    selectedJobTypes.appendln(item.getJobType());
		}
		
		String timestamp = DateFormatUtils.format(new Date(), TIMESTAMP_FORMAT);
		
		StringBuilder outputFilename = new StringBuilder();
		outputFilename.append(FILENAME_PREFIX);
		outputFilename.append(timestamp);
		outputFilename.append(FILENAME_SUFFIX);
		
        try {
            FileUtils.writeStringToFile(new File(hotFolder, outputFilename.toString()), selectedJobTypes.toString(), StandardCharsets.UTF_8, false);
            if (selectedItems.size() == 1) {
                label_Message.setText(String.format("Data requested for %s", selectedJobTypes));
            } else {
                label_Message.setText("Data requested for multiple items");
            }
        } catch (IOException ex) {
            LOGGER.error("Unable to write data to hot folder {} : {}", hotFolder, ex.getMessage());
            label_Message.setText(ex.getMessage());
        }

        selectionModel.clearSelection();
	}

    public void logout() {
        // TODO Auto-generated method stub
        
    }

}

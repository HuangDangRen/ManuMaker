/**
 *  ManuMaker V1.0 is an open-source tool for creating step-by-step manuals.   
 *  Copyright (C) 2016 Daniel M. Lachmann
 *  
 *  To contact me via email, write to: ManuMaker.Dev@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gallery.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import gallery.abstractclasses.ControllerAbstr;
import gallery.model.GalleryProjectSettings;
import gallery.utils.ToolTipMaker;
import gallery.utils.TypeUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TableImportFormController extends ControllerAbstr {
	@FXML
	TitledPane TP_TableImportForm;
	
	@FXML
	TextField TF_TableCaption;
	
	@FXML
	TextField TF_TableIdentifier;
	
	@FXML
	Label LBL_ImportPath;
	
	@FXML
	Button BT_ChooseFile;
	
	@FXML
	Button BT_OK;
	
	@FXML
	Button BT_Cancel;
	
	private GalleryBrowserController owningController = null;
	private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	private Stage dialogStage;
	private String tableData = null;
	private String tableCaption = null;
	private String tableIdentifier = null;
	private String tableTMP = null; 
	private Node oldChild = null;
	public final String htmlPlaceHolder = "plhtmlblockpl";
	
	private boolean mandatoryFieldsFilled(){
		boolean ret = true;
		// this.TF_ProjectTitle.getText().matches("[^\\s-]")
		if(this.tableCaption != null && this.tableData != null && this.tableIdentifier != null){
			ret = ret && (!this.tableData.isEmpty());
			ret = ret && (!this.tableCaption.isEmpty());
			ret = ret && (!this.tableIdentifier.isEmpty());
			ret = ret && (!this.tableTMP.contains(this.htmlPlaceHolder));
		} else {
			ret = false;
		}

		return ret;
	}
	
	/** 
	 * Sets the reference to the owning controller
	 * @param nOC
	 */
	public void setOwningController(GalleryBrowserController nOC) {
		try{
			this.owningController = nOC;
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		this.invar("setOwningController() @ end of method");
 	}
	
	
	/**
	 * Make the TableImportFormController remember the GUI component it replaced
	 * @param nOC
	 */
	public void setOldChild(Node nOC) {
		this.oldChild = nOC;
	}
	
	/**
	 * Get the GUI component which the TableImportFormController temporarily replaced
	 * @return
	 */
	public Node getOldChild() {
		return this.oldChild;
	}
	
	
	/**
	 * Does nothing here...
	 */
	@Override
	public void updateUI() {
		
	}

	@Override
	public void initCTRL() {
		this.LBL_ImportPath.setText("No file selected...");
		
		this.BT_ChooseFile.setTooltip(ToolTipMaker.simpleToolTip("Select a table in HTML format from the filesystem. \n"
				+ "To save a table as HTML in Open/Libre Office, select \"Save As -> .html\". "
				+ "The table can then be directly imported. "
				+ "Note that the styling may be slightly different when displayed in ManuMaker's editor / viewer.\n\n"
				+ "Large tables may make ManuMaker's \"Export to printable html\" function unusable."));
		
		this.TF_TableCaption.setTooltip(ToolTipMaker.simpleToolTip("Give the table a short, concise caption to be displayed under it."));
		this.TF_TableIdentifier.setTooltip(ToolTipMaker.simpleToolTip(	"Give this table a unique identifier."));
		
		this.BT_OK.setTooltip(ToolTipMaker.simpleToolTip("Copy the filled-out template code to the System Clipboard, \n"
				+ "and paste it in the desired location in the \"Refine\" Tab's editor. \n\n"
				+ "The tables contents can be carefully edited in the editor. \n"
				+ "Depending on how it was exported, the table's borders etc. may not be protected\n"
				+ "against editing, and can be deleted in the Editor. \n"));
		
				
		
		try {
			this.tableTMP = TypeUtils.makeListToString(Files.readAllLines(Paths.get("resources/html_templates/TableTemplate.html"),Charset.forName("UTF8")));
		} catch (IOException e) {
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("ERROR: Template could not be loaded!");
	    	alert.setHeaderText("Table template not loaded!");
	    	alert.setContentText("Please ensure the template is available in the application directory.");
	    	alert.showAndWait();
			e.printStackTrace();
		}		
		
		
	}
	
	@FXML
	public void handleCaptionText() {
		if(this.TF_TableCaption.getText() != null) {
			String text = this.TF_TableCaption.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.tableCaption = text;
			} else {
				this.tableCaption = text + "\n";
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleIdentifierText() {
		if(this.TF_TableIdentifier.getText() != null) {
			String text = this.TF_TableIdentifier.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.tableIdentifier = text;
			} else {
				this.tableIdentifier = text + "\n";
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleChooseFile() {
		FileChooser chooser = new FileChooser();
		File file = null;
		
		chooser.setTitle("Choose a HTML Table:");

		file = new File(System.getProperty("user.home"));
		    
		chooser.setInitialDirectory(file);
		List<String> extensions = new ArrayList<String>();
		extensions.add(".html");
		extensions.add(".htm");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".html", extensions);
		chooser.setSelectedExtensionFilter(extFilter);
		File selectedFile = chooser.showOpenDialog(this.dialogStage);
		
		if(selectedFile != null && selectedFile.exists()) {
			try {
				tableData = FileUtils.readFileToString(selectedFile, "UTF8");
				this.owningController.markerInsert = true;
				this.LBL_ImportPath.setText(selectedFile.getAbsolutePath());
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR: table could not be loaded!");
				alert.setHeaderText("ERROR!");
				alert.setContentText("The selected file could not be loaded.");
				alert.showAndWait();
				this.LBL_ImportPath.setText("No file selected...");
				e.printStackTrace();
			}
		} else {
			this.owningController.markerInsert = false;
		}
	}
	
	@FXML
	public void handleCancel() {
		this.TF_TableCaption.setText(null);
		this.TF_TableIdentifier.setText(null);
		this.LBL_ImportPath.setText("Please select file path...");
		this.tableCaption = null;
		this.tableData = null;
		this.tableIdentifier = null;
		this.owningController.markerInsert = false;
		this.LBL_ImportPath.setText("No file selected...");
		this.owningController.tableImportFormControllerDone();		
	}
	
	@FXML
	public void handleOK() {
		if(this.mandatoryFieldsFilled()){
			String table = "";
			// 1. Fill Template: 
			table = tableTMP.replace("plidentifierpl", this.tableIdentifier);
			table = table.replace("pltablecodepl", this.tableData);
			table = table.replace("plcaptionpl", this.tableCaption);
			
			Map<DataFormat, Object> cb = new HashMap<DataFormat, Object>();
			cb.put(DataFormat.HTML, table);
			this.clipboard.setContent(cb);

			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Table successfully created");
			alert.setHeaderText("Table prepared!");
			alert.setContentText(	"Please insert the table into the annotation text\n"
								+ 	"by right-clicking at the desired location and \n"
								+ 	"choosing \"Paste\"");
			alert.showAndWait();
			this.LBL_ImportPath.setText("No file selected...");
			this.owningController.tableImportFormControllerDone();
			
			
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Form not filled out!");
			alert.setHeaderText("Some fields are empty!");
			alert.setContentText(	"Please fill all the fields in this form \n"
								+ 	"and load a table from a .html file.");
			this.owningController.markerInsert = false;
			alert.showAndWait();
		}
	}


	@Override
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		check = check & (this.owningController != null);
		
		if(check == false){
			System.out.println("\n\rTableImportFormController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
	

}

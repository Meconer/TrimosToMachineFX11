/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trimostomachine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 *
 * @author mats.andersson
 */
public class MainWindowFXMLController implements Initializable {

    @FXML
    private Label xLabel, zLabel;

    @FXML
    private TextField fanucToolNoTextField;

    @FXML
    private TextArea toolFileTextArea;

    @FXML
    private CheckBox fanucUseRadiusCheckBox, fanucSetRadiusToZeroCheckBox;

    @FXML
    private TabPane machineTabPane;

    @FXML
    private Tab fanucMachineTab, millPlusMachineTab, heidenhainMachineTab;

    @FXML
    private TextField fanucExtraLengthOffset;

    @FXML
    private TextField fanucExtraRadiusOffset;

    @FXML
    private TextField millPlusToolNoTextField;

    @FXML
    private CheckBox millPlusUseRadiusCheckBox;

    @FXML
    private CheckBox millPlusSetRadiusToZeroCheckBox;

    @FXML
    private TextField millPlusExtraLengthOffset;

    @FXML
    private TextField millPlusExtraRadiusOffset;

    @FXML
    private TextField heidToolNoTextField;

    @FXML
    private CheckBox heidSetRadiusToZeroCheckBox;

    @FXML
    private TextField heidExtraLengthOffset;

    @FXML
    private TextField heidExtraRadiusOffset;

    private TrimosDevice trimosDevice;
    private int fanucToolNo = 1;
    private int millPlusToolNo = 1;
    private int heidToolNo = 1;

    private enum SelectedTab {
        FANUC, MILLPLUS, HEIDENHAIN, UNKNOWN
    };
    private SelectedTab selectedTab;

    @FXML
    private void onMenuSettingsClicked() {
        Configuration.getConfiguration().showConfigurationDialog();
    }

    @FXML
    private void showAboutBox() {
        Alert aboutBox = new Alert(Alert.AlertType.INFORMATION);
        aboutBox.setHeaderText("Om");
        aboutBox.setContentText("Trimos To Machine\nVersion 0.1");
        aboutBox.showAndWait();
    }

    @FXML
    private void exitAction() {
        System.exit(0);
    }
    
    
    @FXML
    private void fanucUseRadiusCheckBoxChanged() {
        if (fanucUseRadiusCheckBox.isSelected()) {
            fanucSetRadiusToZeroCheckBox.setDisable(false);
        } else {
            fanucSetRadiusToZeroCheckBox.setDisable(true);
        }
    }

    @FXML
    private void fanucSetToOneButtonClicked() {
        fanucToolNo = 1;
        fanucToolNoTextField.setText(Integer.toString(fanucToolNo));
    }

    @FXML
    private void fanucIncToolNo() {
        fanucToolNo++;
        fanucToolNoTextField.setText(Integer.toString(fanucToolNo));
    }

    @FXML
    private void fanucDecToolNo() {
        fanucToolNo--;
        if (fanucToolNo <= 0) {
            fanucToolNo = 1;
        }
        fanucToolNoTextField.setText(Integer.toString(fanucToolNo));
    }

    @FXML
    private void fanucAddStartSection() {
        toolFileTextArea.appendText("%\nO555\n");
    }

    @FXML
    private void fanucAddEndSection() {
        toolFileTextArea.appendText("M30\n%\n");
    }

    @FXML
    void millPlusAddStartSection() {
        toolFileTextArea.appendText("%PM\n");
    }

    @FXML
    void millPlusDecToolNo() {
        millPlusToolNo--;
        if (millPlusToolNo <= 0) {
            millPlusToolNo = 1;
        }
        millPlusToolNoTextField.setText(Integer.toString(millPlusToolNo));
    }

    @FXML
    void millPlusIncToolNo() {
        millPlusToolNo++;
        millPlusToolNoTextField.setText(Integer.toString(millPlusToolNo));
    }

    @FXML
    void millPlusSetToOneButtonClicked() {
        millPlusToolNo = 1;
        millPlusToolNoTextField.setText(Integer.toString(millPlusToolNo));
    }

    @FXML
    void millPlusUseRadiusCheckBoxChanged() {
        if (millPlusUseRadiusCheckBox.isSelected()) {
            millPlusSetRadiusToZeroCheckBox.setDisable(false);
        } else {
            millPlusSetRadiusToZeroCheckBox.setDisable(true);
        }
    }

    @FXML
    private void heidSetToOneButtonClicked() {
        heidToolNo = 1;
        heidToolNoTextField.setText(Integer.toString(heidToolNo));
    }

    @FXML
    private void heidIncToolNo() {
        heidToolNo++;
        heidToolNoTextField.setText(Integer.toString(heidToolNo));
    }

    @FXML
    private void heidDecToolNo() {
        heidToolNo--;
        if (heidToolNo <= 0) {
            heidToolNo = 1;
        }
        heidToolNoTextField.setText(Integer.toString(heidToolNo));
    }

    @FXML
    private void heidAddStartSection() {
        toolFileTextArea.appendText("BEGIN TOOL   .T MM\n");
        toolFileTextArea.appendText("T       NAME             L           R\n");
    }

    @FXML
    private void heidAddEndSection() {
        toolFileTextArea.appendText("[END]\n");
    }

    @FXML
    private void machineTabChanged() {
        System.out.println("Machine tab changed");
        selectedTab = SelectedTab.UNKNOWN;
        if (machineTabPane.getSelectionModel().getSelectedItem().equals(fanucMachineTab)) {
            selectedTab = SelectedTab.FANUC;
        }
        if (machineTabPane.getSelectionModel().getSelectedItem().equals(millPlusMachineTab)) {
            selectedTab = SelectedTab.MILLPLUS;
        }
        if (machineTabPane.getSelectionModel().getSelectedItem().equals(heidenhainMachineTab)) {
            selectedTab = SelectedTab.HEIDENHAIN;
        }
    }

    @FXML
    void clearTextArea() {
        if (!toolFileTextArea.getText().isEmpty()) {
            if ( Utils.askForOk("Är du säker?", "Rensa?") ) {
                toolFileTextArea.clear();
            }
        }
    }

    @FXML
    void removeLastReadLine() {
        toolFileTextArea.setText(removeLastLine(toolFileTextArea.getText()));
        switch (selectedTab) {
            case FANUC: {
                fanucDecToolNo();
                break;
            }
            case MILLPLUS: {
                millPlusDecToolNo();
                break;
            }
            case HEIDENHAIN: {
                heidDecToolNo();
                break;
            }
        }
    }

    // Save the text area to a file that the user selects.
    @FXML
    private void saveFileAction() {
        // Use a file chooser dialog.
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(Configuration.getConfiguration().getInitialDirectoryName()));
        fc.setTitle("Spara till fil");

        File fileToSave = fc.showSaveDialog(null);
        if (fileToSave != null) {
            // User wants the file saved 
            Path path = Paths.get(fileToSave.getAbsolutePath());
            try {
                // Do the save.
                BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                String[] lines = toolFileTextArea.getText().split("\n");
                for (String line : lines) {
                    bw.write(line + "\r\n");
                }
                bw.close();
            } catch (IOException ex) {
                Utils.showError("Kunde inte spara offsetfilen. " + ex.getMessage());
            }
        }
    }

    // Remove the last line from a string with lines delimited with \n and
    // a \n as the last character.
    private String removeLastLine(String textArea) {
        // First remove the last endline
        // Get the pos.
        int lastEndLinePos = textArea.lastIndexOf('\n');
        // Check that it exists.
        if (lastEndLinePos > 0) {
            textArea = textArea.substring(0, lastEndLinePos);

            // Now remove the text from the last endLine to the end.
            lastEndLinePos = textArea.lastIndexOf('\n');
            if (lastEndLinePos > 0) {
                textArea = textArea.substring(0, lastEndLinePos);
            } else {
                // No more endlines. Must be the first line so we empty the
                // textArea
                return "";
            }

            // And put a \n in at the end so everything is as it should be.
            textArea += '\n';

        }
        return textArea;
    }

    private void handlePositionMessage(PositionMessage message) {

        if (message != null) {
            xLabel.setText(doubleToString( message.getXVal(), 3));
            zLabel.setText(doubleToString( message.getZVal(), 3));
            switch (selectedTab) {
                case FANUC: {
                    double zVal = message.getZVal();
                    zVal += getFanucExtraLengthOffset();
                    double xVal = message.getXVal();
                    xVal += getFanucExtraRadiusOffset();
                    toolFileTextArea.appendText(getFanucToolText(xVal, zVal));
                    setFanucToolNo(fanucToolNo + 1);
                    break;
                }
                case MILLPLUS: {
                    double zVal = message.getZVal();
                    zVal += getMillPlusExtraLengthOffset();
                    double xVal = message.getXVal();
                    xVal += getMillPlusExtraRadiusOffset();
                    toolFileTextArea.appendText(getMillPlusToolText(xVal, zVal));
                    setMillPlusToolNo(millPlusToolNo + 1);
                    break;
                }
                case HEIDENHAIN: {
                    double zVal = message.getZVal();
                    zVal += getHeidenhainExtraLengthOffset();
                    double xVal = message.getXVal();
                    xVal += getHeidenhainExtraRadiusOffset();
                    toolFileTextArea.appendText(getHeidenhainToolText(xVal, zVal));
                    setHeidenhainToolNo(heidToolNo + 1);
                    break;
                }
                default:
                    break;
            }
        }
    }

    private double offsetStringToDouble(String stringValue) {
        stringValue = stringValue.replaceAll(",", ".");
        try {
            return Double.parseDouble(stringValue);
        } catch (Exception e) {
            return -99999.0;
        }
    }

    private double getFanucExtraLengthOffset() {
        String stringValue = fanucExtraLengthOffset.getText();
        return offsetStringToDouble(stringValue);
    }

    private double getFanucExtraRadiusOffset() {
        String stringValue = fanucExtraRadiusOffset.getText();
        return offsetStringToDouble(stringValue);
    }

    private double getMillPlusExtraLengthOffset() {
        String stringValue = millPlusExtraLengthOffset.getText();
        return offsetStringToDouble(stringValue);
    }

    private double getMillPlusExtraRadiusOffset() {
        String stringValue = millPlusExtraRadiusOffset.getText();
        return offsetStringToDouble(stringValue);
    }

    private double getHeidenhainExtraLengthOffset() {
        String stringValue = heidExtraLengthOffset.getText();
        return offsetStringToDouble(stringValue);
    }

    private double getHeidenhainExtraRadiusOffset() {
        String stringValue = heidExtraRadiusOffset.getText();
        return offsetStringToDouble(stringValue);
    }

    TrimosDevice.MessageHandler messageHandler ;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    

    void setTrimosDevice(TrimosDevice trimosDevice) {
        this.trimosDevice = trimosDevice;
        messageHandler = new TrimosDevice.MessageHandler() {
            @Override
            public void handleMessage(PositionMessage message) {
                handlePositionMessage(message);
            }
        };
        trimosDevice.setMessageHandler(messageHandler);
    }

    private String getFanucToolText(double xVal, double zVal) {
        String toolText = "G10 L10 P" + fanucToolNo
                + " R" + doubleToString(zVal, 3) + "\n";
        if (fanucUseRadiusCheckBox.isSelected()) {
            toolText += "G10 L10 P" + (fanucToolNo + 20);
            if (fanucSetRadiusToZeroCheckBox.isSelected()) {
                toolText += " R0\n";
            } else {
                toolText += " R" + doubleToString(xVal, 3) + "\n";
            }
        }
        return toolText;
    }

    private String getMillPlusToolText(double xVal, double zVal) {
        String toolText = "P" + toolNumberToPlaceNumber(millPlusToolNo)
                + " T" + millPlusToolNo
                + " L" + doubleToString(zVal, 3);
        if (millPlusUseRadiusCheckBox.isSelected()) {
            if (millPlusSetRadiusToZeroCheckBox.isSelected()) {
                toolText += " R0";
            } else {
                toolText += " R" + doubleToString(xVal, 3);
            }
        }
        toolText += "\n";
        return toolText;
    }

    // The dmu 50 uses two magazine chains. The tools are placed in this order:
    // Tool Place
    // ==========
    //   1    2
    //   2    17
    //   3    3
    //   4    18
    //   5    4
    //   6    19
    //   7    5
    //   8    20
    // and so on.
    private int toolNumberToPlaceNumber(int toolNumber) {
        if (isOdd(toolNumber)) {
            return 2 + toolNumber / 2;
        } else {
            return 16 + toolNumber / 2;
        }
    }

    // Returns true if number is odd.
    private boolean isOdd(int n) {
        return n % 2 != 0;
    }

    private String spaces(int n) {
        String s = "";
        for (int i = 0; i < n; i++) {
            s += " ";
        }
        return s;
    }

    private String doubleToString(double val, int decimals) {
        String format = "%." + Integer.toString(decimals) + "f";
        return String.format(Locale.US, format, val);
    }
    
    private String getHeidenhainToolText(double xVal, double zVal) {
        String toolText = Integer.toString(heidToolNo);
        toolText += spaces(8 - toolText.length());
        toolText += "T" + heidToolNo;
        toolText += spaces(25 - toolText.length());
        toolText += "+" + doubleToString( zVal, 3);
        toolText += spaces(37-toolText.length());
        if (heidSetRadiusToZeroCheckBox.isSelected()) {
            toolText += "+0\n";
        } else {
            toolText += "+" + doubleToString( xVal, 3) + "\n";
        }

        return toolText;
    }

    private void setFanucToolNo(int toolNo) {
        fanucToolNo = toolNo;
        fanucToolNoTextField.setText(Integer.toString(fanucToolNo));
    }

    private void setMillPlusToolNo(int toolNo) {
        millPlusToolNo = toolNo;
        millPlusToolNoTextField.setText(Integer.toString(millPlusToolNo));
    }

    private void setHeidenhainToolNo(int toolNo) {
        heidToolNo = toolNo;
        heidToolNoTextField.setText(Integer.toString(heidToolNo));
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trimostomachine;

import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mats.andersson
 */
public class TrimosToMachine extends Application {
    
    MainWindowFXMLController controller;
    TrimosDevice trimosDevice;
    private final ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue();

    
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindowFXMLDocument.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Trimos Verktygsfiler");
        
        controller = loader.<MainWindowFXMLController>getController();
        trimosDevice = new TrimosDevice(messageQueue);
        trimosDevice.startTrimosConnection();
        controller.setTrimosDevice(trimosDevice);

        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop() {
        trimosDevice.stopThreads();
    }
}

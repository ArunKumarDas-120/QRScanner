package com.scaner;

import java.io.IOException;

import com.scaner.constant.ScanerConstants;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Scanner extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(ScanerConstants.ROOT_FXML));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

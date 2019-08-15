package com.scaner.controller;

import java.util.List;
import java.util.Objects;

import com.scaner.task.CameraFinderService;
import com.scaner.task.ScanningService;
import com.scaner.vo.CameraVo;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ScannerController {

	private double xOffset = 0;
	private double yOffset = 0;
	@FXML
	private ComboBox<CameraVo> listOfCameras;
	private final CameraFinderService camFinderService = new CameraFinderService();
	private final ScanningService scanningService = new ScanningService();
	private final QRCodeDataListiner QRCodeDataListiner = new QRCodeDataListiner();

	@FXML
	public void move(MouseEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setX(event.getScreenX() - xOffset);
		stage.setY(event.getScreenY() - yOffset);
	}

	@FXML
	public void position(MouseEvent event) {
		xOffset = event.getSceneX();
		yOffset = event.getSceneY();
	}

	@FXML
	public void close() {
		Platform.exit();
	}

	@FXML
	public void minimize(MouseEvent event) {
		Stage stage = (Stage) getScene(event).getWindow();
		stage.setIconified(true);
	}

	@FXML
	public void findCameras(Event event) {
		Scene scene = getScene(event);
		Label status = (Label) scene.lookup("#status");
		status.textProperty().bind(camFinderService.messageProperty());
		if (!camFinderService.isRunning())
			camFinderService.start();
		camFinderService.setOnSucceeded(e -> {
			List<CameraVo> camList = camFinderService.getValue();
			listOfCameras.getItems().clear();
			listOfCameras.getItems().addAll(camList);
			listOfCameras.setPromptText("   Select  ");
			status.textProperty().unbind();
			camFinderService.cancel();
			camFinderService.reset();
		});

	}

	@FXML
	public void startScanning(Event event) {
		Scene scene = getScene(event);
		ImageView viewPort = (ImageView) scene.lookup("#view-port");
		Label status = (Label) scene.lookup("#status");
		CameraVo cameraVo = listOfCameras.getSelectionModel().getSelectedItem();
		if (Objects.nonNull(cameraVo)) {
			if (!scanningService.isRunning()) {
				status.textProperty().bind(scanningService.messageProperty());
				scanningService.setCamNumber(cameraVo.getIndex());
				scanningService.setImageView(viewPort);
				scanningService.setProcessImage(true);
				scanningService.setActivateScanning(true);
				scanningService.start();
				scanningService.setOnRunning(e -> scanningService.valueProperty().addListener(QRCodeDataListiner));
			} else {
				scanningService.valueProperty().removeListener(QRCodeDataListiner);
				status.textProperty().unbind();
				scanningService.setActivateScanning(false);
				scanningService.setOnSucceeded(e -> {
					listOfCameras.getSelectionModel().clearSelection();
					scanningService.reset();
					status.setText(null);
				});

			}
		}

	}

	private Scene getScene(Event event) {
		return ((Node) event.getSource()).getScene();
	}
}

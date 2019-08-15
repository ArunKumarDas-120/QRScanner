package com.scaner.task;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

public class ScanningService extends Service<String> {

	private ImageView imageView;
	private boolean activateScanning;
	private boolean processImage;
	private int camNumber;

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public boolean isActivateScanning() {
		return activateScanning;
	}

	public void setActivateScanning(boolean activateScanning) {
		this.activateScanning = activateScanning;
	}

	public boolean isProcessImage() {
		return processImage;
	}

	public void setProcessImage(boolean processImage) {
		this.processImage = processImage;
	}

	public int getCamNumber() {
		return camNumber;
	}

	public void setCamNumber(int camNumber) {
		this.camNumber = camNumber;
	}

	public Task<String> getTask(ImageView imageView, boolean activateScanning, boolean processImage, int camNumber) {
		this.imageView = imageView;
		this.activateScanning = activateScanning;
		this.processImage = processImage;
		this.camNumber = camNumber;
		return createTask();
	}

	@Override
	protected Task<String> createTask() {
		return new Task<String>() {

			@Override
			protected String call() throws Exception {
				Webcam cam = Webcam.getWebcams().get(camNumber);
				cam.setViewSize(WebcamResolution.VGA.getSize());
				updateMessage("Opening Camera");
				cam.open();
				BufferedImage grabbedImage;
				MultiFormatReader reader = new MultiFormatReader();
				updateMessage("Scan QRCode");
				while (activateScanning) {

					try {
						if ((grabbedImage = cam.getImage()) != null) {
							imageView.imageProperty().set(SwingFXUtils.toFXImage(grabbedImage, null));
							grabbedImage.flush();
							BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(grabbedImage);
							BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
							Result res = reader.decode(bitmap);
							updateValue(res.getText());
						}
					} catch (Exception e) {
					}

				}
				cam.close();
				imageView.imageProperty().set(null);
				return null;
			}
		};
	}

}

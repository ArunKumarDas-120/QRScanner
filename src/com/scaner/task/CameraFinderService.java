package com.scaner.task;

import java.util.ArrayList;
import java.util.List;

import com.github.sarxos.webcam.Webcam;
import com.scaner.vo.CameraVo;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CameraFinderService extends Service<List<CameraVo>> {

	public  Task<List<CameraVo>> getTask() {
		return createTask();
	}
	
	@Override
	protected Task<List<CameraVo>> createTask() {

		return new Task<List<CameraVo>>() {
			@Override
			protected List<CameraVo> call() throws Exception {
				updateMessage("Finding Cams...");
				List<CameraVo> listOfCameraVo = new ArrayList<>();
				List<Webcam> camList = Webcam.getWebcams();
				int index = 0;
				for (Webcam webCam : camList) {
					listOfCameraVo.add(new CameraVo(webCam.getName(), index++));
				}
				updateMessage("");
				return listOfCameraVo;
			}

		};
	}

}

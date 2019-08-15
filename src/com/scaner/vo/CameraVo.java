package com.scaner.vo;

public class CameraVo {

	private String cameraName;
	private int index;

	public CameraVo() {

	}

	public CameraVo(String cameraName, int index) {
		this.cameraName = cameraName;
		this.index = index;
	}

	public String getCameraName() {
		return cameraName;
	}

	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return cameraName;
	}


}

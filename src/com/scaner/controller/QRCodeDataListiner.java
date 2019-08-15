package com.scaner.controller;

import java.util.Objects;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class QRCodeDataListiner implements ChangeListener<String> {

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if(Objects.nonNull(newValue))
			System.out.println(newValue);
		
	}

}

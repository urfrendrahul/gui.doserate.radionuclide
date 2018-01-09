package com.radio.nuclies.doseRates.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FileHandlerImpl implements FileHandler {

	@Override
	public List<String> getFileNames(String folderpath) {
		File folder = new File(folderpath);
		File[] listOfFiles = folder.listFiles();
		List<String> fileNames = new ArrayList<>();
		if(listOfFiles==null){
			return fileNames;
		}
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String name = listOfFiles[i].getName();
				fileNames.add(name.substring(0,name.indexOf('.')));
			}
		}
		return fileNames;
	}

}

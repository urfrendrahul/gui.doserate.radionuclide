package com.radio.nuclies.doseRates.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.radio.nuclies.doseRates.constant.CalculationType;
import com.radio.nuclies.doseRates.constant.StablityCategories;
import com.radio.nuclies.doseRates.handler.FileHandler;

@Service
public class DoseRatesServiceImpl implements DoseRatesService {
	@Autowired
	private FileHandler fileHandler;
	
	@Autowired
	private Environment env;
	
	@Override
	public List<String> getCalType() {
		List<String> calculationTypes = new ArrayList<>();
		for(CalculationType cal : CalculationType.values()){
			calculationTypes.add(cal.name());
		}
		return calculationTypes;
	}

	@Override
	public List<String> getStablityCategories() {
		List<String> calculationTypes = new ArrayList<>();
		for(StablityCategories stablity : StablityCategories.values()){
			calculationTypes.add(stablity.name());
		}
		return calculationTypes;
	}

	@Override
	public List<String> getFileNames() {
		return fileHandler.getFileNames(env.getProperty("nuclide.file.path"));
	}

}

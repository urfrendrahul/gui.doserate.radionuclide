package com.radio.nuclies.doseRates.service;

import java.util.List;

import com.radio.nuclies.doseRates.constant.CalculationType;
import com.radio.nuclies.doseRates.constant.StablityCategories;

public interface DoseRatesService {
	
	public List<String> getCalType();
	public List<String> getStablityCategories();
	public List<String> getFileNames();
	public double calculateDoseRate(CalculationType calculationType, double speed, double releaseHeight, List<StablityCategories> categories, List<Double> distances);

}

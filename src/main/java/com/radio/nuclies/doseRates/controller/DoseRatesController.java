package com.radio.nuclies.doseRates.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radio.nuclies.doseRates.constant.CalculationType;
import com.radio.nuclies.doseRates.constant.StablityCategories;
import com.radio.nuclies.doseRates.service.DoseRatesService;

@CrossOrigin
@RestController
public class DoseRatesController {
	
	@Autowired
	DoseRatesService doseRatesService;
	
	@RequestMapping(value = "/calTypes", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<String> getCalculationType() {
        return doseRatesService.getCalType();
    }
	
	@RequestMapping(value = "/categories", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<String> getStablityCategories() {
        return doseRatesService.getStablityCategories();
    }
	
	@RequestMapping(value = "/fileNames", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<String> getFileNames() {
        return doseRatesService.getFileNames();
    }
	
	@RequestMapping(value = "/doseRates", method = GET, produces = APPLICATION_JSON_VALUE)
    public Double calulateDoseRates(@RequestParam("calcType") CalculationType calculationType, 
    								@RequestParam("categories") List<StablityCategories> categories,
    								@RequestParam("speed") double speed,
    								@RequestParam("height") double releaseHeight,
    								@RequestParam("distances") List<Double> distances) {
        return doseRatesService.calculateDoseRate(calculationType,speed,releaseHeight,categories,distances);
    }

}

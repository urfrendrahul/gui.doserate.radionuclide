package com.radio.nuclies.doseRates.utils;

import static java.lang.Math.exp;

public class DecayCorrectionEvaluator {
	
	public static double evaluate(double dc, double xj, double u)
	{
		return exp(-dc*(xj/u));
	}
	   

}

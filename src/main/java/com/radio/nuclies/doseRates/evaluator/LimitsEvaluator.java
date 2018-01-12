package com.radio.nuclies.doseRates.evaluator;

import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class LimitsEvaluator {
	
	public static double zLimits(double sz, double H, double ns, int steps, double xj, double stack_distance,
			double A) {
		double testz = ns * sz;

		double z_ini_lim_sdm = 0.01;
		double z_fin_lim_sdm = A * sqrt(xj);
		double z_ini_lim_gpm = A * sqrt(xj);
		double z_fin_lim_gpm = H + steps;

		return (testz);
	}

	public static double yLimits(double sy, double ns, int steps) {
		double y_ini_lim = 0, y_fin_lim = 0;

		if (ns * sy < steps) {
			y_ini_lim = -ns * sy;
			y_fin_lim = ns * sy;
		} else if (ns * sy >= steps) {
			y_ini_lim = -steps;
			y_fin_lim = steps;
		}

		return (y_ini_lim);
	}

	public static double yLimitsSP(double sy, double ns, int steps) {
		double y_ini_lim = 0, y_fin_lim = 0;
		if (ns * sy < steps) {
			y_ini_lim = -ns * sy;
			y_fin_lim = ns * sy;
		} else if (ns * sy >= steps) {
			y_ini_lim = -steps;
			y_fin_lim = steps;
		}

		return (y_ini_lim);
	}

	public static double yLimitsSA(double xj, double ns, int steps) {
		double angle;
		double y_ini_lim = 0, y_fin_lim = 0;
		angle = (2.0 * 22.0) / (16.0 * 7.0 * 2.0);
		y_ini_lim = 0.0;
		y_fin_lim = (xj * sin(angle));
		return (y_ini_lim);
	}

}

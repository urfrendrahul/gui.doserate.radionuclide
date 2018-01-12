package com.radio.nuclies.doseRates.evaluator;

import static com.radio.nuclies.doseRates.evaluator.ReferenceData.sig;
import static java.lang.Math.pow;

public class SigmaEvaluator {

	public static double sigmaYS(double xj, int st) {
		double ay, sy = 0;
		if (xj < 100.0) {
			ay = sig[st][0];
			sy = ay * pow(xj, 0.9031);
		} else

		if (xj >= 100.0 && xj < 1000.0) {
			ay = sig[st][0];
			sy = ay * pow(xj, 0.9031);
		} else if (xj >= 1000.0) {
			ay = sig[st][0];
			sy = ay * pow(xj, 0.9031);
		}
		return (sy);
	}

	public static double sigmaY(double xj, int st) {
		double ay, sy;
		ay = sig[st][0];
		sy = ay * pow(xj, 0.9031);
		return (sy);
	}

	public static double sigmaYtibl(double xj, double u, double w_star, double H) 
	{
		double sy_tibl, c1, c2;
		c1 = 1. / 3.;
		c2 = 2. / 3.;
		if (xj <= (u * H / w_star)) {
			sy_tibl = c1 * w_star * xj / u;
		} else {
			sy_tibl = c1 * (w_star / u) * pow(xj, c2) * pow(H, c1);
		}
		return (sy_tibl);
	}

	public static double sigmaZ(double xj, int st) {
		double az, sz = 0, r, q;
		if (xj < 100.0) {
			r = sig[st][3];
			q = sig[st][2];
			az = sig[st][1];
			sz = az * pow(xj, q) + r;
		} else if (xj >= 100.0 && xj < 1000.0) {
			r = sig[st][6];
			q = sig[st][5];
			az = sig[st][4];
			sz = az * pow(xj, q) + r;
		} else if (xj >= 1000.0) {
			r = sig[st][9];
			q = sig[st][8];
			az = sig[st][7];
			sz = az * pow(xj, q) + r;
		}
		if (sz >= 5000.0) {
			sz = 5000.0;
		}
		return (sz);
	}

}

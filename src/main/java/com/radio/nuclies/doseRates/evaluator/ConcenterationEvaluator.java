package com.radio.nuclies.doseRates.evaluator;

import static com.radio.nuclies.doseRates.evaluator.ReferenceData.w;
import static com.radio.nuclies.doseRates.evaluator.ReferenceData.x;
import static com.radio.nuclies.doseRates.evaluator.SigmaEvaluator.sigmaYS;
import static com.radio.nuclies.doseRates.evaluator.SigmaEvaluator.sigmaYtibl;
import static com.radio.nuclies.doseRates.evaluator.SigmaEvaluator.sigmaZ;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ConcenterationEvaluator {

	public static double evaluate(double dist, double xj, double yj, double stack_distance, double H, int sts, int N,
			double u, double A, double w_star, double ns, double sz, int count) {

		int t;
		double xjj, xinitialc, xfinalc, fnp, syl_p, sys_p, syf_p, sz_p, hf_p, p, dp = 0, conc_sdm, xpy;
		double ddp = 0.0;

		xinitialc = 0.01;
		xfinalc = xj;

		for (t = 1; t <= N; t++) {
			// xinitialc = xj;
			// xfinalc = xj+1;

			xjj = (xinitialc + xfinalc) / 2.0 + x[t - 1] * (xinitialc - xfinalc) / 2.0;
			hf_p = A * sqrt(xjj + stack_distance);
			sys_p = sigmaYS(xjj, sts);
			syl_p = sigmaYtibl(xjj, u, w_star, H);
			syf_p = sqrt(sys_p * sys_p + syl_p * syl_p);
			sz_p = sigmaZ(xjj, sts);

			p = (hf_p - H) / sz_p;

			if (sts == 1) {
				dp = (2.5 * A) / (pow(xjj, 1.5)) - 5 * (hf_p - H) / (pow(xjj, 2));
			} else if (sts == 2) {
				dp = 4.16667 * A / (pow(xjj, 1.5)) - 8.33333 * (hf_p - H) / (pow(xjj, 2));
			} else if (sts == 3) {
				dp = 0.00125 * (hf_p - H) / (xjj * (pow((1 + 0.0002 * xjj), 0.5)))
						+ 6.25 * A * (pow((1 + 0.0002 * xjj), 0.5)) / (pow(xjj, 1.5))
						- 12.5 * (hf_p - H) * (pow((1 + 0.0002 * xjj), 0.5)) / (pow(xjj, 2));
			} else if (sts == 4) {
				dp = 0.0125 * (hf_p - H) / (xjj * (pow((1 + 0.0015 * xjj), 0.5)))
						+ 8.33333 * A * (pow((1 + 0.0015 * xjj), 0.5)) / (pow(xjj, 1.5))
						- 16.66667 * (hf_p - H) * (pow((1 + 0.0015 * xjj), 0.5)) / (pow(xjj, 2));
			} else if ((sts == 5) && (xjj < 100.0)) {
				dp = A / (2.0 * (0.0 + 0.063 * pow(xjj, 0.871)) * sqrt(xjj)) - (0.054873 * (-H + A * sqrt(xjj)))
						/ (pow((0.0 + 0.063 * pow(xjj, 0.871)), 2.0) * (pow(xjj, 0.129)));
			} else if ((sts == 5) && (xjj >= 100.0) && (xjj <= 1000.0)) {
				dp = A / (2.0 * (-1.3 + 0.211 * pow(xjj, 0.678)) * sqrt(xjj)) - (0.143058 * (-H + A * sqrt(xjj)))
						/ (pow((-1.3 + 0.211 * pow(xjj, 0.678)), 2.0) * (pow(xjj, 0.322)));
			}

			else if ((sts == 5) && (xjj > 1000.0)) {
				dp = -((2.05265 * (-H + A * sqrt(xjj)))
						/ ((pow((-34.0 + 6.73 * pow(xjj, 0.305)), 2.0) * (pow(xjj, 0.695)))))
						+ A / (2.0 * sqrt(xjj) * (-34.0 + 6.73 * pow(xjj, 0.305)));
			} else if ((sts == 6) && (xjj < 100.0)) {
				dp = A / (2.0 * (0.0 + 0.053 * pow(xjj, 0.814)) * sqrt(xjj)) - (0.043142 * (-H + A * sqrt(xjj)))
						/ (pow((0.0 + 0.053 * pow(xjj, 0.814)), 2.0) * (pow(xjj, 0.186)));
			}

			else if ((sts == 6) && (xjj >= 100.0) && (xjj <= 1000.0)) {

				dp = A / (2.0 * (-0.35 + 0.086 * pow(xjj, 0.74)) * sqrt(xjj)) - (0.06364 * (-H + A * sqrt(xjj)))
						/ (pow((-0.35 + 0.086 * pow(xjj, 0.74)), 2.0) * (pow(xjj, 0.26)));
			}

			else if ((sts == 6) && (xjj > 1000.0)) {
				dp = -((3.249 * (-H + A * sqrt(xjj))) / (pow((-48.6 + 18.05 * pow(xjj, 0.18)), 2.0) * (pow(xjj, 0.82))))
						+ A / (2.0 * sqrt(xjj) * (-48.6 + 18.05 * pow(xjj, 0.18)));
			}

			xpy = exp(-(yj * yj) / (2.0 * syf_p * syf_p));
			fnp = (1 / syf_p) * exp(-(p * p) / 2);
			conc_sdm = xpy * fnp * dp;
			ddp += w[t - 1] * conc_sdm;
		}
		ddp = ddp * (xfinalc - xinitialc) / 2.0;

		return (ddp);

	}

}

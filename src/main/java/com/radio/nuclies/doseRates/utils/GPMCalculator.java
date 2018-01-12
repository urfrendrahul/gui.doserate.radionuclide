package com.radio.nuclies.doseRates.utils;

import static com.radio.nuclies.doseRates.utils.SigmaEvaluator.sigmaY;
import static com.radio.nuclies.doseRates.utils.SigmaEvaluator.sigmaYS;
import static com.radio.nuclies.doseRates.utils.SigmaEvaluator.sigmaYtibl;
import static com.radio.nuclies.doseRates.utils.SigmaEvaluator.sigmaZ;
import static java.lang.Math.exp;
import static java.lang.Math.sqrt;

public class GPMCalculator {

	double conc_GPM_sp(double dist, double H, double releaser, double u, int st) {
		int steps_z;
		double dosz_GPM = 0.0, ddosez_GPM = 0, xpy_GPM, xpz_GPM, conc, phi, sy, sz, pi = 22. / 7.;
		double dz = 1.0, zj, kk, r0, buildup, yj;
		sy = sigmaY(dist, st);
		sz = sigmaZ(dist, st);
		yj = 0.0;
		zj = 0.01;
		xpz_GPM = exp(-((zj - H) * (zj - H)) / (2.0 * sz * sz)) + exp(-((zj + H) * (zj + H)) / (2.0 * sz * sz));
		xpy_GPM = exp(-(yj * yj) / (2.0 * sy * sy));
		conc = (releaser * xpy_GPM * xpz_GPM) / (2.0 * pi * sy * sz * u);

		return (conc);
	}

	double conc_GPM_sa(double dist, double H, double releaser, double u, int st) {
		int steps_z;
		double dosz_GPM = 0.0, ddosez_GPM = 0, xpy_GPM, xpz_GPM, conc, phi, sy, sz, pi = 22. / 7.;
		double dz = 1.0, zj, kk, r0, buildup, yj = 0.0;
		sy = sigmaY(dist, st);
		sz = sigmaZ(dist, st);
		zj = 0.01;
		xpz_GPM = exp(-((zj - H) * (zj - H)) / (2.0 * sz * sz)) + exp(-((zj + H) * (zj + H)) / (2.0 * sz * sz));
		xpy_GPM = exp(-(yj * yj) / (2.0 * sy * sy));
		conc = (releaser * xpz_GPM) / ((sqrt(2.0 * pi)) * dist * sz * u * (2.0 * pi / 16));
		return (conc);
	}

	double conc_sdm(double dist, double xj, double stack_distance, double H, double releaser, double u, int sts,
			double A, double w_star, double w_e, int count) {

		int k, l, steps_z, N = 64, ns = 3;
		double zj = 1.0, zj_sdm, zj_gpm, kk, syl, sys, syf, hf0, hf;
		double xpy, xpz_sdm, xpz, xpz_gpm, conc, phi, ddosez, ddosez_sdm, ddosez_gpm, dosez, sy, sz, pi = 22. / 7., r0,
				buildup;
		double dz = 1.0, dos = 0.0, yj = 0.0;
		syl = sigmaYtibl(xj, u, w_star, H);
		sys = sigmaYS(xj, sts);
		syf = sqrt(syl * syl + sys * sys);
		sz = sigmaZ(xj, sts);
		hf0 = A * sqrt(stack_distance);
		hf = A * sqrt(xj + stack_distance);
		conc = 0.0;
		xpz = exp(-((zj - H) * (zj - H)) / (2.0 * sz * sz)) + exp(-((zj + H) * (zj + H)) / (2.0 * sz * sz));
		if (((H - ns * sz) <= hf) && (zj <= hf)) {

			conc = (releaser / (2.0 * pi * u * hf)) * ConcenterationEvaluator.evaluate(dist, xj, yj, stack_distance, H,
					sts, N, u, A, w_star, ns, sz, count);

		} else {
			xpy = exp(-(yj * yj) / (2.0 * sys * sys));
			conc = (releaser * xpy * xpz) / (2.0 * pi * sys * sz * u);
		}
		return (conc);
	}

}

package com.radio.nuclies.doseRates.evaluator;

import static com.radio.nuclies.doseRates.evaluator.LimitsEvaluator.yLimits;
import static com.radio.nuclies.doseRates.evaluator.LimitsEvaluator.yLimitsSA;
import static com.radio.nuclies.doseRates.evaluator.LimitsEvaluator.yLimitsSP;
import static com.radio.nuclies.doseRates.evaluator.LimitsEvaluator.zLimits;
import static com.radio.nuclies.doseRates.evaluator.ReferenceData.PI;
import static com.radio.nuclies.doseRates.evaluator.ReferenceData.w;
import static com.radio.nuclies.doseRates.evaluator.SigmaEvaluator.sigmaY;
import static com.radio.nuclies.doseRates.evaluator.SigmaEvaluator.sigmaYS;
import static com.radio.nuclies.doseRates.evaluator.SigmaEvaluator.sigmaYtibl;
import static com.radio.nuclies.doseRates.evaluator.SigmaEvaluator.sigmaZ;
import static java.lang.Math.exp;
import static java.lang.Math.sqrt;


public class Integeration {

	public double doseY(double dist, double xj, double dx, double stack_distance, double H, double releaser, double u,
			double energy, double ut, double ua, double dc, double gammaabun, int sts, int x_ini_lim, double x_fin_lim,
			int steps, int N, double A, double w_star, double w_e, int count) {
		
		double xpy, xpz, n, conc, r0, phi, dy, dz, i, k, dcaycor, sys, syl, symax;
		double buildup, kk, dos, ddose, sy, sz, ns = 3.0, valuey, valuez, sy1, sz1, valuez1;
		double x0, y, y0, z, z0, z_limit, y_limit;
		double a, b;
		double dosy = 0.0;
		double yj, hf;
		x0 = dist;
		y0 = 0.0; // x0, y0 and z0 are the initial coordinates of the receptor
		z0 = 0.0;
		dos = 0.0; // Initialization of the dose.
		kk = (ut - ua) / ua;
		dy = 1.0;
		dz = 1.0;
		hf = A * sqrt(xj + stack_distance);
		// Integration done using first holding x and z constant and varying y.
		// After the integration along y is complete for a particular volume
		// elemnt,
		// integration along z is done still holding x conxtant and at
		// integration along x is done at the end.
		syl = sigmaYtibl(xj, u, w_star, H);
		sys = sigmaYS(xj, sts);
		sz = sigmaZ(xj, sts);
		if (syl > sys) {
			symax = syl;
		} else {
			symax = sys;
		}
		dcaycor = DecayCorrectionEvaluator.evaluate(dc, xj, u);
		z_limit = zLimits(sz, H, ns, steps, xj, stack_distance, A);
		y_limit = yLimits(symax, ns, steps);

		valuey = ns * syl;

		for (int j = 1; j <= N; j++) {
			yj = (y_ini_lim + y_fin_lim) / 2.0 + x[j - 1] * ((y_fin_lim - y_ini_lim) / 2.0);
			dosy += w[j - 1] * dosez(dist, xj, stack_distance, yj, H, releaser, u, energy, ut, ua, dcaycor, gammaabun,
					x0, y0, z0, sts, z_ini_lim_sdm, z_fin_lim_sdm, z_ini_lim_gpm, z_fin_lim_gpm, N, steps, ns, A,
					w_star, hf, w_e, count);
		}
		dosy = dosy * (y_fin_lim - y_ini_lim) / 2.0;

		return (dosy);
	}

	public double doseZ(double dist, double xj, double stack_distance, double yj, double H, double releaser, double u,
			double energy, double ut, double ua, double dcaycor, double gammaabun, double x0, double y0, double z0,
			int sts, double z_ini_lim_sdm, double z_fin_lim_sdm, double z_ini_lim_gpm, double z_fin_lim_gpm, int N,
			int steps, double ns, double A, double w_star, double hf, double w_e, int count) {

		int k, l, steps_z;
		int tag, proc, ierr;
		double dosz = 0.0, dos_sdm = 0.0, dos_gpm = 0.0, dosz_sdm = 0.0, dosz_gpm = 0.0, zj, zj_sdm, zj_gpm, kk, syl,
				sys, syf, hf0;
		double xpy, xpz_sdm, xpz, xpz_gpm, conc, phi, ddosez, ddosez_sdm, ddosez_gpm, dosez, sy, sz, r0,
				buildup;
		double dz = 1.0, dos = 0.0;
		kk = (ut - ua) / ua;
		syl = sigmaYtibl(xj, u, w_star, H);
		sys = sigmaYS(xj, sts);
		syf = sqrt(syl * syl + sys * sys);
		sz = sigmaZ(xj, sts);
		hf0 = A * sqrt(stack_distance);
		hf = A * sqrt(xj + stack_distance);
		steps_z = (int)( (H + steps) / dz);
		dosez = 0.0;
		for (zj = 0.01; zj <= steps_z; zj++) {
			xpz = exp(-((zj - H) * (zj - H)) / (2.0 * sz * sz)) + exp(-((zj + H) * (zj + H)) / (2.0 * sz * sz));
			if (((H - ns * sz) <= hf) && (zj <= hf)) {

				conc = (releaser / (2.0 * PI * u * hf))
						* ConcenterationEvaluator.evaluate(dist, xj, yj, stack_distance, H, sts, N, u, A, w_star, ns, sz, count);
				r0 = sqrt((xj - x0) * (xj - x0) + (yj - y0) * (yj - y0) + (zj - z0) * (zj - z0));
				buildup = (1.0 + kk * ut * r0);
				phi = (conc * buildup * exp(-ut * r0)) / (4.0 * PI * r0 * r0);
				ddosez_sdm = .0005 * phi * energy * ua * gammaabun;
				dosz_sdm += ddosez_sdm;
			} else {
				xpy = exp(-(yj * yj) / (2.0 * sys * sys));
				conc = (releaser * xpy * xpz) / (2.0 * pi * sys * sz * u);
				r0 = sqrt((xj - x0) * (xj - x0) + (yj - y0) * (yj - y0) + (zj - z0) * (zj - z0));
				buildup = (1.0 + kk * ut * r0);
				phi = (conc * buildup * exp(-ut * r0)) / (4.0 * PI * r0 * r0);
				ddosez_gpm = .0005 * phi * energy * ua * gammaabun;
				dosz_gpm += ddosez_gpm;
			}

		}
		dosz = dosz_sdm + dosz_gpm;
		return (dosz);
	}

	double doseYSA(double dist, double xj, double dx, double H, double releaser, double u, double energy, double ut,
			double ua, double dc, double gammaabun, int st, int steps, int N) {
		
		double kk, dy, dz, dcaycor, sy, sz, ns = 3.0, x0, y0, z0, z_limit, y_limit, yj, dosy = 0.0;
		x0 = dist;
		y0 = 0.0;
		z0 = 0.0; // x0, y0 and z0 are the initial coordinates of the receptor
		kk = (ut - ua) / ua;
		dy = 1.0;
		dz = 1.0;
		// Integration:
		// Integration done using first holding x and z constant and varying y.
		// After the integration along y is complete for a particular volume
		// elemnt,
		// integration along z is done still holding x conxtant and at
		// integration along x is done at the end.
		sy = sigmaY(xj, st);
		sz = sigmaZ(xj, st);
		dcaycor = DecayCorrectionEvaluator.evaluate(dc, xj, u);
		y_limit = yLimitsSA(xj, ns, steps);
		for (int jj = 1; jj <= N; jj++) {
			yj = (y_ini_lim + y_fin_lim) / 2.0 + x[jj - 1] * ((y_fin_lim - y_ini_lim) / 2.0);
			dosy += w[jj - 1] * dosezsa(dist, xj, yj, H, releaser, u, energy, ut, ua, dcaycor, gammaabun, x0, y0, z0,
					st, N, steps, ns, y_ini_lim, y_fin_lim);
		}
		// dosy = dosy * (y_fin_lim - y_ini_lim) / 2.0;

		dosy = dosy * (y_fin_lim - y_ini_lim);
		return (dosy);
	}

	public double doseZSA(double dist, double xj, double yj, double H, double releaser, double u, double energy,
			double ut, double ua, double dcaycor, double gammaabun, double x0, double y0, double z0, int st, int N,
			int steps, double ns, double y_ini_lim, double y_fin_lim) {
		
		int steps_z;
		double dosz = 0, dosz_GPM = 0.0, ddosez_GPM = 0, xpy_GPM, xpz_GPM, conc, phi, sy, sz;
		double dz = 1.0, zj, kk, r0, buildup;
		kk = (ut - ua) / ua;
		sy = sigmaY(xj, st);
		sz = sigmaZ(xj, st);
		steps_z = (int)((H + steps) / dz);
		for (zj = 0.01; zj <= steps_z; zj += dz) {
			xpz_GPM = exp(-((zj - H) * (zj - H)) / (2.0 * sz * sz)) + exp(-((zj + H) * (zj + H)) / (2.0 * sz * sz));
			xpy_GPM = exp(-(yj * yj) / (2.0 * sy * sy));
			conc = (releaser * xpz_GPM) / ((sqrt(2.0 * PI)) * xj * sz * u * (2.0 * PI / 16));
			r0 = sqrt((xj - x0) * (xj - x0) + (yj - y0) * (yj - y0) + (zj - z0) * (zj - z0));
			buildup = (1.0 + kk * ut * r0);
			phi = (conc * buildup * exp(-ut * r0)) / (4.0 * PI * r0 * r0);
			ddosez_GPM = .0005 * phi * energy * ua * gammaabun;
			dosz_GPM += ddosez_GPM;
		}
		dosz = dosz_GPM;
		return (dosz);
	}

	public double doseYSP(double dist, double xj, double dx, double H, double releaser, double u, double energy,
			double ut, double ua, double dc, double gammaabun, int st, int steps, int N) {
		double kk, dy, dz, dcaycor, sy, sz, ns = 3.0, x0, y0, z0, z_limit, y_limit, yj, dosy = 0.0;
		x0 = dist;
		y0 = 0.0;
		z0 = 0.0; // x0, y0 and z0 are the initial coordinates of the receptor
		kk = (ut - ua) / ua;
		dy = 1.0;
		dz = 1.0;
		// Integration:
		// Integration done using first holding x and z constant and varying y.
		// After the integration along y is complete for a particular volume
		// elemnt,
		// integration along z is done still holding x conxtant and at
		// integration along x is done at the end.
		sy = sigmaY(xj, st);
		sz = sigmaZ(xj, st);
		dcaycor = DecayCorrectionEvaluator.evaluate(dc, xj, u);
		y_limit = yLimitsSP(sy, ns, steps);
		for (int jj = 1; jj <= N; jj++) {
			yj = (y_ini_lim + y_fin_lim) / 2.0 + x[jj - 1] * ((y_fin_lim - y_ini_lim) / 2.0);
			dosy += w[jj - 1] * dosezsp(dist, xj, yj, H, releaser, u, energy, ut, ua, dcaycor, gammaabun, x0, y0, z0,
					st, N, steps, ns);
		}
		dosy = dosy * (y_fin_lim - y_ini_lim) / 2.0;
		return (dosy);
	}

	public double doseZSP(double dist, double xj, double yj, double H, double releaser, double u, double energy,
			double ut, double ua, double dcaycor, double gammaabun, double x0, double y0, double z0, int st, int N,
			int steps, double ns) {
		int steps_z;
		double dosz = 0, dosz_GPM = 0.0, ddosez_GPM = 0, xpy_GPM, xpz_GPM, conc, phi, sy, sz;
		double dz = 1.0, zj, kk, r0, buildup;
		kk = (ut - ua) / ua;
		sy = sigmaY(xj, st);
		sz = sigmaZ(xj, st);
		steps_z = (int) ((H + steps) / dz);
		for (zj = 0.01; zj <= steps_z; zj += dz) {
			xpz_GPM = exp(-((zj - H) * (zj - H)) / (2.0 * sz * sz)) + exp(-((zj + H) * (zj + H)) / (2.0 * sz * sz));
			xpy_GPM = exp(-(yj * yj) / (2.0 * sy * sy));
			conc = (releaser * xpy_GPM * xpz_GPM) / (2.0 * PI * sy * sz * u);
			r0 = sqrt((xj - x0) * (xj - x0) + (yj - y0) * (yj - y0) + (zj - z0) * (zj - z0));
			buildup = (1.0 + kk * ut * r0);
			phi = (conc * buildup * exp(-ut * r0)) / (4.0 * PI * r0 * r0);
			ddosez_GPM = .0005 * phi * energy * ua * gammaabun;
			dosz_GPM += ddosez_GPM;
		}
		dosz = dosz_GPM;
		return (dosz);
	}

}

package com.radio.nuclies.doseRates.handler;

import static java.lang.Math.pow;

import com.radio.nuclies.doseRates.utils.AttenuationCoefficientEvaluator;

public class DoseRateHandler {

	// xlimits is the array containing the limits of integration along the x
	// coordinate. It is equal to 6 times the mean free path of the photon
	// depending on the energy.

	double xlimitini[];
	double xlimitfin[];
	int x_int_range[];
	double linearatt[];
	double energyatt[];
	double xlimits[];
	double decaycorr[];
	int st_cat[];
	double z_ini_lim_sdm, z_fin_lim_sdm, z_ini_lim_gpm, z_fin_lim_gpm, y_ini_lim, y_fin_lim;

	public void handle() {
		int count, option, choice, no_of_stcat;
		double ur, k, pi, u, H, sy, ay, az, sz, releaser, r, q, dos, xl;
		double energy, ua, ut, dc, gammaabun, linearattn, xin, xfi, dx, sts;
		double integral_sum, my_int, x_ini_lim, x_fin_lim, my_dose;
		double xj, x0, y, y0, z, z0, dist, us, ul;
		double dosx_sa = 0.0, dosx_sp = 0.0, conc_sp = 0.0, conc_SDM = 0.0, conc_sa = 0.0;
		int n, no_of_energy, no_of_downdist;
		int i, j;
		int st, nx, steps, integ_steps, ndist, mfp, tempx, stb, flag = 0;
		int N = 64;
		double dosx = 0.0, w_star, w_e, h0, tibl_ht, tibl_ht0, pot_tgrad, heat_flux, rho, spec_heat, g, ambient_temp,
				density_spec;
		g = 9.8;
		ambient_temp = 295.0;
		rho = 1.2;
		spec_heat = 1000.0;
		density_spec = rho * spec_heat;
		dx = 1.0;
		double stack_distance = 0.0, dosx_SDM;
		double array[][];
		double array1[][];
		int file_no = 1;
		double thalf, xpz, xpy, conc, concen, pii = 22.0 / 7.0, A, xpz_SP, xpy_SP, conc_SP, conc_SA, xpz_SA, xpy_SA;
		int array_size, array_size1, choice1, choice2, choice3, choice4, choice5;

		mfp = 5;

		tibl_ht0 = H;
		pot_tgrad = 0.03;
		double c3 = 1. / 3.;
		heat_flux = 350.0;
		A = pow(((2.7 * heat_flux) / (density_spec * pot_tgrad * u)), 0.5);
		w_star = pow(((g * heat_flux * tibl_ht0) / (ambient_temp * density_spec)), c3);
		w_e = 0.5 * u * A * A / tibl_ht0;

		no_of_energy = flag;

		// fprintf( out_file2,", ,Dose rate (microSv/hr)" );
		// fprintf( out_file2,"\nStability Category, Downwind Distance
		// (m),Energy(MeV)" );

		for (int bb = 0; bb <= no_of_stcat - 1; bb++) {
			stb = st_cat[bb];
			st = stb;
			if (st == 1) {
				fprintf(out_file2, "\nA,");
			} else if (st == 2) {
				fprintf(out_file2, "\nB,");
			} else if (st == 3) {
				fprintf(out_file2, "\nC,");
			} else if (st == 4) {
				fprintf(out_file2, "\nD,");
			} else if (st == 5) {
				fprintf(out_file2, "\nE,");
			} else if (st == 6) {
				fprintf(out_file2, "\nF,");
			}
			sts = st;
			for (int enn = 0; enn <= no_of_energy - 1; enn++) {
				fprintf(out_file2, ",%e", array[enn][0]);
			}

			for (int k = 0; k <= no_of_downdist - 1; k++) {
				dist = dista[k];
				linearattn = AttenuationCoefficientEvaluator.evaluate(dist, mfp, no_of_energy);
				fprintf(out_file2, "\n");
				fprintf(out_file2, ",%e", dist);

				for (int ii = 0; ii <= no_of_energy - 1; ii++) {
					dosx = 0.0;
					dosx_SDM = 0.0;
					dosx_sa = 0.0;
					dosx_sp = 0.0;
					conc_sp = 0.0;
					conc_sa = 0.0;
					conc_SDM = 0.0;
					energy = array[ii][0];
					ua = energyatt[ii];
					ut = linearatt[ii];
					x_ini_lim = xlimitini[ii];
					x_fin_lim = xlimitfin[ii];
					steps = x_int_range[ii];
					integ_steps = steps / ((double) dx);
					thalf = array[0][3];
					dc = 0.693 / thalf;
					releaser = array[0][2];
					gammaabun = array[ii][1];

					for (int m = 1; m <= N; m++) {
						xj = (x_ini_lim + x_fin_lim) / 2.0 + x[m - 1] * ((x_fin_lim - x_ini_lim) / 2.0);

						if (choice1 == 1) {
							dosx_sp += w[m - 1] * doseysp(dist, xj, dx, H, releaser, u, energy, ut, ua, dc, gammaabun,
									st, steps, N);
						} else if (choice1 == 2) {
							dosx_sa += w[m - 1] * doseysa(dist, xj, dx, H, releaser, u, energy, ut, ua, dc, gammaabun,
									st, steps, N);
						} else if (choice1 == 3) {
							dosx_SDM += w[m - 1] * dosey(dist, xj, dx, stack_distance, H, releaser, u, energy, ut, ua,
									dc, gammaabun, sts, x_ini_lim, x_fin_lim, steps, N, A, w_star, w_e, count);
						} else if (choice1 == 4) {
							conc_sp += conc_GPM_sp(dist, H, releaser, u, st);
							fprintf(out_file2, ",%e", conc_sp);
							m = N;
						} else if (choice1 == 5) {
							conc_sa += conc_GPM_sa(dist, H, releaser, u, st);
							fprintf(out_file2, ",%e", conc_sa);
							m = N;
						} else if (choice1 == 6) {
							xj = dist;
							conc_SDM += conc_sdm(dist, xj, stack_distance, H, releaser, u, sts, A, w_star, w_e, count);
							m = N;
							fprintf(out_file2, ",%e", conc_SDM);
						}
					}

					if (choice1 == 1) {
						dosx = dosx_sp * (x_fin_lim - x_ini_lim) / 2.0;
						fprintf(out_file2, ",%e", dosx);
					} else if (choice1 == 2) {
						dosx = dosx_sa * (x_fin_lim - x_ini_lim) / 2.0;
						fprintf(out_file2, ",%e", dosx);
					} else if (choice1 == 3) {
						dosx = dosx_SDM * (x_fin_lim - x_ini_lim) / 2.0;
						fprintf(out_file2, ",%e", dosx);
					}

					printf("\n dist : %e\t energy : %e\t dose : %e\t Stability: %d\n", dist, energy, dosx, st);
					// fprintf( out_file2,",%e", dosx);
				}
			}
		}
		printf("w*   %lf\nwe   %lf\nA   %lf", w_star, w_e, A);
	}

}

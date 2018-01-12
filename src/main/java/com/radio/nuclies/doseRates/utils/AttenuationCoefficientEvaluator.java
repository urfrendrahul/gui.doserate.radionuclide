package com.radio.nuclies.doseRates.utils;

public class AttenuationCoefficientEvaluator {

	public static double evaluate(double dist, int mfp, int no_of_energy){

		double tempen, tempenatt, templinatt, enr, deltae, deltaea, deltala, earatio, laratio, xin, xfi;
		int i, nx;
		for(int j=0; j<=no_of_energy-1; j++)
		{
			tempen =  photonenergy[j];
			for( i=0; i<=36; i++)
			{
				if( tempen == refenergy[i] )
				{
					tempenatt = enatt[i];
					templinatt = linatt[i];
					energyatt[j] = tempenatt;
					linearatt[j] = templinatt;
					xlimits[j] = mfp/templinatt;

					if( dist > xlimits[j] )
					{
					  xin = dist - xlimits[j];  
					  xfi = dist + xlimits[j];
					  xlimitini[j] = xin;
					  
					  xlimitfin[j] = xfi;
		 		    }
					else
					if(dist <= xlimits[j])
					{
					 xin = 1.0;		
				  	 xfi = dist + xlimits[j];
				  	 xlimitini[j] = xin;
				  	 xlimitfin[j] = xfi;
				 	}

				 	nx = int(xfi - xin);
				 	x_int_range[j] = nx;
				}

				else

				if( tempen > refenergy[i] && tempen < refenergy[i+1]  )
				{
	                deltae = refenergy[i+1]-refenergy[i];
	                deltaea = enatt[i+1]-enatt[i];
	                deltala = linatt[i+1]-linatt[i];
	                earatio = deltaea/deltae;
	                laratio = deltala/deltae;
	                tempenatt = enatt[i+1] - (refenergy[i+1]-tempen)*(earatio);
					templinatt = linatt[i+1]-(refenergy[i+1]-tempen)*(laratio);
					energyatt[j] = tempenatt;
					linearatt[j] = templinatt;
					xlimits[j] = mfp/templinatt;

					if( dist > xlimits[j] )
					{
					  xin = dist - xlimits[j];
					  xfi = dist + xlimits[j];
					  xlimitini[j] = xin;
					  xlimitfin[j] = xfi;
		 		    }
					else
					if(dist <= xlimits[j])
					{
					 xin = 1.0;
				  	 xfi = dist + xlimits[j];
				  	 xlimitini[j] = xin;
					 xlimitfin[j] = xfi;
				 	}
					 nx = int(xfi - xin) ;
					 x_int_range[j] = nx;
				}
			}
		}
	return(nx);
	 
	}
}

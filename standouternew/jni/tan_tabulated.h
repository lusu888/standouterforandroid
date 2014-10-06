
#ifndef __TAN_TABULATED__
#define __TAN_TABULATED__

#include <math.h>


#define ntab         100      // number of tabulations
#define ratio0       0.01f     // initial smallest value
#define MULT_RATIO   1.1f      // multiplier for a ratio

/* returns values from -PI/2 to PI/2
 *
 *
 * NOTICE (Vlad 1-6-08): This header file include definitions,
 *  global variables and also bodies of functions. Since functions
 *  and variables are static they are unique regardless of who and
 *  how many times include this header.
 *
 * For efficiency reasons, it is the responsibility of a programmer
 * to make sure module global variables ratio[] and val[] were initialized
 * by calling initAtan() once before getATan() is used. It doesn't matter
 * what class makes initialization, only the order is important.
 */


const float ratio_tan[ntab] = {	1.000000000000000e-02,
		1.100000000000000e-02,
		1.210000000000000e-02,
		1.331000000000000e-02,
		1.464100000000000e-02,
		1.610510000000000e-02,
		1.771561000000001e-02,
		1.948717100000001e-02,
		2.143588810000001e-02,
		2.357947691000001e-02,
		2.593742460100002e-02,
		2.853116706110002e-02,
		3.138428376721002e-02,
		3.452271214393103e-02,
		3.797498335832414e-02,
		4.177248169415655e-02,
		4.594972986357221e-02,
		5.054470284992944e-02,
		5.559917313492239e-02,
		6.115909044841463e-02,
		6.727499949325610e-02,
		7.400249944258172e-02,
		8.140274938683990e-02,
		8.954302432552390e-02,
		9.849732675807629e-02,
		0.108347059433884,
		0.119181765377272,
		0.131099941915000,
		0.144209936106500,
		0.158630929717150,
		0.174494022688864,
		0.191943424957751,
		0.211137767453526,
		0.232251544198879,
		0.255476698618767,
		0.281024368480643,
		0.309126805328708,
		0.340039485861578,
		0.374043434447736,
		0.411447777892510,
		0.452592555681761,
		0.497851811249937,
		0.547636992374931,
		0.602400691612424,
		0.662640760773666,
		0.728904836851033,
		0.801795320536136,
		0.881974852589750,
		0.970172337848725,
		1.06718957163360,
		1.17390852879696,
		1.29129938167665,
		1.42042931984432,
		1.56247225182875,
		1.71871947701163,
		1.89059142471279,
		2.07965056718407,
		2.28761562390247,
		2.51637718629272,
		2.76801490492200,
		3.04481639541419,
		3.34929803495561,
		3.68422783845118,
		4.05265062229629,
		4.45791568452592,
		4.90370725297852,
		5.39407797827637,
		5.93348577610401,
		6.52683435371441,
		7.17951778908585,
		7.89746956799443,
		8.68721652479388,
		9.55593817727327,
		10.5115319950006,
		11.5626851945007,
		12.7189537139507,
		13.9908490853458,
		15.3899339938804,
		16.9289273932684,
		18.6218201325953,
		20.4840021458548,
		22.5324023604403,
		24.7856425964843,
		27.2642068561327,
		29.9906275417460,
		32.9896902959206,
		36.2886593255127,
		39.9175252580639,
		43.9092777838703,
		48.3002055622574,
		53.1302261184831,
		58.4432487303314,
		64.2875736033646,
		70.7163309637011,
		77.7879640600712,
		85.5667604660783,
		94.1234365126861,
		103.535780163955,
		113.889358180350,
		125.278293998385
};

const float val[ntab] = {9.999666686665238e-03,
		1.099955636554075e-02,
		1.209940953153609e-02,
		1.330921410097073e-02,
		1.463995399173716e-02,
		1.610370780059751e-02,
		1.771375704313867e-02,
		1.948470481191492e-02,
		2.143260576066287e-02,
		2.357510836916716e-02,
		2.593161048026649e-02,
		2.852342912194574e-02,
		3.137398562571743e-02,
		3.450900701707448e-02,
		3.795674457059910e-02,
		4.174821027250647e-02,
		4.591743169196455e-02,
		5.050172539642483e-02,
		5.554198851207536e-02,
		6.108300727190753e-02,
		6.717378033778031e-02,
		7.386785323583976e-02,
		8.122365828852315e-02,
		8.930485181386684e-02,
		9.818063691395956e-02,
		0.107926055674897,
		0.118622228804866,
		0.130356513378931,
		0.143222540177021,
		0.157320080885284,
		0.172754682102876,
		0.189636987841455,
		0.208081655667081,
		0.228205755192694,
		0.250126523757470,
		0.273958346811564,
		0.299808835646024,
		0.327773900637128,
		0.357931773832077,
		0.390336030656489,
		0.425007804594652,
		0.461927581718227,
		0.501027191561581,
		0.542182845252711,
		0.585210256684590,
		0.629862945790781,
		0.675834691562400,
		0.722766730639960,
		0.770259699123257,
		0.817889584141108,
		0.865226250642490,
		0.911852623942399,
		0.957382481074322,
		1.00147507255992,
		1.04384538017974,
		1.08426955005198,
		1.12258573882781,
		1.15869113295441,
		1.19253618296479,
		1.22411714774711,
		1.25346792721449,
		1.28065195122362,
		1.30575465481866,
		1.32887685105461,
		1.35012913688538,
		1.36962734165947,
		1.38748894761316,
		1.40383036827408,
		1.41876495364831,
		1.43240159129097,
		1.44484378264212,
		1.45618908919111,
		1.46652885972904,
		1.47594816613333,
		1.48452588972596,
		1.49233491280904,
		1.49944238043838,
		1.50591000598394,
		1.51179440078351,
		1.51714741348894,
		1.52201646879378,
		1.52644489835218,
		1.53047225905290,
		1.53413463556767,
		1.53746492538500,
		1.54049310547884,
		1.54324648043055,
		1.54574991228988,
		1.54802603277677,
		1.55009543863049,
		1.55197687103580,
		1.55368738011928,
		1.55524247553111,
		1.55665626411969,
		1.55794157567840,
		1.55911007770316,
		1.56017238004988,
		1.56113813032758,
		1.56201610080725,
		1.56281426757108
};

/*
inline static void initAtan()
	{

	ratio_tan[0] = ratio0;
	Math::ATan (val[0], ratio_tan[0]);

	for (int i=1; i<ntab; i++)
		{
		ratio_tan[i] = ratio_tan[i-1] * MULT_RATIO;
		Math::ATan (val[i], ratio_tan[i]);
		}
	}
*/

/* error is within 1 deg.
 */
inline static float getATan(int dy, int dx)
	{

	float rr, sign_res;

	// debug
#ifdef USE_MATH_ATAN
	Math::ATan(rr, dy, dx);
	// shorten domain to -PI/2..PI/2
	while (rr>PI/2)
		rr = rr-PI;
	while (rr<-PI/2)
		rr = rr+PI;
	return(rr);
#endif

	// ----------------------------------------
	if ( (dy>=0 && dx>=0) || (dy<0 && dx<0))
		{
		sign_res = 1;
		}
	else
		{
		sign_res = -1;
		}

	if (dy==0)
		return (0);

	if (dx==0)
		return (PI_HALF*SIGN(dy));

	rr = Abs ((float)dy/(float)dx); // bug, integer division

	// extreme values
	if ( rr<=ratio_tan[0])
		return (val[0]*sign_res);

	if ( rr>=ratio_tan[ntab-1])
		return (val[ntab-1]*sign_res);

	// tree search with granularity 12
	if ( rr<ratio_tan[25])
		{
		if ( rr <ratio_tan[12])
			{
			for (int i=1; i<=12; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		if ( rr>=ratio_tan[12])
			{
			for (int i=13; i<=25; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		} // 1..24

	if ( rr>=ratio_tan[25] && rr <ratio_tan[50])
		{
		if ( rr <ratio_tan[37])
			{
			for (int i=26; i<=37; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		if ( rr>=ratio_tan[37])
			{
			for (int i=38; i<=50; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		} // 25..49

	if ( rr>=ratio_tan[50] && rr <ratio_tan[75])
		{
		if ( rr <ratio_tan[62])
			{
			for (int i=51; i<=62; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		if ( rr>=ratio_tan[62])
			{
			for (int i=63; i<=75; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		} // 50.. 75

	if ( rr>=ratio_tan[75])
		{
		if ( rr <ratio_tan[87])
			{
			for (int i=76; i<=87; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		if ( rr>=ratio_tan[87])
			{
			for (int i=88; i<=99; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		} // 75..99

	// let user know that atan failes
	return 0;
	//User::PanicUnexpectedLeave();
	}

inline static float getATan(float dy, float dx)
	{

	float rr, sign_res = -1;

	// debug

#ifdef USE_MATH_ATAN
	Math::ATan(rr, dy, dx);
	// shorten domain to -PI/2..PI/2
	while (rr>PI/2)
		rr = rr-PI;
	while (rr<-PI/2)
		rr = rr+PI;
	return(rr);
#endif

	// ----------------------------------------
	if ( (dy>0 && dx>0) || (dy<0 && dx<0))
		{
		sign_res = 1;
		}

	if (dy==0)
		return (0);

	if (dx==0)
		return (PI_HALF*SIGN(dy));

	rr = Abs ((float)dy/(float)dx); // bug, integer division

	// extreme values
	if ( rr<=ratio_tan[0])
		return (val[0]*sign_res);

	if ( rr>=ratio_tan[ntab-1])
		return (val[ntab-1]*sign_res);

	// tree search with granularity 12
	if ( rr<ratio_tan[25])
		{
		if ( rr <ratio_tan[12])
			{
			for (int i=1; i<=12; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		if ( rr>=ratio_tan[12])
			{
			for (int i=13; i<=25; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		} // 1..24

	if ( rr>=ratio_tan[25] && rr <ratio_tan[50])
		{
		if ( rr <ratio_tan[37])
			{
			for (int i=26; i<=37; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		if ( rr>=ratio_tan[37])
			{
			for (int i=38; i<=50; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		} // 25..49

	if ( rr>=ratio_tan[50] && rr <ratio_tan[75])
		{
		if ( rr <ratio_tan[62])
			{
			for (int i=51; i<=62; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		if ( rr>=ratio_tan[62])
			{
			for (int i=63; i<=75; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		} // 50.. 75

	if ( rr>=ratio_tan[75])
		{
		if ( rr <ratio_tan[87])
			{
			for (int i=76; i<=87; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		if ( rr>=ratio_tan[87])
			{
			for (int i=88; i<=99; i++)
				{
				if ( rr<=ratio_tan[i] && rr>ratio_tan[i-1])
					return (val[i]*sign_res);
				}
			}
		} // 75..99

	return 0;

	}

#endif

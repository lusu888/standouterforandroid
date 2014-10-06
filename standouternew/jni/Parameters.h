#ifndef PARAMETERS_H
#define PARAMETERS_H

#define START_FILE   0  // program starts with this file

// ======== Parameters for cell phone AND desktop (C++/Symbian)

// macroses
#define ABS(a)			((a)>0?(a):(-(a)))
#define MEAN(a, b)      ( 0.5f * ((a)+(b)) )
#define SIGN(a)			((a)<0?(-1):(1))
#define MIN(a,b)		(((a) < (b)) ? (a) : (b))
#define MAX(a,b) 		(((a) > (b)) ? (a) : (b))
#define MAX3(a,b,c) 	(MAX(MAX(a, b), c))
#define MIN3(a,b,c) 	(MIN(MIN(a, b), c))
#define SQR(a)          ((a)*(a))
#define CLIP0to255(a)   ((a)<0?0:((a)>255?255:(a)))
#define INDMAX3(a,b,c)  ((a>=b && a>=c)?0:(b>c?1:2))// returns an index of the max: 0, 1, or 2
#define ANG_DIF(abs_dif) (abs_dif<=PI_HALF?abs_dif:(PI-abs_dif))
#define RAD_THRESHOLD(npxl) ((npxl)<LONG_LINE?RADCHANGE:((npxl)<2*LONG_LINE?0.5*RADCHANGE:0.25*RADCHANGE))) // 3 diff. thresholds
#define INBOX(x, y, x1, x2, y1, y2)         (((x)>(x1)) && ((x)<(x2)) && ((y)>(y1)) && ((y)<(y2))) // 4 constraints
#define MIDLINE_LEN			200 // LENGTH OF THE MIDLINE FROM THE CENTER OF THE SCREEN

// image size
#define LANDSCAPE_ORIENTATION

#ifdef LANDSCAPE_ORIENTATION
#define IMAGE_W	320 // 480 320
#define IMAGE_H	240 // 360 240
#else
#define IMAGE_W	240
#define IMAGE_H	320
#endif

#define IMAGE_SZ (IMAGE_H*IMAGE_W)
#define FOCAL_LENGTH 313.0f //433; old: 307- 640x480 resolution (in terms of 320x240 resolution after downsampling); 381 - in 320x240 resolution;
#define ONE_OVER_FOCAL_LENGTH      (1.0f/ (float)FOCAL_LENGTH )
#define CAMERA_HEIGHT 		1.6f

// math constants
#define PI		(3.14159f)
#define PI_HALF	(PI/2.0f)
#define DEG2RAD (PI/180.0f)
#define RAD2DEG (180.0f/PI)
#define NBINS_GENERAL_HISTOGRAM 10// general histogramming
// tan tabulated
#define ntab         100      // number of tabulations
#define ratio0       0.01f     // initial smallest value
#define MULT_RATIO   1.1f      // multiplier for a ratio

// =============   important parameters

// edge and lines
#define THRESHOLD_EDGE  	15	// 15 initial threshold for nonmax. supression = thresh_edgemap_adjusted
#define THRESHOLD1_COEF	    1.2f // threshold to grow (1.5 times threshold to start the line)
#define SCL_GLOBAL_THRESH	(10.0f/256.0f) // ads 5 in case of 256/2 intensity
//#define SCL_LOCAL_THRESH	(10.0/256.0) // ads 5 in case of 255/2 intensity
#define MAX_LINES		    2000
#define	RADCHANGE           (PI/20.0f)      // (PI_HALF/30) radian CHANGE TO be linear

#define MIN_LINES		  	10   // (Zebra)
#define MIN_LINES2		  	4    // (2stripe)
#define MIN_LINE_SUM_LENGTH 10.0f // meters (Zebra)
#define MIN_LINE_SUM_LENGTH2 5.0f // meters (2stripe)

#define ALLOW_JUMP1PIX_LINE_EXTRACTION (true) // crashes if false
#define	MIN_LEN_ANG       5            // 8 min length to calculate radians
#define	MIN_LINE_REGISTER  MIN_LEN_ANG // min #pixel to grow the line
#define LONG_LINE		  20 // in npxl angular THERSHOLD ARE CUT IN HALF

//#define RGB_STEP          3   // how long do we step to sample rgb

// gap
//#define MERGE_GAP_MAX	  5          // max gap along one dimension
//#define	MERGE_LINE_COLLINEARITY_TRESH		(4*DEG2RAD) //  3.0 collinearity of 2 lines, reduced for long ones?
//#define	MERGE_LINE_ITERATIONS    2     // maximal number of merging iterations
//#define COLOR_SAMPLES				5 // NUMBER OF COLOR SAMPLES IN GET_RGB AD GET_POLARITIES

// white threshold parameters
#define PERCENTILE_WHITE_COLOR		0.9f // used in percentile255()
#define PERCENTILE_WHITE_COLOR2		0.95f // used in percentile255()
#define SLACK_WHITE					0.9f // deviation from max white allowed
#define SLACK_WHITE2				0.95f // deviation from max white allowed
//#define NUMBER_ZONES				32  // NUMBER OF ZONES ALONG X
//#define POINTS_ZONE_X   			(IMAGE_W/NUMBER_ZONES) // length of horizontal line to calculate local threshold
//#define HIGH_WHITE                  230 // HIGH VALUE FOR DEFAULT THRESHOLD
#define WHITE_BITMAP_COL			((unsigned char)250) // color in a white bitmap to mark pixels that passed "white" test
//#define DOWN_FROM_MEAN_BETWEEN_PEAKS 0.7 // multiplies the mean (white) between peaks to get a fine threshold value

// dominant orientation function...
#define NUM_WINDOW_BINS    30        // Gaussian window made of descrete bins (e.g. to be applied to rad.)
#define NUM_STEPS		  100 // can be tricky due to overlap and cyclic ang. axis
#define SLIDING_STEP  	  (PI/(float)NUM_STEPS) // how much window advances each iteration
#define WINDOW_BIN_WIDTH          (WINDOW_WIDTH_RAD / NUM_WINDOW_BINS) // width of window bin
#define MIN_LINE_LEN_DOMIN_ORIENT (2*MIN_LINE_REGISTER) // min. npix for line to be taken into account for domin. orientation

// Ground
#define SCALE_GRND					20
#define min_fwd						10 //??
#define LARGE_NEG_NUMBER      (-10000)
#define MAX_LINE_REGISTER_3D  (20.0f) // in meters

// reasoning
//#define ANGLE_OF_CW_TYPE	  30
#define MAX_ANG_RANGE_CROSSWALKLINES (PI/10.0f) // 18deg seems to be optimal, max. range of angles in CW
#define MAX_LINE_ANG_DIFF_3D_SLIDING_WINDOW   (PI/20.0f) // max diff for line pairs to be counted in the gaussian window
#define MAX_LINE_ANG_DIFF_3D  (PI/20.0f) // max diff for line pairs (domin line)
#define WINDOW_WIDTH_RAD      (2.0f*MAX_LINE_ANG_DIFF_3D) //
#define STD_GAUSS             (WINDOW_WIDTH_RAD/3.0f)   // std of a gaussian window, dont use full range of 6std!
#define MAX_3D_DIST            20 // max mean z-distance in m. from the camera
//#define CW_MAX_WIDTH		   10
#define CW_MIN_WIDTH		   3.0f
#define CW_MAX_LENGTH		  (MAX_3D_DIST) // maximum length of the crosswalk
//#define MIN_CW_H			   4
#define MIN_ZEBRA_WIDTH 	   2

#define MIN_SCORE_STRIPE        1700 // 200 sum of lines lengths in pix. (within MAX_LINE_ANG_DIFF from a dominant orientation)
#define WHITE_PEAK_R_METERS 	(STRIPE_WIDTH/2) // Radius only - makes the whole stripe width twice this Radius
#define WHITE_PEAK_R_BINS		((int)(WHITE_PEAK_R_METERS / BIN_STEP_DISTANCE+.5))  // DISTANCE BETWEEN PEKS IN BINS
#define MIN_POWER				(MAX_BIN_COUNT_DOMIN/2) // min power of the peak
#define MIN_POWER_RATIO_2STRIPE	0.2f // the ratio (from max) for the peak to be valid
#define PEAK_POWER_VALID	    0.05f
//#define SCORE_THRESHOLD		500 // score to trigger feedback

#define CW_Nw  20			// CW deformable template
#define CW_Ndx 10
#define MIN_STRIPE_WIDTH	0.3f
#define MAX_STRIPE_WIDTH    1.2f
#define MEAN_STRIPE_WIDTH   ((MIN_STRIPE_WIDTH + MAX_STRIPE_WIDTH)/2.0f)
#define MIN_STRIPE_WIDTH2	0.3f
#define MAX_STRIPE_WIDTH2   0.8f
#define MEAN_STRIPE_WIDTH2  ((MIN_STRIPE_WIDTH2 + MAX_STRIPE_WIDTH2)/2.0f)

#define WIDTH_STEP			((float)((MAX_STRIPE_WIDTH - MIN_STRIPE_WIDTH)/(float)CW_Nw)) // not reaching max...
#define DX_STEP				((2*(double)MAX_STRIPE_WIDTH) / (double)CW_Ndx) // dx cahnges from 0..MAX_STRIPE_WIDTH
//#define MIN_VOTES			(10*1000*1000) // 20 mil->10mil
//#define SECOND_WINNER_PERC  0.7
#define PERC_MAX_POWER_MAX	0.7f  // defines the percentage of max that is still considered as a part of stripe for finding max (should be noise dependent?)
#define PERC_MAX_POWER_ENG	0.5f  // defines the percentage of max that is still considered as a part of stripe for Energy
#define MIN_SKIPS			4    // min number of points with PERC_MAX_POWER_MAX
#define MAX_PEAKS			20   // max number of detected peaks in
#define COST_OF_ABSENT_PEAK     1.0f
#define COST_OF_ABSENT_TROUGH	3.0f // ther is no missing paint inbetween stripes!
#define COST_OF_DIFF_WIDTH      2.0f // 1 bin difference is not as crucial as missing paint?
#define THRESHOLD_COST			15.0f //(3*(COST_OF_ABSENT_PEAK + COST_OF_ABSENT_TROUGH + COST_OF_DIFF_WIDTH) )
#define STAIRS_THRESHOLD_LENGTH2PEKAS   5

// histogramming of the white matter
#define NBINS				   240// DISTANCE histogramming (good for plotting at 320x240 resolution)
#define BIN_STEP_DISTANCE 	   (0.5f*(float)CW_MAX_LENGTH/(float)NBINS) // 5 cm per bin ------------------------------------
#define ONE_OVER_BIN_STEP_DISTANCE (1.0f/BIN_STEP_DISTANCE)
#define BIN_PER_METER		   (1.0f / BIN_STEP_DISTANCE)
#define BIN_DISTANCE_FIRST 	   (-CW_MAX_LENGTH/2)
#define BIN_DISTANCE_LAST 	   (CW_MAX_LENGTH/2)
#define MAX_BIN_COUNT_DOMIN		1000000  // prevents bin sturationf
#define MAX_BIN_COUNT_ORTHO		1000000  // prevents bin sturation
#define STRIPE_WIDTH			0.7f
#define STRIPE_W_TOLER1	        0.8f  // tolerance for only first width
#define STRIPE_W_TOLER2	        1.5f  // tolerance for only first width
#define	STRIPE_W_DEVIAT			0.3f  // deviation from the stripe width
#define MIN_PEAKS				5 // min number of peaks

#define GREY_COLOR	100	  // CLEARS BITMAP
//#define USE_MATH_ATAN
#define R_FILT			2

// ===============================   D E S K T O P specific (C++ specific)

#define DIRECTORY "images\\April2010\\ppm\\"

//#define SAVE_BITMAPS // takes time but saves all 8 bitmaps and lines
//#define SAVE_LINES_ONLY // extract&saves lines and stops any further processing

#define MIN_FILE     0  //
#define MAX_FILE     79 // some files are missing
// screen size
#define SCR_W	1920 // 1920 = 640*3
#define SCR_H   1200 // = 1200 = 480*2.5
// specific types to make transition easier
/*
typedef double TReal;
typedef signed int TInt;
typedef unsigned int TUint;
typedef unsigned char TUint8;
typedef unsigned short int TUint16;
typedef int TBool;
#define ETrue true
*/
#define Abs(a)		(ABS(a)) // for compatability

#define MYDEBUG      1   // causes program to print some debug info
#define FLIKER       1   // flickers some images

static int temp_ar[IMAGE_W*IMAGE_H];
#endif




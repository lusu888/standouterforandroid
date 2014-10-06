#ifndef __LINES__
#define __LINES__


#include "Parameters.h"
#include "edgelet.h"
#include "TPointR.h"

// from ground.h
float normal[3] = {0.0, -0.9, -0.45}, one_over_normal[3];
float normals[][3] = {{0.003297, -0.930360, -0.366633}, // 0-93 (94 files)
		{-0.004091, -0.941024, -0.338315},{0.038327, -0.927901, -0.370851},{0.006660, -0.919222, -0.393682},{0.030788, -0.894995, -0.445013},{0.060295, -0.883648, -0.464253},// 1-5
		{0.044064, -0.936317, -0.348381},{0.031610, -0.945839, -0.323094},{-0.003358, -0.922888, -0.385053},{0.058168, -0.926572, -0.371591},{0.012309, -0.849478, -0.527481}, // 6-10
		{-0.005670, -0.940802, -0.338909},{-0.004488, -0.876337, -0.481677},{-0.030547, -0.941354, -0.336034},{0.023770, -0.895156, -0.445119},{0.018839, -0.917510, -0.397266}, // 11-15
		{0.050926, -0.944703, -0.323950},{0.022951, -0.941249, -0.336932},{-0.032997, -0.847836, -0.529231},{-0.004458, -0.899940, -0.435991},{0.060251, -0.927349, -0.369315}, // 16-20
		{0.054481, -0.921557, -0.384402},{0.008981, -0.850574, -0.525779},{0.004840, -0.954377, -0.298566},{0.080478, -0.931254, -0.355371},{-0.015146, -0.872681, -0.488055}, // 21-25
		{-0.012535, -0.876601, -0.481055},{-0.007969, -0.870829, -0.491521},{0.051006, -0.914018, -0.402454},{-0.003036, -0.876724, -0.480985},{0.018657, -0.885186, -0.464863}, // 26-30
		{0.011776, -0.833796, -0.551947},{-0.029401, -0.871233, -0.489988},{-0.036697, -0.851489, -0.523087},{0.014173, -0.874838, -0.484209},{0.007862, -0.863397, -0.504464}, // 31-35
		{-0.043198, -0.854310, -0.517966},{-0.029687, -0.875561, -0.482195},{0.016288, -0.889738, -0.456181},{-0.022175, -0.836238, -0.547919},{0.001459, -0.862529, -0.506005}, // 36-40
		{-0.013290, -0.867039, -0.498064},{0.065369, -0.953916, -0.292867},{-0.080562, -0.842284, -0.532979},{-0.020848, -0.860851, -0.508430},{-0.025684, -0.831538, -0.554874}, // 41-45
		{0.141412, -0.923281, -0.357148},{0.018862, -0.837244, -0.546503},{-0.092961, -0.900190, -0.425461},{-0.002791, -0.809315, -0.587368},{0.058989, -0.854846, -0.515519}, // 46-50
		{0.027019, -0.919491, -0.392181},{-0.082230, -0.938883, -0.334271},{0.045408, -0.844053, -0.534334},{0.024623, -0.893190, -0.449004},{-0.014982, -0.936313, -0.350846}, // 51-55
		{-0.009043, -0.847766, -0.530293},{0.007759, -0.860074, -0.510111},{0.006108, -0.840136, -0.542341},{0.032868, -0.893154, -0.448548},{-0.006993, -0.927258, -0.374358}, // 56-60
		{0.008393, -0.809293, -0.587345},{-0.002485, -0.847606, -0.530620},{0.039905, -0.919776, -0.390410},{0.072934, -0.894645, -0.440786},{0.075683, -0.916403, -0.393038}, // 61-65
		{0.056584, -0.922612, -0.381557},{0.037335, -0.979495, -0.197981},{0.078775, -0.872546, -0.482138},{0.058190, -0.933215, -0.354575},{0.027876, -0.929525, -0.367703}, // 66-70
		{0.012748, -0.865982, -0.499912},{0.110411, -0.918091, -0.380680},{0.040665, -0.939289, -0.340709},{0.017887, -0.956326, -0.291754},{-0.078432, -0.920708, -0.382290}, // 71-75
		{0.042233, -0.889180, -0.455605},{-0.010313, -0.940008, -0.340998},{0.0 -0.9 -0.3}, {0.020235, -0.949314, -0.313677}, {-0.010363, -0.901918, -0.431784}, // 76-80
		{-0.049916, -0.860930, -0.506269},{-0.001813, -0.876263, -0.481829},{-0.010755, -0.874433, -0.485027},{-0.024790, -0.884031, -0.466770},{-0.026911, -0.874944, -0.483476}, // 81-85
		{0.031646, -0.851460, -0.523464},{-0.002963, -0.853596, -0.520928},{0.018027, -0.893665, -0.448372},{-0.009646, -0.853176, -0.521534},{0.023930, -0.855115, -0.517886}, // 86-90
		{-0.026216, -0.844333, -0.535177},{-0.015716, -0.855995, -0.516744},{0.003262, -0.854201, -0.519933} // 91-93
}; // 93 (problem with #78)


float camera_height;
float side_vec[3], forward_vec[3];
float P0[3], P0side, P0frwd;
float norm2_by_f;
int fnum;

enum ECrosswalkResults2stripes
{
	ESingleStripeVisible = 0,
	EBothStripesVisible,
	ENotDefined,
} crosswalk_result;

enum ECrosswalkResultsReason // 5 reasons for giving up...
{
	ENoDOminantLine=0,
	ELessThan2Lines,
	ENoMaxD,
	EStripeWidth,
	EFirstFound,
	ESecondFound,
	EWrongDist,
	ENoCandidates,
	EBothStripes
} cw_reason;


int nlines;
int crosswalk_lines;
TEdgelet LineArray[MAX_LINES];
bool Zebra_cw;

// Dominant orientation variables
float Gauss_weight[NUM_WINDOW_BINS];
float cluster_score[NUM_STEPS];
bool local_max_found;
float max_score;
float dominant_orient;
TEdgelet domin_line;        // a line that goes through mid screen and has domin. orientation
float domin_line_normal[2]; // used in calculating distance;

// white threshold
int col_ar[IMAGE_W];  // column array: values in the current column
int thresh_col[IMAGE_W];// 90% percentile in each row

int bin_cnts255[256];

// detect CW
float minE;
struct TRunningAverage
{
	// sizes can change in the function DetectCrosswalk()
	TPointR CWcenterGrnd; // center of the crosswalk on the ground (unscaled) = median of valid line centers;
	float CWcenter_domin,
		CWcenter_ortho;   // projection of the center on the domin and normal to domin. orientation

	float CWradius[2];    // median distances along domin and ortho; also (domin left, ortho far, somin right, ortho close)

	// boxes
	TPoint upper_left, upper_right, lower_left, lower_right ;     // 2D bounding box
	TPointR upper_leftG, upper_rightG, lower_leftG, lower_rightG; // Grnd bounding box
	float start_side;

	// reasoning
	float stripe_width;
	float stripe_shift;
	int detected; // 0 - no CW; 1 - Zebra; 2 - TwoStripe;

	float cw_width;
	float orient;
	//float normal[3];
} CWrunav;

// up to 100 peaks
struct TCWPeaks
{
	int count; 			  // number of extremes
	unsigned char ind_extreme[NBINS];     // extreme indices (NBINS = 240) // was char
	float power_extreme[NBINS]; // power of each extreme
	float width_meters[NBINS];  // width of the peak
	int valid_peaks;
	int invalid_peaks;
} CWPeaks;

float bestR;



signed long long max_vote, min_vote; // delete!!!!!!!!










// Analysis of the histograms
float noise; // noise in the histogram
//float score;
float votes_ratio; // how much max_vote larger than an expected average one
float av_energy_dw; //debug

// used for sorting
float sum_len;
float line_feature1[MAX_LINES]; // for median distance
float line_feature2[MAX_LINES]; // for median distance
float line_feature3[MAX_LINES]; // weighted median
int order_index[MAX_LINES];
float closest_line;
TPointR Rright, Rup;
TPointR vec_stripe_width;
int im_lines[IMAGE_H][IMAGE_W];
bool line_verify;

long long bin_count_domin[NBINS], bin_count_ortho[NBINS], bin_count[NBINS]; // bins for counting distances are real due to the weighting
long long bin_cnt_max_domin, bin_cnt_max_ortho;      // max bin count
signed long long CWvotes[CW_Nw][CW_Ndx]; // votes for CW model

bool conv_hist;
// boundaries of the ortho histogram
int left_ortho, right_ortho;
float stripe_xx[MAX_PEAKS][2]; // at each peak we define x1x2 - through on the left, x2x3 - peak, x3x4 through on the right
int stripe_width_in_bins;

#endif



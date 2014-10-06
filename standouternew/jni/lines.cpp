/* lines.cpp is not implemented as a class and keeps lines.h for
 * data purposes only.
 *
 */
//
#include <math.h>
#include <string.h>
#include <jni.h>

#include "lines.h"
#include "edgelet.h"
#include "Parameters.h"
#include "tan_tabulated.h"
#include "TPointR.h"
#include "SortingPercentile.h"

#ifndef _DESKTOP_
#define JNIEXPORT
#define JNICALL
#endif

#ifdef __cplusplus
extern "C" {
#endif



void histogram255(int vect[], int n) // result is in bin_cnts255
{
	memset(bin_cnts255, 0, sizeof(int)*256);
	for (int i=0; i<n; i++)
		bin_cnts255[vect[i]]++;
}


/* finds an index with prcnt counting from the top
*  so the value can be overestiamted...
*/
int percentile255(int vect[], int n, float prcnt)
{
	int res = 0;
	long cum_sum = 0, cumT = n * (1.0 - prcnt); // 24

	// make a histogram
	histogram255(vect, n);

	// cumulative sum of bins till reach prcnt
	for (int i=255; i>-1; i--)
	{
		cum_sum += bin_cnts255[i];
		if (cum_sum >= cumT)
		{
			res = i; // for this case index is a value
			break;
		}
	}

	return (res);

} // percentile255()


void ScaleGrndInt(float fd, float sd, int &col, int &row)
{
	col = sd * SCALE_GRND + IMAGE_W/2; // zero is in the mid-screen (xshift = IMAGE_W/2)
	row = IMAGE_H - 10 - fd * SCALE_GRND; // inversion of axis since fwd=-row
}

void ScaleGrnd(float fd, float sd, float &col, float &row)
{
	col = sd * SCALE_GRND + IMAGE_W/2; // zero is in the mid-screen (xshift = IMAGE_W/2)
	row = IMAGE_H - 10 - fd * SCALE_GRND; // inversion of axis since fwd=-row
}

void ScaleGrndPoint(TPointR sd_fd, TPointR &col_row)
{
	col_row.iX = (float)(sd_fd.iX * SCALE_GRND + IMAGE_W/2); // zero is in the midscreen (xshift = IMAGE_W/2)
	col_row.iY = (float)(IMAGE_H - 10 - sd_fd.iY * SCALE_GRND); // inversion of axis since fwd=-row
}


// also initializes normals according to the file number
void CreateGround()
{


	// initializes normal, side_vec and forward_vec of iAppView object
	float temp;

	if (Abs(normal[1]) < 0.001f)
	{
		normal[1] = -0.001f;// typical value is negative
	}

	if (Abs(normal[2]) < 0.001f)
	{
		normal[2] = -0.001f; // typical value is negative
	}

	// speed up
	one_over_normal[0] = 1.0f / normal[0];
	one_over_normal[1] = 1.0f / normal[1];
	one_over_normal[2] = 1.0f / normal[2];

	//1. Ground coordinate system
	side_vec[0] = 1 - normal[0] * normal[0]; // subtruct from PGR x_axis their projection on normal
	side_vec[1] = 0 - normal[0] * normal[1]; // among others gurantee forward[0]=0.0
	side_vec[2] = 0 - normal[0] * normal[2];
	temp = (float)sqrt(SQR(side_vec[0])+SQR(side_vec[1])+SQR(side_vec[2]) ); // normalize
	if (Abs(temp) < 0.0001)
	{
		temp = 0.0001;
	}
	side_vec[0] /= temp;
	side_vec[1] /= temp;
	side_vec[2] /= temp;

	// in the right coordinate system the side vector would point to the right
	// for the reference see http://en.wikipedia.org/wiki/Cross_product
	forward_vec[0] = normal[1] * side_vec[2] - normal[2] * side_vec[1]; // screwed!!!
	forward_vec[1] = normal[2] * side_vec[0] - normal[0] * side_vec[2];
	forward_vec[2] = normal[0] * side_vec[1] - normal[1] * side_vec[0];
	temp = (float)sqrt(SQR(forward_vec[0])+SQR(forward_vec[1])+SQR(forward_vec[2]) );
	if (Abs(temp)<0.0001)
	{
		temp = 0.0001;
	}
	forward_vec[0] /= temp;
	forward_vec[1] /= temp;
	forward_vec[2] /= temp;

	// point on a ground plane (where user's feet are)
	P0[0] = -normal[0]*camera_height;
	P0[1] = -normal[1]*camera_height;
	P0[2] = -normal[2]*camera_height;

	// often used in calculations
	P0side = P0[0]*side_vec[0] + P0[1]*side_vec[1] + P0[2]*side_vec[2];
	P0frwd = P0[0]*forward_vec[0] + P0[1]*forward_vec[1] + P0[2]*forward_vec[2];
	norm2_by_f = normal[2]*FOCAL_LENGTH;
}

void Ground2xyz(float xyz[3], float f, float s, float nr)
{
	// translates ground into camera coordiante system
	xyz[0] = f*forward_vec[0] + s*side_vec[0] + nr*normal[0] + P0[0];
	xyz[1] = f*forward_vec[1] + s*side_vec[1] + nr*normal[1] + P0[1];
	xyz[2] = f*forward_vec[2] + s*side_vec[2] + nr*normal[2] + P0[2];
}

void xyz2uv(float uv[2], float x, float y, float z)
{
	// translates camera coordinate system into the im. coord. system
	if (z==0)
	{
		z = 0.0001;
	}
	if (z<0)
	{
		uv[0] = -x * FOCAL_LENGTH / z; // u
		uv[1] = -y * FOCAL_LENGTH / z; // v
	}
	else
	{
		uv[0] = x * FOCAL_LENGTH / z; // u
		uv[1] = y * FOCAL_LENGTH / z; // v
	}
}


inline bool uv2xyz(int u, int v, float &x, float &y, float &z)
{
	float disp = (float)(normal[0]*u + normal[1]*v + normal[2]*FOCAL_LENGTH); // it is zero at the horizon
	z = (-CAMERA_HEIGHT*FOCAL_LENGTH) / disp;
	x = (float)u*(z*ONE_OVER_FOCAL_LENGTH);
	y = (float)v * (z * ONE_OVER_FOCAL_LENGTH);
	if (!finite(x) || isnan(x) || !finite(y)
		|| isnan(y))
		return false;
	else
		return true;
}

inline void xyz2Grnd(float x, float y, float z, float &fd, float &sd)
{
	fd = (x-P0[0])*forward_vec[0] + (y-P0[1])*forward_vec[1] + (z-P0[2])*forward_vec[2];
	sd = (x-P0[0])*side_vec[0] + (y-P0[1])*side_vec[1] + (z-P0[2])*side_vec[2];
}

inline void Grnd2xyz(float &x, float &y, float &z, float fd, float sd)
{
	x = fd*forward_vec[0]+sd*side_vec[0]+P0[0];
	y = fd*forward_vec[1]+sd*side_vec[1]+P0[1];
	z = fd*forward_vec[2]+sd*side_vec[2]+P0[2];
}

// init (passes some parameters from Java and also plays a role of the constructor)
JNIEXPORT void JNICALL
Java_cameraStream_CppFunctionsJNI_initialize
(JNIEnv *env, jclass jc, jint file_number) {

	fnum = file_number; // lines.h includes ground.h

	normal[0] = normals[fnum][0];
	normal[1] = normals[fnum][1];
	normal[2] = normals[fnum][2];

	//sum_len = 0;
	camera_height = CAMERA_HEIGHT;

	line_verify = true;
	Zebra_cw = false; // more common cw is TwoStripe

	nlines = 0;
	crosswalk_lines = 0;

	// pre-generate coefficients for a Gaussian window
	float Z_GAUSS = (float)sqrt(2.0f*PI*SQR(STD_GAUSS)); // normalizing constant
	for (int i=0; i<NUM_WINDOW_BINS; i++) // for all window bins
	{
		float x =(float)(-WINDOW_WIDTH_RAD/2.0f+i*WINDOW_BIN_WIDTH);
		Gauss_weight[i]= (float)exp( -SQR(x)/(2.0f*SQR(STD_GAUSS)));
		Gauss_weight[i] /= Z_GAUSS;
	}


	local_max_found = false;
	crosswalk_result = ENotDefined;
	cw_reason = ENoDOminantLine;
	max_score = 0;
	conv_hist = true;


} //initialize

JNIEXPORT void JNICALL
Java_cameraStream_CppFunctionsJNI_initializeCam
(JNIEnv *env, jclass jc, jint file_number) {

	fnum = file_number; // lines.h includes ground.h

	normal[0] = normals[fnum][0];
	normal[1] = normals[fnum][1];
	normal[2] = normals[fnum][2];

	//sum_len = 0;
	camera_height = CAMERA_HEIGHT;

	line_verify = true;
	Zebra_cw = false; // more common cw is TwoStripe

	nlines = 0;
	crosswalk_lines = 0;

	// pre-generate coefficients for a Gaussian window
	float Z_GAUSS = (float)sqrt(2.0f*PI*SQR(STD_GAUSS)); // normalizing constant
	for (int i=0; i<NUM_WINDOW_BINS; i++) // for all window bins
	{
		float x =(float)(-WINDOW_WIDTH_RAD/2.0f+i*WINDOW_BIN_WIDTH);
		Gauss_weight[i]= (float)exp( -SQR(x)/(2.0f*SQR(STD_GAUSS)));
		Gauss_weight[i] /= Z_GAUSS;
	}


	local_max_found = false;
	crosswalk_result = ENotDefined;
	cw_reason = ENoDOminantLine;
	max_score = 0;
	conv_hist = true;


} //initialize


// getLines() sets up Zebra_cw variable
JNIEXPORT int JNICALL
Java_cameraStream_CppFunctionsJNI_getLines
(JNIEnv *env, jclass jc, jboolean Zebra_cw_flag, jintArray gradIn,
jintArray signIn, jintArray sign2In) {

	// Java to C++ arrays
	jboolean isCopy;
	jint *im_edge = env->GetIntArrayElements(gradIn, 0);
	jint *im_edge_sign = env->GetIntArrayElements(signIn, 0);
	jint *im_edge_sign2 = env->GetIntArrayElements(sign2In, 0);

	// initialize
	nlines = 0;
	Zebra_cw = Zebra_cw_flag;

	// indices
	int x, y, offset;
	//TEdgelet edgelet;
	int temp;

	// edgelets are extracted column-wise, so it is faster to search for 
	// gap elimination; column-wise in Landscape orientation means
	// row-wise starting with the largest y. Border is 4 pixels around.

	int v, u;
	int xhor, yhor;
	int xmin, xmax, ymin, ymax;
	float tempf = normal[2]*FOCAL_LENGTH;
	float tempf2 = tempf+CAMERA_HEIGHT*FOCAL_LENGTH/MAX_3D_DIST; // used in a stronger constraint
	int tresh = THRESHOLD_EDGE;

	// ======================   roughly horizontal lines (always extracted regardless of cw type)

	memcpy (temp_ar, im_edge, sizeof(int)*IMAGE_SZ); // copy since it will be destroyed

	// going top->down starting from the left
	for (x=R_FILT+2; x<IMAGE_W-(R_FILT+2); x++)
	{
		// find y range for the y given tilted horizon line
		u = x-IMAGE_W/2;
		if (Abs(normal[1])<0.001)
		{
			normal[0] = 0.001;
		}

		// using MAX_3D_DIST (rather than horizon) is a stronger constraint
		yhor = (int)((-tempf2 - normal[0]*u)/normal[1] + IMAGE_H/2);

		ymin = yhor;
		ymax = IMAGE_H-(R_FILT+2); 
		if (ymin<R_FILT+2)
			ymin = MIN(R_FILT+2, ymax);
		if (ymax>IMAGE_H-(R_FILT+2))
			ymax = MAX(IMAGE_H-(R_FILT+2), ymin);
		if (ymax<R_FILT+2)
			ymax = R_FILT+2; // bug, was MAX(0, ymax) !!
		if (ymin>IMAGE_H-(R_FILT+2))
			ymin = MIN(IMAGE_H-(R_FILT+2), ymax);

		// threshold adjusted by key input
		//int thr = tresh;

		// adjust for global column threshold 2x240
		//thr += thresh_col[x]*SCL_GLOBAL_THRESH; // ads 0.1 in case 255


		for (y=ymin; y<ymax; y++)
		{	
			// address
			int adr = y*IMAGE_W+x;
			temp = temp_ar[adr];

			// over threshold?
			if ( temp > tresh*THRESHOLD1_COEF) // make it hard to start the line
			{
				LineArray[nlines].grad = temp;

				if (Zebra_cw)
				{
					// use vertical gradient for Zebra

					LineArray[nlines].polarity = im_edge_sign[adr]>128; // positive if top is whiter then bottom
					// init and grow a line (higher threshold for this...)
					if ( LineArray[nlines].initAndGrow_hor (TPoint (x, y),
							(int*)temp_ar, (int*)im_edge_sign, tresh ) ) // make it easy to continue
					{
						if ( nlines<MAX_LINES-1) 
							nlines++;
					}
				}
				else
					{
					// use horizontal gradient for 2Stripe

					LineArray[nlines].polarity = im_edge_sign2[adr]>128; // positive if top is whiter then bottom
					// init and grow a line (higher threshold for this...)
					if ( LineArray[nlines].initAndGrow_hor (TPoint (x, y),
							(int*)temp_ar, (int*)im_edge_sign2, tresh) ) // make it easy to continue
					{
						if ( nlines<MAX_LINES-1) 
							nlines++;
					}
				}

			} // if over thresh?
		} // loop
	} //  roughly horizontal lines 

	// Standardize heads and tails so head is geometrically on the top
	// (should not affect anything but gap_closing!)
	if (Zebra_cw)
	{

		for (int n=0; n<nlines; n++)
		{

			if (LineArray[n].head.iX < LineArray[n].tail.iX) // never happens due to the mechanim of line growing
			{
				// swap head and tail coordinates iY, iX
				temp = LineArray[n].head.iX;
				LineArray[n].head.iX = LineArray[n].tail.iX;
				LineArray[n].tail.iX = temp;
				temp = LineArray[n].head.iY;
				LineArray[n].head.iY = LineArray[n].tail.iY;
				LineArray[n].tail.iY = temp;
			}

			LineArray[n].length = LineArray[n].len();
			if (LineArray[n].length > LONG_LINE / 2)
			{
				LineArray[n].valid = true;
				LineArray[n].dx = (LineArray[n].head.iX - LineArray[n].tail.iX)
					/ LineArray[n].length;
				LineArray[n].dy = (LineArray[n].head.iY - LineArray[n].tail.iY)
					/ LineArray[n].length;
			}
			else
			{
				LineArray[n].valid = false;
			}
		} // nlines
	}
	else
	{
		// Standardize heads and tails so head is geometrically on the top
		// (should not affect anything but gap_closing!)
		for (int n=0; n<nlines; n++)
		{
			if (LineArray[n].head.iY > LineArray[n].tail.iY)
			{
				// swap head and tail coordinates iY, iX
				temp = LineArray[n].head.iX;
				LineArray[n].head.iX = LineArray[n].tail.iX;
				LineArray[n].tail.iX = temp;

				temp = LineArray[n].head.iY;
				LineArray[n].head.iY = LineArray[n].tail.iY;
				LineArray[n].tail.iY = temp;

			}
		
			LineArray[n].length = LineArray[n].len();
			if (LineArray[n].length > LONG_LINE / 2)
			{
				LineArray[n].valid = true;
				LineArray[n].dx = (LineArray[n].head.iX - LineArray[n].tail.iX)
					/ LineArray[n].length;
				LineArray[n].dy = (LineArray[n].head.iY - LineArray[n].tail.iY)
					/ LineArray[n].length;
			}
			else
			{
				LineArray[n].valid = false;
			}
		
		} // nlines
	}

	if (!Zebra_cw)
	{ 		
		int nlines_hor = nlines; // Lines that are horizontal and are extracted first
		memcpy (temp_ar, im_edge, sizeof(int)*IMAGE_SZ); // see CopyBitmap() in Symbian

		// going left->right starting from the left-top
		for (y=IMAGE_H-(R_FILT+2)-1; y>=R_FILT+2; y--)
			{
			// find x range for the y given tilted horizon line
			offset=y*IMAGE_W;
			
			// find x range for the y given tilted horizon line
			v    = y-IMAGE_H/2;
			if (Abs(normal[0])<0.001)
				{
				normal[0] = 0.001;
				}

			// using MAX_3D_DIST (rather than horizon) is a stronger constraint
			xhor = (int)((-tempf2 - normal[1]*v)/normal[0] + IMAGE_W/2);
			
			if (normal[0]>0) 
				{
				// horizon is tilted to the down-right
				xmin = R_FILT+2;
				xmax = xhor;
				}
			else
				{
				// horizon is tilted to the down-left
				xmin = xhor;  
				xmax = IMAGE_W-(R_FILT+2);
				}
			
			if (xmin<R_FILT+2)
				xmin = MIN(R_FILT+2, xmax);
			if (xmin>IMAGE_W-(R_FILT+2))
				xmin = MIN(IMAGE_W-(R_FILT+2), xmax);
			if (xmax>IMAGE_W-(R_FILT+2))
				xmax = MAX(IMAGE_W-(R_FILT+2), xmin);
	
			for (x=xmin; x<xmax; x++)
				{				
				// address
				int adr = offset+x;
				temp = temp_ar[adr];

				if ( temp > tresh*THRESHOLD1_COEF)
					{
					LineArray[nlines].grad = temp;
					LineArray[nlines].polarity = im_edge_sign2[adr]>128; // positive if top is whiter then bottom

					// init and grow a line (higher threshold for this...)
					if ( LineArray[nlines].initAndGrow_ver (TPoint (x, y),
							(int*)temp_ar, (int*)im_edge_sign2, tresh) )
						{
						if ( nlines<MAX_LINES-1) 
							nlines++;
						}
					}
				}
			
			
			} // end of loops	

		// Standardize heads and tails so head is geometrically on the top
		// (should not affect anything but gap_closing!)
		for (int n=nlines_hor; n<nlines; n++)
		{
			if (LineArray[n].head.iY > LineArray[n].tail.iY)
			{
				// swap head and tail coordinates iY, iX
				temp = LineArray[n].head.iX;
				LineArray[n].head.iX = LineArray[n].tail.iX;
				LineArray[n].tail.iX = temp;

				temp = LineArray[n].head.iY;
				LineArray[n].head.iY = LineArray[n].tail.iY;
				LineArray[n].tail.iY = temp;
			}

		
			LineArray[n].length = LineArray[n].len();
			if (LineArray[n].length > LONG_LINE / 2)
			{
				LineArray[n].valid = true;
				LineArray[n].dx = (LineArray[n].head.iX - LineArray[n].tail.iX)
					/ LineArray[n].length;
				LineArray[n].dy = (LineArray[n].head.iY - LineArray[n].tail.iY)
					/ LineArray[n].length;
			}
			else
			{
				LineArray[n].valid = false;
			}
		
		} // nlines

	} // vertical lines, if (!Zebra_cw)


	// release
	env->ReleaseIntArrayElements(gradIn, im_edge, JNI_ABORT);     // unpin
	env->ReleaseIntArrayElements(signIn, im_edge_sign, JNI_ABORT);
	env->ReleaseIntArrayElements(sign2In, im_edge_sign2, JNI_ABORT);

	return (nlines);

} // -- GetLines()


JNIEXPORT void JNICALL
Java_cameraStream_CppFunctionsJNI_undoPerspective
(JNIEnv *env, jclass jc, jboolean unwarp_image, jintArray grayIn, jintArray grndOut) {

	// Java to C++ arrays
	jboolean isCopy1;
	jint *im_gray = env->GetIntArrayElements(grayIn, 0);
	jint *im_grnd = env->GetIntArrayElements(grndOut, &isCopy1);

	// create normal and ground vectors
	CreateGround();

	float x, y, z; // distance along the camera viewing direction and mean z
	int u, v, v_hor;     // indices in the gray image and vertical row of the horizon
	float col, row, fwd, side;       // indices in ground coord of side and forward vectors
	TEdgelet *LinePtr = LineArray;

	float temp1 = normal[0] * (float)ONE_OVER_FOCAL_LENGTH;
	float temp2 = normal[1] * (float)ONE_OVER_FOCAL_LENGTH;

	// undo perspective for lines
	for (int i=0; i<nlines; i++)
	{

		//                 head

		// Camera coordinates: calculate z of ground plane in the direction of u, v
		u = LinePtr[i].head.iX-IMAGE_W/2;
		v = LinePtr[i].head.iY-IMAGE_H/2;

		z = (float)(-camera_height/(normal[2]+ temp1*u+ temp2*v)); // meters, camera system

		if (!finite(z) || isnan(z) || z > MAX_3D_DIST || z<0)
		{
			LinePtr[i].valid = false;
			continue;
		}

		x = (float)(u*z*ONE_OVER_FOCAL_LENGTH);
		y = (float)(v*z*ONE_OVER_FOCAL_LENGTH);

		if (!finite(x) || isnan(x) ||
			!finite(y) || isnan(y) )
		{
			LinePtr[i].valid = false;
			continue;
		}

		// project on the ground in terms of forward and side vectors
		side = (float)(x*side_vec[0] + y*side_vec[1] + z*side_vec[2] - P0side);
		fwd =  (float)(x*forward_vec[0] + y*forward_vec[1] + z*forward_vec[2] - P0frwd);

		LinePtr[i].headGrnd.iX = side;
		LinePtr[i].headGrnd.iY = fwd;

		//                 tail

		// Camera coordinates: calculate z of ground plane in the direction of u, v
		u = LinePtr[i].tail.iX-IMAGE_W/2;
		v = LinePtr[i].tail.iY-IMAGE_H/2;
		z = (float)(-camera_height/(normal[2] + temp1*u + temp2*v)); // meters, camera system

		if (!finite(z) || isnan(z) || z > MAX_3D_DIST || z<0)
		{
			LinePtr[i].valid = false;
			continue;
		}

		x = (float)(u*z*ONE_OVER_FOCAL_LENGTH);
		y = (float)(v*z*ONE_OVER_FOCAL_LENGTH);

		if (!finite(x) || isnan(x) ||
			!finite(y) || isnan(y) )
		{
			LinePtr[i].valid = false;
			continue;
		}

		// project on the ground in terms of forward and side vectors
		side = (float)(x*side_vec[0] + y*side_vec[1] + z*side_vec[2] - P0side);
		fwd =  (float)(x*forward_vec[0] + y*forward_vec[1] + z*forward_vec[2] - P0frwd);

		LinePtr[i].tailGrnd.iX = side;
		LinePtr[i].tailGrnd.iY = fwd;

		// invert y
		LinePtr[i].radGrnd = (getATan(-LinePtr[i].headGrnd.iY+
			LinePtr[i].tailGrnd.iY, LinePtr[i].headGrnd.iX-
			LinePtr[i].tailGrnd.iX));

	} // i

	// -------------------------  Unwarp the image
	if (unwarp_image)
	{

		// Gray is converted to Grnd bitmap
		int* BitmapGrayPtr = im_gray;
		int* BitmapGrndPtr = im_grnd;

		for (u=-IMAGE_W/2; u<IMAGE_W/2; u++)
		{

			// horizon as a function of u

			v_hor = (float)((-normal[2]*FOCAL_LENGTH - normal[0]*u)/normal[1]);

			if (v_hor<-IMAGE_H/2) // BUG
				v_hor = -IMAGE_H/2;
			if (v_hor > IMAGE_H/2-1)
				v_hor = IMAGE_H/2-1;

			for (v = v_hor; v<IMAGE_H/2; v++)
			{

				// Camera coordinates: calculate z of ground plane in the direction of u, v
				z = (float)(-camera_height/(normal[2]+
					normal[0]*u*ONE_OVER_FOCAL_LENGTH+
					normal[1]*v*ONE_OVER_FOCAL_LENGTH)); // meters, camera system
				if (!finite(z) || isnan(z) || Abs(z) > MAX_3D_DIST)
				{
					continue;
				}
				x = (float)(u*z*ONE_OVER_FOCAL_LENGTH);
				y = (float)(v*z*ONE_OVER_FOCAL_LENGTH);

				if (!finite(x) || isnan(x) ||
					!finite(y) || isnan(y) )
				{
					continue;
				}

				// project on the ground in terms of forward and side vectors
				side = (x-P0[0])*side_vec[0] + (y-P0[1])*side_vec[1] +
					(z-P0[2])*side_vec[2];
				fwd = (x-P0[0])*forward_vec[0] + (y-P0[1])*forward_vec[1] +
					(z-P0[2])*forward_vec[2];
				ScaleGrnd(fwd, side, col, row);

				// boundary check
				if (col>=0 && col<IMAGE_W && row>=0 && row<IMAGE_H)
				{

					BitmapGrndPtr[(int)row*IMAGE_W+(int)col] =
						BitmapGrayPtr[(int)((v+IMAGE_H/2)*IMAGE_W+(u+IMAGE_W/2))];
				}

			} // j
		}// i

	}// UNWARP_IMAGE


	// release
	env->ReleaseIntArrayElements(grayIn, im_gray, JNI_ABORT);     // unpin

	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(grndOut, im_grnd, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(grndOut, im_grnd, JNI_ABORT); // unpin only

} // UndoPerspective()

// DominantOrientation()
JNIEXPORT bool JNICALL
Java_cameraStream_CppFunctionsJNI_DominantOrientation
(JNIEnv *env, jclass jc, jfloatArray dominant) {

	jboolean isCopy;
	jfloat *dom_line = env->GetFloatArrayElements(dominant, &isCopy);


	int i, j;   			    // indices througout the function
	float cur_ang;				// middle of the sliding window in angulare space
	TEdgelet *LinePtr;			// pointer to the current line
	//float max_score;           // sum of length weighted by the Gaussian window
	float ang;           // line angle shifted PI/2 to place typical angles in the mid range!
	int Gauss_ind, domin_ind;  // indices inside of the binned Gaussian weight array
	float weight, ang_diff;     // Gaussian weight, difference from pseudo_ang and current angle

	// ======================================== DOMINANT ORIENTATION

	// 1. Sliding Gaussian window -PI/2..PI/2 in the angular space (weighted by line length in 2D)
	// cluster_score[NUM_STEPS] collects all these weighted scores with a step 4 deg and SD = 6deg
	//
	for (i=0; i<NUM_STEPS; i++) // every 4 deg we check a bin 18 deg wide
	{
		// Current middle of the sliding window starting from -PI/2
		cur_ang = (float)(-PI/2+i*SLIDING_STEP); // we don't use WINDOW_WIDTH_RAD for that!

		// dominant score = sum of line lengths for lines with similar orientations
		cluster_score[i] = 0.0f;
		for (j=0; j<nlines; j++)
		{
			LinePtr = &(LineArray[j]);
			if (!LinePtr->valid || LinePtr->npxl < MIN_LINE_LEN_DOMIN_ORIENT)//  i==j  is OK to include
				continue;

			ang = LinePtr->radGrnd;

			ang_diff = ANG_DIF(Abs(ang-cur_ang)); // is within 0..PI/2
			if (ang_diff < MAX_LINE_ANG_DIFF_3D_SLIDING_WINDOW) // rad
			{
				// find the weight
				Gauss_ind = NUM_WINDOW_BINS/2+(int)(ang_diff/WINDOW_BIN_WIDTH ); // use only half of Gaussian?
				if (Gauss_ind > NUM_WINDOW_BINS-1) // probably don't need it
					continue;
				weight = Gauss_weight[Gauss_ind]; // such index works even for non symmetric weights
				cluster_score[i] += weight * LinePtr->length; // use length in 2D!
			}
		} // j
	} // i


	// 2. Dominant line orientation(s) from a max cluster_score[NUM_STEPS]
	domin_ind = 0; // initialize before finding a maximum
	max_score = cluster_score[domin_ind];
	dominant_orient = (float)(-PI/2.0f+domin_ind*SLIDING_STEP); // here -PI/2 is a beginning of the angle range

	for (i=1; i<NUM_STEPS; i++) //  find a max score
	{
		if (max_score<cluster_score[i])
		{
			max_score = cluster_score[i];
			domin_ind = i;
			dominant_orient = -PI/2+domin_ind*SLIDING_STEP;	// here -PI/2 is a beginning of the angle range
		}
	} // i

	// Threshold the score to get rid of not probable candidates and cut the analysis
	if (max_score > MIN_SCORE_STRIPE)
	{
		local_max_found = true;
	}
	else
	{
		local_max_found = false;
		crosswalk_result = ENotDefined;
		cw_reason = ENoDOminantLine;
		return false;
	}


	// 3. Translate a dominant line into the TEdgelet class variable;
	//   and decide on the orientation (use-row_threshold => vertical lines)
	domin_line.rad = -dominant_orient;

	// save mid-line direction in 3D
	domin_line.dx = (float)cos(domin_line.rad);
	domin_line.dy = (float)sin(domin_line.rad);



	// important for finding threshold
	if (!Zebra_cw)
	{
		domin_line.tail.SetXY(IMAGE_W/2.0f, IMAGE_H);

		if (domin_line.dy<0)
		{
			domin_line.dy = -domin_line.dy;
			domin_line.dx = -domin_line.dx;
		}

	}
	else
	{
		domin_line.tail.SetXY(0, IMAGE_H/2.0f);

		if (domin_line.dx<0)
		{
			domin_line.dy = -domin_line.dy;
			domin_line.dx = -domin_line.dx;
		}
	}

	domin_line.head.SetXY((float)(domin_line.tail.iX+domin_line.dx*MIDLINE_LEN),
		(float)(domin_line.tail.iY-domin_line.dy*MIDLINE_LEN));

	// calculate the normal to the dominant line going
	domin_line_normal[0] = domin_line.dy; // side
	domin_line_normal[1] = -domin_line.dx;// fwd

	if (Zebra_cw)
	{
		// we want it to go forward > 0
		if (domin_line_normal[1]<0)
		{
			domin_line_normal[0] = -domin_line_normal[0];
			domin_line_normal[1] = -domin_line_normal[1];
		}
	}
	else
	{
		// we want it to go right > 0
		if (domin_line_normal[0]<0)
		{
			domin_line_normal[0] = -domin_line_normal[0];
			domin_line_normal[1] = -domin_line_normal[1];
		}
	}

	//domin_line.dx = -0.12533443;

	dom_line[0] = domin_line.tail.iX; // for file 0:
	dom_line[1] = domin_line.tail.iY;
	dom_line[2] = domin_line.head.iX;
	dom_line[3] = domin_line.head.iY;
	dom_line[4] = domin_line.rad;
	dom_line[5] = domin_line.dx;
	dom_line[6] = domin_line.dy;

	// release
	if (isCopy == JNI_TRUE)
		env->ReleaseFloatArrayElements(dominant, dom_line, 0);         // copy and unpin
	else
		env->ReleaseFloatArrayElements(dominant, dom_line, JNI_ABORT); // unpin

	return true;
}// DominantOrientation()

// ThreshouldWhite()
JNIEXPORT void JNICALL
Java_cameraStream_CppFunctionsJNI_ThreshouldWhite
(JNIEnv *env, jclass jc, jintArray BlurIn) {

	jboolean isCopy1;
	jint *im_blur = env->GetIntArrayElements(BlurIn, &isCopy1);

	if (!Zebra_cw)
	{
		for (int y = 0; y < IMAGE_H; y++)
		{
			int offset = IMAGE_W*y;
			// record row values into 1D array
			for (int x = 0; x < IMAGE_W; x++)
				col_ar[x] = im_blur[offset+x];

			thresh_col[y] = (float)(SLACK_WHITE2 * percentile255(col_ar, IMAGE_W,
				PERCENTILE_WHITE_COLOR2)); //% 90%
		}
	}
	else
	{
		for (int x = 0; x < IMAGE_W; x++)
		{
			// record column values into 1D array
			for (int y = 0; y < IMAGE_H; y++)
				col_ar[y] = im_blur[IMAGE_W*y+x];

			thresh_col[x] = (float)(SLACK_WHITE * percentile255(col_ar, IMAGE_H,
				PERCENTILE_WHITE_COLOR)); //% 90%
		}
	}

	// release
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(BlurIn, im_blur, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(BlurIn, im_blur, JNI_ABORT); // unpin

} // ThreshouldWhite()

// WhiteBitmap(()
JNIEXPORT void JNICALL
Java_cameraStream_CppFunctionsJNI_WhiteBitmap
(JNIEnv *env, jclass jc, jintArray BlurIn, jintArray WhiteOut) {

	jboolean isCopy1, isCopy2;
	jint *im_blur = env->GetIntArrayElements(BlurIn, &isCopy1);
	jint *im_white = env->GetIntArrayElements(WhiteOut, &isCopy2);

	// white pixels are those over the threshold
	int adr;

	if (!Zebra_cw)
	{
		for (int y = 0; y < IMAGE_H; y++)
		{
			int offset = y * IMAGE_W;
			int tr = thresh_col[y];
			for (int x = 0; x < IMAGE_W; x++)
			{
				adr = offset + x;
				if (im_blur[adr] > tr)
				{
					im_white[adr] = WHITE_BITMAP_COL;
				}
			} // x
		} // y
	}
	else
	{
		for (int y = 0; y < IMAGE_H; y++)
		{
			int offset = y * IMAGE_W;
			for (int x = 0; x < IMAGE_W; x++)
			{
				adr = offset + x;
				if (im_blur[adr] > thresh_col[x])
				{
					im_white[adr] = WHITE_BITMAP_COL;
				}
			} // x
		} // y
	}

	// release
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(BlurIn, im_blur, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(BlurIn, im_blur, JNI_ABORT); // unpin

	// release
	if (isCopy2 == JNI_TRUE)
		env->ReleaseIntArrayElements(WhiteOut, im_white, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(WhiteOut, im_white, JNI_ABORT); // unpin


}

// Discard lines that aren't parallel to dominant orientation
void ParallelLines()
{
	TEdgelet *LinePtr;			// pointer to the current line



	// deviation from dominant line?
	for (int i=0; i<nlines; i++)
	{
		// validity based on the previous constraints: length and MAX_3D along z
		LinePtr = &(LineArray[i]);
		if (!LinePtr->valid)
			continue;

		// init
		float dang = ANG_DIF(Abs(LinePtr->radGrnd-dominant_orient));

		// Other constraints: parallelism and Grnd distance; also collects some info
		if (dang >= MAX_LINE_ANG_DIFF_3D) // lines are more parallel in 3D  - 9 deg.
		{
			LinePtr->valid = false;
			continue;
		}
	} // i

	// 2. Calculate the projection of ground coordiantes on domin. and ortho directions
	for (int i=0; i<nlines; i++)
	{

		// validity based on the previous constraints: length and MAX_3D along z
		LinePtr = &(LineArray[i]);
		if (!LinePtr->valid)
		{
			LinePtr->headDom.iX = 0;
			LinePtr->headDom.iY = 0;
			LinePtr->tailDom.iX = 0;
			LinePtr->tailDom.iY = 0;
			continue;
		}

		// projection of ground line coordinates on domin_line, and domin_line_normal;
		LinePtr->headDom.iX = (float)(domin_line.dx * LinePtr->headGrnd.iX +       // dominant
		domin_line.dy * LinePtr->headGrnd.iY);
		LinePtr->headDom.iY = (float)(domin_line_normal[0] * LinePtr->headGrnd.iX + // ortho
		domin_line_normal[1] * LinePtr->headGrnd.iY);

		LinePtr->tailDom.iX = (float)(domin_line.dx * LinePtr->tailGrnd.iX +        // dominant
		domin_line.dy * LinePtr->tailGrnd.iY);
		LinePtr->tailDom.iY = (float)(domin_line_normal[0] * LinePtr->tailGrnd.iX + // ortho
		domin_line_normal[1] * LinePtr->tailGrnd.iY);

	} // i



	// 3. Justification by opposite orientation only for vertical lines (2 stripe crosswalk)
	if (!Zebra_cw)
	{
		for (int i=0; i<nlines; i++)
		{
			if (!LineArray[i].valid)
				continue;

			bool polarity_i = LineArray[i].polarity;

			// find a pair
			bool found_pair = false;
			for (int j=0; j<nlines; j++)
			{
				// should be valid of opposite polarity
				if (!LineArray[j].valid || LineArray[j].polarity == polarity_i)
					continue;

				float dist;
				if (polarity_i)
				{
					// there should be a line on the right if i line, distance measured along
					dist = LineArray[j].headDom.iY - LineArray[i].headDom.iY ; // ortho distance
				}
				else
				{
					// there should be a line on the left if i line, distance measured along
					dist = LineArray[i].headDom.iY - LineArray[j].headDom.iY; // ortho distance

				}

				// one pair is enough
				if (dist > MIN_STRIPE_WIDTH2 && dist < MAX_STRIPE_WIDTH2 )
				{
					found_pair = true;
					break;
				}
			}

			if (!found_pair)
				LineArray[i].valid = false;

		} // i
	} // if

} // ParallelLines()

// Estimate cross-walk size based on line coordinates
bool BoundingBox()
{
	TEdgelet *LinePtr;			// pointer to the current line

	// 4. CW middle = median/mean of line middles weighted
	crosswalk_lines = 0; // about 50
	sum_len = 0;
	for (int i=0; i<nlines; i++)
	{

		// validity based on the previous constraints: length and MAX_3D along z
		LinePtr = &(LineArray[i]);
		if (!LinePtr->valid)
			continue;

		// Grnd coordinates of lines' middle
		line_feature1[crosswalk_lines] = (float)
			MEAN(LinePtr->headGrnd.iX, LinePtr->tailGrnd.iX); // av. X
		line_feature2[crosswalk_lines] = (float)
			MEAN(LinePtr->headGrnd.iY, LinePtr->tailGrnd.iY); // av. Y
		line_feature3[crosswalk_lines] = LinePtr->length3D(); // length in 3D (used as a weight)

		sum_len += line_feature3[crosswalk_lines];
		crosswalk_lines++;

	} // i

	// enough lines?
	int min_lines = (Zebra_cw)?MIN_LINES:MIN_LINES2; // depends on the cw type??
	float min_length = (Zebra_cw)?MIN_LINE_SUM_LENGTH:MIN_LINE_SUM_LENGTH2;

	if (crosswalk_lines < min_lines || sum_len < min_length) // 50 is typical
	{
		CWrunav.detected = 0;
		cw_reason = ELessThan2Lines;
		return false;
	}

	// Median CW center on the ground: weight centers of the lines with their length
	for (int i=0; i<crosswalk_lines; i++)
		order_index[i] = i;

	// when there is a small number of lines or they are distant (2stirpe) we use mean
	if (!Zebra_cw)
	{
		//CWrunav.CWcenterGrnd.iX = meanW(crosswalk_lines, line_feature1, line_feature3, order_index);
		CWrunav.CWcenterGrnd.iX = middle(crosswalk_lines, line_feature1);
	}
	else
	{
		Quick_sort2(0, crosswalk_lines-1, line_feature1,  order_index);
		CWrunav.CWcenterGrnd.iX = medianW(crosswalk_lines, line_feature1,
				line_feature3, order_index); // side
	}

	for (int i=0; i<crosswalk_lines; i++)
		order_index[i] = i;

	Quick_sort2(0, crosswalk_lines-1, line_feature2,  order_index);
	CWrunav.CWcenterGrnd.iY = medianW(crosswalk_lines, line_feature2,
		line_feature3, order_index); // fwd

	// project the center on dominant/ortho to get origins for white matter histograms
	CWrunav.CWcenter_domin = domin_line.dx * CWrunav.CWcenterGrnd.iX +
		domin_line.dy * CWrunav.CWcenterGrnd.iY;
	CWrunav.CWcenter_ortho = domin_line_normal[0] * CWrunav.CWcenterGrnd.iX +
		domin_line_normal[1] * CWrunav.CWcenterGrnd.iY;

	// 5. Median radiuses (along domin and ortho) calculated in
	// two iterations (based on terminals/centers and then based on white matter/closest line)
	crosswalk_lines = 0; // about 50
	closest_line = 100;  // 100 m
	for (int i=0; i<nlines; i++)
	{

		// validity based on the previous constraints: length and MAX_3D along z
		LinePtr = &(LineArray[i]);
		if (!LinePtr->valid)
			continue;


		// Rdomin: max. of distances from the CW center to line terminals along domin
		line_feature1[crosswalk_lines] = MAX(Abs(LinePtr->headDom.iX - CWrunav.CWcenter_domin),
			Abs(LinePtr->tailDom.iX - CWrunav.CWcenter_domin));

		// Rortho: max. of distances from the CW center to line terminals along ortho
		line_feature2[crosswalk_lines] = MAX(Abs(LinePtr->headDom.iY - CWrunav.CWcenter_ortho),
			Abs(LinePtr->tailDom.iY - CWrunav.CWcenter_ortho));

		// closest line (signed radius toward user) in domin/ortho coord: signed diff.
		// from center to closest line along
		// up (forward in 3D) direction ( domin for 2stripe or orto for zebra);
		// closest line is a global parameter used in Box(), should be negative!

		if (!Zebra_cw)
		{
			// tails are always at the bottom
			float domin_dist = LinePtr->tailDom.iX - CWrunav.CWcenter_domin;
			if (closest_line > domin_dist)
				closest_line = domin_dist;
		}
		else
		{
			float ortho_dist = MIN(LinePtr->headDom.iY - CWrunav.CWcenter_ortho,
			LinePtr->tailDom.iY - CWrunav.CWcenter_ortho);
			if (closest_line > ortho_dist)
				closest_line = ortho_dist; // closest line is a global parameter used in Box(), affects ortho border
		}

		crosswalk_lines++;
	} // i

	if (!Zebra_cw)
		// CW radius along dominant, Zebra, make sure it overestimates actual width a lost (due to Median underestimation)
		//CWrunav.CWradius[0] = 2.5*medianW(crosswalk_lines, line_feature1, line_feature3, order_index); // dominanat dir (to be tightened by white_matter_ortho)
		CWrunav.CWradius[0] = MIN(CW_MAX_LENGTH,
				max_arr(crosswalk_lines, line_feature1)+0.1); // dominanat dir (to be tightened by white_matter_ortho)
	else
	{
		// median CW distances from the center on the ground
		for (int i=0; i<crosswalk_lines; i++)
			order_index[i] = i;
		Quick_sort2(0, crosswalk_lines - 1, line_feature1, order_index);

		// CW radius along dominant, Zebra, make sure it overestimates actual width a lost (due to Median underestimation)
		CWrunav.CWradius[0] = 1.5*medianW(crosswalk_lines, line_feature1,
				line_feature3, order_index); // dominanat dir (to be tightened by white_matter_ortho)

	}


	// CW radius along ortho
	for (int i=0; i<crosswalk_lines; i++)
		order_index[i] = i;

	// median is low noise under-estimate of R
	Quick_sort2(0, crosswalk_lines - 1, line_feature2, order_index);
	if (!Zebra_cw)
	{
		// make sure it overestimates actual width a bit (dominated by true terminals?)
		//CWrunav.CWradius[1] = 1.5*meanW(crosswalk_lines, line_feature2, line_feature3, order_index); // ortho dir (to be tightened by first_line)
		CWrunav.CWradius[1] = MIN(CW_MAX_LENGTH, max_arr(crosswalk_lines, line_feature2)+0.1); // ortho dir (to be tightened by first_line)
	}
	else
	{
		// make sure it overestimates actual width a bit (due to the splitting?)
		CWrunav.CWradius[1] = 3.0*medianW(crosswalk_lines, line_feature2, line_feature3, order_index); // ortho dir (to be tightened by first_line)
		//CWrunav.CWradius[1] = MIN(CW_MAX_LENGTH, max_arr(crosswalk_lines, line_feature2)+0.1);
	}

	// two orthogonal directions to be scaled with radiuses;
	// used in the Box(); they are independent of the crosswalk type
	// for use of  Rright, Rup see below BoxGround(Rright, Rup);
	if (!Zebra_cw)
	{
		Rup.SetXY(domin_line.dx*CWrunav.CWradius[0], domin_line.dy*CWrunav.CWradius[0]);
		Rright.SetXY(domin_line_normal[0]*CWrunav.CWradius[1], domin_line_normal[1]*CWrunav.CWradius[1]);
	}
	else
	{
		Rup.SetXY(domin_line_normal[0]*CWrunav.CWradius[1], domin_line_normal[1]*CWrunav.CWradius[1]);
		Rright.SetXY(domin_line.dx*CWrunav.CWradius[0], domin_line.dy*CWrunav.CWradius[0]);
	}

	// make sure signs are right
	if (Rright.iX<0)
		Rright.negate();
	if (Rup.iY<0)
		Rup.negate();

	return true;
} // BoundingBox()

/*  Validate white matter with lines (now depends on the dominant orientation)
    Based on line/polarity estimate location of white matter
    Approximation used: We use only the most prominent (hor/ver) component
    of the vector representing stripe width
*/
void JustifyWhiteByLines(int *im_whiteL)
{
	TEdgelet *LinePtr;			// pointer to the current line
	float tempf = normal[2]*FOCAL_LENGTH;
	float tempf2 = tempf+CAMERA_HEIGHT*FOCAL_LENGTH/MAX_3D_DIST;

	//initialize histogram
	for (int i=0; i<NBINS; i++)
	{
		bin_count_domin[i] = 0;
		bin_count_ortho[i] = 0;
	}

	// vec_stripe_width = stripe width in Grnd coord. (perpendicular to dominant orientation)
	if (!Zebra_cw )
		vec_stripe_width.SetXY(MEAN_STRIPE_WIDTH2 * domin_line_normal[0], // guranteed to point to the right, and perpendicular to dominant
		MEAN_STRIPE_WIDTH2 * domin_line_normal[1]);
	else
		vec_stripe_width.SetXY(MEAN_STRIPE_WIDTH * domin_line_normal[0], // guranteed to point up, and perpendicular to dominant
		MEAN_STRIPE_WIDTH * domin_line_normal[1]);


	// loop over all lines
	for (int i=0; i<nlines; i++)
	{
		TPointR new_head, new_tail;
		float dy1, dy2, dx1, dx2;
		float x, y, z, uv[2];

		// included boundaries of CW???.....................

		LinePtr = &(LineArray[i]);
		if (!LinePtr->valid)
			continue;

		// For all CW types!
		// 6a. makes sense of line polarities

		// Polarity defines where a line enclosing white matter should be:
		// positive for Zebra are left to right; for 2stripe from down to top
		if (LinePtr->polarity > 0) // stripe is on top
		{
			new_head = LinePtr->headGrnd + vec_stripe_width;
			new_tail = LinePtr->tailGrnd + vec_stripe_width;
		}
		else
		{
			new_head = LinePtr->headGrnd - vec_stripe_width;
			new_tail = LinePtr->tailGrnd - vec_stripe_width;
		}

		// translate it to screen coordiantes (to combine with 2D white map)
		Grnd2xyz(x, y, z, new_head.iY, new_head.iX);
		xyz2uv(uv, x, y, z);

		// difference with head in 2D (for interpolation)
		dy1 =  uv[1] + IMAGE_H/2 - LinePtr->head.iY;
		dx1 =  uv[0] + IMAGE_W/2 - LinePtr->head.iX;

		Grnd2xyz(x, y, z, new_tail.iY, new_tail.iX);
		xyz2uv(uv, x, y, z);

		// difference with tail in 2D
		dy2 = uv[1] + IMAGE_H/2 - LinePtr->tail.iY;
		dx2 = uv[0] + IMAGE_W/2 - LinePtr->tail.iX;

		if (Zebra_cw)
		{
			// vertical padding fo Zebra
			for (int x = LinePtr->tail.iX; x <= LinePtr->head.iX; x++)
			{
				// interpolation coefficient
				float k = ((float)(x-LinePtr->tail.iX)) / (LinePtr->head.iX - LinePtr->tail.iX);    // relative path from tail to head  > 0
				int y_interp = (float)(LinePtr->tail.iY + (LinePtr->head.iY - LinePtr->tail.iY) * k + 0.5f); // estimated position on the line
				int y_pad = (float)(y_interp+dy2+k*(dy1-dy2));												// estimated position on the line after padding

				// padd
				if (dy2>0)
				{
					for (int y = y_interp; y < y_pad; y++)
					{
						// boundary condition on y
						if (y<IMAGE_H && y>0)
							im_whiteL[y*IMAGE_W+x] = WHITE_BITMAP_COL;
					}
				}
				else
				{
					for (int y = y_interp; y > y_pad; y--)
					{
						// boundary condition on y
						if (y<IMAGE_H && y>0)
							im_whiteL[y*IMAGE_W+x] = WHITE_BITMAP_COL;
					}
				}


			}
		}
		else  // horizontal padding for TwoStripe
		{
			if (ABS(LinePtr->rad * RAD2DEG) > 45)
			{
				// indexing is vertical (y)

				for (int y = LinePtr->tail.iY; y > LinePtr->head.iY; y--) // note the decrement to get to the head!
				{
					// interpolation coefficient
					float k = ((float)(y-LinePtr->tail.iY)) / (LinePtr->head.iY - LinePtr->tail.iY);     // relative path from tail to head  > 0
					int x_interp = (float)(LinePtr->tail.iX + (LinePtr->head.iX - LinePtr->tail.iX) * k + 0.5f);  // estimated position on the line
					int x_pad = (float)(x_interp+dx2+k*(dx1-dx2));												 // estimated position on the line after padding

					// padd
					if (dx2>0)
					{
						for (int x = x_interp; x < x_pad; x++)
						{
							// boundary condition on x
							if (x<IMAGE_W && x>0)
								im_whiteL[y*IMAGE_W+x] = WHITE_BITMAP_COL;
						}
					}
					else
					{
						for (int x = x_interp; x > x_pad; x--)
						{
							// boundary condition on x
							if (x<IMAGE_W && x>0)
								im_whiteL[y*IMAGE_W+x] = WHITE_BITMAP_COL;
						}
					}
				} // for y

			}
			else // ABS(LinePtr->rad * RAD2DEG) <= 45
			{
				// indexing is horizontal (x)

				// tail on the left of the head
				if (LinePtr->tail.iX < LinePtr->head.iX)
				{
					for (int x_interp = LinePtr->tail.iX; x_interp < LinePtr->head.iX; x_interp++) // note the decrement to get to the head!
					{
						// interpolation coefficient
						float k = ((float)(x_interp-LinePtr->tail.iX)) / (LinePtr->head.iX - LinePtr->tail.iX);     // relative path from tail to head  > 0
						int y_interp = (float)(LinePtr->tail.iY + (LinePtr->head.iY - LinePtr->tail.iY) * k + 0.5f);         // estimated position on the line
						int x_pad = (float)(x_interp+dx2+k*(dx1-dx2));												        // estimated position on the line after padding

						// padd
						if (x_interp < x_pad)
						{
							for (int x = x_interp; x < x_pad; x++)
							{
								// boundary condition on x
								if (x<IMAGE_W && x>0)
									im_whiteL[y_interp*IMAGE_W+x] = WHITE_BITMAP_COL;
							}
						}
						else
						{
							for (int x = x_interp; x > x_pad; x--)
							{
								// boundary condition on x
								if (x<IMAGE_W && x>=0)
									im_whiteL[y_interp*IMAGE_W+x] = WHITE_BITMAP_COL;
							}
						}

					}
				}
				else // tail on the right of the head
				{
					for (int x_interp = LinePtr->tail.iX; x_interp >= LinePtr->head.iX; x_interp--) // note the decrement to get to the head!
					{
						// interpolation coefficient
						float k = ((float)(x_interp-LinePtr->tail.iX)) / (LinePtr->head.iX - LinePtr->tail.iX);     // relative path from tail to head  > 0
						int y_interp = (float)(LinePtr->tail.iY + (LinePtr->head.iY - LinePtr->tail.iY) * k + 0.5f);  // estimated position on the line
						int x_pad = (float)(x_interp+dx2+k*(dx1-dx2));												         // estimated position on the line after padding

						// padd
						if (x_interp < x_pad )
						{
							for (int x = x_interp; x < x_pad; x++)
							{
								// boundary condition on x
								if (x<IMAGE_W && x>=0)
									im_whiteL[y_interp*IMAGE_W+x] = WHITE_BITMAP_COL;
							}
						}
						else
						{
							for (int x = x_interp; x > x_pad; x--)
							{
								// boundary condition on x
								if (x<IMAGE_W && x>=0)
									im_whiteL[y_interp*IMAGE_W+x] = WHITE_BITMAP_COL;
							}
						}
					}
				}

			} // else (ver/hor padding)

		} // else (Zebra/2stripes)

	} // nlines


} // JustifyWhiteByLines()

inline void bin_white_ortho(int col, float dist_grnd, float scl)
{

	int bin_ind = NBINS/2 + (int) (dist_grnd * ONE_OVER_BIN_STEP_DISTANCE + 0.5);

	//if (bin_ind==NBINS/2)
	//	col /= 2;

	// boundary check
	if (bin_ind >= 0 && bin_ind < NBINS)
		bin_count_ortho[bin_ind] += (float)(col * scl); // typical is 200 * 25 = 5000 / pixel
}

/* Projects grey matter along dominant orientation
*/
inline void bin_white_domin(int col, float dist_grnd, float scl)
{
	// Project grey matter along domin (domin_line_normal[1]>0  with zero at the middle of the CW)
	int bin_ind = NBINS/2 + (int) (dist_grnd / (float)BIN_STEP_DISTANCE+0.5f);

	// boundary check
	if (bin_ind >= 0 && bin_ind < NBINS)
		bin_count_domin[bin_ind] += (float)(col * scl); // typical is 200 * 25 = 5000 / pixel
}

// Histogram white matter
void WhiteMatterHistograms(int *im_white, int *im_whiteL, int *im_blur)
{

	float tempf = normal[2]*FOCAL_LENGTH;
	float tempf2 = tempf+(CAMERA_HEIGHT*FOCAL_LENGTH)/MAX_3D_DIST;
	float x_side, y_fwd; // TYPE is really important here
	float x, y, z;

	// transfer changes to the white matter
	if (line_verify)
	{
		for (int y = 0; y<IMAGE_H; y++)
		for (int x = 0; x<IMAGE_W; x++)
			if (im_white[y*IMAGE_W+x] == WHITE_BITMAP_COL)
				im_white[y*IMAGE_W+x] = im_whiteL[y*IMAGE_W+x];
	}


	// 6b. Histograms of white matter under horizon and within CWradius[0, 1]
	for (int u=-IMAGE_W/2; u<IMAGE_W/2; u+=2)
	{

		int ix = u+IMAGE_W/2;

		// horizon strip as a function of u
		int v_hor = (float)(-tempf2 - normal[0]*u)/normal[1];

		if (v_hor<-IMAGE_H/2) // BUG
			v_hor = -IMAGE_H/2;
		if (v_hor > IMAGE_H/2-1)
			v_hor = IMAGE_H/2-1;

		for (int v = v_hor; v<IMAGE_H/2; v+=2)
		{

			// address in the map
			int iy = v+IMAGE_H/2;

			// skip non-white pixels but gather their average color
			if (im_white[iy*IMAGE_W+ix] != WHITE_BITMAP_COL)
				continue;

			// uv->xyz
			if (!uv2xyz(u, v, x, y, z))
				continue;

			// xyz->grnd
			xyz2Grnd(x, y, z, y_fwd, x_side);

			// grnd->new origin at CW center (simplify histogram positioning)
			float dist_from_center_domin = x_side*domin_line.dx +
					y_fwd*domin_line.dy -CWrunav.CWcenter_domin;

			float dist_from_center_ortho = x_side*domin_line_normal[0] +
					y_fwd*domin_line_normal[1] - CWrunav.CWcenter_ortho;

			// Ignore white matter outside the crosswalk
			if (ABS(dist_from_center_domin) > CWrunav.CWradius[0] ||
				ABS(dist_from_center_ortho) > CWrunav.CWradius[1] ||
				((!Zebra_cw) && dist_from_center_domin < closest_line) ||
				(Zebra_cw && dist_from_center_ortho < closest_line))
			{
				//im_white[iy][ix] = 200; skips this to correspoind to Symbian
				continue;
			}

			// uses domin line normal and side coordiantes; scalse with z^2!
			float scl = SQR(z);

			// speed up: sum up around 1/4 of white points
			bin_white_domin(im_blur[iy*IMAGE_W+ix], dist_from_center_ortho, scl); // scale with square of distance	BUG?
			bin_white_ortho(im_blur[iy*IMAGE_W+ix], dist_from_center_domin, scl);

			bin_white_domin(im_blur[(iy+1)*IMAGE_W+ix+1], dist_from_center_ortho, scl); // scale with square of distance	BUG?
			bin_white_ortho(im_blur[(iy+1)*IMAGE_W+ix+1], dist_from_center_domin, scl);

			bin_white_domin(im_blur[(iy+1)*IMAGE_W+ix], dist_from_center_ortho, scl); // scale with square of distance	BUG?
			bin_white_ortho(im_blur[(iy+1)*IMAGE_W+ix], dist_from_center_domin, scl);

			bin_white_domin(im_blur[iy*IMAGE_W+ix+1], dist_from_center_ortho, scl); // scale with square of distance	BUG?
			bin_white_ortho(im_blur[iy*IMAGE_W+ix+1], dist_from_center_domin, scl);



		} // v
	}// u

} // WhiteMatterHistograms()

// Smooth out the histogram
void SmoothHistograms()
{
	// 6c. smoooth out ortho histogram
	if (conv_hist)
	{
		// Convolve histogram to smooth out the noise R = stripe_width/2
		int Rbins = 30; // radius of the convolution kernel

		// first step in integral image calculation
		bin_count[0] = 0;
		for (int ii = - Rbins/2; ii <= Rbins/2; ii++)
		{
			int ind = ii; // ind takes into account the boundary condition
			if (ind < 0)
				ind = 0;
			if (ind > NBINS-1)
				ind = NBINS - 1;
			bin_count[0] += bin_count_ortho[ind];
		}

		// second step in integral image calculation
		for (int i = 1; i < NBINS; i++)
		{
			int ind = -Rbins/2+i-1; // ind takes into account the boundary condition
			if (ind < 0)
				ind = 0;
			if (ind > NBINS-1)
				ind = NBINS - 1;

			bin_count[i] = bin_count[i-1] - bin_count_ortho[ind];

			ind = Rbins/2+i; // ind takes into account the boundary condition
			if (ind < 0)
				ind = 0;
			if (ind > NBINS-1)
				ind = NBINS - 1;

			bin_count[i] += bin_count_ortho[ind];
		}

		// normalize
		float dev_k = 1.0f / (Rbins + 1);
		for (int i = 0; i < NBINS; i++)
		{
			bin_count[i] *= dev_k;
		}
		memcpy(bin_count_ortho, bin_count, NBINS*sizeof(long long ));
	}


	//6d. Tighten along CW width (ortho hist for all CW types)

	// find max
	float max_ortho = 0;
	for (int i=0; i<NBINS; i++)
	{
		if (max_ortho < bin_count_ortho[i])
			max_ortho = bin_count_ortho[i];
	}

	// find boundaries of the bin's count bulk (10% of the max)
	left_ortho = NBINS/2-1;
	right_ortho = NBINS/2-1;
	for (int i=NBINS/2-1; i>=0; i--) // find left
	{
		if (bin_count_ortho[i] > max_ortho*0.1f)
			left_ortho = i;
		else
			break;
	}
	for (int i=NBINS/2; i<NBINS; i++) // find right
	{
		if (bin_count_ortho[i] > max_ortho*0.1f)
			right_ortho = i;
		else
			break;
	}

	// translate boundaries into the ditances
	// float Rgrnd =  BIN_STEP_DISTANCE * (right_ortho-left_ortho)/2; // radius along domin
	// Rdomin.SetXY(domin_line.dx*Rgrnd, domin_line.dy*Rgrnd);        // vector
	CWrunav.CWcenter_domin = (float)(BIN_STEP_DISTANCE *
			(NBINS/2 - (right_ortho-left_ortho)/2)); // new center projection?

} // SmoothHistograms()

// Rdomin points to the right and Rortho points up
void BoxGround(TPointR Rright, TPointR Rup)

{
	// upper-right  (we are in fwd/side system coordiantes; so fwd means plus/up)
	TPointR::add(CWrunav.upper_rightG, CWrunav.CWcenterGrnd, Rright);
	TPointR::add(CWrunav.upper_rightG, CWrunav.upper_rightG, Rup);

	// upper-left
	TPointR::sub(CWrunav.upper_leftG, CWrunav.CWcenterGrnd, Rright);
	TPointR::add(CWrunav.upper_leftG, CWrunav.upper_leftG, Rup);

	// correction for the closest line
	TPointR Rup_corrected(Rup);
	if (!Zebra_cw)
	{
		if (-closest_line < Rup.iY)
			Rup_corrected.scl(Abs(closest_line/Rup.iY));
	}
	else
	{
		if (-closest_line < Rup.iY)
			Rup_corrected.scl(Abs(closest_line/Rup.iY ));
	}

	// lower-right
	TPointR::add(CWrunav.lower_rightG, CWrunav.CWcenterGrnd, Rright);
	TPointR::sub(CWrunav.lower_rightG, CWrunav.lower_rightG, Rup_corrected);

	// lower-left
	TPointR::sub(CWrunav.lower_leftG, CWrunav.CWcenterGrnd, Rright);
	TPointR::sub(CWrunav.lower_leftG, CWrunav.lower_leftG, Rup_corrected);

	// point for shift feedback
	CWrunav.start_side =  0.5*(CWrunav.lower_leftG.iX+CWrunav.lower_rightG.iX);


} // BoxGround(TPointR Rright, TPointR Rup)

void TranslateBox2D()
{
	float x, y, z, uv[2];

	Grnd2xyz(x, y, z, CWrunav.upper_leftG.iY, CWrunav.upper_leftG.iX);
	xyz2uv(uv, x, y, z); //upper-left
	CWrunav.upper_left.iX = (int)uv[0]+IMAGE_W/2;
	CWrunav.upper_left.iY = (int)uv[1]+IMAGE_H/2;

	//upper-right
	Grnd2xyz(x, y, z, CWrunav.upper_rightG.iY, CWrunav.upper_rightG.iX);
	xyz2uv(uv, x, y, z);
	CWrunav.upper_right.iX = (int)uv[0] + IMAGE_W/2;
	CWrunav.upper_right.iY = (int)uv[1] + IMAGE_H/2;

	//lower-left
	Grnd2xyz(x, y, z, CWrunav.lower_leftG.iY, CWrunav.lower_leftG.iX);
	z = MAX(z, 0.0);
	xyz2uv(uv, x, y, z);
	CWrunav.lower_left.iX = (int)uv[0] + IMAGE_W/2;
	CWrunav.lower_left.iY = (int)uv[1] + IMAGE_H/2;

	//lower-right
	Grnd2xyz(x, y, z, CWrunav.lower_rightG.iY, CWrunav.lower_rightG.iX);
	z = MAX(z, 0.0);
	xyz2uv(uv, x, y, z);
	CWrunav.lower_right.iX = (int)uv[0] + IMAGE_W/2;
	CWrunav.lower_right.iY = (int)uv[1] + IMAGE_H/2;
}

void showHough(signed long long max_vote,
		signed long long min_vote, int *im_hist)
{
	// show Hough space normalized
	for (int i = 0; i<CW_Nw; i++)
	{
		for (int j=0; j<CW_Ndx; j++)
		{
			im_hist[i*IMAGE_W+IMAGE_W-1-CW_Ndx+j] = (int)
				((double)((255*(CWvotes[i][j]-min_vote)))/
				(double)(max_vote-min_vote)); // histogram has only 240 bins
		}
	}
}

// Hough of the white matter (only for horizontal lines)
void HoughOfHistogram(int *im_hist)
{
	// 7. Hough on the domin white matter (only for horizontal lines)
	if (Zebra_cw )
	{
		for (int i = 0; i<CW_Nw; i++)
		{
			for (int j=0; j<CW_Ndx; j++)
			{
				CWvotes[i][j] = 0; // initialize votes
			}
		}

		//count votes
		for (int bin_ind = 0; bin_ind < NBINS; bin_ind++)
		{
			// get x distance of each bin
			double x = (bin_ind - NBINS/2) * BIN_STEP_DISTANCE;

			// for all possible stripe width
			for (int i = 0; i<CW_Nw; i++)
			{
				// current stripe width
				double cur_w = MIN_STRIPE_WIDTH + i * WIDTH_STEP;

				// for all possible starting points
				for (int j=0; j<CW_Ndx; j++)
				{
					// current starting point
					double cur_dx = j * DX_STEP;

					// current index of a stripe
					int num_stripes = floor((x-cur_dx)/cur_w+0.5f);

					// even stripes are white
					if (isEven(num_stripes))
						CWvotes[i][j] += bin_count_domin[bin_ind];
					else
						CWvotes[i][j] -= bin_count_domin[bin_ind];
				}
			}
		}

		// find a maximum in the vote matrix

	    float debug3 = BIN_STEP_DISTANCE;
		float debug4 = WIDTH_STEP;

		int wini = 0, winj = 0;

		//signed long long max_vote, min_vote;

		max_vote= CWvotes[wini][winj],
		min_vote = CWvotes[wini][winj];

		signed long long all_votes = 0;
		for (int i = 0; i<CW_Nw; i++)
		{
			for (int j=0; j<CW_Ndx; j++)
			{
				all_votes += CWvotes[i][j];

				if (max_vote < CWvotes[i][j])
				{
					max_vote = CWvotes[i][j];
					wini = i; // 0 means 0.3m for stripe width
					winj = j; // 0 means center in the middle of the white stripe
				}
				if (min_vote > CWvotes[i][j])
					min_vote = CWvotes[i][j];
			}
		}

		showHough(max_vote, min_vote, im_hist);

		// stripe average width
		votes_ratio = (float)(max_vote*CW_Nw*CW_Ndx)/all_votes;
		if (votes_ratio > 2.0)// && max_vote2/max_vote1 < SECOND_WINNER_PERC) //all rivals are reasonable!!!
		{
			CWrunav.stripe_width = MIN_STRIPE_WIDTH + wini * WIDTH_STEP;
			CWrunav.stripe_shift = winj * DX_STEP;
		}
		else
		{
			CWrunav.stripe_width = 0;
			CWrunav.stripe_shift = 0;
		}
	}
	else // 2stripe
	{
		CWrunav.stripe_width = 0.5*(MIN_STRIPE_WIDTH2 + MAX_STRIPE_WIDTH2);
	}

	// 8. Smooth out domin. histogram and make its copy (it will be destroyed by FindMax() )
	if (conv_hist)
	{
		// Convolve domin histogram to smooth out the noise R = stripe_width/2
		int Rbins = (int)(0.5*CWrunav.stripe_width/BIN_STEP_DISTANCE + 0.5); // radius of the convolution kernel
		float dev_k = 1.0/(Rbins+1);
		for (int i=0; i<NBINS; i++)
		{
			bin_count[i] = 0;

			// convolve with boundary repeated
			for (int ii=i-Rbins/2; ii<=i+Rbins/2; ii++)
			{
				int ind = ii; // ind takes into account the boundary condition
				if (ind<0)
					ind = 0;
				if (ind>NBINS-1)
					ind = NBINS - 1;
				bin_count[i] +=  bin_count_domin[ind];
			}

			bin_count[i] *= dev_k;
		}

		memcpy(bin_count_domin, bin_count, NBINS*sizeof(long long )); // make a copy for display
	}
	else
	{
		memcpy(bin_count, bin_count_domin, NBINS*sizeof(long long )); // make a copy for FindMax
	}

	bestR = CWrunav.stripe_width/2.0;
} // HoughOfHistogram()

/* Normalizes histogram preserving the strength but trying to avoid saturation

*/
void show_hist(int* BitmapPtr)
{

	//---------------- along ortho orientation

	// find max bin count (global class var)
	bin_cnt_max_ortho = 0; // max count in the histogram

	for (int bin_ind = 0; bin_ind < NBINS; bin_ind++)
	{
		if (bin_cnt_max_ortho < bin_count_ortho[bin_ind])
			bin_cnt_max_ortho = bin_count_ortho[bin_ind];
	}

	if (bin_cnt_max_ortho==0)
		bin_cnt_max_ortho = 1.0f;

	// For small signals: makes histogram peak to commensurate with max count
	// For strong signal normalize to avoid saturation
	//bin_cnt_max_ortho= MAX(MAX_BIN_COUNT_ORTHO, bin_cnt_max_ortho); // fixed for small signal

	// print histogram into a bitmap
	for (int bin_ind = 0; bin_ind < NBINS; bin_ind++)
	{
		// starts at the bottom since it grows up
		int ix = (float)((319-NBINS)/2+bin_ind);

		// length of the bar in the vertical direction
		int bar_len = (float) (((IMAGE_H - 4) *
				bin_count_ortho[bin_ind]) / bin_cnt_max_ortho);

		// draw white vertical line upto bar_len
		for (int iy = IMAGE_H-1; iy > IMAGE_H-1 - bar_len; iy--)
		{
			BitmapPtr[iy * IMAGE_W + ix] = 220;
		}
	}


	//---------------- along domin orientation

	// find max bin count (global class var)
	bin_cnt_max_domin = 0; // max count in the histogram

	for (int bin_ind = 0; bin_ind < NBINS; bin_ind++)
	{
		if (bin_cnt_max_domin < bin_count_domin[bin_ind])
			bin_cnt_max_domin = bin_count_domin[bin_ind];
	}


	if (bin_cnt_max_domin==0)
		bin_cnt_max_domin = 1;

	// print histogram into a bitmap
	for (int bin_ind = 0; bin_ind < NBINS; bin_ind++)
	{
		// starts at the bottom since forw grows up
		int iy = IMAGE_H-1-bin_ind;

		// length of the bar in the horizontal direction
		int bar_len = (float) (((IMAGE_W - 4) * bin_count_domin[bin_ind])
				/ bin_cnt_max_domin);

		// draw white vertical line up to bar_len
		for (int ix = 0; ix < bar_len; ix++)
		{
			BitmapPtr[iy * IMAGE_W + ix] = 255;
		}
	}
}

void show_hist_hist(int* BitmapPtr)
{
	for (int bin_ind = 0; bin_ind < NBINS; bin_ind++)
	{
		// starts at the bottom since forw grows up
		int iy = IMAGE_H-1-bin_ind;

		// length of the bar in the horizontal direction
		int bar_len = (int) (bin_count[bin_ind]);

		// draw white vertical line upto bar_len
		for (int ix = 0; ix < bar_len; ix++)
		{
			BitmapPtr[iy * IMAGE_W + ix] = 25;
		}
	}

}


// Energy model that is more accurate than Hough i.e. can incorporate missing stripes (only for horizontal lines)
void EnergyModel()
{
	// sort CWPeaks.ind_extreme so that the peaks are in the order of increasing index
	Quick_sort(0, CWPeaks.count - 1, CWPeaks.ind_extreme);

	int rmin = (int )( (STRIPE_W_TOLER1 * stripe_width_in_bins)/2.0 + 0.5);
	int rmax = (int )( (STRIPE_W_TOLER2 * stripe_width_in_bins)/2.0 + 0.5);
	int rdev = (int )( (STRIPE_W_DEVIAT * stripe_width_in_bins)/2.0 + 0.5);

	minE = 100000000;
	float Energy;

	// 9a. evaluate the noise (destractively, so make a copy)
	memcpy(bin_count, bin_count_domin, NBINS*sizeof(long long )); // make a copy of the histogram

	int R = stripe_width_in_bins;
	float min_val = 10000000000.0;

	// min peak
	for (int ipeak=0; ipeak < CWPeaks.count; ipeak++)
	{
		int ind = CWPeaks.ind_extreme[ipeak];
		float val = bin_count[ind];
		if (min_val > val)
			min_val = val;
	}

	// cut out the power around peaks
	for (int ipeak=0; ipeak < CWPeaks.count; ipeak++)
	{
		int ind = CWPeaks.ind_extreme[ipeak];
		int left = MAX(0, ind-R);
		int right = MIN(NBINS-1, ind+R);
		for (int i=left; i<=right; i++)
			bin_count[i] = 0;
	}

	// evaluate noise that is below smallest peak
	noise = 0;
	int noise_bins = 0;
	for (int i=0; i<NBINS; i++)
	{
		float val = bin_count[i];
		if (val != 0 && val<min_val*0.7)
		{
			noise += val;
			noise_bins++;
		}
	}

	if (noise_bins>0)
		noise =  noise/noise_bins * 320.0/bin_cnt_max_domin; // scale
	else
		noise = 0.0;

	if (noise_bins < NBINS/20 && noise < 5)
		noise = 0;

	// 9b. loop over width, and peaks ro find a global solution (minE), only horizontal lines
	int ind, ind2;
	for (int r = rmin; r<rmax; r++)
	{
		Energy = 0.0;

		for (int ipeak=0; ipeak < CWPeaks.count; ipeak++)
		{

			float T, T2;
			float max_val, max_val2;
			int x0, x1, x2;


			// location and value of an extreme
			ind   = CWPeaks.ind_extreme[ipeak];
			max_val   =  bin_count_domin[ind];
			T = (PERC_MAX_POWER_ENG - noise/255.0) * max_val;  // peak
			//T2 = (PERC_MAX_POWER_ENG + noise/255.0) * max_val; // trough

			x1 = ind - r;
			x2 = ind + r;

			// inspectes the peak
			for (int j = x1; j <= x2; j++)
				Energy += COST_OF_ABSENT_PEAK     * (bin_count_domin[j] <= T);

			// sthreshold for a trough
			if (ipeak >0)
			{
				ind2   = CWPeaks.ind_extreme[ipeak-1];
				max_val2   =  bin_count_domin[ind2];
				T2 = (PERC_MAX_POWER_ENG + noise/255.0) * 0.5*(max_val+max_val2);

				x0 = ind2+r;
				float dw = MIN(ABS(x1-x0-2*r), Abs(x1-x0-2*3*r));
				Energy += COST_OF_DIFF_WIDTH * dw;


				// inspects a trough before the peak (treaky since it can have different lengths)
				for (int j = x0; j < x1; j++)
					Energy += COST_OF_ABSENT_TROUGH * (bin_count_domin[j] > T2);
			}
		} // loop over peaks

		// minimize energy over the whole cw
		if (minE > Energy)
		{
			minE = Energy;
			bestR = r;
		}

	} // loop over w

	// debug for seeing where energy was added
	memset(bin_count, 0.0, NBINS*sizeof(float));
	Energy = 0;
	for (int ipeak=0; ipeak < CWPeaks.count; ipeak++)
	{

		float T, T2;
		float max_val, max_val2;
		int x0, x1, x2;

		// location and value of an extreme
		ind   = CWPeaks.ind_extreme[ipeak];
		max_val   =  bin_count_domin[ind];
		T = (PERC_MAX_POWER_ENG - noise/255.0) * max_val;  // peak
		T2 = (PERC_MAX_POWER_ENG + noise/255.0) * max_val; // trough

		x1 = ind - bestR;
		x2 = ind + bestR;

		// inspectes the peak
		for (int j = x1; j <= x2; j++)
			bin_count[j] = 10 * COST_OF_ABSENT_PEAK * (bin_count_domin[j] <= T);

		// sthreshold for a trough
		if (ipeak >0)
		{
			ind2   = CWPeaks.ind_extreme[ipeak-1];
			max_val2   =  bin_count_domin[ind2];
			T2 = (PERC_MAX_POWER_ENG + noise/255.0) * 0.5*(max_val+max_val2);

			x0 = ind2+bestR;

			// inspects a trough before the peak (treaky since it can have different lengths)
			for (int j = x0; j < x1; j++)
				bin_count[j] = 10*COST_OF_ABSENT_TROUGH * (bin_count_domin[j] > T2);

			// width deviations
			float dw = MIN(ABS(x1-x0-2*bestR), ABS(x1-x0-2*3*bestR));
			for (int j = x1; j > x1-dw; j--)
				bin_count[j] = 10 * COST_OF_DIFF_WIDTH;
			Energy += COST_OF_DIFF_WIDTH * dw;

		}


	} // loop over peaks

	// punishment for width difference
	av_energy_dw = Energy / CWPeaks.count;
	//show_hist_hist((TUint8*)im_hist);


	// Delete: arrange array of sides
	for (int ipeak=0; ipeak < CWPeaks.count; ipeak++)
	{
		stripe_xx[ipeak][0] = CWPeaks.ind_extreme[ipeak] - bestR;
		stripe_xx[ipeak][1] = CWPeaks.ind_extreme[ipeak] + bestR;
	}

} // EnergyModel()

/* Finds a maximum and power in bin_count_domin[],
* and destroys values within radius R of max in bin_count[];
* returns true, the power and ind of average power;
* if not successful returns false.
*/
bool FindMax(int n, int R, int &ind, float &power)
{
	// NOTE: R for destruction should be actually a stripe width
	// (destroyes half of dark and half of white width)

	float max_val = 0;
	power = 0;
	int skips;         // number of bins with little power
	bool skips_verify; // verify skips only away from boundary
	float weighted_ind = 0;
	int left, right;  // delimeters of the power and destuction region

	// find the first max
	for (int i = 0; i < n; i++)
	{
		if (max_val < bin_count[i])
		{
			max_val = bin_count[i];
			ind = i;
		}
	} // i

	// calculate power either within R+R/2 or when skips start
	left = MAX(0, ind-R-R/2);         // R/2 allows to count skips as well
	right = MIN(NBINS-1, ind+R+R/2);

	// don't look for skips at the border
	if (left==0 || right==NBINS-1)
	{
		for (int i = left; i <= right; i++) // just destroy bin counts and exit (no power or peak is found)!!!!!
			bin_count[i] = 0;
		skips_verify = false;
	}
	else
		skips_verify = true;

	if (!skips_verify)
		return false;

	// look on the left side
	skips = 0;
	for (int i = ind; i >= left; i--)
	{
		float val = bin_count_domin[i];

		//  a CONTINUOUS skip?
		if (val < max_val * PERC_MAX_POWER_MAX) // 0.3 of max_val is considered low power
			skips++;
		else
			skips = 0; // count only consequtive one

		// enough skips?
		if (skips>=R/2)
			break;
		else if (ind-i<R) // collect power only within R
		{
			power += val;		   // overall power
			weighted_ind += i*val; // weighted index for proper peak alighnment!
		}

	} // i

	// destroys all matter around max (coz not sufficient skips) and EXIT without max found
	if (skips_verify && skips < MIN(MIN_SKIPS, R/2) )
	{
		for (int i = left; i <= right; i++)
			bin_count[i] = 0;
		return false;
	}

	// look on the right side
	skips = 0;
	for (int i = ind+1; i <= right; i++)
	{
		float val = bin_count_domin[i];
		if (val < max_val * PERC_MAX_POWER_MAX)
			skips++;
		else
			skips = 0; // count only consequtive one

		if (skips>=R/2)
			break;
		else if (i-(ind+1)<R) // collect power only within R
		{
			power += val;          // overall power
			weighted_ind += i*val; // weighted index
		}
	} // i

	// destroys all matter around max (coz not sufficient skips) and EXIT without max found
	if (skips_verify && skips < MIN(MIN_SKIPS, R/2))
	{
		for (int i = left; i <= right; i++)
			bin_count[i] = 0;
		return false;
	}
	else
	{
		weighted_ind /= power;
		ind = int(weighted_ind + 0.5); // weighted index of the max!

		// destroy copy array only within R!!!
		left = MAX(0, ind-R/1.3);
		right = MIN(NBINS-1, ind+R/1.3);

		for (int i = left; i <= right; i++)
			bin_count[i] = 0;

		return true;
	}

} // FindMax( )



// Find peaks in the dominant orientation histogram
void FindPeaks()
{

	// 8. Find maximums in the histogram (we know from previous code an average CWrunav.stripe_width)
	float power = 0;
	int ind = 0, ind2;
	CWPeaks.count = 0;
	bool res;
	stripe_width_in_bins = CWrunav.stripe_width / BIN_STEP_DISTANCE+.5; // WHITE_PEAK_R_BINS before

	int attempts = 0;
	float min_power;

	do {
		// scan bin_count[ NBINS] for maximum destructively within R = stripe_width_in_bins, bin_count_domin retained as a copy;
		res = FindMax(NBINS, stripe_width_in_bins, ind, power);

		// mechanism for 2stripe CW to get rid of small peaks


		if(!Zebra_cw)
		{
			if (attempts == 0)
				min_power = power * MIN_POWER_RATIO_2STRIPE; // different tritment when applied to a second or third peaks...
		}
		else
			min_power = MIN_POWER;

		if (res && power >  min_power)
		{
			// valid peak? (e.g. dark matter on the sides)
			if (res)
			{
				CWPeaks.ind_extreme[CWPeaks.count] = ind;
				CWPeaks.power_extreme[CWPeaks.count] = power; // ???
				CWPeaks.count++;
			}
		}

		if (res && power < MIN_POWER ) // 0.3 max?
			break;

		// count attempts when power is big but verification did not work
		attempts++;
	}
	while (attempts < MAX_PEAKS); // try extract many peaks even if some aren't valid

} // FindPeaks()


// DetectCrosswalk()
JNIEXPORT int JNICALL
Java_cameraStream_CppFunctionsJNI_DetectCrosswalk
(JNIEnv *env, jclass jc, jintArray BlurIn, jintArray WhiteOut,
		jintArray WhiteLOut, jintArray HistOut,
		jdoubleArray debug_doubleOut, jlongArray debug_longOut) {

	jboolean isCopy1, isCopy2, isCopy3, isCopy4, isCopy5, isCopy6;

	jint *im_blur = env->GetIntArrayElements(BlurIn, &isCopy1);
	jint *im_white = env->GetIntArrayElements(WhiteOut, &isCopy2);
	jint *im_whiteL = env->GetIntArrayElements(WhiteLOut, &isCopy3);
	jint *im_hist = env->GetIntArrayElements(HistOut, &isCopy4);
	jdouble *debug_double = env->GetDoubleArrayElements(debug_doubleOut, &isCopy5);
	jlong *debug_long = env->GetLongArrayElements(debug_longOut, &isCopy6);

	// Init global variables
	CWrunav.detected = false;
	//Decisions[fnum] = 0;  // C++ specific
	crosswalk_result = ENotDefined;
	cw_reason = ENoCandidates;
	CWPeaks.valid_peaks = 0;
	CWPeaks.count = 0;

	minE = 0.0;
	bestR = 0.0;
	noise = 0.0;

	CWrunav.CWradius[0] = CWrunav.CWradius[1] = 0;
	CWrunav.stripe_width = 0;
	votes_ratio = 0;
	CWrunav.stripe_width = 0;
	av_energy_dw = 0; // debug

	// 0. Dominant orientation found?
	if (!local_max_found)
	{
		cw_reason = ENoDOminantLine;
		CWrunav.detected = 0;
		goto release_label; // no need for further processing if there is no dominant line
	}


	// 1. Discard lines that aren't parallel to dominant orientation
	ParallelLines();


	// 2. Estimate crosswalk size based on line coordinates
	if (!BoundingBox()) // false e.g. when lines are few or too short (cumulative length)
	{
		CWrunav.CWradius[0] = CWrunav.CWradius[1] = -1;
		CWrunav.detected = 0;
		goto release_label;
	}

	// 3. based on line polarity estimate location of white matter
	JustifyWhiteByLines(im_whiteL);

	// 4. Histogram white matter
	WhiteMatterHistograms(im_white, im_whiteL, im_blur);

	// 5. Smooth out the histogram
	SmoothHistograms();

	// Show bbox in the ground coordinate (uses closest_line as a global parameter)
	BoxGround(Rright, Rup);

	// Translate and show bbox in the image coordinates
	TranslateBox2D();

	// wide enough?
	if (2*CWrunav.CWradius[0] < MIN_ZEBRA_WIDTH)
	{
		CWrunav.detected = false;
		goto release_label;
	}

	// 6. Hough of the white matter (only for horizontal lines)
	HoughOfHistogram(im_hist);

	// Show histogram in the bitmap
	show_hist(im_hist);

	if (!Zebra_cw || (CWrunav.stripe_width > MIN_STRIPE_WIDTH &&
		CWrunav.stripe_width < MAX_STRIPE_WIDTH) )
	{

		// 7. Find peaks in the dominant orientation histogram (come sorted by max value, not power)
		FindPeaks();

		// Min number of peaks
		if (CWPeaks.count < ((!Zebra_cw)?2:MIN_PEAKS))
		{
			CWrunav.detected = false;
			goto release_label;
		}

		// Other tests
		if(!Zebra_cw)
		{
			// 8. Analogy of the energy model for 2 stripe (finds the width of the peaks in a smoothed hist)

			// for all peaks, define their width
			for (int ipeak=0; ipeak < CWPeaks.count; ipeak++)
			{
				int ind = CWPeaks.ind_extreme[ipeak]; // index
				float val_max = bin_count_domin[ind]; // max
				CWPeaks.width_meters[ind] = 0;        // initialize the width

				int bins_in_peak = 0;
				for (int j=ind; j<NBINS; j++)
				{
					if (bin_count_domin[j] > val_max * 0.2)
						bins_in_peak++;
					else
						break;
				}
				// uses 'ind' twice for round off purposes
				for (int j=ind; j>0; j--)
				{
					if (bin_count_domin[j] > val_max * 0.4)
						bins_in_peak++;
					else
						break;
				}

				CWPeaks.width_meters[ipeak] = bins_in_peak * BIN_STEP_DISTANCE;
			}


			// width/dist test for >=2 stripes
			if (Abs(CWPeaks.ind_extreme[1] - CWPeaks.ind_extreme[0]) * BIN_STEP_DISTANCE+.5 < CW_MIN_WIDTH ||
				CWPeaks.width_meters[0]  < MIN_STRIPE_WIDTH2 || CWPeaks.width_meters[0] > MAX_STRIPE_WIDTH2 ||
				CWPeaks.width_meters[1]  < MIN_STRIPE_WIDTH2 || CWPeaks.width_meters[1] > MAX_STRIPE_WIDTH2)

			{
				CWrunav.detected = false;
				goto release_label;
			}

			// exactly 2 stripes
			if (CWPeaks.count == 2)
			{
				CWrunav.detected = 2;
				goto release_label;
			}

			// more than 2 stripes just check the third peak for power or if failed for width
			else if (CWPeaks.count > 2)
			{
				if (CWPeaks.power_extreme[2] < CWPeaks.power_extreme[0] * MIN_POWER_RATIO_2STRIPE )
					CWrunav.detected = 2; // eliminate smaller peaks
				else
					CWrunav.detected = 0;

			}

		}
		else // Zebra model
		{
			// a good cue for stairs ()
			if (2.0*CWrunav.CWradius[1] / CWPeaks.count > STAIRS_THRESHOLD_LENGTH2PEKAS)
			{
				CWrunav.detected = false;
				goto release_label;
			}

			// 8a. Energy model that is more accurate than Hough (can incorporate missing stripes)
			EnergyModel();

			// Decision on Zebra
			if (minE / CWPeaks.count < THRESHOLD_COST)
				CWrunav.detected = 1;
			else
				CWrunav.detected = 0;

		} // energy model for Zebra

	} // if (CWrunav.stripe_width > 0.3 && CWrunav.stripe_width < 1.2 )

	// release Java variables
	release_label:

	// output
	debug_double[0] = CWrunav.CWcenter_domin;
	debug_double[1] = CWrunav.CWcenter_ortho;
	debug_double[2] = CWrunav.CWradius[0];
	debug_double[3] = CWrunav.CWradius[1];
	debug_double[4] = CWrunav.CWcenterGrnd.iX;
	debug_double[5] = CWrunav.CWcenterGrnd.iY;
	debug_double[6] = domin_line.dx;
	debug_double[7] = domin_line.dy;
	debug_double[8] = closest_line;
	debug_double[9] = 0;
	debug_double[10] = 0;

	debug_long[0] = max_vote;
	debug_long[1] = min_vote;

	// release
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(BlurIn, im_blur, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(BlurIn, im_blur, JNI_ABORT); // unpin

	if (isCopy2 == JNI_TRUE)
		env->ReleaseIntArrayElements(WhiteOut, im_white, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(WhiteOut, im_white, JNI_ABORT); // unpin

	if (isCopy3 == JNI_TRUE)
		env->ReleaseIntArrayElements(WhiteOut, im_whiteL, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(WhiteOut, im_whiteL, JNI_ABORT); // unpin

	if (isCopy4 == JNI_TRUE)
		env->ReleaseIntArrayElements(HistOut, im_hist, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(HistOut, im_hist, JNI_ABORT); // unpin

	// debug var
	if (isCopy5 == JNI_TRUE)
		env->ReleaseDoubleArrayElements(debug_doubleOut, debug_double, 0);         // copy and unpin
	else
		env->ReleaseDoubleArrayElements(debug_doubleOut, debug_double, JNI_ABORT); // unpin

	// debug var
	if (isCopy6 == JNI_TRUE)
		env->ReleaseLongArrayElements(debug_longOut, debug_long, 0);         // copy and unpin
	else
		env->ReleaseLongArrayElements(debug_longOut, debug_long, JNI_ABORT); // unpin


	if (CWrunav.detected)
		if (Zebra_cw)
			return 1;
		else
			return 2;
	else
		return 0;


} // DetectCW()


// import lines into Java
JNIEXPORT int JNICALL
Java_cameraStream_CppFunctionsJNI_getLines2Java
(JNIEnv *env, jclass jc, jintArray linesOut, jintArray linesGroundOut) {

	// Java to C++ arrays
	jboolean isCopy1, isCopy2;
	jint *lines = env->GetIntArrayElements(linesOut, &isCopy1); // only output
	jint *linesGrnd = env->GetIntArrayElements(linesGroundOut, &isCopy2); // only output


	// copy to Java (for drawing)
	for (int n = 0; n < nlines; n++) {

		linesGrnd[n * 5 + 4] = LineArray[n].valid ;

		if (!LineArray[n].valid)
			continue;

		lines[n * 5 + 0] = LineArray[n].head.iX;
		lines[n * 5 + 1] = LineArray[n].head.iY;
		lines[n * 5 + 2] = LineArray[n].tail.iX;
		lines[n * 5 + 3] = LineArray[n].tail.iY;
		lines[n * 5 + 4] = LineArray[n].polarity;

		linesGrnd[n * 5 + 0] = LineArray[n].headGrnd.iX * SCALE_GRND + IMAGE_W/2; // zero is in the mid-screen (xshift = IMAGE_W/2)
		linesGrnd[n * 5 + 1] = IMAGE_H - 10 - LineArray[n].headGrnd.iY * SCALE_GRND;
		linesGrnd[n * 5 + 2] = LineArray[n].tailGrnd.iX * SCALE_GRND + IMAGE_W/2; // zero is in the mid-screen (xshift = IMAGE_W/2)
		linesGrnd[n * 5 + 3] = IMAGE_H - 10 - LineArray[n].tailGrnd.iY * SCALE_GRND;

	}

	// release
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(linesOut, lines, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(linesOut, lines, JNI_ABORT); // unpin only
	if (isCopy2 == JNI_TRUE)
		env->ReleaseIntArrayElements(linesGroundOut, linesGrnd, 0);         // copy and unpin
	else
		env->ReleaseIntArrayElements(linesGroundOut, linesGrnd, JNI_ABORT); // unpin only

}

#ifdef __cplusplus
}
#endif

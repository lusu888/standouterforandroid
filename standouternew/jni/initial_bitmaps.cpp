#include <math.h>
#include <string.h>
#include <jni.h>
#include <cpu-features.h>
#include "TPoint.h"
#include "Parameters.h"

#ifndef _DESKTOP_
#define JNIEXPORT
#define JNICALL
#endif

#ifdef __cplusplus
extern "C" {
#endif

//float average_intensity;

//#define STRIP_THE_CODE


// to compile cd /cygdrive/c/eclipse_workspace/workspace/CameraAccess3/jni
// then call /cygdrive/c/android-sdk-windows/android-ndk-r4b/ndk-build 

// debug function sumArray()
JNIEXPORT int JNICALL 
Java_com_cameraStream_CppFunctionsJNI_sumArray
(JNIEnv *env, jclass jc, jint sz, jintArray in) {
	jint *data = env->GetIntArrayElements(in, 0);
	int out= 0;
	for (int i=0; i<sz; i++)
		out += data[i];
    env->ReleaseIntArrayElements(in, data, 0);
    //return out;
    TPoint a;
    a.SetXY(3, 4);
    return (a.iX);
} // sumArray()

// unpack()
JNIEXPORT void JNICALL 
Java_com_cameraStream_CppFunctionsJNI_unpack
(JNIEnv *env, jclass jc, jint sz, jintArray rgbIn, 
		 jintArray redOut,  jintArray greenOut, jintArray blueOut) {

	// Java to C++ arrays
	jboolean isCopy1, isCopy2, isCopy3; // copy flags

	jint *rgb_pix = env->GetIntArrayElements(rgbIn, 0);
	jint *red_pix = env->GetIntArrayElements(redOut, &isCopy1);
	jint *green_pix = env->GetIntArrayElements(greenOut, &isCopy2);
	jint *blue_pix = env->GetIntArrayElements(blueOut, &isCopy3);
	
	#ifndef STRIP_THE_CODE	

	// ----------------------------------  process
	for (int i=0; i<sz; i++)
	{
		red_pix[i]   = (rgb_pix[i] >> 16) & 0xFF; // (color >> 16) & 0xFF
		green_pix[i] = (rgb_pix[i] >> 8 ) & 0xFF;  // (color >> 8) & 0xFF
		blue_pix[i]  =  rgb_pix[i] & 0xFF;         // color & 0xFF
	}

	#endif
	
	// release
	env->ReleaseIntArrayElements(rgbIn, rgb_pix, JNI_ABORT); // Free the buffer without copying back any changes
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(redOut, red_pix, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(redOut, red_pix, JNI_ABORT);

	if (isCopy2 == JNI_TRUE)
		env->ReleaseIntArrayElements(greenOut, green_pix, 0);
	else
		env->ReleaseIntArrayElements(greenOut, green_pix, JNI_ABORT);

	if (isCopy3 == JNI_TRUE)
		env->ReleaseIntArrayElements(blueOut, blue_pix, 0);
	else
		env->ReleaseIntArrayElements(blueOut, blue_pix, JNI_ABORT);
	 
} // unpack()

// pack()
JNIEXPORT void JNICALL 
Java_com_cameraStream_CppFunctionsJNI_pack
(JNIEnv *env, jclass jc, jint sz, jintArray rgbOut, 
		 jintArray redIn,  jintArray greenIn, jintArray blueIn) {
	
	// Java to C++ arrays
	jboolean isCopy1; // copy flags

	jint *rgb_pix = env->GetIntArrayElements(rgbOut, &isCopy1);
	jint *red_pix = env->GetIntArrayElements(redIn, 0);
	jint *green_pix = env->GetIntArrayElements(greenIn, 0);
	jint *blue_pix = env->GetIntArrayElements(blueIn, 0);
	
	#ifndef STRIP_THE_CODE
	// ----------------------------------  process
	for (int i=0; i<sz; i++)
	{
		rgb_pix[i] = (0xFF) << 24 | (red_pix[i]) << 16 | (green_pix[i]) << 8
				| blue_pix[i];
	}
	#endif

	// release
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(rgbOut, rgb_pix, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(rgbOut, rgb_pix, JNI_ABORT);
	env->ReleaseIntArrayElements(redIn, red_pix, JNI_ABORT); // Free the buffer without copying back any changes
	env->ReleaseIntArrayElements(greenIn, green_pix, JNI_ABORT);
	env->ReleaseIntArrayElements(blueIn, blue_pix, JNI_ABORT);

} // pack()

// zeroArray()
JNIEXPORT void JNICALL 
Java_com_cameraStream_CppFunctionsJNI_zeroArray
(JNIEnv *env, jclass jc, jint sz, jintArray arOut) {
	
	// Java to C++ arrays
	jboolean isCopy1; // copy flags
	jint *ar_pix = env->GetIntArrayElements(arOut, &isCopy1);
	
	// process
	#ifndef STRIP_THE_CODE
	memset(ar_pix, 0, sizeof(jint)*sz);
	#endif
	
	// release
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(arOut, ar_pix, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(arOut, ar_pix, JNI_ABORT);

} // zeroArray()

// copyArray()
JNIEXPORT void JNICALL
Java_com_cameraStream_CppFunctionsJNI_copyArray
(JNIEnv *env, jclass jc, jint sz, jintArray arIn, jintArray arOut) {

	// Java to C++ arrays
	jboolean isCopy1; // copy flags
	jint *ar_in = env->GetIntArrayElements(arIn, 0);
	jint *ar_out = env->GetIntArrayElements(arOut, &isCopy1);

	// process
	#ifndef STRIP_THE_CODE
	memcpy(ar_out, ar_in, sizeof(jint)*sz);
	#endif

	// release
	env->ReleaseIntArrayElements(arIn, ar_in, JNI_ABORT); // Free the buffer without copying back any changes
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(arOut, ar_out, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(arOut, ar_out, JNI_ABORT);

} // copyArray()

// getGray()
JNIEXPORT float JNICALL
Java_com_cameraStream_CppFunctionsJNI_getGray
(JNIEnv *env, jclass jc, jint sz, jintArray grayOut, 
		 jintArray redIn,  jintArray greenIn, jintArray blueIn) {
	
	// Java to C++ arrays
	jboolean isCopy1; // copy flags

	jint *im_gray = env->GetIntArrayElements(grayOut, &isCopy1);
	jint *red_pix = env->GetIntArrayElements(redIn, 0);
	jint *green_pix = env->GetIntArrayElements(greenIn, 0);
	jint *blue_pix = env->GetIntArrayElements(blueIn, 0);
	
	// ----------------------------------  process
	#ifndef STRIP_THE_CODE

	float average_intensity = 0.0f;
	for (int i=0; i<sz; i++)
	{
		//int gray = red_pix[i] + 2*green_pix[i] + blue_pix[i];
		//im_gray[i] = gray >> 2;

		// the cast to float avoid default cast to double that makes it inconsistent
		// with C++ for some marginal cases such as rgb=9,10,13 grey = 8 or 9?
		int gray = (float) (0.2989f * red_pix[i] + 0.5870f * green_pix[i] + 0.1140f
				* blue_pix[i] + 0.5f);

		if (gray > 255 )
			gray = 255;
		else
			gray = gray;

		im_gray[i] = gray;
		average_intensity += gray;

	}
	average_intensity /= sz;


	#endif
	
	// release
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(grayOut, im_gray, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(grayOut, im_gray, JNI_ABORT);

	env->ReleaseIntArrayElements(redIn, red_pix, JNI_ABORT); // Free the buffer without copying back any changes
	env->ReleaseIntArrayElements(greenIn, green_pix, JNI_ABORT);
	env->ReleaseIntArrayElements(blueIn, blue_pix, JNI_ABORT);

	return(average_intensity);

} // getGray()

// get Gray from YUV
JNIEXPORT float JNICALL
Java_com_cameraStream_CppFunctionsJNI_YUVtoGray
(JNIEnv *env, jclass jc, jint sz, jintArray grayOut, jbyteArray YIn) {

	//return 100.0f;

	// Java to C++ arrays
	jboolean isCopy1; // copy flags

	jint *im_gray = env->GetIntArrayElements(grayOut, &isCopy1);
	jbyte *im_Y = env->GetByteArrayElements(YIn, 0);

	// ----------------------------------  process
	#ifndef STRIP_THE_CODE

	float average_intensity = 0.0f;
	for (int i=0; i<sz; i++)
	{
		int gray = im_Y[i] & 0xff;
		average_intensity += (float)gray;
		im_gray[i] = gray; // & 0xff

	}
	average_intensity /= sz;

	#endif

	// release
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(grayOut, im_gray, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(grayOut, im_gray, JNI_ABORT);

	env->ReleaseByteArrayElements(YIn, im_Y, JNI_ABORT); // Free the buffer without copying back any changes

	return(average_intensity);
}


// getBlur()
JNIEXPORT void JNICALL 
Java_com_cameraStream_CppFunctionsJNI_getBlur
(JNIEnv *env, jclass jc, jintArray blurOut, jintArray grayIn) {
	
	// Java to C++ arrays
	jboolean isCopy1; // copy flags

	jint *im_gray = env->GetIntArrayElements(grayIn, 0);
	jint *im_blur = env->GetIntArrayElements(blurOut, &isCopy1);
	
	// ----------------------------------  process
#ifndef STRIP_THE_CODE

	int offset, offset2, offset3, x;

	// loops over bitmap bytes, lernel = [ 1 2 1]/4
	for (offset = 0; offset < IMAGE_H * IMAGE_W; offset += IMAGE_W) {
		for (x = 1; x < IMAGE_W - 1; x++) {
			int sum = im_gray[offset + x - 1] + 2 * im_gray[offset + x]
					+ im_gray[offset + x + 1];
			temp_ar[offset + x] = sum >> 2; // temporal array described in Parameters.h
		}
	} // intermediate output/input

	// loops over intermediate input bytes [ 1; 2; 1]/4
	for (offset = 0, offset2 = IMAGE_W, offset3 = 2 * IMAGE_W; offset < IMAGE_W
			* IMAGE_H - 2 * IMAGE_W; offset = offset2, offset2 = offset3, offset3
			+= IMAGE_W) {
		for (x = 0; x < IMAGE_W; x++) {
			int sum = temp_ar[offset + x] + 2 * temp_ar[offset2 + x]
					+ temp_ar[offset3 + x];
			im_blur[offset2 + x] = sum >> 2;
		}
	} // output

	// fill out periphery by copies of the neighbors (needed
	// for consistency since default values may vary across platforms)

	jint *im_blur2 = im_blur + IMAGE_W; // neighboring row

	for (x=1; x<IMAGE_W-1; x++)
		im_blur[x] = im_blur2[x]; // top

	im_blur += IMAGE_W*(IMAGE_H-1);
	im_blur2 = im_blur - IMAGE_W;
	for (x=1; x<IMAGE_W-1; x++)
		im_blur[x] = im_blur2[x]; // bottom

	// restore the pointer
	im_blur = env->GetIntArrayElements(blurOut, &isCopy1);

	im_blur2 = im_blur+1; // neighboring column

	for (x=0; x<IMAGE_H*IMAGE_W; x+=IMAGE_W)
		im_blur[x] = im_blur2[x]; // left side

	im_blur += IMAGE_W-1;
	im_blur2 = im_blur-1;
	for (x=0; x<IMAGE_H*IMAGE_W; x+=IMAGE_W)
		im_blur[x] = im_blur2[x]; // right side

#endif

	// release

	env->ReleaseIntArrayElements(grayIn, im_gray, JNI_ABORT); // Free the buffer without copying back any changes
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(blurOut, im_blur, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(blurOut, im_blur, JNI_ABORT);

} // getBlur

// ported from C++
JNIEXPORT void JNICALL 
Java_com_cameraStream_CppFunctionsJNI_getGrad
(JNIEnv *env, jclass jc, 
		jintArray blurIn, jintArray gradOut, jintArray gradbwOut, 
		jintArray signOut, jintArray sign2Out) {

	// Java to C++ arrays
	jboolean isCopy1, isCopy2, isCopy3, isCopy4; // copy flags

	jint *im_blur = env->GetIntArrayElements(blurIn, 0);
	jint *im_edge = env->GetIntArrayElements(gradOut, &isCopy1);
	jint *im_edge_bw = env->GetIntArrayElements(gradbwOut, &isCopy2);
	jint *im_edge_sign = env->GetIntArrayElements(signOut, &isCopy3);
	jint *im_edge_sign2 = env->GetIntArrayElements(sign2Out, &isCopy4);

	#ifndef STRIP_THE_CODE
	int x, offset, offset2, offset3, ind; // indices
	int *center_pxl;


	// 1. dI/dx, filter = [-1 0 1], result in 0	
	for (offset = 0; offset<IMAGE_SZ; offset += IMAGE_W)
	{
		for (x=1; x<IMAGE_W-1; x++)
		{
			int grad = im_blur[offset+x+1] - im_blur[offset+x-1];
			im_edge[offset+x] = ABS (grad);

			// record the sign
			grad += 128;
			if (grad>255)
				grad = 255;
			else if (grad<0)
				grad = 0;	
			im_edge_sign2[offset+x] = grad;
		}
	} // end of bitmap loop

	// 1a. Fill out the left and right sides
	for (offset = 0; offset<IMAGE_SZ; offset += IMAGE_W)
	{
		im_edge[offset] = 0;
		im_edge[offset+IMAGE_W-1] = 0;
	} // end of bitmap loop

	// 2. dI/dy, filter = [-1; 0; 1]
	for (offset = 0, offset2 = IMAGE_W, offset3 = IMAGE_W+IMAGE_W; // initial condition
	offset<IMAGE_SZ-2*IMAGE_W;								       // exit condition
	offset = offset2, offset2 = offset3, offset3 += IMAGE_W)       // increments
	{
		for (x=0; x<IMAGE_W; x++)
		{
			int grad = im_blur[offset+x] - im_blur[offset3+x];
			im_edge_bw[offset2+x] = ABS (grad);

			// record the sign
			grad += 128;
			if (grad>255)
				grad = 255;
			else if (grad<0)
				grad = 0;	
			im_edge_sign[offset2+x] = grad;
		}
	} // end of bitmap loop

	// 2a. Fill out the top and the bottom
	for (x=0; x<IMAGE_W; x++)
	{
		im_edge_bw[x] = 0;
		im_edge_bw[x+IMAGE_SZ-IMAGE_W] = 0;
		im_edge_sign[x] = 128;
		im_edge_sign[x+IMAGE_SZ-IMAGE_W] = 128;
	} // end of bitmap loop

	for (x=0; x<IMAGE_H; x++)
	{
		im_edge_sign2[x*IMAGE_W] = 128;
		im_edge_sign2[x*IMAGE_W+IMAGE_W-1] = 128;
	}

	// 3. Threshold and Non-max suppression starting along x
	for (offset = 0; offset<IMAGE_SZ; offset += IMAGE_W)
	{
		for (x=2; x<IMAGE_W-2; x++)
		{

			center_pxl = &(im_edge[offset+x]);

			if (    *center_pxl > THRESHOLD_EDGE &&
					*center_pxl >= *(center_pxl+1) &&
					*center_pxl >= *(center_pxl-1) &&
					*center_pxl >= *(center_pxl+2) &&
					*center_pxl >= *(center_pxl-2))
			{
				// destructive writing and Bitmap marks
				*(center_pxl+1) = 0;
				*(center_pxl-1) = 0;
				*(center_pxl+2) = 0;
				*(center_pxl-2) = 0;
			}
			else
			{
				*(center_pxl) = 0;
			}
		}
	}

	// 4. Non max suppression along y
	for (offset = 2*IMAGE_W; offset<IMAGE_SZ-2*IMAGE_W; offset += IMAGE_W)
	{
		for (x=0; x<IMAGE_W; x++)
		{
			center_pxl = &(im_edge_bw[offset+x]);

			// local maximum within neighborhood of 2
			if (    *center_pxl > THRESHOLD_EDGE &&
					*center_pxl >= *(center_pxl+IMAGE_W) &&
					*center_pxl >= *(center_pxl-IMAGE_W) &&
					*center_pxl >= *(center_pxl+IMAGE_W+IMAGE_W) &&
					*center_pxl >= *(center_pxl-IMAGE_W-IMAGE_W))
			{
				// destructive writing and Bmap marks
				*(center_pxl+IMAGE_W) = 0;
				*(center_pxl-IMAGE_W) = 0;
				*(center_pxl+IMAGE_W+IMAGE_W) = 0;
				*(center_pxl-IMAGE_W-IMAGE_W) = 0;
			}
			else
			{
				*(center_pxl) = 0;
			}			
		}
	}

	// 5. unite bitmaps to get amplitude
	for (ind = 0; ind<IMAGE_SZ; ind ++)
	{
		im_edge[ind] = MAX(im_edge[ind], im_edge_bw[ind]);

		// debug correct sign bitmap for non max supression and thresholding
		if (im_edge[ind] < THRESHOLD_EDGE)
		{
			im_edge_bw[ind] = 0; // debug bitmap
		}
		else
			im_edge_bw[ind] = 255;
	}
	#endif

	// release
	env->ReleaseIntArrayElements(blurIn, im_blur, JNI_ABORT); // Free the buffer without copying back any changes
	if (isCopy1 == JNI_TRUE)
		env->ReleaseIntArrayElements(gradOut, im_edge, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(gradOut, im_edge, JNI_ABORT);
	if (isCopy2 == JNI_TRUE)
		env->ReleaseIntArrayElements(gradbwOut, im_edge_bw, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(gradbwOut, im_edge_bw, JNI_ABORT);
	if (isCopy3 == JNI_TRUE)
		env->ReleaseIntArrayElements(signOut, im_edge_sign, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(signOut, im_edge_sign, JNI_ABORT);
	if (isCopy4 == JNI_TRUE)
		env->ReleaseIntArrayElements(sign2Out, im_edge_sign2, 0); // Copy the contents of the buffer back into array and free the buffer
	else
		env->ReleaseIntArrayElements(sign2Out, im_edge_sign2, JNI_ABORT);

}




#ifdef __cplusplus
}
#endif

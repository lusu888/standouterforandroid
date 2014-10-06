package com.standouter.standouternew;


//  public class before
public class CppFunctionsJNI {

	static {
        System.loadLibrary("processJNI");
    }
	
	// zero int arrays
	static native void zeroArray(int sz, int [] arOut);
	
	// init
	static native void initialize(int file_number);
	static native void initializeCam(int file_number);
	
	// unpack int into rgb
	static native void unpack(int sz, int[] pixels, int[] redPixels,
			int[] greenPixels, int[] bluePixels); 	
	
	// pack rgb into integer
	static native void pack(int sz, int[] pixels, int[] redPixels,
			int[] greenPixels, int[] bluePixels);
	
	// get gray from rgb
	static native float getGray(int sz, int[] grayOut, 
			int[] redIn, int[] greenIn, int[] blueIn);	
	
	// get blur from gray
	static native void getBlur(int[] blurOut, int[] grayIn);	
	
	// get grad from gray
	static native void getGrad(int[] blurIn, 
			int[] gradOut, int[] gradbwOut, int[] signOut, int[] sign2Out);
	
	static native void copyArray(int sz, int[] arIn,  int[] arOut);
	
	/* input: grad, sing, sign2
	 * output: linesOut = x1, y1, x2, y2, polarity (most of the output for inner use within C++)
	 * return: nlines
	*/
	static native int getLines(boolean Zebra_cw_flag, int[] gradIn, int[] signIn, 
			int[] sign2In);
	
	// Undo Perspective
	static native void undoPerspective(boolean unwarp_image, 
			int[] grayIn, int[] grndOut);
	
	static native boolean DominantOrientation(float[] dominant);
	
	static native void ThreshouldWhite(int[] blurIn);
	
	static native void WhiteBitmap(int[] blurIn, int[] whiteOut);
	
	static native int DetectCrosswalk(int[] blurIn, int[] WhiteOut, 
			int[] WhiteLOut, int[] HistOut, double[] debug_double, long[] debug_long); // returns the type of a crosswalk (0-fa, 1-zebra, 2-2stripe)
	// float cwOut: center_domin/ortho, Rdomin/ortho, CWcenterGrnd.iX/iY, domin_lkine.dx, dy
	
	// interface with lines in Java
	static native void getLines2Java(int[] linesOut, int[] linesGrndOut);
	
	static native float YUVtoGray(int sz, int[] grayOut, byte[] YIn);
	
}



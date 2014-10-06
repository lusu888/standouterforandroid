#ifndef EDGELET_H_
#define EDGELET_H_

#include "Parameters.h"
#include "TPoint.h"
#include "TPointR.h"

enum line_status
	{
	EShortLine = 100,  // just the code that > 2*PI
	};


class TEdgelet
{

public:

	bool polarity;		// positive=true (i.e dark followed by light when moving up), negative = false
	int grad;			// gradient; not used much? see polarity as a binary counterpart
	TPoint head, tail;   // head (left) and tail (right) either in 2D or in 3D
	TPointR headGrnd, tailGrnd; // head (left) and tail (right) either in 2D or in 3D
	TPointR headDom, tailDom;   // projection of ground on iX = domin_line, iY =  domin_line_normal;
	//float headx, heady, headz, tailx, taily, tailz;
	float dx, dy;       // unit vector either in 2D or in 3D
	float rad0;         // initial orientation rad0=NOT_INITIALIZED when not initialized; used to track deviations during line grow
	float rad, radGrnd; // orientation in rad;
	int npxl;          // number of pixels; might be noe very accurate due to the jumps over 1 pixel
	float length;       // length in 2D
	bool valid;        // 1 when has a proper length in 3D and also a pair line in stripe

	// to be depreciated

	int rgb[6]; // mean rgb 3 pxl. bottom and top
	bool has_pair; // pair for a stripe line in 2 stripe cwalk
	float saturation[2]; // saturation on the left and on the right samples according to RGB
	float belief; // obsolete; probability that line belongs to figure (crosswalk)

public:

bool initAndGrow_hor(TPoint pos, const int* bitmapEdgePtr, const int *bitmapSignPtr, int thresh); 		// grows an edgelet
bool initAndGrow_ver(TPoint pos, const int* bitmapEdgePtr, const int *bitmapSignPtr, int thresh);
bool isLinear();               // verifies that an edgelet is linear by comparing initial and current angles
float len();                    // physical length of an edgelet (slow)
float len2();                  // = len()*len()
float ShortestDist(TEdgelet *other_line);
float ShortestDist(TPoint mid);
float length3D(); // length in 3D, used for validation after UndoPerspective
};
#endif /*EDGELET_H_*/

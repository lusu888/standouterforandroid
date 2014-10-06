#include "Parameters.h"
#include "edgelet.h"
#include "tan_tabulated.h"

#define GROWTH_CONE_HOR   5 // 3 WARNING: make sure to adjust initialization of the array!!!
#define GROWTH_CONE_VER   5 //
#define MAX_PIXELS_IN_EDGELET (IMAGE_W+IMAGE_W)


static int* winnerPtr_array[MAX_PIXELS_IN_EDGELET];

// SHortest signed distance from the line direction to the midpoint of anoter line

float TEdgelet::ShortestDist(TEdgelet *other_line)
{
	// shortest signed distance from the midpoint of the other line to the calling line
	// along normal of the calling line; relies on dy, dx; MIdpoints helps when the
	// lines aren't excatly parallel.

	// midpoint of other_line
	TPoint mid((other_line->tailGrnd.iX + other_line->headGrnd.iX)/2,
		(other_line->tailGrnd.iY + other_line->headGrnd.iY)/2) ;

	// description of the current line via normal and P0 (e.g. head):
	float line_normal[2];
	line_normal[0] = dy; // we know that dy>0
	line_normal[1] = -dx;

	//---  along calling line normal:

	// Signed distance from origin to line1 = head*normal
	float d = line_normal[0]*headGrnd.iX + line_normal[1]*headGrnd.iY;

	// Signed distance from origin to mid of the other_line along the normal to the current line = mid*normal
	float d2 = line_normal[0]*mid.iX + line_normal[1]*mid.iY;

	return (d-d2);

}

// Overload  - shortest distance from the point to the line

float TEdgelet::ShortestDist(TPoint mid)
{
	// shortest signed distance from the midpoint of the other line to the calling line
	// along normal of the calling line; relies on dy, dx calculated BEFORE UndoPerspective
	// and CleanLines (functions where it is called).
	// description of the current line via normal and P0 (e.g. head):
	float line_normal[2];
	line_normal[0] = dy; // we know that dy>0
	line_normal[1] = -dx;

	//---  along calling line normal:

	// Signed distance from origin to line1 = head*normal
	float d = line_normal[0]*headGrnd.iX + line_normal[1]*headGrnd.iY;

	// Signed distance from origin to mid of the other_line along the normal to the current line = mid*normal
	float d2 = line_normal[0]*mid.iX + line_normal[1]*mid.iY;

	return (d-d2);

}



// checks if the line long enough to define linearity
// if it not long enough sets up initial orientation rad0 and returns true;
// if the line is long it sets up its rad measure;
// returns true if the line doesn't deviate too much from rad0 otherwice returns false

bool TEdgelet::isLinear()
{

	// line width along X
	float cur_dx = head.iX-tail.iX; // was tail-head before
	float cur_dy = head.iY-tail.iY; // was tail-head before

	// calculate orientation
	if ( rad0==EShortLine) // called only once!
	{
		rad0 = getATan (cur_dy, cur_dx); // init rad0
		rad = rad0; // BUG found rad0->rad
	}
	else // recalculate orientation as line growth
	{
		rad = getATan (cur_dy, cur_dx);
	}

	if ( ANG_DIF(Abs(rad0-rad)) > RAD_THRESHOLD(npxl) // 1 equals to 6 deg/10 pix, 2: 3deg./pxl
		return false;
	else
		return true;

	return false;
} // isLinear()



// Grow tail horizontally and vertically
// both bitmap and "holdying camera" are in Landscape orientation

bool TEdgelet::initAndGrow_hor(TPoint pos, const int *bitmapEdgePtr,
								const int *bitmapSignPtr, int thresh)
{

	// keeps track of picked edges and postpones their destruction in the edge map until line is long enough
	// growth cone is 3 pixels

	head.SetXY (pos.iX, pos.iY);
	tail.SetXY (pos.iX, pos.iY);
	rad0 = EShortLine;  	// means no initialization yet
	npxl = 0;               // underestimates the actual number of pixels

	int base; // base address of the initialtion point
	TPoint c[GROWTH_CONE_HOR]; // 3 relative indices of continuation points
	int imax = 0; // index of max edge point
	int val, maxval, adr;
	int *winnerPtr, *winnerPtr2; // abs. address of the winner with max intensity	in the growth cone

	// boundary condition in Landscape mode/Portrait holding orientation (2 pixels for growth +border)
	while ((head.iX>R_FILT+2) && (head.iX<IMAGE_W-R_FILT-2) &&
		(head.iY>R_FILT+2) && (head.iY<IMAGE_H-R_FILT-2))
	{
		// address of a head
		base = (int)(head.iY*IMAGE_W+head.iX);

		// right of the head
		c[0].iX = 1;
		c[1].iX = 1;
		c[2].iX = 1;
		c[3].iX = 1;
		c[4].iX = 1;

		c[0].iY = 0;
		c[1].iY = -1;
		c[2].iY = 1;
		c[3].iY = -2;
		c[4].iY = 2;

		// index of the winner (0..5)
		maxval = 0;
		for (int k=0; k<GROWTH_CONE_HOR-2; k++) // up to 45 deg.
		{
			adr = base+IMAGE_W*c[k].iY+1;

			bool grad_sign = bitmapSignPtr[adr]>128;
			if (grad_sign != polarity)
				continue;

			val = bitmapEdgePtr[adr];
			if ( maxval < val)
			{
				maxval = val; // check for polarities?
				imax=k;
			}
		}

		if ( maxval < thresh) // try to go over the gap
		{
			if (ALLOW_JUMP1PIX_LINE_EXTRACTION)
			{

				// 2 pix. right from the head
				c[0].iX = 2;
				c[1].iX = 2;
				c[2].iX = 2;
				c[3].iX = 2;
				c[4].iX = 2;

				// index of the winner (0..3)
				maxval = 0;
				for (int k=0; k<GROWTH_CONE_HOR; k++)
				{
					adr = base + IMAGE_W*c[k].iY + 2;
					bool grad_sign = bitmapSignPtr[adr] > 128;
					if (grad_sign != polarity)
						continue;

					val = bitmapEdgePtr[adr];
					if ( maxval < val )
					{
						maxval = val;
						imax=k;
					}
				}
			}

			// 1. break because of threshold
			if ( !ALLOW_JUMP1PIX_LINE_EXTRACTION || maxval < thresh )
			{
				break;
			}
			else
			{
				// address of a winner
				winnerPtr2 = (int*)&(bitmapEdgePtr[base + IMAGE_W*c[imax].iY + c[imax].iX ]);
				winnerPtr_array[npxl] = winnerPtr2;
				npxl++; // increment the number of pixels
			}
		} // if ( *winnerPtr < thresh)
		else
		{
			// address of a winner
			winnerPtr = (int*)&(bitmapEdgePtr[base + IMAGE_W*c[imax].iY + c[imax].iX ]);
			winnerPtr_array[npxl] = winnerPtr;
			npxl++; //*winnerPtr> thresh
		}
		// move the tail either 1 or 2 pixels
		head.iX += c[imax].iX;
		head.iY += c[imax].iY;

		//  2. BREAK BECAUSE OF NON-LINEARITY when it is long enough
		if (npxl>MIN_LEN_ANG && !isLinear())
			break;

	} // while growing a head horizontally

	if ( npxl >= MIN_LINE_REGISTER) // previously MIN_LEN_ANG
	{
		// destroy used edges
		for (int k=0; k<npxl; k++)
		{
			*(winnerPtr_array[k]) = 0;
		}

		return true; // BUG source!!!!!!!
	}
	else
		return false;

} // initAndGrow_hor(TPoint pos, const int *bitmapPtr, const int *bitmapBlurPtr, int thresh)

/*
 * Grow tail horizontally and vertically
 * both bitmap and "holdying camera" are in Landscape orientation
 */

// grows vertical lines
bool TEdgelet::initAndGrow_ver(TPoint pos, const int* bitmapEdgePtr, const int *bitmapSignPtr, int thresh)
	{

	// keeps track of picked edges and postpones their destruction in the edge map until line is long enough
	// growth cone is 5 pixels

	head.SetXY (pos.iX, pos.iY);
	tail.SetXY (pos.iX, pos.iY);
	rad0 = EShortLine; 	// means no initialization yet
	npxl = 0;

	int base; // base address of the initialtion point
	TPoint c[GROWTH_CONE_VER]; // 3 relative indices of continuation points
	int imax = 0; // index of max edge point
	int val, maxval, adr;
	int *winnerPtr, *winnerPtr2; // abs. address of the winner with max intensity

	// boundary condition in Landscape mode/Portrait holding orientation (2 pixels for growth +border)
	while ((head.iX>R_FILT+2) && (head.iX<IMAGE_W-R_FILT-2) &&
		(head.iY>R_FILT+2) && (head.iY<IMAGE_H-R_FILT-2))
	{
		// address of a head
		base = (int)(head.iY*IMAGE_W+head.iX);

		// top of the head
		c[0].iY = -1;
		c[1].iY = -1;
		c[2].iY = -1;
		c[3].iY = -1;
		c[4].iY = -1;

		c[0].iX = 0;
		c[1].iX = -1;
		c[2].iX = 1;
		c[3].iX = -2;
		c[4].iX = 2;

		// index of the winner (0..3)
		maxval = 0;
		for (int k=0; k<GROWTH_CONE_VER; k++) // up to 45 deg.
		{
			adr = base-IMAGE_W+c[k].iX;

			bool grad_sign = bitmapSignPtr[adr]>128;
			if (grad_sign != polarity)
				continue;

			val = bitmapEdgePtr[adr];
			if ( maxval < val)
			{
				maxval = val; // check for polarities?
				imax=k;
			}
		}

		if ( maxval < thresh ) // try to go over the gap
		{
			if (ALLOW_JUMP1PIX_LINE_EXTRACTION)
			{

				// 2 pix. over the head
				c[0].iY = -2; //c[0].iX = -2;
				c[1].iY = -2; //c[1].iX = -1;
				c[2].iY = -2; //c[2].iX = 0;
				c[3].iY = -2; //c[3].iX = 1;
				c[4].iY = -2; //c[4].iX = 2;

				// index of the winner (0..5)
				maxval = 0;
				for (int k=0; k<GROWTH_CONE_VER; k++)
				{
					adr = base-2*IMAGE_W+c[k].iX;
					bool grad_sign = bitmapSignPtr[adr] > 128;
					if (grad_sign != polarity)
						continue;

					val = bitmapEdgePtr[adr];
					if ( maxval < val )
					{
						maxval = val;
						imax=k;
					}
				}
			}

			// 1. break because of threshold
			if ( !ALLOW_JUMP1PIX_LINE_EXTRACTION || maxval < thresh )
			{
				break;
			}
			else
			{
				// address of a winner
				winnerPtr2 = (int*)&(bitmapEdgePtr[base + IMAGE_W*c[imax].iY + c[imax].iX ]);
				winnerPtr_array[npxl] = winnerPtr2;
				npxl++; // increment the number of pixels
			}
		} // if ( *winnerPtr < thresh)
		else
		{
			// address of a winner
			winnerPtr = (int*)&(bitmapEdgePtr[base + IMAGE_W*c[imax].iY + c[imax].iX ]);
			winnerPtr_array[npxl] = winnerPtr;
			npxl++; //*winnerPtr> thresh
		}
		// move the tail either 1 or 2 pixels
		head.iX += c[imax].iX;
		head.iY += c[imax].iY;

		//  2. BREAK BECAUSE OF NON-LINEARITY when it is long enough
		if (npxl>MIN_LEN_ANG && !isLinear())
			break;

	} // while growing a head horizontally

	if ( npxl >= MIN_LINE_REGISTER) // previously MIN_LEN_ANG
	{
		// destroy used edges
		for (int k=0; k<npxl; k++)
		{
			*(winnerPtr_array[k]) = 0;
		}

		return true; // BUG source!!!!!!!
	}
	else
		return false;
} // Grow_ver


float TEdgelet::len()
{
	// beaware of an alternative - npxl

	float dx = tail.iX-head.iX;
	float dy = tail.iY-head.iY;

	if ( dy==0)
		return Abs(dx);
	if ( dx==0)
		return Abs(dy);
	float res;
	res = (float)sqrt (dx*dx+dy*dy);
	return (res);
}

float TEdgelet::len2()
{
	// beaware of an alternative - npxl

	float dx = tail.iX-head.iX;
	float dy = tail.iY-head.iY;

	if ( dy==0)
		return Abs(dx);
	if ( dx==0)
		return Abs(dy);

	return (dx*dx+dy*dy);
}

float TEdgelet::length3D()
{
	// beaware of an alternative - npxl

	float dx = tailGrnd.iX-headGrnd.iX;
	float dy = tailGrnd.iY-headGrnd.iY;

	if ( dy==0)
		return Abs(dx);
	if ( dx==0)
		return Abs(dy);
	float res;
	res = (float)sqrt (dx*dx+dy*dy);
	return (res);
}

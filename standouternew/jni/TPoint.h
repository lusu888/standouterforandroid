#ifndef TPOINT_H
#define TPOINT_H


//#include <iostream>
#include <math.h>

using namespace std;

// Class to represent points.
class TPoint {

public:
	int iX, iY;

	// Constructor uses default arguments to allow calling with zero, one,
	// or two values.
	TPoint(int x = 0, int y = 0) {
		iX = x;
		iY = y;
	}

	// Extractors.
	int x() { return iX; }
	int y() { return iY; }

	// Stters
	void SetXY(int x, int y) {
		iX = x;
		iY = y;
	}


	// Distance to another point.  Pythagorean thm.
	float dist(TPoint other) {
		float xd = iX - other.iX;
		float yd = iY - other.iY;
		return (float)sqrt(xd*xd + yd*yd);
	}

	TPoint operator+(TPoint b)
	{
		return TPoint(iX + b.iX, iY + b.iY);
	}
	TPoint operator-(TPoint b)
	{
		return TPoint(iX - b.iX, iY - b.iY);
	}

	// Move the existing point.
	void move(float a, float b)
	{
		iX += a;
		iY += b;
	}

};


#endif

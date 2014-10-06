#ifndef TPOINTR_H
#define TPOINTR_H

#include <math.h>


class TPointR
	{
public:

	float iX;
	float iY;

public:

	inline TPointR(){}; // default constructor?
	inline TPointR(float aX,float aY){iX=aX; iY=aY;};
	void SetXY(float aX,float aY){iX=aX; iY=aY;};
	static void add(TPointR &res, TPointR a, TPointR b) {res.iX = a.iX + b.iX; res.iY = a.iY + b.iY;};
	static void sub(TPointR &res, TPointR a, TPointR b) {res.iX = a.iX - b.iX; res.iY = a.iY - b.iY;};
	void scl(float k) {iX *= k; iY *= k;};
	void negate() {iX = -iX; iY = -iY;};
	float dot(TPointR a, TPointR b) {return a.iX*b.iX+a.iY*b.iY;};
	float len() {return (float)sqrt(SQR(iX )+SQR(iY));};
	// Add or subtract two points.
	TPointR operator+(TPointR b)
	{
		return TPointR(iX + b.iX, iY + b.iY);
	}
	TPointR operator-(TPointR b)
	{
		return TPointR(iX - b.iX, iY - b.iY);
	}
	TPointR operator*(float k)
	{
		return TPointR(iX*k, iY*k);
	}


	};

#endif

//sorting functions
#ifndef SORTING_HEADER
#define SORTING_HEADER

inline bool isEven(int n)
{
	return(n%2==0);
}

inline bool isOdd(int n)
{
	return(n%2!=0);
}


/*Function for partitioning the array
returns a pivot index and puts small elements on
the left of pivot and large element on the right
of pivot*/
int inline Partition(int low, int high, float arr[])
{
	float high_vac,low_vac,pivot/*,itr*/;

	pivot=arr[low];

	while(high>low)
	{
		high_vac=arr[high];


		while(pivot<=high_vac)
		{
			if(high<=low) break;
			high--;
			high_vac=arr[high];
		}

		// swap pivot with high_vac
		arr[low]=high_vac;
		low_vac=arr[low];
		while(pivot>low_vac)
		{
			if(high<=low) break;
			low++;
			low_vac=arr[low];
		}
		arr[high]=low_vac;
	}
	arr[low]=pivot;
	return low;
}

int inline Partition(int low, int high, unsigned char arr[])
{
	float high_vac,low_vac,pivot/*,itr*/;

	pivot=arr[low];

	while(high>low)
	{
		high_vac=arr[high];


		while(pivot<=high_vac)
		{
			if(high<=low) break;
			high--;
			high_vac=arr[high];
		}

		// swap pivot with high_vac
		arr[low]=high_vac;
		low_vac=arr[low];
		while(pivot>low_vac)
		{
			if(high<=low) break;
			low++;
			low_vac=arr[low];
		}
		arr[high]=low_vac;
	}
	arr[low]=pivot;
	return low;
}

void inline Quick_sort(int low, int high, float arr[])
{
/* keep partitioning arr[] leaving a pivot out
	since it is already been sorted until low=high*/
	int Piv_index;
	if(low<high)
	{
		Piv_index=Partition(low,high,arr);
		Quick_sort(low,Piv_index-1,arr);
		Quick_sort(Piv_index+1,high,arr);
	}
}

void inline Quick_sort(int low, int high, unsigned char arr[])
{
/* keep partitioning arr[] leaving a pivot out
	since it is already been sorted until low=high*/
	int Piv_index;
	if(low<high)
	{
		Piv_index=Partition(low,high,arr);
		Quick_sort(low,Piv_index-1,arr);
		Quick_sort(Piv_index+1,high,arr);
	}
}

// here arr2[] is an index array that reflects changes in indices after sorting
// arr2 is supposed to be a sorted array of integers values from 0 to high-1
void inline InitArr(int N, int arr2[])
{
	for (int i= 0; i<N; i++)
		arr2[i] = i;
}
// move high and low towards each other and swap elements on the way
// if they contradict a randomly chosen pivot;
// the algorithm jangles with three values at the same time: high_vac, low_vac,
// and a pivot; First a high value is put on a pivot place, then low value put
// in a high place and then the pivot is writeen in the low place.
int inline Partition2(int low, int high, float arr[], int arr2[])
{
	float high_vac,low_vac,pivot/*,itr*/;
	int temp;

	pivot=arr[low]; // random selection of a pivot; at the end: elements_low < Pivot <= elements_high
	temp = arr2[low]; // arr2[]

	while(high>low) // stopping criterion
	{
		high_vac=arr[high];

		while(pivot<=high_vac) // move index
		{
			if(high<=low) break; // check stopping criterion again
			high--;              // move high closer to the pivot if possible
			high_vac=arr[high];  // element to be put on the left from the pivot
		}

		// swap arr element chosen as a pivot with high_vac that happened to be smaller than ivot
		arr[low]=high_vac; // first exchange
		arr2[low]=arr2[high];    // arr2[]
		low_vac=arr[low];

		while(pivot>low_vac)
		{
			if(high<=low) break;
			low++;
			low_vac=arr[low];
		}
		arr[high]=low_vac; // second exchange
		arr2[high]=arr2[low];   // arr2[]
	}
	arr[low]=pivot;  // third exchange
	arr2[low]=temp;  // arr2[]
	return low;
}

void inline Quick_sort2(int low, int high, float arr[], int arr2[])
{
/* keep partitioning arr[] leaving a pivot out
	since it is already been sorted until low=high*/
	int Piv_index;
	if(low<high)
	{
		Piv_index=Partition2(low,high,arr,arr2);
		Quick_sort2(low,Piv_index-1,arr,arr2);
		Quick_sort2(Piv_index+1,high,arr,arr2);
	}
}
/*
// Returns percentile (pr=[0..1]) of an ascendingly sorted array (arr) with N elements
float inline percnt(float pr, int N, float arr[])
{
	float val;
	int cnt = 0; // number of elements traced

	// precondition
	if (pr<0.0 || pr>1.0)
		exit(1);

	val = arr[cnt];	// first element (obligatory to avoid division by zero)
	cnt++;
	while (cnt < N*pr)
	{
		val=arr[cnt]; // accumulate array values
		cnt++;
	}

	return (val);
} // percnt
*/

// Returns percentile (pr=[0..1]) of an ascendingly sorted array (arr) with N elements
float inline percnt(float pr, int N, float arr[])
{
	float val;
	int cnt = 0; // number of elements traced

	return (arr[(int)(N*pr)]);
} // percnt

/*
unsigned char inline percnt(float pr, int N, unsigned char arr[])
{
	unsigned char val;
	int cnt = 0; // number of elements traced

	// precondition
	if (pr<0.0 || pr>1.0)
		exit(1);

	val = arr[cnt];	// first element (obligatory to avoid division by zero)
	cnt++;
	while (cnt < N*pr)
	{
		val=arr[cnt]; // accumulate array values
		cnt++;
	}

	return (val);
} // percnt
*/

unsigned char inline percnt(float pr, int N, unsigned char arr[])
{
	unsigned char val;
	int cnt = 0; // number of elements traced

	return (arr[(int)(N*pr)]);
} // percnt

// retunrns a value similar to percentile except that it is average of all values either on
// the left of percentile if it is <0.5 or on the right if it is >0.5
float inline percnt_mean(float pr, int N, float arr[])
{
	float sum;
	int cnt = 0; // number of elements traced

	if (pr<0.5) // speed up
	{
		sum = arr[cnt];	// first element (obligatory to avoid division by zero)
		cnt++;
		while (cnt < N*pr)
		{
			sum+=arr[cnt]; // accumulate array values
			cnt++;
		}
	}
	else
	{
		sum = arr[N-1];
		cnt++;
		while (N-cnt > N*pr)
		{
			sum+=arr[N-1-cnt]; // accumulate array values
			cnt++;
		}
	}
	return (sum/cnt);
} // percnt

// retunrs max absolute value
float inline max_abs(int N, float arr[])
{
	float res = 0, val;
	for (int i=0; i<N; i++)
	{
		val = ABS(arr[i]);
		if (res < val)
			res = val;
	}
	return (res);
}

// retunrs mean absolute value
float inline mean_abs(int N, float arr[])
{
	float res, val = 0;
	for (int i=0; i<N; i++)
		val += ABS(arr[i]);
	return (val/N);
}


// median
float inline median(int N, float arr[])
{
	if (isOdd(N))
		return(arr[N/2]);
	else
		return(0.5*(arr[N/2]+arr[(N-1)/2]) );
}

// median weighted (weight array is accessibled via ord_ind since it wasn't sorted)
float inline medianW(int N, float arr[], float w[], int ord_ind[])
{
	int i;

	float sum = 0, cur_sum = 0;
	for (i=0; i<N; i++)
		sum += w[i];

	// sum to get
	sum /=2.0;
	i = 0;
	cur_sum = w[i];
	while (cur_sum<=sum)
	{
		cur_sum+=w[ord_ind[i]]; // proper order of counting weights
		i++;
	}

	return (arr[i-1]);
}

float inline max_arr(int N, float arr[])
{
	float max_val = -100000;
	for (int i=0; i<N; i++)
		if (max_val < arr[i])
			max_val = arr[i];

	return (max_val);

}
// median/mean
float inline median_mean(int N, float arr[])
{
	return(0.5*(percnt(0.49, N, arr) + percnt(0.51, N, arr)));
}

// mean weighted (weight array is accessibled via ord_ind since it wasn't sorted)
float inline meanW(int N, float arr[], float w[], int ord_ind[])
{

	float sum_w = 0, sum_arr = 0;

	for (int i=0; i<N; i++)
	{
		sum_w += w[ord_ind[i]];
		sum_arr += arr[i]*w[ord_ind[i]];
	}

	return (sum_arr/sum_w);
}

float inline middle(int N, float arr[])
{

	float minval = arr[0], maxval = arr[0];

	for (int i=1; i<N; i++)
	{
		if (minval>arr[i])
			minval=arr[i];
		if (maxval<arr[i])
			maxval=arr[i];
	}

	return (0.5f*(minval + maxval));

}
// middle of two clusters possibly of different size
float inline diff10and90(int N, float arr[])
{
	float prc_large = percnt(0.9, N, arr);
	float prc_small = percnt(0.1, N, arr);
	return( prc_large - prc_small);
}

// 90th - 10th percentiles: explores variability
float inline between10and90(int N, float arr[])
{
	float prc_large = percnt(0.9, N, arr);
	float prc_small = percnt(0.1, N, arr);
	return( 0.5*( prc_large + prc_small));
}


#endif

class Border
{
	// defining border dimensions
	// as 'L' suppose to be infinity any big number
	// do this job.
	long l = 1000000;
	int w ;
}

class Sensor
{
	// class sensor suppose tell sensor when to operate
	// using probability whether it should be working 
	// for next step_time interval.
	// p is probability for the same.
	float p;
}

class Infiltrator
{
	// step_time is time interval between two decision
	// it is how frequent is sensor is taking decision
	// whether it will be on or off.
	// it is time taken by infiltrator unit to examine
	// and move to next block if feasible
	int step_time = 10;
}


class Clock
{
	// class Clock measures time taken by infiltrator 
	// to invade Defending Country(D.C)
	public static void main (String args[])
	{
		// intializing object of border class
		Border b = new Border();
		// intializing object of Sensor class 
		Sensor s = new Sensor();
		// intializing object of Infiltrator class
		Infiltrator i = new Infiltrator();
		long l = b.l;
		// storing first command-line argument width
		// in object of class Border
		b.w = Integer.parseInt(args[0]);
		int w = b.w;
		// storing second command-line argument 
		// probability in object of Sensor
		s.p = Float.parseFloat(args[1]);
		float p = s.p;
		// pseudo-random number generator 
		// with range of [min,max]
		int min = 0;
		int max = 99;
		int random_int;
		// as result can vary 
		// mean over n iterations is more 
		// likely to give stable result
		int avg_time =0;
		// n is no of iterations
		int n = 100;
		// iterations begins
		for(int iter = 0; iter <n; iter++)
		{
			// infiltrator starts job
			// time = 0
			int t = 0;
			w = b.w;
			// runs till remaining width to cover
			// is not non-zero and non negative 
			// and probability is not 1
			while (b.w!=0 && w >= 0 && p!=1)
			{
				int flag = 0;
				// to capture if an any forward block 
				// has sensor off
				random_int = (int)(Math.random() * (max - min + 1) + min);
				// waiting till sensor of currents blocks 
				// goes off.
				while(random_int < p*100)
				{
					t = t + i.step_time;
					// generating probability for next step_time
					random_int = (int)(Math.random() * (max - min + 1) + min);
				}
				for (int temp = 0 ; temp < 3; temp++) 
				{
					// checing for any forward block with 
					// sensor off if found, no further examination
					// surrounding blocks. Move forward.
					random_int = (int)(Math.random() * (max - min + 1) + min);
					if(random_int >= p*100)
					{
						flag = 1;
						break;
					}		
				}
				// reduce remaining width by one unit 
				// after forward movement
				if(flag == 1)
				{
					w = w - 1;
				}
				// calculating time to move forward
				t = t + i.step_time;
			}
			// if probability is 1 OR
			// Sensor are always ON then it is
			// impossible to move forward
			// hence infinite time
			// or Max value available
			if(p==1 && b.w!=0)
			{
				t = Integer.MAX_VALUE;
			}
			// to get average units of time 
			// taking 1 unit of time = 10 sec
			t/=10;
			avg_time+=t;
		}
		// dividing avg_time to get mean time
		avg_time /= n;
		// to get avg time in sec.
		avg_time *= 10;
		// intializing a new class Clock object 
		// to get output
		Clock obj= new Clock();
		// function prints and return
		// input parameter(here avg_time)
		obj.return_output(avg_time);
		
	}
	int return_output(int avg_time)
	{
		// printing input parameter
		System.out.println(avg_time);
		// returning input parameter
		return avg_time;
	}

}
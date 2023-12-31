package ecte331ProjB;

class Data{
	//create variables which have to be shared
	int A1,A2,A3,B1,B2,B3;
	//create default boolean values
	boolean GoB1 = false;
	boolean GoB2 = false;
	boolean GoA2 = false;
	boolean GoB3 = false;
	boolean GoA3 = false;
	
	//method for executing the summation formula
	public static int Sum(int i) {
		int ans;
		 double val = i*((i+1)/2.0);
		 ans = (int) val;
		 return ans;
	 }
}

class ThreadA implements Runnable {
    private Data d;

    public ThreadA(Data d) {
        this.d = d;
    }

    @Override
    public void run() {
        // run function A1
        synchronized (d) {
            // execute formula for A1
            d.A1 = Data.Sum(100);
            System.out.println("Thread A1 value is: " + d.A1);
            d.GoB1 = true;
            d.notify();
        }

        //run function A2
        synchronized(d) {
            while(!d.GoA2) {
                try {
                    d.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //once boolean flag is clear, execute commands below
            d.A2 = d.B2+ Data.Sum(400); 
            System.out.println("Thread A2 value is: "+d.A2);
            d.GoB3 = true;
            d.notify();
        }
        
        //run function A3
        synchronized(d) {
        	while(!d.GoA3) {
        		try {
					d.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        	//execute following once flag is cleared
        	d.A3 = d.B3+ Data.Sum(600);
        	System.out.println("Thread A3 value is: "+d.A3);
        }
        

    }
}



class ThreadB implements Runnable {
	
	private Data d;
	
	public ThreadB(Data d) {
		// TODO Auto-generated constructor stub
		this.d =d;	
	}

	@Override
	public void run() {
		// Code for Thread B
		// run function B1
		synchronized(d) {
			while(!d.GoB1) {
				try {
					d.wait();
				}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			}
			// execute B1 commands, once flag cleared
			d.B1 = d.A1+Data.Sum(200);
			System.out.println("Thread B1 value is: "+d.B1);
			d.GoB2 = true;
			
		}
		
		//run function B2
		synchronized(d) {
			while(!d.GoB2) {
			try {
				d.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			// run function B2 once flag cleared
			d.B2 = Data.Sum(300); 
			System.out.println("Thread B2 value is: "+d.B2);
			d.GoA2 = true;
			d.notify();
		}
		
		//run function B3
		synchronized(d) {
			while(!d.GoB3) {
				try {
					d.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//once boolean flag is true, execute following commands
			d.B3 = d.A2 + Data.Sum(500);
			System.out.println("Thread B3 value is: "+d.B3);
			d.GoA3 = true;
			d.notify();
		}
		
	}	
	
}


public class PartB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//initialize the threads and call them
		Data d = null;
		
		int i=0;
        int numIterations=10;
        
        while(i++<numIterations){
        d = new Data();
		Thread tA = new Thread(new ThreadA(d));
		Thread tB = new Thread(new ThreadB(d));
		
		//start Thread A & B
		tA.start();
		tB.start();
		try {
			
			tA.join();
			tB.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		}
		System.out.println("Iteration "+ i+ " executed!");
		//print the final values again and then comment out, so it doesn't print twice
		/*System.out.println("Value of A1: "+d.A1);
		System.out.println("Value of B1: "+d.B1);
		System.out.println("Value of B2: "+d.B2);
		System.out.println("Value of A2: "+d.A2);
		System.out.println("Value of B3: "+d.B3);
		System.out.println("Value of A3: "+d.A3);*/
		
	}
}
}

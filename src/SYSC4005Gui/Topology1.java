package SYSC4005Gui;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFrame;


public class Topology1 {
public final static int SERVER_IDLE = -1;
	
	private int N = 5;
	private int serverStates[];
	private double probability[];
	private double lambdas[];
	private int[][] queueLength;
	private int timeSlotCount;
	private int currentTimeSlot = 0;
	private int Policy=0;
	private boolean[][] queueIsconnected;
	private int iterations;
	private kFrame kfrm;
	
	
	/**
	 * Construct the Simulation System.
	 * 
	 * @param stream A stream of random fnumbers
	 * @param timeSlotCount The maximum time slots
	 * @param probability The probability that a queue is connected to the server
	 * @param lambda The parameter in bernoulli used to determine how often new tasks are added to a queue 
	 * @param policy The scheduling policy
	 * @param iterations The number of iterations to run for
	 */
	public Topology1(int timeSlotCount, double probability[], double lambdas[], int policy, int iterations, kFrame kf){
		assert(N == probability.length);
		
		this.timeSlotCount = timeSlotCount;		
		this.probability = probability;
		this.lambdas = lambdas;
		//this.policy = policy;
		//this.policy.setSimulationSystem(this);
		Policy = policy;
		this.iterations = iterations;
		kfrm = kf;
		serverStates = new int[timeSlotCount];
		queueLength = new int[N][timeSlotCount];		
		queueIsconnected = new boolean[N][timeSlotCount];
	}
	
	
	/**
	 * @return The number of queues in the system
	 */
	public int getN() {
		return N;
	}

	/**
	 * @param n The queue index
	 * @return The probability that queue n is connected.
	 */
	private double getProbability(int n) {
		return probability[n];
	}
	
	/**
	 * @param n The queue index
	 * @param t The time slot number
	 * @return True if the queue is connected to the server at the time slot; otherwise false
	 */
	public boolean isConnected(int n, int t) {
		return queueIsconnected[n][t];
	}
	
	/**
	 * @param t The time slot number
	 * @param state The state, either SERVER_IDLE or the index of the queue that is connected
	 */
	public void setServerState(int t, int state) {
		this.serverStates[t] = state;		
		//System.out.println("Server state at " + t + " is " + state);
	}


	/**
	 * @param n The index of the queue
	 * @param t The time slot number
	 * @return True if the queue is empty at the time slot, otherwise false
	 */
	public boolean isEmpty(int n, int t) {
		if (t < 0) return true;
		return getQueueLength(n, t) == 0;
	}
	
	/**
	 * Simulates the system.  Note this method can only be called once for each iteration.
	 */
	private void simulateSystem() {
		for (int t = 0; t < timeSlotCount; t++) {
			advanceTimeSlot();
		}
	}
	
	/**
	 * Advances the simulation to the next timeslot.
	 */
	private void advanceTimeSlot() {
		assert(currentTimeSlot < timeSlotCount);
		
		// calculate Cn
		for (int n = 0; n < N; n++) {
			queueIsconnected[n][currentTimeSlot] = BernoulliGeneratorNext(getProbability(n))==1;  //new BernoulliGenerator(stream, getProbability(n)).next() == 1;			
		}		
		
		if(Policy == 1)
		{
			allocateServerRR(currentTimeSlot);
		}
		if(Policy == 2)
		{
			allocateServerLCQ(currentTimeSlot);
		}
		if(Policy == 3)
		{
			allocateServerRAN(currentTimeSlot);
		}
		//policy.allocateServer(currentTimeSlot);
		
		for (int n = 0; n < N; n++) {
			int Xn = 0;		
			int Hn = 0;
			if (currentTimeSlot > 0) {
				Xn = queueLength[n][currentTimeSlot - 1];
				
				if (serverStates[currentTimeSlot] == n && getQueueLength(n, currentTimeSlot - 1) > 0) {
					Hn = 1;
				}
			}	
			
			int An = BernoulliGeneratorNext(lambdas[n]); //new BernoulliGenerator(stream, lambdas[n]).next();
			//debugTotalAn += An;
			
			// equation found in section 2.1 
			queueLength[n][currentTimeSlot] = Xn - Hn + An;
		}			
		
		//System.out.println("Queue Lengths " + queueLength[0][currentTimeSlot] + " " + queueLength[1][currentTimeSlot] + " " + queueLength[2][currentTimeSlot] + " " + queueLength[3][currentTimeSlot] + " " + queueLength[4][currentTimeSlot]);
		currentTimeSlot++;
	}


	/**
	 * @param n The queue index
	 * @param t The time slot number
	 * @return The length of queue n at time slot t.
	 */
	public int getQueueLength(int n, int t) {
		return queueLength[n][t];
	}
	
	/**
	 * @return The average queue length of all queues for the entire simulation.
	 */
	private double getAverageQueueOccupancy() {
		double total = 0;
		for (int n = 0; n < N; n++) {
			for (int t = 0; t < timeSlotCount; t++)
			total += queueLength[n][t];
		}		
		return total / (N * timeSlotCount);
	}



	private void reset() 
	{
		currentTimeSlot = 0;	
	}
	
	/**
	 * @param Prints the simulation results to the file specified.
	 */
	public void runAndPrintToFile(String fileName) {
		double totals[] = new double[iterations];	
		double total = 0;
		for (int i = 0; i < iterations; i++ ) {
			reset();
			simulateSystem();
			totals[i] = getAverageQueueOccupancy();			
			System.out.print(totals[i]+",");
			total += totals[i];
		}		
		
		double mean = total / iterations;
		double sampleDeviation = 0;
		for (int i = 0; i < iterations; i++) {
			sampleDeviation += Math.pow(totals[i] - mean, 2);
		}
		
		sampleDeviation /= iterations - 1;
		sampleDeviation = Math.sqrt(sampleDeviation);		
		
		// 95% CI
		double interval = sampleDeviation / Math.sqrt(iterations) * 1.96;
		
		double lower = mean - interval;
		double upper = mean + interval;
		kfrm.getTF14().setText(""+mean);
		kfrm.getTF15().setText(""+upper);
		kfrm.getTF16().setText(""+lower);
		/*
		PrintWriter writer = null;
		try {
			//writer = new PrintWriter(new FileOutputStream(new File(fileName), true));
			//writer.println("lambda, mean, lowerCI, upperCI");
		    //writer.println(lambdas[0] + ", " + mean + ", " + lower + ", " + upper);


			//writer.println("Average = " + getAverageQueueOccupancy());
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		*/
	}
	
	public int BernoulliGeneratorNext(double d) {

		if (Math.random() < d) {
			return 1;
		}
		return 0;
	}
	
	
	int currentQueue = 0;
	
	public void allocateServerRR(int t) {
		
		if (!isConnected(currentQueue, t) || isEmpty(currentQueue, t - 1)) {
			setServerState(t, SERVER_IDLE);
			
		} else {
			setServerState(t, currentQueue);
		}
		
		
		currentQueue++;
		if (currentQueue == getN()) {
			currentQueue = 0;
		}		
	}
	
	public void allocateServerRAN(int t) {
		ArrayList<Integer> connectedQueues = new ArrayList<Integer>();
		for (int n = 0; n < getN(); n++) {
			if (isConnected(n, t)  && !isEmpty(n, t - 1)) {
				connectedQueues.add(n);
			}
		}		
		
		int count = connectedQueues.size();
		if (count < 1) {
			setServerState(t, SERVER_IDLE);
			return;
		}
		int n = (int) Math.floor(Math.random()* count); //system.getRandomStream().next() * count);
		
		setServerState(t, connectedQueues.get(n));		
	}
	
	
	public void allocateServerLCQ(int t) {
		ArrayList<Integer> longestQueues = new ArrayList<Integer>();
		
		int longestLength = 0; 
		for (int n = 0; n < getN(); n++ ) {
			if (isConnected(n, t) && !isEmpty(n, t - 1)) {
				int length = getQueueLength(n, t - 1);
				if (length > longestLength) {
					longestQueues.clear();
					longestQueues.add(n);
					longestLength = length;
				} else if (length == longestLength) {
					longestQueues.add(n);
				}
			}
		}
		
		if (longestLength == 0) {
			setServerState(t, SERVER_IDLE);
		} else {
			if (longestQueues.size() == 1) {
				setServerState(t, longestQueues.get(0));
			} else {
				int n = (int) Math.floor(Math.random() * longestQueues.size());//system.getRandomStream().next() * longestQueues.size());
				
				setServerState(t, longestQueues.get(n));	
			}
		}		
	}
	
}

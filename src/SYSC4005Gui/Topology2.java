package SYSC4005Gui;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Topology2 {
public final static int SERVER_IDLE = -1;
	
	private int N = 5;
	private int K = 1;
	private int serverStates[][];
	private double probability[][];
	private double lambdas[];
	private int[][] queueLength;
	private int timeSlotCount;
	//private AbstractRandomStream stream;
	private int currentTimeSlot = 0;
	//private AbstractPolicy2 policy;
	private boolean[][][] queueIsconnected;
	private int iterations;
	private int Policy;

	private double num1;

	private double num2;

	private double runRan;

	private boolean testUni;
	
	private kFrame kfrm;
	
	/**
	 * Construct the Simulation System.
	 * 
	 * @param stream A stream of random numbers
	 * @param timeSlotCount The maximum time slots
	 * @param probability The probability that a queue is connected to the server
	 * @param lambda The parameter in bernoulli used to determine how often new tasks are added to a queue 
	 * @param policy The scheduling policy
	 * @param iterations The number of iterations to run for
	 * @param K The number of servers
	 */
	public Topology2(int timeSlotCount, double probability[][], double lambdas[], int policy, int iterations, int K, kFrame kf) {
		assert(N == probability.length);
		
		//this.stream = stream;
		this.timeSlotCount = timeSlotCount;		
		this.probability = probability;
		this.lambdas = lambdas;
		this.Policy = policy;
		//this.policy.setSimulationSystem(this);
		this.iterations = iterations;
		kfrm = kf;
		serverStates = new int[timeSlotCount][K];
		queueLength = new int[N][timeSlotCount];		
		queueIsconnected = new boolean[N][timeSlotCount][K];
		
		this.K = K;
	}
	
	public Topology2(boolean testUNI, double a, double b, int timeSlotCount, double probability[][], double lambdas[], int policy, int iterations, int K, kFrame kf) {
		assert(N == probability.length);
		this.testUni = testUNI;
		num1 = a;
		num2 = b;
		//this.stream = stream;
		this.timeSlotCount = timeSlotCount;		
		this.probability = probability;
		this.lambdas = lambdas;
		this.Policy = policy;
		kfrm = kf;
		//this.policy.setSimulationSystem(this);
		this.iterations = iterations;
		
		serverStates = new int[timeSlotCount][K];
		queueLength = new int[N][timeSlotCount];		
		queueIsconnected = new boolean[N][timeSlotCount][K];
		
		this.K = K;
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
	private double getProbability(int n, int k) {
		return probability[n][k];
	}
	
	/**
	 * @param n The queue index
	 * @param t The time slot number
	 * @return True if the queue is connected to the server at the time slot; otherwise false
	 */
	public boolean isConnected(int n, int t, int k) {
		return queueIsconnected[n][t][k];
	}
	
	/**
	 * @param t The time slot number
	 * @param state The state, either SERVER_IDLE or the index of the queue that is connected
	 */
	public void setServerState(int t, int state, int k) {
		this.serverStates[t][k] = state;		
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
	
	public boolean isEmpty(int n, int t, int offset) {
		if (t < 0) return true;
		return getQueueLength(n, t) + offset == 0;
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
			for (int k = 0; k < K; k++) {
				queueIsconnected[n][currentTimeSlot][k] = BernoulliGeneratorNext(getProbability(n, k)) == 1;	//new BernoulliGenerator(stream, getProbability(n, k)).next() == 1;			
			}
		}		
		
		//policy.allocateServer(currentTimeSlot);
		
		if(Policy == 1)
		{
			allocateServerASLCQ(currentTimeSlot, testUni);
		}
		if(Policy == 2)
		{
			allocateServerLCSLFCQ(currentTimeSlot,testUni);
		}
		if(Policy == 3)
		{
			allocateServerRan(currentTimeSlot,testUni);
		}
		
		for (int n = 0; n < N; n++) {
			int Xn = 0;		
			int Hn = 0;
			if (currentTimeSlot > 0) {
				Xn = queueLength[n][currentTimeSlot - 1];
				
				for (int k = 0; k < K; k++) {
					if (serverStates[currentTimeSlot][k] == n) {
						Hn += 1;
					}
				}
			}	
			
			if (Hn > Xn) {
				Hn = Xn;
			}
			
			int An = BernoulliGeneratorNext(lambdas[n]);//new BernoulliGenerator(stream, lambdas[n]).next();
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


	/**
	 * @return The random number stream used for this simulation
	 */
//	public AbstractRandomStream getRandomStream() {
//		return stream;
//	}
	
	private void reset() 
	{
		currentTimeSlot = 0;	
	}
	
	/**
	 * @param Prints the simulation results to the file specified.
	 */
	public void runAndPrintToFile(String fileName, int progval) {
		kfrm.prog().setValue(progval);
		double totals[] = new double[iterations];	
		double total = 0;
		for (int i = 0; i < iterations; i++ ) {
			reset();
			simulateSystem();
			totals[i] = getAverageQueueOccupancy();			
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
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(new File(fileName), true));
			writer.println("lambda, mean, lowerCI, upperCI");
			writer.println(lambdas[0] + ", " + mean + ", " + lower + ", " + upper);
			//writer.println("debug An = " + debugTotalAn);
			writer.println("Average = " + getAverageQueueOccupancy());
			
//			if(kfrm.checkboxTicked())
//			{
			kfrm.getTF14().append(""+mean+",");
			kfrm.getTF15().append(""+upper+",");
			kfrm.getTF16().append(""+lower+",");
//			}
//			else
//			{
//				kfrm.getTF14().setText(""+mean);
//				kfrm.getTF15().setText(""+upper);
//				kfrm.getTF16().setText(""+lower);
//			}
		
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		
	}


	public int getK() {
		return K;
	}
	
	public int BernoulliGeneratorNext(double d) {

		if (Math.random() < d) {
			return 1;
		}
		return 0;
	}
	
	//policies
	
	//Randomized
	public void allocateServerRan(int t, boolean tesUniform) {
		for (int k = 0; k < getK(); k++) {			
			
			ArrayList<Integer> connectedQueues = new ArrayList<Integer>();
			for (int n = 0; n < getN(); n++) {
				
				if (isConnected(n, t, k)  && !isEmpty(n, t - 1)) {
					connectedQueues.add(n);
				}
			}		
			
			int count = connectedQueues.size();
			if (count < 1) {
				setServerState(t, Topology1.SERVER_IDLE, k);
				return;
			}
			if(tesUniform)
			{
				int n = (int) Math.floor(nxtnxt() * count);
				setServerState(t,connectedQueues.get(n),k);	
			}
			else
			{
				int n = (int) Math.floor(Math.random() * count);//system.getRandomStream().next() * longestQueues.size());
				setServerState(t, connectedQueues.get(n),k);	
			}
//			int n = (int) Math.floor(system.getRandomStream().next() * count);
//			
//			setServerState(t, connectedQueues.get(n), k);	
		}
	}	
	
	//LCQ
	public void allocateServerASLCQ(int t, boolean tesUniform) {
		for (int k = 0; k < getK(); k++) {
			ArrayList<Integer> longestQueues = new ArrayList<Integer>();
			
			int longestLength = 0; 
			for (int n = 0; n < getN(); n++ ) {
				if (isConnected(n, t, k) && !isEmpty(n, t - 1)) {
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
				setServerState(t, Topology1.SERVER_IDLE, k);
			} else {
				if (longestQueues.size() == 1) {
					setServerState(t, longestQueues.get(0), k);
				} else {
					if(tesUniform)
					{
						int n = (int) Math.floor(nxtnxt() * longestQueues.size());
						setServerState(t, longestQueues.get(n),k);	
					}
					else
					{
						int n = (int) Math.floor(Math.random() * longestQueues.size());//system.getRandomStream().next() * longestQueues.size());
						setServerState(t, longestQueues.get(n),k);	
					}
//					int n = (int) Math.floor(Math.random() * longestQueues.size());
//					
//					setServerState(t, longestQueues.get(n), k);	
				}
			}
		}
	}
	
	
	//LCS
	public void allocateServerLCSLFCQ(int t, boolean tesUniform) {
		List<ServerConnectedPair> pairs = new ArrayList<ServerConnectedPair>();
		for (int k = 0; k <getK(); k++) {
			ServerConnectedPair pair = new ServerConnectedPair();
			pair.k = k;
			for (int n = 0; n < getN(); n++) {
				if (isConnected(n, t, k)) {
					pair.connectedCount++;
				}
			}
			pairs.add(pair);
		}
		
		Collections.sort(pairs);
		int queueOffsets[] = new int[getN()];
		for (ServerConnectedPair pair: pairs) {
			int k = pair.k;
			ArrayList<Integer> longestQueues = new ArrayList<Integer>();
			
			int longestLength = 0; 
			for (int n = 0; n < getN(); n++ ) {
				if (isConnected(n, t, k) && !isEmpty(n, t - 1, queueOffsets[n])) {
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
				setServerState(t, Topology1.SERVER_IDLE, k);
			} else {
				if (longestQueues.size() == 1) {
					setServerState(t, longestQueues.get(0), k);
					queueOffsets[longestQueues.get(0)]--;
				} else {
					if(tesUniform)
					{
						int n = (int) Math.floor(nxtnxt() * longestQueues.size());
						setServerState(t, longestQueues.get(n),k);	
						queueOffsets[longestQueues.get(n)]--;
					}
					else
					{
						int n = (int) Math.floor(Math.random() * longestQueues.size());//system.getRandomStream().next() * longestQueues.size());
						setServerState(t, longestQueues.get(n),k);	
						queueOffsets[longestQueues.get(n)]--;
					}
//					int n = (int) Math.floor(system.getRandomStream().next() * longestQueues.size());
//					
//					system.setServerState(t, longestQueues.get(n), k);
					
				}
			}
		}
	}
	
	//helper
	private class ServerConnectedPair implements Comparable<ServerConnectedPair>
	{
		public int connectedCount = 0;
		public int k = -1;
		
		@Override
		public int compareTo(ServerConnectedPair other) {
			
			//return Integer.valueOf(connectedCount).compareTo(Integer.valueOf(other.connectedCount));
			//return Integer.compare(, );
			return compare(connectedCount, other.connectedCount);
		}
		
		public int compare(int x, int y) {
		    return (x < y) ? -1 : ((x == y) ? 0 : 1);
		}
	}
	
	//uniform 
		public void nextNext(double num1, double num2){
			this.num1=num1;
			this.num2=num2;
			runRan=-1;
			
		}
		
		public double nxtnxt()
		{
			if(runRan==-1)
			{
				runRan = Math.random();
			}
			else
			{
				runRan=(runRan+ ((num2 + num1) * Math.random() + num1))%1;
			}
			return runRan;
		}
}
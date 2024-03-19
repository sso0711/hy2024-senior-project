package com.mirlab.metric.wraparound;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.Result;

/**
 * 
 * @author Lee Gilho Wrab around metric : AsyncProvisioningPathTime
 * 
 *         This Class is Thread and measure avg pro time
 * 
 */
public class AsyncClientProactiveProTimeThread_WR {
	public static DecimalFormat df = new DecimalFormat("#0.00");
	public static DecimalFormat df2 = new DecimalFormat("#0.00");
	static long totalTime = 0;
	static double e = 0;
	public static int guardTime = 0;


	public AsyncClientProactiveProTimeThread_WR() {

	}

	public void run() {

	
		guardTime = Global.GUARD_TIME;

		measureTime(); //
//		measureRate();

		System.out.println("##### END ");

		double time = totalTime;
		Result.WR_ADD_RESULT(df.format(time), 10, 2);
		Result.WR_ADD_RESULT(df.format(e), 11, 2);

		Initializer.INITIAL_CHANNEL_POOL();
		
	}

	public static void setGlobalStartMap(String dstKey, long time) {
		
//		proStartTime.put(dstKey, time);

//		System.out.println("### (In thread)  start :  " + proStartTime.size() + " # # key : " + dstKey);
	}

	public static void setGlobalLastMap(String dstKey, long time) {
		
		System.out.println("testsetsetset");
		
		Global.proLastTime.put(dstKey, time);

//		System.out.println("### (In thread)  last :  " + proLastTime.size());

	}

	public static void measureTime() {

		System.out.println("measureTime TEST ");
		
		int ThresholdExceededCount = 0;
		long Threshold = 2000;

		Set set = Global.proStartTime.keySet();
		Iterator it = set.iterator();

		System.out.println(
				"### startime size : lasttime size : " + Global.proStartTime.size() + "    :   " + Global.proLastTime.size());
		Log.WR_ADD_LOG_PANEL("### startime size : lasttime size : " + Global.proStartTime.size() + "    :   " + Global.proLastTime.size(), AsyncClientProactiveProTimeThread_WR.class.getSimpleName());


		while (it.hasNext()) {
			String key = (String) it.next();
			System.out.println("##key test: " + key);
			if (Global.proLastTime.containsKey(key)) {

				System.out.println("### provision Time  : " + (Global.proLastTime.get(key) - Global.proStartTime.get(key)));
				totalTime += (Global.proLastTime.get(key) - Global.proStartTime.get(key));
				Log.WR_ADD_LOG_PANEL("### provision Time  : " + (Global.proLastTime.get(key) - Global.proStartTime.get(key)), AsyncClientProactiveProTimeThread_WR.class.getSimpleName());
				
			}

		}
		
		Log.WR_ADD_LOG_PANEL("\n " , AsyncClientProactiveProTimeThread_WR.class.getSimpleName());
		System.out.println("### TOTAL Time : " + totalTime);
		System.out.println("### AVG Time : " + totalTime / Global.proStartTime.size());
		Log.WR_ADD_LOG_PANEL("### [TOTAL] Time : " + totalTime, AsyncClientProactiveProTimeThread_WR.class.getSimpleName());
		Log.WR_ADD_LOG_PANEL("### [AVG] Time : " + totalTime / Global.proStartTime.size(), AsyncClientProactiveProTimeThread_WR.class.getSimpleName());
	}

	public static void measureRate() {

		PriorityQueue<ProTime> prolastQueue = new PriorityQueue<>(new ComparatorAscending());
		ArrayList<Integer> perSecondLastList = new ArrayList<>();

		Set set = Global.proLastTime.keySet();
		Iterator it = set.iterator();

		// Guard Time
		int frontGdTime = Global.proLastTime.size();

		frontGdTime = (frontGdTime * guardTime) / 100;

		int backGdTime = Global.proLastTime.size() - frontGdTime;

		int checkGdTime = 1;

		System.out.println("###  check guard time  :  " + (frontGdTime * 3) / 10);
		Log.WR_ADD_LOG_PANEL("###  check guard time  :  " + (frontGdTime * 3) / 10, AsyncClientProactiveProTimeThread_WR.class.getSimpleName());

		//convert map to priority queue 
		
		while (it.hasNext()) {
			
			String key = (String) it.next();
//			System.out.println("gdTime : " + frontGdTime + "  checkGdTime : " + checkGdTime);

			if (checkGdTime < backGdTime) {
				if (checkGdTime > frontGdTime) {

					long lastValue = Global.proLastTime.get(key);
					ProTime pLast = new ProTime(lastValue);

					prolastQueue.add(pLast);
				}
			}
			checkGdTime++;

		}

		int untilCheckThiscount = prolastQueue.size();



		int rateCount = 1;


//		System.out.println("compare to : " + arriveTime + "       :      " + intervalTime);
//		System.out.println("### pro size :  last    : " + prolastQueue.size());

		boolean rateCheck = true;
		int arrayListIndex = 0;
		int total = 0;
		
		long startTime = prolastQueue.poll().time;
		long lastTime = 0;
		long time = 0; 
		int countRate = 1;
		
		
		/**
		 * 
		 */
		int arrayLastIndexSet = 0;

		while (rateCheck) {


			if (prolastQueue.size() == 0) {
				System.out.println("### out loop ");
				Log.WR_ADD_LOG_PANEL("### out loop ", AsyncClientProactiveProTimeThread_WR.class.getSimpleName());
				break;
			}
			
			time = prolastQueue.poll().time;
			countRate++;
			
			if(prolastQueue.size()==0) {
				
				lastTime = time;
				System.out.println("### out loop ");
				Log.WR_ADD_LOG_PANEL("### out loop ", AsyncClientProactiveProTimeThread_WR.class.getSimpleName());
				break;
				
			}
			
			

		}
		
		long totalTime = (lastTime - startTime);

		double rateTime = setDivide(totalTime);
		
//		double e = countRate /rateTime;
		e = Math.round((countRate /rateTime)*100)/100.0;
		//temporary
		
//		System.out.println("##### [AVG] tes : " + countRate/rateTime);
		Log.WR_ADD_LOG_PANEL("\n " , AsyncClientProactiveProTimeThread_WR.class.getSimpleName());
		System.out.println("##### [AVG] Number of flows that can be received per second ");
		Log.WR_ADD_LOG_PANEL("##### [AVG] Number of flows that can be received per second ", AsyncClientProactiveProTimeThread_WR.class.getSimpleName());
//		System.out.println("##### [AVG] tes : " +Math.round(e*100)/100.0 );
		System.out.println("##### [AVG] Rate : " + e  );
		Log.WR_ADD_LOG_PANEL("##### [AVG] Rate : " + e  , AsyncClientProactiveProTimeThread_WR.class.getSimpleName());

		System.out.println(" ");
		System.out.println(" ");
		

	}


	

	public static double setDivide(long num) {

		char[] converToChar;
		char[] setToChar;
		double setDivNum = 0;
		;
		String str = String.valueOf(num);

		if (str.length() > 3) {

			converToChar = str.toCharArray();
			setToChar = new char[converToChar.length + 1];

			for (int i = 0; i < setToChar.length - 4; i++) {
				setToChar[i] = converToChar[i];
			}

			setToChar[setToChar.length - 4] = '.';

			for (int i = setToChar.length - 3; i < setToChar.length; i++) {

				setToChar[i] = converToChar[i - 1];

			}

			String str2 = "";

			for (int i = 0; i < setToChar.length; i++) {
				str2 = str2 + setToChar[i];
			}

			setDivNum = Double.parseDouble(str2);

			return setDivNum;

		} else {

			converToChar = str.toCharArray();

			setToChar = new char[converToChar.length + 2];
			

			System.out.println("");

			setToChar[0] = '0';

			setToChar[1] = '.';
			setToChar[2] = converToChar[0];
			setToChar[3] = converToChar[1];
			setToChar[4] = converToChar[2];

			System.out.println("");

			String str2 = "";

			for (int i = 0; i < setToChar.length; i++) {
				str2 = str2 + setToChar[i];
			}

			setDivNum = Double.parseDouble(str2);


			return setDivNum;

		}

	}
	
}



class ProTime {
	long time;

	ProTime(long time) {
		this.time = time;
	}
}

class ComparatorAscending implements Comparator<ProTime> {
	public int compare(ProTime L1, ProTime L2) {
		if (L1.time < L2.time) {
			return -1;
		} else {
			return 1;
		}
	}
}

package com.mirlab.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import com.mirlab.enumType.ControllerMode;
import com.mirlab.enumType.SouthboundMetric;
import com.mirlab.global.Global;

public class BenchmarkTimer {

	public static void ADD_CURRENT_TIME(LinkedList l) {
		l.add((long) System.currentTimeMillis());
	}

	public static void ADD_CURRENT_TIME(ArrayList l) {
		l.add((long) System.nanoTime());
	}

	public static boolean ABOVE_THRESHOLD(long iniTime, long threshold) {
		if (System.nanoTime() - iniTime > threshold) {
			return true;
		} else {
			return false;
		}

	}

	public static long GET_MAXIMUM(LinkedList l) {
		Collections.sort(l);
		long temp = (Long) l.getLast();
		return temp;
	}

	public static long GET_MAXIMUM(ArrayList l) {
		Collections.sort(l);
		long temp = (Long) l.get(l.size() - 1);
		return temp;
	}

	public static long GET_MINIMUM(LinkedList l) {
		Collections.sort(l);
		long temp = (Long) l.getFirst();
		return temp;
	}

	public static long GET_MINIMUM(ArrayList l) {
		Collections.sort(l);
		long temp = (Long) l.get(0);
		return temp;
	}

	public static double GET_TIME(LinkedList maxl, LinkedList minl) {
		double result = (GET_MAXIMUM(maxl) - GET_MINIMUM(minl)) / (double) 1000000;
		return result;
	}

	public static double GET_AVERAGE_TIME_WITHOUT_RE(ArrayList out, ArrayList in) {
		double result = 0;
		long totalTime = 0;
		int count = 0;
		int size = 0;
		boolean STOP = false;

		if (in.size() <= out.size()) {
			size = in.size();
		} else {
			size = out.size();
		}
		double th;

		if (Global.conMode == ControllerMode.CLUSTER) {
			if (Global.southboundMetric == SouthboundMetric.REACTIVE_PATH_PROVISIONING_TIME) {
				if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
					th = 20;

				} else {
					th = 20;
				}

			} else {
				if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET
						|| Global.UNKNOWN_PACKET_TYPE == Global.ARP_REQUEST) {
					th = 20;
				} else {
					th = 20;
				}
			}
		} else {
			if (Global.southboundMetric == SouthboundMetric.REACTIVE_PATH_PROVISIONING_TIME) {
				if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
					th = 20;

				} else {
					th = 1;
				}

			} else {
				if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET
						|| Global.UNKNOWN_PACKET_TYPE == Global.ARP_REQUEST) {
					th = 20;
				} else {
					th = 1;
				}
			}
		}

		while (!STOP) {
			result = 0;
			totalTime = 0;
			count = 0;
			for (int i = 0; i < size; i++) {
				double tempD = ((((Long) out.get(i)) - ((Long) in.get(i))) / (double) 1000000);
				long timeL = (Long) out.get(i) - (Long) in.get(i);

//			System.out.println("" + tempD);

				if (tempD > (double) th) {
					count++;

				} else {

					totalTime = totalTime + timeL;
				}

			}

			result = (double) totalTime / ((double) (out.size() - count)) / (double) 1000000;

			if (Math.abs(out.size() - count) < 10 || result < 0) {
				th = th + 1;
				continue;
			} else {
				break;
			}

		}

		return result;
	}

	public static double GET_AVERAGE_TIME(ArrayList out, ArrayList in) {// 获得平均时间
		double result = 0;
		long totalTime = 0;

		for (int i = 0; i < out.size(); i++) {
			totalTime = totalTime + ((Long) out.get(i)).longValue() - ((Long) in.get(i)).longValue();
			System.out.println("" + ((((Long) out.get(i)) - ((Long) in.get(i))) / (double) 1000000));

		}

		result = (double) totalTime / (double) out.size() / (double) 1000000;

		return result;
	}

	public static double GET_TIME(ArrayList maxl, ArrayList minl) {//
		double result = (GET_MAXIMUM(maxl) - GET_MINIMUM(minl)) / (double) 1000000;
		return result;
	}

}

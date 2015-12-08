package com.tang.base.common.utils;

public class RuntimeUtil {

	public static String memoryInfo() {
		Runtime currRuntime = Runtime.getRuntime();
		int maxMemory = (int) (currRuntime.maxMemory() / 1024 / 1024);
		int nFreeMemory = (int) (currRuntime.freeMemory() / 1024 / 1024);
		int nTotalMemory = (int) (currRuntime.totalMemory() / 1024 / 1024);
		return "Max" + maxMemory + "M Free" + nFreeMemory + "M Total" + nTotalMemory;
	}
}

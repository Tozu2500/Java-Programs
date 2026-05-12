package com.misc;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class RamInfo {

	public static void main(String[] args) {
		
		OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		
		long totalMemoryBytes = osBean.getTotalMemorySize();
		long freeMemoryBytes  = osBean.getFreeMemorySize();
		
		double totalMemoryGB = totalMemoryBytes / (1024.0 * 1024 * 1024);
		double freeMemoryMB = freeMemoryBytes  / (1024.0 * 1024);
		
		System.out.printf("Total RAM: %.2f GB%n", totalMemoryGB);
		System.out.printf("Free RAM: %.2f MB%n", freeMemoryMB);

	}

}

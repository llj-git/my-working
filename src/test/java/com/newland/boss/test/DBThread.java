package com.newland.boss.test;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.logging.log4j.Logger;

import com.newland.boss.cloud.web.balanceLibrary.dao.SQLDMLDao;

import org.apache.logging.log4j.LogManager;

public class DBThread implements Callable<String> {

	private Integer threadNumber;

	private String sql;

	public DBThread(Integer threadNumber, String sql) {
		super();
		this.threadNumber = threadNumber;
		this.sql = sql;
	}

	@Override
	public String call() throws Exception {
		System.out.println(">>>" + threadNumber + "任务启动");
		Date dateTmp1 = new Date();
		Thread.sleep(1000);
		Date dateTmp2 = new Date();
		long time = dateTmp2.getTime() - dateTmp1.getTime();
		System.out.println(">>>" + threadNumber + "任务终止");
		return threadNumber + "任务返回运行结果,当前任务时间【" + time + "毫秒】";
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		System.out.println("----程序开始运行----");
		Date date1 = new Date();
		int taskSize = 5;
		// 创建一个线程池
		ExecutorService pool = Executors.newFixedThreadPool(taskSize);
		// 创建多个有返回值的任务
		List<Future> list = new ArrayList<>();
		for (int i = 1; i < taskSize; i++) {
			Callable c = new DBThread(i, i + " ");
			// 执行任务并获取Future对象
			Future f = pool.submit(c);
			list.add(f);
		}
		// 关闭线程池
		pool.shutdown();
		// 获取所有并发任务的运行结果
		for (Future f : list) {
			// 从Future对象上获取任务的返回值，并输出到控制台
			System.out.println(">>>" + f.get().toString());
		}
		Date date2 = new Date();
		System.out.println("----程序结束运行----，程序运行时间【" + (date2.getTime() - date1.getTime()) + "毫秒】");

	}

}

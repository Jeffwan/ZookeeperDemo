package com.diorsding.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.diorsding.zookeeper.constants.Constants;

public class CreateSession {
	
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(Constants.connectionString, 
				Constants.timeout, Constants.timeout, retryPolicy);
		
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}
}

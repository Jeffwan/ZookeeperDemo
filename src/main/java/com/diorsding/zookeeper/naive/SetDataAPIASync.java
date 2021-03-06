package com.diorsding.zookeeper.naive;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.diorsding.zookeeper.helper.ZookeeperClientHelper;

public class SetDataAPIASync implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zookeeper = null;
	
	public static void main(String[] args) throws Exception {
		String path = "/zk-book"; 

		zookeeper = new ZooKeeper(ZookeeperClientHelper.connectionString, ZookeeperClientHelper.timeout, new SetDataAPIASync());
		
		connectedSemaphore.await();
		
		zookeeper.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

		// Object ctx is null. Whatever we pass, we can use it later in callback
		zookeeper.setData(path, "456".getBytes(), -1, new IStatCallback(), null);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			}
		}
	}
}

class IStatCallback implements AsyncCallback.StatCallback {

	public void processResult(int rc, String path, Object ctx, Stat stat) {
		System.out.println(String.format("RC: %d, Path: %s, Object %s, Stat %s", rc, path, ctx, stat));
		if (rc == 0) {
			System.out.println("SUCCESS");
		}
	}
	
}
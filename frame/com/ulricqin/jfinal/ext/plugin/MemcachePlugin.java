package com.ulricqin.jfinal.ext.plugin;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.jfinal.plugin.IPlugin;

public class MemcachePlugin implements IPlugin {

	private static MemCachedClient client = new MemCachedClient();
	private static String prefix = "";
	
	private String[] addr;
	private Integer[] weights;

	public MemcachePlugin(String[] addr, Integer[] weights) {
		this.addr = addr;
		this.weights = weights;
	}

	@Override
	public boolean start() {
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers(addr);
		pool.setWeights(weights);
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(200);
		pool.setMaxIdle(1000 * 30 * 30);
		pool.setMaintSleep(30);
		pool.setNagle(false);
		pool.setSocketTO(30);
		pool.setSocketConnectTO(0);
		pool.initialize();
		return true;
	}

	@Override
	public boolean stop() {
		SockIOPool pool = SockIOPool.getInstance();
		pool.shutDown();
		return true;
	}
	
	public static void setPrefix(String prefix) {
		MemcachePlugin.prefix = prefix;
	}

	public static Object get(String key) {
		return client.get(prefix + key);
	}
	
	public static boolean set(String key, Object val) {
		return client.set(prefix + key, val);
	}
	
	public static boolean flushAll() {
		return client.flushAll();
	}

	public static void main(String[] args) {
		String[] addr = { "127.0.0.1:11211" };
		Integer[] weights = { 3 };
		MemcachePlugin plugin = new MemcachePlugin(addr, weights);
		plugin.start();

		// 将数据放入缓存
		client.set("name", "Rain");
		String name = (String) client.get("name");
		System.out.println(name);

		client.set("name", "Flame");
		System.out.println(client.get("name"));

		// 删除缓存数据
		client.delete("name");
		System.out.println(client.get("name"));

		plugin.stop();

	}

}

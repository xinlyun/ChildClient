package com.lin.childclient.connection;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConnectionManager implements Connection.SocketConnectCall{
	private static ConnectionManager connectionManager;
	private TryConnectCall tryConnectCall;
	private MsgCallBack msgCallBack;
	String host; int port;
	Connection conn;
	public static ConnectionManager getInstence(){
		if(connectionManager==null){
			connectionManager = new ConnectionManager();
		}
		return connectionManager;
	}

	private volatile static long activeCycle = 1000;


	private static Set<Connection> pool = Collections
			.synchronizedSet(new HashSet<Connection>());


	private ConnectActiveMonitor monitor = new ConnectActiveMonitor();




	public Connection createConnection(String host, int port,TryConnectCall tryConnectCall)
	{
		this.host = host;
		this.port = port;
		this.tryConnectCall = tryConnectCall;
		conn = new Connection(this.host, this.port,ConnectionManager.this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				conn.beginConnect();
			}
		}).start();

		pool.add(conn);
		if(monitor.running == false)monitor.start();
		return conn;
	}

	public Connection createConnection(Socket socket) throws IOException {
		Connection conn = new Connection(socket);
		pool.add(conn);
		return conn;
	}

	public static void removeConnection(Connection conn) {
		pool.remove(conn);
	}

	@Override
	public void connectSucceful(String host) {
		if(tryConnectCall!=null)tryConnectCall.connectSucceful(host);
	}
	@Override
	public void connectFail(int err) {
		if(tryConnectCall!=null)tryConnectCall.connectFail(err);
		pool.remove(conn);
	}

	class ConnectActiveMonitor extends Thread {
		private volatile boolean running = false;
		public void run() {
			running = true;
			byte[] bytes = new byte[512];
			while (running) {
				long time = System.currentTimeMillis();
				for (Connection con : pool) {
					try {
						if (con.getLastActTime() + activeCycle < time){
//							System.out.println(con.receive(bytes));
							String str  = con.receive(bytes);
							Message msg = msgHandler.obtainMessage();
							msg.what = 0;
							msg.obj = str;
							msgHandler.sendMessage(msg);
							sleep(20);
						}
					} catch (IOException e) {
						removeConnection(con);
					} catch (Exception e) {
					}
				}

				yield();
			}
		}

		void setRunning(boolean b) {
			running = b;
		}
	}

	Handler msgHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					String str = (String) msg.obj;
					if(msgCallBack!=null){
						msgCallBack.receive(str);
//

					}
					break;
				default:
					break;
			}
		}
	};
	public void setMsgCallBack(MsgCallBack msgCallBack){
		this.msgCallBack = msgCallBack;
	}
	public interface TryConnectCall{
		void connectSucceful(String host);
		void connectFail(int err);
	}
	public interface MsgCallBack{
		void receive(String msg);
		void receive(byte[] bytes);
		void sendBack(int code);
	}


}
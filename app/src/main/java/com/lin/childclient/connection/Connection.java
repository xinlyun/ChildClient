package com.lin.childclient.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {
	private Socket socket;
	private OutputStream out;
	private InputStream in;
	private long lastActTime = 0;
	private String host = "";
	private SocketConnectCall connectCall;
	private int port ;
	Connection(String host, int port,SocketConnectCall connectCall) {
		this.host = host;
		this.port = port;
		this.connectCall = connectCall;

	}
	public boolean beginConnect(){
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(host, port));
			in = socket.getInputStream();
			out = socket.getOutputStream();
			System.out.println("connect succeful");
			if(connectCall!=null)connectCall.connectSucceful(host);
			return true;
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			if(connectCall!=null)connectCall.connectFail(1);
			System.out.println("connect fail");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(connectCall!=null)connectCall.connectFail(0);
			e.printStackTrace();
			return false;
		}
	}

	Connection(Socket socket) throws IOException {
		this.socket = socket;
		this.host = socket.getInetAddress().getHostAddress();
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	public void send(String m) throws IOException {
		lastActTime = System.currentTimeMillis();
		out.write(m.getBytes("utf8"));
		out.flush();
	}
	
	public String receive(byte[] bytes) throws IOException {
		lastActTime = System.currentTimeMillis();
//		byte[] str = new byte[512];
		int length=0;
		while((length=in.read(bytes))==-1){
			
		};
		return new String(bytes,0,length);
	}


	public synchronized void close() throws IOException {
		lastActTime = System.currentTimeMillis();
		ConnectionManager.removeConnection(this);
		if (socket != null)
			socket.close();
		if (in != null)
			in.close();
		if (out != null)
			out.close();
	}

	public synchronized long getLastActTime() {
		return lastActTime;
	}
	public void setConnectCall(SocketConnectCall connectCall){
		this.connectCall = connectCall;
	}
	interface SocketConnectCall{
		void connectSucceful(String host);
		void connectFail(int err);

	}
}
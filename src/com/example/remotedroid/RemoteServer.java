package com.example.remotedroid;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.example.remotedroid.natives.FrameHandler;
import com.example.remotedroid.network.EventPacket;
import com.example.remotedroid.network.Packet;
import com.example.remotedroid.network.PacketHeader;
import com.example.remotedroid.network.PacketHeader.OpCode;
import com.example.remotedroid.network.PacketListener;
import com.example.remotedroid.util.HongUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class RemoteServer extends Activity {
	//패킷의 최대 크기.
	private static final int MAXDATASIZE = 4090;
	int WIDTH = FrameHandler.WIDTH;
    int HEIGHT = FrameHandler.HEIGHT;
	//패킷을 저장하기위한 버퍼의 할당 ..
	byte[] screenbuff= new byte[1000000];
	//패킷전송받았을 때 메소드 실행하기위한 리스너 ..
	PacketListener packetlistener;
	private OutputStream sendStream;
	//패킷의 전체 사이즈와 받은 사이즈를 저장하기 위한 변수..
	int receivedSize=0;
	int jpgTotalSize=0;

	//전송받은 screenbuff를 비트맵으로 담기 위한 변수 .
	Bitmap bitmap;
	
	///canvas를 그리기 위한 thread
	MyViewThread mThread;
	ServerThread serverThread;
	
	///
	int Xposition;
	int Yposition;
	int EventCode;
	int flag=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(new MySurfaceView(this));
		
		packetlistener = new PacketListener() {

			@Override
			public void onPacketReceived(Packet packet) {
				// TODO Auto-generated method stub

				if (packet.getOpcode() == PacketHeader.OpCode.JPGINFO_SEND) {
					Log.e("info", "recv");
					receivedSize = 0;
					jpgTotalSize = HongUtil.atoi(packet.getPayload(), packet
							.getHeader().getPayloadLength() - 1);
				} else if (packet.getOpcode() == PacketHeader.OpCode.JPGDATA_SEND) {

					if (jpgTotalSize > receivedSize) {

						int CurTransSize = (jpgTotalSize - receivedSize) > MAXDATASIZE ? MAXDATASIZE
								: (jpgTotalSize - receivedSize);
						System.arraycopy(packet.getPayload(), 0, screenbuff,
								receivedSize, CurTransSize);
						receivedSize += CurTransSize;

					}
					if (jpgTotalSize == receivedSize) {
						printscreen(jpgTotalSize);
//						Message message = handler.obtainMessage(1);
//						handler.sendMessage(message);
					}
				}

			}

			@Override
			public void onInterrupt() {
				// TODO Auto-generated method stub

			}
		};
		
		///serverthread start
		System.out.println("remote start server");
		serverThread = new ServerThread(handler,packetlistener,getApplicationContext());
		serverThread.start();
	}
	//////////////// surfaceview 구현 부분.
	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

		public MySurfaceView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			getHolder().addCallback(this);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mThread = new MyViewThread(getHolder());
			mThread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			boolean done = true;
			while(done){
				try {
					mThread.join();
					done = false;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
//	////////////////////////////
//	//클립이벤트 발생될때 마다 스트림으로 전송. 패킷 만들어서 전송
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		Packet packet=null;
//		if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			Xposition = (int) event.getX();
//			Yposition = (int) event.getY();
//			EventCode = EventPacket.KEYDOWN;
//			System.out.println("move:"+Xposition+","+Yposition);
//        }
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			Xposition = (int) event.getX();
//			Yposition = (int) event.getY();
//			EventCode = EventPacket.TOUCHDOWN;
//			System.out.println("Down:"+Xposition+","+Yposition);
//		}
//		if (event.getAction() == MotionEvent.ACTION_UP) {
//			EventCode = EventPacket.TOUCHUP;
//			System.out.println("UP!");
//		}
//		if(event.getAction()==MotionEvent.ACTION_MOVE||event.getAction()==MotionEvent.ACTION_DOWN){
//			packet = makeEventPacket1(EventCode, Xposition, Yposition);
//		}else{
//			packet = makeEventPacket2(EventCode);
//		}
//		
//		try {
//		//	send(packet);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return super.onTouchEvent(event);
//	}

	/////////////////////////// surfaceview 사용 Thread 구현
	class MyViewThread extends Thread{
		private SurfaceHolder surfaceHolder;
		private boolean running = true;
		
		public MyViewThread(SurfaceHolder _holder) {
			// TODO Auto-generated constructor stub
			surfaceHolder=_holder;
		}
		public void run(){
			Canvas c;
			while(running){
				c = null;
				try{
					c= surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						//여기가 surface뷰를 그리는 메소드
						drawBackground(c);
					}
				}finally{
					if(c!=null){
						surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}
	
	////
	public void drawBackground(Canvas _canvas){
		if(flag==0){
			_canvas.drawARGB(255, 255, 255, 255);
		}else if(flag==1){
			_canvas.drawBitmap(bitmap, 0, 0, null);
		}
	}
	////
	void printscreen(int length){
        bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        bitmap = BitmapFactory.decodeByteArray(screenbuff, 0,length);
        flag=1;
	}
	Handler handler = new Handler() {

		public void handleMessage(Message message) {
			super.handleMessage(message);
			if (message.what == 1) {
				sendStream = (OutputStream) message.obj;
			} else if (message.what == 0) {

			}
		}
	};
	
	//두가지 경우
	//하나는 좌표가 있는 터치다운.
	public Packet makeEventPacket1(int eventCode,int xPosition, int yPosition){
		byte[] bytebuffer = new byte[10];
		byte[] buffer = new byte[2];
		int totalSize=0;
		totalSize+= atoi(eventCode, buffer);
		System.out.println("event int :"+ByteToInt(buffer));
		System.arraycopy(buffer, 0, bytebuffer, 0, 2);
		byte[] buffer2 = new byte[4];
		totalSize+= atoi(xPosition, buffer2);
		System.out.println("xposition:"+ByteToInt(buffer2));
		System.arraycopy(buffer2, 0, bytebuffer, 2, 4);
		totalSize+= atoi(yPosition, buffer2);
		System.out.println("yposition:"+ByteToInt(buffer2));
		System.arraycopy(buffer2, 0, bytebuffer, 6, 4);
		System.out.println("total:"+totalSize);
		Packet packet = new Packet(OpCode.EVENT_RECEIVED, bytebuffer, totalSize);
		for(int i=0; i<10; i++){
			System.out.print(packet.getPayload()[i]);
		}
		System.out.println();
		return packet;
	}
	//나머지 하나는 좌표가 없는 터치업.
		public Packet makeEventPacket2(int eventCode){
			byte[] bytebuffer = new byte[6];
			byte[] buffer = new byte[2];
			int totalSize=0;
			totalSize+= atoi(eventCode, buffer);
			System.arraycopy(buffer, 0, bytebuffer, 0, 2);
			byte[] buffer2 = new byte[4];
			totalSize+= atoi(EventPacket.KEYUP, buffer2);
			System.arraycopy(buffer2, 0, bytebuffer, 2, 4);
			Packet packet = new Packet(OpCode.EVENT_RECEIVED, bytebuffer, totalSize);
			return packet;
		}
	public static int atoi(int num, byte[] buffer){
		int temp=num;
		int count=0;
		for(int i=buffer.length; i>0; i--){
			temp = num;
			int jesu = (int)Math.pow(10, i-1);
			if((num/jesu)==0){
				buffer[count]=(byte)'0';
			}
			buffer[count] = (byte)(num/jesu+'0');
			temp = num%jesu;
			count++;
		}
		return count;
	}
	private static int ByteToInt(byte [] data){
		int result = 0;
		for(int i=0; i<data.length; i++){
			if(data[i] == ' ')
				continue;
			result = result * 10 + (data[i]-'0');
		}
		return result;
	}
	
//	public void send(Packet packet) throws IOException{
//		//get packet size for transmission
//		int packetSize = packet.getHeader().getPacketLength();
//				
//		synchronized(java.lang.Object.class){
//			sendStream.write(packet.asByteArray(), 0, packetSize);
//		}
//	}
}

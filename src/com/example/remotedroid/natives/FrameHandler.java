 
/*
 * Remoteroid - A remote control solution for Android platform, including handy file transfer and notify-to-PC.
 * Copyright (C) 2012 Taeho Kim(jyte82@gmail.com), Hyomin Oh(ohmnia1112@gmail.com), Hongkyun Kim(godgjdgjd@nate.com), Yongwan Hwang(singerhwang@gmail.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package com.example.remotedroid.natives;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class FrameHandler {
	
	static {
		System.loadLibrary("fbuffer");
	}
	
//	private ByteBuffer frameBuffer;
//	private ByteArrayOutputStream frameStream;
	
	
	private Bitmap bitmap;
	public static int WIDTH = 480;
    public static int HEIGHT = 800;
    public static int BYTE_PER_PIXEL = 2;
    public static int SCREEN_NUM = 2; // 화면 갯수
    public static int size = WIDTH * HEIGHT * BYTE_PER_PIXEL * SCREEN_NUM;
    byte[] buffer = new byte[size];
	private Context context;
	
	private int displaySize;
	private int width;
	private int height;
	private int pixel;
	private int orientation;
	private int pixelFormat;
	private int jpegSize;
	
	private boolean isBuffered = false;
	
	/**
	 * Read frame buffer from device.
	 * @param buff Byte buffer where frame buffer's data will be stored
	 * @param pixelformat
	 * @return
	 */
	//private native int getFrameBuffer(byte[] buff, int pixelformat);
	private native int getFrameBuffer(byte[] buff);
	
	
	public FrameHandler(Context context) {
		this.context = context;
		
		setDisplayValue();
		setBitmap(getDisplayBitmap());
		
//		frameBuffer = ByteBuffer.allocate(displaySize);
		
//		frameStream = new ByteArrayOutputStream();				
	}
	
	
	private void setDisplayValue() {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	
		this.width = dm.widthPixels/2;
		this.height = dm.heightPixels/2;
		this.pixelFormat=display.getPixelFormat();		
		this.pixel=4;
		this.orientation = getDisplayOrientation();		
		this.displaySize = width*height*pixel;
		
	}

	// get Device Display Bitmap
	public Bitmap getDisplayBitmap(){
		return Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
	}
	
	// get Device Display Orientation
	public int getDisplayOrientation(){
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display.getOrientation();
	}
	
	// Compress JPEG
	public byte[] getFrameStream(){
//		if(orientation != getDisplayOrientation()){
//			setDisplayValue();
//			setBitmap(getDisplayBitmap());
//		}	
		Log.i("frame :","start");
		jpegSize = getFrameBuffer(buffer);
		Log.d("frame :","end");
		///btye를 바로 png 형태로 압축??.
		
		Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(buffer));
		//System.out.println(bitmap.getByteCount());
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 15, byteArray);
		jpegSize = byteArray.toByteArray().length;
		return byteArray.toByteArray();
	}
	
	public int getJpegSize(){
		return jpegSize;
	}
	
	/*
	public Bitmap getTestFrameStream(){
		
		if(orientation != getDisplayOrientation()){
			setDisplayValue();
			setBitmap(getDisplayBitmap());
		}
		
		int ret = getFrameBuffer(buffer, pixelFormat);
		
		
		frameBuffer.put(buffer, 0, displaySize);
		frameBuffer.rewind();
		bitmap.copyPixelsFromBuffer(frameBuffer);
		
		return bitmap;
	}
	*/
	/*
	public ByteBuffer getFrameBuffer() {
		return frameBuffer;
	}

	public void setFrameBuffer(ByteBuffer frameBuffer) {
		this.frameBuffer = frameBuffer;
	}
*/
	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getPixel() {
		return pixel;
	}
	public void setPixel(int pixel) {
		this.pixel = pixel;
	}
	public int getDisplaySize() {
		return displaySize;
	}
	public void setDisplaySize(int displaySize) {
		this.displaySize = displaySize;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public int getOrientation() {
		return orientation;
	}
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	public boolean isBuffered() {
		return isBuffered;
	}
	public void setBuffered(boolean isBuffered) {
		this.isBuffered = isBuffered;
	}

}

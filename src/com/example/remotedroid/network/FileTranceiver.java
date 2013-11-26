 
package com.example.remotedroid.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;



import com.example.remotedroid.network.PacketHeader.OpCode;


import android.util.Log;

public class FileTranceiver extends PacketSender{
	private static final String TAG = "FileTranceiver";
	FileReceiver fileReceiver;
	FileSender fileSender;
	
	public FileTranceiver(OutputStream stream, FileTransmissionListener listener){
		super(stream);
		fileReceiver = new FileReceiver(listener);
		fileSender = new FileSender(listener);
	}

	
	
	/**
	 * FileReceiver receive File Information(file name, size) and file data
	 * And store file to SDCARD
	 * @author ssm
	 */
	class FileReceiver{
		private long totalFileSize;
		private long recvFileSize;
		private File file;

		private FileOutputStream out;
		private FileTransmissionListener mListener;
		
		public FileReceiver(FileTransmissionListener listener){
			this.mListener = listener;
		}
		
		/**
		 * Create file that Received information(file name, size)		   
		 * @param packet
		 */
		
		
		/**
		 * Store file to SDCARD that received file data
		 * @param packet
		 */

	
	}
	
	class FileTransmitStopException extends Exception{
		
	}
	
	/**
	 * FileSender send file information(file name, size) and file data to host
	 * @author ssm
	 */
	class FileSender{
		private final int 		MAXDATASIZE 			= Packet.MAX_LENGTH-PacketHeader.LENGTH;
		private byte [] 		buffer 					= new byte[MAXDATASIZE];			
		private FileInputStream	in 						= null;
		private ArrayList<File>	fileList				= null;	
		private FileTransmissionListener mListener;
		private boolean isTransfer 						= false;
		
		public FileSender(FileTransmissionListener listener){		
			mListener = listener;
		}
		
		/**
		 * Set fileList for transmit and 
		 * Send to host that ready for send file
		 * @param fileList
		 */
		public void setFilesToSend(ArrayList<File> fileList) throws IOException{
			if(isTransfer)
				return;
			
			this.fileList = fileList;
			
			if(!fileList.isEmpty()){
			
				send(new Packet(OpCode.READY_TO_SEND, null, 0));
				mListener.onReadyToSend(fileList);
			}
		}
		
		/**
		 * Send first file information(Name, Size) of FileList to Host
		 * @throws IOException
		 */
		public void SendFileInfo(){		
			if(fileList.isEmpty()){
				try {
					Log.i("qq","FILETRANSFER_COMPLETE");
					send(new Packet(OpCode.FILETRANSFER_COMPLETE, null, 0));
					isTransfer = false;
					mListener.onAllFileTransferSucceeded();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			File currentFile = fileList.get(0);
			FileinfoPacket fileInfoPacket = new FileinfoPacket(currentFile);
			mListener.onSendFileInfo(currentFile);
			try {
				send(fileInfoPacket);
			} catch (IOException e) {
				e.printStackTrace();
				mListener.onFileTransferInterrupted();
			}		
		}
		
		public void transferStopRequested(){
			isTransfer = false;
		}
		
		public void SendFileData(){
			isTransfer = true;
			new SendFileDataThread().start();		
		}
		
		/**
		 *Send to host that first File data of file list
		 */
		class SendFileDataThread extends Thread{
			public void run(){
				try{
					File file = fileList.remove(0);
					long fileSize = file.length();
					long sentFileSize = 0;
					
					in = new FileInputStream(file);			
					
					while(fileSize > sentFileSize){					
						if(isTransfer == false){
							Log.i("qq","CANCEL FILETRANSFER_COMPLETE");
							send(new Packet(OpCode.FILETRANSFER_COMPLETE, null, 0));
							mListener.onAllFileTransferSucceeded();
							throw new FileTransmitStopException();												
						}							
						
						int iCurrentSendSize =
								(int) ((fileSize - sentFileSize) > MAXDATASIZE ? MAXDATASIZE : (fileSize - sentFileSize));
						in.read(buffer, 0, iCurrentSendSize);	
						
						send(new Packet(OpCode.FILEDATA_RECEIVED, buffer, iCurrentSendSize));
						
						sentFileSize += iCurrentSendSize;						
					}
					mListener.onFileSent(file);
					
					SendFileInfo();
				}catch(FileTransmitStopException e){			
			
				}catch(FileNotFoundException e){
					e.printStackTrace();
					mListener.onFileTransferInterrupted();
				}catch(IOException e){					
					e.printStackTrace();
					mListener.onFileTransferInterrupted();
				}finally{
					if(in != null)
					{
						try{
							in.close();				
						}catch(IOException e){};
						in = null;
					}
				}		
			}
		}
		
		public void DeleteFileList(){
			if(fileList==null)
				return;
			fileList.clear();		
		}
	}	
}

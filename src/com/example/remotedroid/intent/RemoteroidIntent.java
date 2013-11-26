 
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

package com.example.remotedroid.intent;

/**
 * A class represents action, category, extras related to Intent.
 * @author Taeho Kim
 *
 */
public final class RemoteroidIntent {
	/**
	 * An action used for PendingIntent to notify user about SMS has been sent successful or not.<div/>
	 * <strong>Extras:</strong><br/>
	 * {@link RemoteroidIntent#EXTRA_PHONE_NUMBER}
	 */
	public static final String ACTION_SMS_SENT = "com.example.remotedroid.intent.action.SMS_SENT";
	
	/**
	 * An action that successfully connected to server. (Broadcast)
	 * @see RemoteroidIntent#EXTRA_IP_ADDESS
	 */
	public static final String ACTION_CONNECTED = "com.example.remotedroid.intent.action.CONNECTED";
	
	/**
	 * An action that a client has disconnected from server.
	 */
	public static final String ACTION_DISCONNECTED = "com.example.remotedroid.intent.action.DISCONNECTED";
	
	/**
	 * An action that client failed to connect to server.
	 */
	public static final String ACTION_CONNECTION_FAILED = "com.example.remotedroid.intent.action.CONNECTION_FAILED";
	
	/**
	 * An action that there connection was interrupted by some reason.
	 */
	public static final String ACTION_INTERRUPTED = "com.example.remotedroid.intent.action.INTERRUPTED";
	
	public static final String ACTION_DEVICE_OPEN_FAILED = "com.example.remotedroid.intent.action.DEVICE_OPEN_FAILED";
	
	public static final String ACTION_LOGIN = "com.example.remotedroid.intent.action.LOGIN";
	public static final String ACTION_REGISTER = "com.example.remotedroid.intent.action.REGISTER";
	
	/**
	 * @see #EXTRA_IP_ADDESS
	 */
	public static final String ACTION_REMOTE_CONNECT = "com.example.remotedroid.intent.action.REMOTE_CONNECT";
	
	/**
	 * Broadcast action
	 */
	public static final String ACTION_DEVICE_REGISTRATION_COMPLETE = "com.example.remotedroid.intent.action.DEVICE_REGISTRATION_COMPLETE";
	
	/**
	 * Broadcast action
	 */
	public static final String ACTION_DEVICE_REGISTRATION_FAILED = "com.example.remotedroid.intent.action.DEVICE_REGISTRATION_FAILED";
	
	public static final String CATEGORY_UNIVERSAL = "com.example.remotedroid.intent.category.UNIVERSAL";
	
	/**
	 * Key for extra data contains server's ip address.
	 * @see RemoteroidIntent#ACTION_CONNECTED
	 */
	public static final String EXTRA_IP_ADDESS = "com.example.remotedroid.intent.extra.IP_ADDRESS";
	/**
	 * Key for extra data contains phone number.
	 * @see RemoteroidIntent#ACTION_SMS_SENT
	 */
	public static final String EXTRA_PHONE_NUMBER = "com.example.remotedroid.intent.extra.PHONE_NUMBER";
	
	/**
	 * An action that client succedded send file.
	 * @see RemoteroidIntent#ACTION_FILE_TRANSMISSION_SECCESS
	 */
	public static final String ACTION_FILE_TRANSMISSION_SECCESS = "com.example.remotedroid.intent.action.FILE_TRANSMISSION_SUCCESS";
	public static final String ACTION_ALL_FILE_TRANSMISSION_SECCESS = "com.example.remotedroid.intent.action.ALL_FILE_TRANSMISSION_SUCCESS";
	
	
	public static final String ACTION_SHOW_DRIVER_INSTALLATION_FRAGMENT = "com.example.remotedroid.intent.action.SHOW_DRIVER_FRAGMENT";
	public static final String ACTION_SHOW_CONNECT_FRAGMENT = "com.example.remotedroid.intent.action.SHOW_CONNECT_FRAGMENT";
	public static final String ACTION_SHOW_CONNECTED_FRAGMENT = "com.example.remotedroid.intent.action.SHOW_CONNECTED_FRAGMENT";
}

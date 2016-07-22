package com.nairobisoftwarelab.sms;

public interface SdpConstants {

	/**
	 * STATUS_PENDING represents a new notification thats has just been entered
	 * into the database. This is the default value
	 */
	int STATUS_PENDING = 0;
	/**
	 * STATUS_ACTIVE represents a notification that has already been processed
	 * to sdp and its active
	 */
	int STATUS_READY = 1;
	int STATUS_ACTIVE = 2;
	/**
	 * STATUS_STOPPED represents a notification that has been taken off line.
	 * When a service is no longer running.
	 */
	int STOP_PENDING = 3;
	int STATUS_STOPPED = 4;

	/**
	 * Service notification is active
	 */
	int SMSNOTSENT = 0;
	/**
	 * Service notification is inactive
	 */
	int SMSSENT = 1;
}

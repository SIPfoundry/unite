package com.ezuce.jitsi;

import javax.media.CaptureDeviceInfo;

import org.jitsi.impl.neomedia.device.CaptureDeviceInfo2;

/**
 * Encapsulates a <tt>CaptureDeviceInfo</tt> for the purposes of its display in
 * the user interface.
 */

public class CaptureDevice {
	/**
	 * The encapsulated info.
	 */
	public final CaptureDeviceInfo info;

	/**
	 * Creates the wrapper.
	 * 
	 * @param info
	 *            the info object we wrap.
	 */
	public CaptureDevice(CaptureDeviceInfo info) {
		this.info = info;
	}

	/**
	 * Determines whether the <tt>CaptureDeviceInfo</tt> encapsulated by this
	 * instance is equal (by value) to a specific <tt>CaptureDeviceInfo</tt>.
	 * 
	 * @param cdi
	 *            the <tt>CaptureDeviceInfo</tt> to be determined whether it is
	 *            equal (by value) to the <tt>CaptureDeviceInfo</tt>
	 *            encapsulated by this instance
	 * @return <tt>true</tt> if the <tt>CaptureDeviceInfo</tt> encapsulated by
	 *         this instance is equal (by value) to the specified <tt>cdi</tt>;
	 *         otherwise, <tt>false</tt>
	 */
	public boolean equals(CaptureDeviceInfo cdi) {
		return (info == null) ? (cdi == null) : info.equals(cdi);
	}

	/**
	 * Gets a human-readable <tt>String</tt> representation of this instance.
	 * 
	 * @return a <tt>String</tt> value which is a human-readable representation
	 *         of this instance
	 */
	@Override
	public String toString() {
		String s;

		if (info == null) {
			s = "None";
		} else {
			s = info.getName();
			if (info instanceof CaptureDeviceInfo2) {
				String transportType = ((CaptureDeviceInfo2) info)
						.getTransportType();

				if (transportType != null)
					s += " (" + transportType + ")";
			}
		}
		return s;
	}
}

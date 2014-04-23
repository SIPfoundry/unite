package org.ezuce.common.async;

import org.jivesoftware.smackx.packet.VCard;

public interface VCardLoaderCallback {
	void vcardLoaded(VCard vCard);
}

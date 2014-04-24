package org.ezuce.commons.ui.custom.message;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jivesoftware.smack.packet.Presence;

public class CustomMessageTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -5928429667671276005L;
	private Presence mPresence;
	private boolean mCustom;
	private int mPriority;
	private String mStatus;
	private String mType;

	public CustomMessageTreeNode(Presence presence) {
		super(presence.getStatus());
		this.mPresence = presence;
	}

	public CustomMessageTreeNode(int priority, String status, String type) {
		this.mCustom = true;
		this.mPriority = priority;
		this.mStatus = status;
		this.mType = type;
	}

	public CustomMessageTreeNode() {
	}

	public Presence getPresence() {
		return mPresence;
	}

	public boolean isCustom() {
		return mCustom;
	}

	public String getStatus() {
		return mStatus;
	}

	public String getType() {
		return mType;
	}

	public int getPriority() {
		return mPriority;
	}
}

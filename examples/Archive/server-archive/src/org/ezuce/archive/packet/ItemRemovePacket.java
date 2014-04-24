package org.ezuce.archive.packet;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.ezuce.archive.model.xml.IXmlElement;

public class ItemRemovePacket extends ArchivePacket {

	private static final MessageFormat gElement = new MessageFormat(
			"<itemremove xmlns=\"{0}\">{1}</itemremove>");

	private List<IXmlElement<?>> mItems = new ArrayList<IXmlElement<?>>();

	@Override
	public String getChildElementXML() {
		StringBuilder items = new StringBuilder();
		for (IXmlElement<?> item : getItems())
			items.append(item.toXml());

		return gElement
				.format(new String[] { gXmlns, items.toString() });
	}

	public List<IXmlElement<?>> getItems() {
		return mItems;
	}

}

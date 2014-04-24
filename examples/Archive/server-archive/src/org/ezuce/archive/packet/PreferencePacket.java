package org.ezuce.archive.packet;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.ezuce.archive.model.xml.Auto;
import org.ezuce.archive.model.xml.Default;
import org.ezuce.archive.model.xml.Item;
import org.ezuce.archive.model.xml.Method;
import org.ezuce.archive.model.xml.Session;

public class PreferencePacket extends ArchivePacket {

	public static final String Element = "pref";

	private static final String gEmptyPrefElement = "<pref xmlns=\""
			+ gXmlns + "\" />";

	private static final MessageFormat gPrefXml = new MessageFormat(
			"<pref xmlns=\"{0}\" >{1}</pref>");

	private Auto auto;
	private Default defaultItem;
	private List<Item> items = new ArrayList<Item>();
	private List<Session> sessions = new ArrayList<Session>();
	private List<Method> methods = new ArrayList<Method>();

	@Override
	public String getChildElementXML() {
		if (isAllElementsEmpty())
			return gEmptyPrefElement;

		// if (getDefaultItem() == null)
		// throw new IllegalStateException(
		// "MUST include a <default/> element that specifies the user's default settings for OTR Mode and Save Mode.");

		// if (getAuto() == null)
		// throw new IllegalStateException(
		// "MUST include an <auto/> element that specifies whether automatic archiving is on or off.");

		// if (getMethods().size() != 0 && getMethods().size() != 3)
		// throw new IllegalStateException(
		// "MUST include at least three <method/> elements, differentiated by the value of the 'type' attribute (i.e., at least one <method/> element each for \"auto\", \"local\", and \"manual\").");

		StringBuilder xml = new StringBuilder();

		if (getAuto() != null)
			xml.append(getAuto().toXml());

		if (getDefaultItem() != null)
			xml.append(getDefaultItem().toXml());

		for (Item item : getItems())
			if (item != null)
				xml.append(item.toXml());

		for (Session session : getSessions())
			if (session != null)
				xml.append(session.toXml());

		for (Method method : getMethods())
			if (method != null)
				xml.append(method.toXml());

		return gPrefXml.format(new String[] { gXmlns, xml.toString() });
	}

	public void setAuto(Auto auto) {
		this.auto = auto;
	}

	public Auto getAuto() {
		return auto;
	}

	public void setItem(List<Item> items) {
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}

	/**
	 * @return a copy of the original array.
	 */
	public List<Method> getMethods() {
		return new ArrayList<Method>(methods);
	}

	public void addMethod(Method newMethod) {
		if (newMethod == null)
			return;

		for (Method method : methods)
			if (newMethod.getType() == method.getType())
				throw new IllegalStateException(
						"MUST include at least three <method/> elements, differentiated by the value of the 'type' attribute (i.e., at least one <method/> element each for \"auto\", \"local\", and \"manual\")");

		methods.add(newMethod);
	}

	public void setDefault(Default def) {
		this.defaultItem = def;
	}

	public Default getDefaultItem() {
		return defaultItem;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	private boolean isAllElementsEmpty() {
		return getAuto() == null && getDefaultItem() == null
				&& getItems().isEmpty() && getSessions().isEmpty()
				&& getMethods().isEmpty();
	}

}

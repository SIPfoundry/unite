package org.ezuce.archive;

import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

public class HtmlFormatter {

	public static String formatBottomHistorySeparator() {
		StringBuffer htmlText = new StringBuffer();
		htmlText.append(formatEmptySeparator());
		htmlText.append("<tr style='line-height:5px;height:5px;margin:5px;padding:5px; "
				+ "border-style:solid; border-width:2px; border-color:gray;'/>");
		htmlText.append(formatEmptySeparator());
		return htmlText.toString();
	}

	public static String formatEmptySeparator() {
		return "<tr style='line-height:10px;font-size:10px;height:10px;margin:10px;padding:10px;'/>";
	}

	public static String formatInitialContent(String tableId) {
		return "<html><body><table width=100% height=100% id='"
				+ tableId
				+ "' style='font-family: arial; font-size: 10px; text-align: left; '></table></body></html>";
	}

	public static void addMockMessages(HTMLDocument doc, Element table)
			throws BadLocationException, IOException {

		String str = "";
		str += "<tr><td width='150'>(07:45:54 PM)</td><td width='100'>gniculae:</td><td>that was a fine move douglas, moving to RPM I mean</td></tr>";
		str += "<tr><td>(07:47:01 PM)</td><td>douglas:</td><td>y, and i'm glad we even started open repos out to native speakers, something also not possible before</td></tr>";
		str += "<tr><td>(07:47:45 PM)</td><td>gniculae:</td><td>right, btw for people that didn't hear yet, we have a Dutch maintainer from open source community now :)</td></tr>";
		str += "<tr><td>(08:03:20 PM)</td><td>Matt:</td><td>thanks guys</td></tr>";
		str += "<tr><td>(03:14:57 PM)</td><td>Lou Uysison:</td><td>hi guys, im getting this error when visiting config page of my openuc instance… http://pastebin.com/GjF8B6a8</td></tr>";
		str += "<tr><td>(03:15:01 PM)</td><td>Lou Uysison:</td><td>any ideas?</td></tr>";
		str += "<tr><td>(03:15:19 PM)</td><td>gniculae:</td><td>looks like Mongo not properly set up</td></tr>";
		str += "<tr><td>(03:15:32 PM)</td><td>gniculae:</td><td>do mongo</td></tr>";
		str += "<tr><td>(03:15:42 PM)</td><td>gniculae:</td><td>and see how it is shown, should be disaplyaed as PRIMARY></td></tr>";
		str += "<tr><td>(03:16:17 PM)</td><td>Lou Uysison:</td><td>yes, not showing as PRIMARY> , only ></td></tr>";
		str += "<tr><td>(03:16:36 PM)</td><td>gniculae:</td><td>did you sipxecs-setup lately?</td></tr>";
		str += "<tr><td>(03:17:13 PM)</td><td>Lou Uysison:</td><td>no, just upgraded, i'll try that.</td></tr>";
		str += "<tr><td>(03:17:25 PM)</td><td>gniculae:</td><td>ok</td></tr>";
		str += "<tr><td>(03:17:31 PM)</td><td>gniculae:</td><td>so you upgraded form unstable?</td></tr>";
		str += "<tr><td>(03:18:44 PM)</td><td>Lou Uysison:</td><td>i upgraded my stable build</td></tr>";
		str += "<tr><td>(03:23:59 PM)</td><td>gniculae:</td><td>hm</td></tr>";
		str += "<tr><td>(03:24:12 PM)</td><td>gniculae:</td><td>have you rebooted machine after?</td></tr>";
		str += "<tr><td>(03:24:47 PM)</td><td>Lou Uysison:</td><td>might not have, i got it working now after sipxecs-setup</td></tr>";
		str += "<tr><td>(03:25:06 PM)</td><td>gniculae:</td><td>ok</td></tr>";
		str += "<tr><td>(03:25:15 PM)</td><td>gniculae:</td><td>I will start an unstable build in about 10</td></tr>";
		str += "<tr><td>(03:25:25 PM)</td><td>gniculae:</td><td>have to add a new branch</td></tr>";
		str += "<tr><td>(03:25:42 PM)</td><td>Lou Uysison:</td><td>ok thanks george</td></tr>";
		str += "<tr><td>(03:25:55 PM)</td><td>gniculae:</td><td>np</td></tr>";
		str += "<tr><td>(03:26:54 PM)</td><td>douglas:</td><td>I wouldn't have guess sipxecs-setup would have fixed it, gniculae what made you know that.</td></tr>";
		str += "<tr><td>(03:28:22 PM)</td><td>gniculae:</td><td>it looked like mongo was not properly configured, so since sipxecs-setup obliterates it and reconfig I assumed that will work</td></tr>";
		str += "<tr><td>(03:29:28 PM)</td><td>douglas:</td><td>obliterates it only if you run --reset-all</td></tr>";
		try {
			doc.insertBeforeEnd(table, str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String formatTopHistoryHeaderSeparator(String header) {
		StringBuffer htmlText = new StringBuffer();
		htmlText.append(formatEmptySeparator());
		htmlText.append("<tr style='border-style:solid;border-width:20px;border-color:yellow;bgcolor=yellow'>");

		htmlText.append("<td colspan='3' align='left'><b>" + header + "</b></td>");
		htmlText.append("</tr>");
		htmlText.append(formatEmptySeparator());
		return htmlText.toString();
	}

}

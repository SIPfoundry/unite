package org.ezuce.im.html;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.UIManager;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.ezuce.im.ui.EzuceTranscriptWindow;
import org.jivesoftware.sparkimpl.plugin.emoticons.Emoticon;
import org.jivesoftware.sparkimpl.plugin.emoticons.EmoticonManager;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

/**
 * Handles HTML formatting. Used by {@linkplain EzuceTranscriptWindow}
 *
 * @author Alex
 *
 */
public class HtmlFormatter {

	public static List<String> formatStyleSheets() {
		List<String> css = new ArrayList<String>();

		css.add(".bodyCol {text-align: left; color: #5B5D5E;}");
		css.add(".avatar {max-height: 30px; max-width: 30px; width: expression(this.width > 30 ? '30px' : true); height: expression(this.height > 30 ? '30px' : true);}");	
		css.add(".timeCol {color: #888C8C; margin-left:8px; margin-right:8px;}");
		css.add(".sep {height: 1px; color: #D6DCDD;}");
		String color = encodeRGB((Color) UIManager.get("Link.foreground"));
		css.add(".link {text-decoration: underline; color: " + color + ";}");
		color = encodeRGB((Color) UIManager.get("Address.foreground"));
		css.add(".address {text-decoration: underline; color: " + color + ";}");
		// not used, seems to be ignored
		css.add(".row {vertical-align: top;}");

		return css;
	}

	public static String formatInitialContent(String tableId) {
		return "<html><body><table width=100% id='" + tableId
				+ "' style='font-family: arial; font-size: 10px;'></table></body></html>";
		}

	public static String formatMessage(String nickname, String body, Color nicknameColor, String avatarUrl) {
		return formatMessage(nickname, body, null, nicknameColor, avatarUrl);
	}

	private static String formatName(String text, String color) {
		return "<span style='color: " + color + ";'><b>" + text + "</b></span>";
	}

	private static String formatAvatar(String url) {		
		return url != null ? "<img width='30' height='30' hspace='8' align='top' src='" + url + "'/>" :"<span/>";
	}
	
	private static String formatMessageBody(StringBuilder htmlText, String body, String sentDate) {
		final StringBuilder sb = new StringBuilder();
		EmoticonManager em = EmoticonManager.getInstance();
		boolean isEmoticonsEnabled = SettingsManager.getLocalPreferences().areEmoticonsEnabled();
		htmlText.append("<pre style='font-family: arial; font-size: 10px; margin-top: 0em; margin-bottom: 0em; text-indent: 0;'>");
		final StringTokenizer tokenizer = new StringTokenizer(body, " \n \t", true);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			Emoticon e = em.getEmoticon(token);
			URL emoticonURL = null;

			if (isEmoticonsEnabled && e != null) {
				emoticonURL = em.getEmoticonURL(e);
			}

			if (emoticonURL != null) {
				flushText(sb, htmlText);
				htmlText.append(formatImage(emoticonURL.toString()));
			} else if ((StringUtils.startsWith(token, "http://") || StringUtils.startsWith(token, "ftp://") || StringUtils.startsWith(token, "https://") || 
					StringUtils.startsWith(token, "www.")) && token.indexOf(".") > 1) {
				flushText(sb, htmlText);
				htmlText.append(formatLink(token));
			} else if (token.startsWith("\\\\") || (token.indexOf("://") > 0 && token.indexOf(".") < 1)) {
				flushText(sb, htmlText);
				htmlText.append(formatAddress(token));
			} else {
				sb.append(StringEscapeUtils.escapeHtml4(token));
			}
		}
		flushText(sb, htmlText);
		htmlText.append("</pre>");
		htmlText.append("</td>");
		if (sentDate != null) {
			htmlText.append("<td class='timeCol' width=64 align='right'><span>").append(sentDate).append("</span></td>");
		}
		htmlText.append("</tr>");

		return htmlText.toString();
	}	

	public static String formatMessage(String nickname, String body, String sentDate, Color nicknameColor,
			String avatarUrl) {
		StringBuilder htmlText = new StringBuilder();
		htmlText.append("<tr valign='top'>");
		htmlText.append("<td width=48>").append(formatAvatar(avatarUrl)).append("</td>");
		htmlText.append("<td class='bodyCol'>");
		htmlText.append(formatName(nickname, encodeRGB(nicknameColor)));

		return formatMessageBody(htmlText, body, sentDate);

	}

	public static String formatMessage(String body) {
		return formatMessage(body, null);
	}

	public static String formatMessage(String body, String sentDate) {
		StringBuilder htmlText = new StringBuilder();

		htmlText.append("<tr valign='top'>");
		htmlText.append("<td width=48></td>");
		htmlText.append("<td class='bodyCol'>");

		return formatMessageBody(htmlText, body, sentDate);
	}

	public static String formatNotificationMessage(String message, Color foregroundColor) {
		StringBuilder htmlText = new StringBuilder();

		htmlText.append("<tr valign='top'>");
		htmlText.append("<td colspan=3 align='left'>").append("<font color='").append(encodeRGB(foregroundColor))
				.append("'>").append(message).append("</td>");
		htmlText.append("</tr>");

		return htmlText.toString();
	}

	public static String formatHistorySeparator(String sentDate) {
		StringBuffer htmlText = new StringBuffer();

		htmlText.append("<tr valign='top' bgcolor='#FFFFCC'>");
		htmlText.append("<td colspan='3' style='font-family: arial;'><b>").append(sentDate + "</b></td>");
		htmlText.append("</tr>");

		return htmlText.toString();
	}

	public static String formatPartySeparator() {
		return "<tr style='line-height:0px;font-size:0px;height:0px;margin:0;padding:0; " +
				"border-style:solid; border-width:1px; border-color:#DDE6E7 #EDF6F7 #EDF6F7 #EDF6F7;'/>";
	}
	
	public static String formatBottomHistorySeparator() {
		StringBuffer htmlText = new StringBuffer();
		htmlText.append(formatEmptySeparator());
		htmlText.append("<tr style='line-height:5px;height:5px;margin:5px;padding:5px; " +
				"border-style:solid; border-width:2px; border-color:gray;'/>");
		htmlText.append(formatEmptySeparator());
		return htmlText.toString();
	}

	public static String formatEmptyLine() {
		return "<tr style='line-height:0px;font-size:0px;height:0px;margin:0;padding:0;'/>";
	}
	
	public static String formatEmptySeparator() {
		return "<tr style='line-height:10px;font-size:10px;height:10px;margin:10px;padding:10px;'/>";
	}
	
	public static String formatEmptyLine2() {
		return "<tr style='line-height:0px;font-size:0px;height:0px;margin:0;padding:0;'><td colspan=3></td></tr>";
	}

	private static String formatText(String text) {
		return "<span>" + text + "</span>";
	}

	private static String formatImage(String url) {
		return "<img src='" + url + "'/>";
	}

	private static String formatLink(String text) {
		return "<a href='" + text + "'>" + text + "</a>";
	}

	private static String formatAddress(String text) {
		return "<a href='"+text+"'><span class='address'>" + text + "</span></a>";
	}

	private static void flushText(StringBuilder sb, StringBuilder htmlText) {
		if (sb.length() > 0) {
			htmlText.append(formatText(sb.toString()));
			sb.delete(0, sb.length() - 1);
		}
	}

	private static String encodeRGB(Color color) {
		int rgb;

		if (color != null) {
			rgb = color.getRGB();
		} else {
			rgb = 0;
		}
		String htmlColor = Integer.toHexString(rgb);
		return "#" + (htmlColor.length() >= 2 ? htmlColor.substring(2).toUpperCase() : "000000");
	}
}

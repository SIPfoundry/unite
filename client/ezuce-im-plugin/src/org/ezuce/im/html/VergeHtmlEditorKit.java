package org.ezuce.im.html;

import javax.swing.SizeRequirements;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.GlyphView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;
import javax.swing.text.html.ParagraphView;

import org.jivesoftware.spark.util.log.Log;

public class VergeHtmlEditorKit extends HTMLEditorKit {
	
	private static final long serialVersionUID = 1L;

	@Override 
	public ViewFactory getViewFactory() {
		return new HTMLFactory() {
			public View create(Element e) {
				View v = super.create(e);
				if (v instanceof InlineView) {
					return new InlineView(e) {
						public int getBreakWeight(int axis, float pos, float len) {
							if (axis == View.X_AXIS) {
								checkPainter();
								int p0 = getStartOffset();
								int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
								if (p1 == p0) {
									return View.BadBreakWeight;
								}
								try {									
									if (getDocument().getText(p0, p1 - p0).indexOf("\r") >= 0) {
										return View.ForcedBreakWeight;
									}
								} catch (BadLocationException ex) {
									Log.error("Cannot break line ", ex);
								}
							}
							return super.getBreakWeight(axis, pos, len);
						}

						public View breakView(int axis, int p0, float pos, float len) {
							if (axis == View.X_AXIS) {
								checkPainter();
								int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
								try {									
									int index = getDocument().getText(p0, p1 - p0).indexOf("\r");
									if (index >= 0) {
										GlyphView v = (GlyphView) createFragment(p0, p0 + index + 1);
										return v;
									}
								} catch (BadLocationException ex) {
									Log.error("Cannot break view", ex);
								}
							}
							return super.breakView(axis, p0, pos, len);
						}
					};
				} else if (v instanceof ParagraphView) {
					return new ParagraphView(e) {
						protected SizeRequirements calculateMinorAxisRequirements(
								int axis, SizeRequirements r) {
							if (r == null) {
								r = new SizeRequirements();
							}
							float pref = layoutPool.getPreferredSpan(axis);
							float min = layoutPool.getMinimumSpan(axis);
							r.minimum = (int) min;
							r.preferred = Math.max(r.minimum, (int) pref);
							r.maximum = Integer.MAX_VALUE;
							r.alignment = 0.5f;
							return r;
						}

					};
				}
				return v;
			}
		};
	}
}

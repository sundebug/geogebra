package org.geogebra.common.spreadsheet.core;

import org.geogebra.common.util.shape.Size;

public interface ViewportAdjusterDelegate {

	// TODO change to double (APPS-5637)
	void setScrollPosition(int x, int y);

	/**
	 * Retrieves the width of the scrollbar used for dragging content with the left mouse button
	 * @return The width of the scrollbar
	 */
	int getScrollBarWidth();

	/**
	 * Update size of the scrollable area.
	 * @param size total size of the scollable content
	 */
	void updateScrollableContentSize(Size size);
}
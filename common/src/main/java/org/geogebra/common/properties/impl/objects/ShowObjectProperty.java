package org.geogebra.common.properties.impl.objects;

import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.Localization;
import org.geogebra.common.properties.BooleanProperty;
import org.geogebra.common.properties.impl.AbstractProperty;

/**
 * Show object
 */
public class ShowObjectProperty extends AbstractProperty<Boolean> implements BooleanProperty {

	private final GeoElement element;

	/***/
	public ShowObjectProperty(Localization localization, GeoElement element) {
		super(localization, "Show");
		this.element = element;
	}

	@Override
	public Boolean getValue() {
		return element.isEuclidianVisible();
	}

	@Override
	protected void doSetValue(Boolean show) {
		element.setEuclidianVisible(show);
		element.updateVisualStyleRepaint(GProperty.VISIBLE);
	}
}

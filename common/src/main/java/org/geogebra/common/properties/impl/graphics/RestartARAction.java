package org.geogebra.common.properties.impl.graphics;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.geogebra3D.euclidian3D.EuclidianView3D;
import org.geogebra.common.main.App;
import org.geogebra.common.main.Localization;
import org.geogebra.common.properties.ActionableProperty;
import org.geogebra.common.properties.IconAssociatedProperty;
import org.geogebra.common.properties.PropertyResource;
import org.geogebra.common.properties.impl.AbstractProperty;

public class RestartARAction extends AbstractProperty implements ActionableProperty,
		IconAssociatedProperty {

	final EuclidianView3D euclidianView;

	public RestartARAction(Localization localization, EuclidianView3D euclidianView) {
		super(localization, "ar.restart");
		this.euclidianView = euclidianView;
	}

	@Override
	public Runnable getAction() {
		return new Runnable() {
			@Override
			public void run() {
				euclidianView.getRenderer().setARShouldRestart();
			}
		};
	}

	@Override
	public PropertyResource getIcon() {
		return PropertyResource.ICON_RELOAD_AR;
	}
}

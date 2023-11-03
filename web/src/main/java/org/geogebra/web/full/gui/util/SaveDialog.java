package org.geogebra.web.full.gui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geogebra.common.move.ggtapi.models.GeoGebraTubeUser;
import org.geogebra.common.move.ggtapi.models.Material;
import org.geogebra.web.full.gui.browser.BrowseResources;
import org.geogebra.web.full.gui.components.CompDropDown;
import org.geogebra.web.full.gui.components.ComponentCheckbox;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.shared.components.dialog.DialogData;
import org.gwtproject.resources.client.ImageResource;
import org.gwtproject.user.client.ui.FlowPanel;
import org.gwtproject.user.client.ui.Image;

public class SaveDialog extends DoYouWantToSaveChangesDialog {
	private ComponentCheckbox templateCheckbox;
	private Image providerImage;
	private Map<Material.Provider, ImageResource> availableProviders;

	/**
	 * base dialog constructor
	 * @param app - see {@link AppW}
	 * @param dialogData - contains trans keys for title and buttons
	 *
	 */
	public SaveDialog(AppW app, DialogData dialogData, boolean addTempCheckBox) {
		super(app, dialogData, false);
		if (addTempCheckBox) {
			addStyleName("templateSave");
		} else {
			templateCheckbox.setVisible(false);
		}
		setOnPositiveAction(() -> {
			if (templateCheckbox.isSelected()) {
				setSaveType(Material.MaterialType.ggsTemplate);
				app.getSaveController().ensureTypeOtherThan(Material.MaterialType.ggs);
			} else if (addTempCheckBox) {
				setSaveType(Material.MaterialType.ggs);
				app.getSaveController().ensureTypeOtherThan(Material.MaterialType.ggsTemplate);
			}
			app.getSaveController().saveAs(getInputField().getText(),
					getSaveVisibility(), this);
		});
	}

	@Override
	public void buildContent() {
		super.buildContent();

		templateCheckbox = new ComponentCheckbox(app.getLocalization(), false,
				"saveTemplate");
		getContentPanel().add(templateCheckbox);

		buildLocationDropDown();
	}

	private void buildLocationDropDown() {
		fillAvailableProviders();

		List<String> providers = new ArrayList<>();
		for (Material.Provider provider : availableProviders.keySet()) {
			providers.add(provider.getName());
		}
		CompDropDown locationDropDown = new CompDropDown((AppW) app,
				app.getLocalization().getMenu("Location"), providers);
		locationDropDown.setFullWidth(true);
		locationDropDown.addChangeHandler(() -> {
			int idx = locationDropDown.getSelectedIndex();
			providerImage.setResource(getProviderIcon(idx));
			((AppW) app).getFileManager().setFileProvider(getSelectedProvider(idx));
		});

		FlowPanel providerImageHolder = new FlowPanel();
		providerImageHolder.addStyleName("imageHolder");
		providerImage = new Image(getProviderIcon(locationDropDown.getSelectedIndex()));
		providerImageHolder.add(providerImage);

		FlowPanel locationHolder = new FlowPanel();
		locationHolder.addStyleName("locationHolder");
		locationHolder.add(providerImageHolder);
		locationHolder.add(locationDropDown);

		getContentPanel().add(locationHolder);
	}

	private void fillAvailableProviders() {
		availableProviders = new HashMap<>();
		availableProviders.put(Material.Provider.TUBE, getProviderIcon(Material.Provider.TUBE));

		GeoGebraTubeUser user = null;
		if (app.getLoginOperation() != null) {
			user = app.getLoginOperation().getModel().getLoggedInUser();
		}
		if (user != null && user.hasGoogleDrive()
				&& ((AppW) app).getLAF().supportsGoogleDrive()) {
			availableProviders.put(Material.Provider.GOOGLE,
					getProviderIcon(Material.Provider.GOOGLE));
		}

		if (((AppW) app).getLAF().supportsLocalSave()) {
			availableProviders.put(Material.Provider.LOCAL,
					getProviderIcon(Material.Provider.LOCAL));
		}
	}

	@Override
	public void show() {
		super.show();
		Material activeMaterial = app.getActiveMaterial();
		templateCheckbox.setSelected(activeMaterial != null && Material.MaterialType.ggsTemplate
				.equals(activeMaterial.getType()));
	}

	private ImageResource getProviderIcon(int selectedIdx) {
		Material.Provider provider = getSelectedProvider(selectedIdx);
		return getProviderIcon(provider);
	}

	private ImageResource getProviderIcon(Material.Provider provider) {
		switch (provider) {
			case GOOGLE:
				return BrowseResources.INSTANCE.location_drive();
			case LOCAL:
				return BrowseResources.INSTANCE.location_local();
			default:
			case TUBE:
				return BrowseResources.INSTANCE.location_tube();
		}
	}

	private Material.Provider getSelectedProvider(int index) {
		switch (index) {
			default:
			case 0: return Material.Provider.TUBE;
			case 1: return Material.Provider.GOOGLE;
			case 2: return Material.Provider.LOCAL;
		}
	}
}
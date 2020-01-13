package org.geogebra.common.kernel.geos;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.GeoElementFactory;
import org.geogebra.common.gui.dialog.options.model.ObjectSettingsModel;
import org.geogebra.common.main.settings.AppConfigGraphing;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ForceInputFormTest extends BaseUnitTest {

    @Test
    public void testRayToStringMode() {
        getApp().setConfig(new AppConfigGraphing());

        GeoRay geoRay = getElementFactory().createGeoRay();
        Assert.assertEquals(GeoRay.EQUATION_USER, geoRay.getToStringMode());
    }

    @Test
    public void testLinesAndConicsToStringMode() {
        getApp().setConfig(new AppConfigGraphing());

        GeoLine geoLine = getElementFactory().createGeoLine();
        GeoElementFactory factory = getElementFactory();
        GeoConic parabola = (GeoConic) factory.create("y=xx");
        GeoConic hyperbola = (GeoConic) factory.create("yy-xx=1");

        Assert.assertEquals(GeoLine.EQUATION_USER, geoLine.getToStringMode());
        Assert.assertEquals(GeoConic.EQUATION_USER, parabola.getToStringMode());
        Assert.assertEquals(GeoConic.EQUATION_USER, hyperbola.getToStringMode());
    }

    @Test
    public void testEquationPropertyIsHidden() {
        getApp().setConfig(new AppConfigGraphing());

        GeoLine geoLine = getElementFactory().createGeoLine();
        GeoElementFactory factory = getElementFactory();
        GeoConic parabola = (GeoConic) factory.create("y=xx");
        GeoConic hyperbola = (GeoConic) factory.create("yy-xx=1");

        ObjectSettingsModel objectSettingsModel = asList(geoLine);
        Assert.assertFalse(objectSettingsModel.hasEquationModeSetting());

        objectSettingsModel = asList(parabola);
        Assert.assertFalse(objectSettingsModel.hasEquationModeSetting());

        objectSettingsModel = asList(hyperbola);
        Assert.assertFalse(objectSettingsModel.hasEquationModeSetting());
    }

    private ObjectSettingsModel asList(GeoElement f) {
        ArrayList<GeoElement> list = new ArrayList<>();
        list.add(f);
        ObjectSettingsModel model = new ObjectSettingsModel(getApp()) {
        };
        model.setGeoElement(f);
        model.setGeoElementsList(list);
        return model;
    }
}

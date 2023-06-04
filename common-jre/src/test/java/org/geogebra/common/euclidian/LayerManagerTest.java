package org.geogebra.common.euclidian;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.geogebra.common.AppCommonFactory;
import org.geogebra.common.jre.headless.AppCommon;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoPolygon;
import org.geogebra.common.main.settings.config.AppConfigNotes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LayerManagerTest extends BaseControllerTest {

	private LayerManager layerManager;
	private GeoElement[] geos;

	@Before
	public void setupApp() {
		getApp().setConfig(new AppConfigNotes());
		layerManager = new LayerManager();
		geos = new GeoElement[10];
		for (int i = 0; i < geos.length; i++) {
			geos[i] = createDummyGeo(getConstruction(), i);
			layerManager.addGeo(geos[i]);
		}
		getApp().setUndoActive(true);
	}

	void resetGeos() {
		for (int i = 0; i < geos.length; i++) {
			geos[i] = createDummyGeo(getConstruction(), i);
			layerManager.addGeo(geos[i]);
		}
	}

	/**
	 *
	 * @param construction the construction.
	 * @param number the number for the label.
	 * @return the geo labeled as number.
	 */
	static GeoElement createDummyGeo(Construction construction, int number) {
		GeoElement geo = new GeoPolygon(construction);
		geo.setLabel("p" + number);
		return geo;
	}

	@Test
	public void testMoveForward() {
 		layerManager.moveForward(asList(geos[3], geos[5], geos[9]));
		//assertSorted(geos, asList(0f, 1f, 2f, 4f, 6f, 7f, 8f, 9f, 10f, 11f));
		assertSorted(geos, asList(0d, 1d, 2d, 4d, 6d, 7d, 8d, 9d, 10d, 11d));

		layerManager.moveForward(Collections.singletonList(geos[0]));
		//assertSorted(geos, asList(1f, 1.5f, 2f, 4f, 6f, 7f, 8f, 9f, 10f, 11f));
		assertSorted(geos, asList(1d, 1.5d, 2d, 4d, 6d, 7d, 8d, 9d, 10d, 11d));
	}

	@Test
	public void testMoveForwardWithUndo() {
		layerManager.moveForward(asList(geos[3], geos[5], geos[9]));
		assertSorted(geos, asList(0d, 1d, 2d, 4d, 6d, 7d, 8d, 9d, 10d, 11d));
		getConstruction().undo();
		assertSorted(geos, asList(0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d));
		getConstruction().redo();
		assertSorted(geos, asList(0d, 1d, 2d, 4d, 6d, 7d, 8d, 9d, 10d, 11d));
		layerManager.moveForward(Collections.singletonList(geos[0]));
		assertSorted(geos, asList(1d, 1.5d, 2d, 4d, 6d, 7d, 8d, 9d, 10d, 11d));
		layerManager.moveForward(Collections.singletonList(geos[0]));
		assertSorted(geos, asList(1d, 2d, 3d, 4d, 6d, 7d, 8d, 9d, 10d, 11d));
	}

	@Test
	public void testMoveForwardWithUndoSingle() {
		layerManager.moveForward(asList(geos[3]));
		assertSorted(geos, asList(0d, 1d, 2d, 4d, 3d, 5d, 6d, 7d, 8d, 9d));
		getConstruction().undo();
		assertSorted(geos, asList(0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d));
		getConstruction().redo();
		layerManager.moveForward(Collections.singletonList(geos[9]));
		assertSorted(geos, asList(0d, 1d, 2d, 4d, 3d, 5d, 6d, 7d, 8d, 9d));
	}

	@Test
	public void testMoveBackward() {
		layerManager.moveBackward(asList(geos[0], geos[9]));
		assertSorted(geos, asList(-1.0d, 0.0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d));
		//assertOrdering(0, 9, 1, 2, 3, 4, 5, 6, 7, 8);

		layerManager.moveBackward(asList(geos[3], geos[5], geos[6]));
		assertSorted(geos, asList(-1.0, 0.0, 1d, 1.25, 1.5, 1.75, 2d, 4d, 7d, 8d));
	//	assertOrdering(0, 9, 1, 3, 5, 6, 2, 4, 7, 8);
	}

	@Test
	public void testMoveToFront() {
		layerManager.moveToFront(Collections.singletonList(geos[7]));
		assertSorted(geos, asList(0d, 1d, 2d, 3d, 4d, 5d, 6d, 8d, 9d, 10d));

		layerManager.moveToFront(asList(geos[3], geos[7]));
		assertSorted(geos, asList(0d, 1d, 2d, 4d, 5d, 6d, 8d, 9d, 10d, 11d));

	}

	@Test
	public void testMoveToBack() {
		layerManager.moveToBack(asList(geos[9], geos[6], geos[4]));
		//assertOrdering(4, 6, 9, 0, 1, 2, 3, 5, 7, 8);
		assertSorted(geos, asList(-3d, -2d, -1d, 0d, 1d, 2d, 3d, 5d, 7d, 8d));

		layerManager.moveToBack(asList(geos[6], geos[2]));
		// assertOrdering(6, 2, 4, 9, 0, 1, 3, 5, 7, 8);
		assertSorted(geos, asList(-5d, -4d, -3d, -1d, 0d, 1d, 3d, 5d, 7d, 8d));

	}

	private void assertOrdering(int... newOrder) {
		assertOrdering(geos, newOrder);
	}

	static void assertSorted(GeoElement[] geos, List<Double> expected) {

		assertEquals(geos.length, expected.size());

		List<Double> actual = Arrays.stream(geos).sorted((geo1, geo2)
						-> Double.compare(geo1.getOrdering(), geo2.getOrdering()))
				.map(GeoElement::getOrdering).collect(Collectors.toList());

		assertEquals(actual, expected);

	}

	static void assertOrdering(GeoElement[] geos, int... newOrder) {
		assertEquals(geos.length, newOrder.length);
		List<Double> actual = new ArrayList<>();
		List<Double> expected = new ArrayList<>();
		for (int i = 0; i < geos.length; i++) {
			actual.add(geos[newOrder[i]].getOrdering());
			expected.add((double)i);
		}

		// assert once to have nice output when failing.
		assertEquals(expected, actual);
	}

}

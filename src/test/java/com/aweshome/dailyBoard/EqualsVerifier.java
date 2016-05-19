package com.aweshome.dailyBoard;

import java.util.List;

import org.junit.Assert;

public class EqualsVerifier<O> {

	public void assertEqualsProperties(O a, O b, O c, List<O> differents) {
		
		this.equalsSymmetryTest(a, b);
		this.equalsSymmetryTest(b, c);
		this.equalsReflexibilityTest(a);
		this.equalsTransitivityTest(a, b, c);
		
		if (differents != null) {
			for (O o : differents) {
				this.equalsReflexibilityTest(o);
				this.equalsSymmetryTest(a, o);
			} 
		}
	}
	
	private void equalsReflexibilityTest(O a) {
		Assert.assertEquals(a, a);
	}
	
	private void equalsSymmetryTest(O a, O b) {
		if (a.equals(b)) {
			Assert.assertEquals(b, a);
		}
		else {
			Assert.assertNotEquals(b, a);
		}
	}
	
	private void equalsTransitivityTest(O a, O b, O c) {
		Assert.assertEquals(a, b);
		Assert.assertEquals(b, c);
		Assert.assertEquals(a, c);
	}
}

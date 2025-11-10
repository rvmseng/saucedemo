package com.saucedemo.assertion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.Assert;

import com.saucedemo.dto.Product;
import com.saucedemo.util.Utility;

public class CustomeAssert {

	public static void assertListSortedByName(List<Product> products, boolean ascending) {
		List<String> actual = new ArrayList<String>();
		for (Product _prd : products) {
			actual.add(_prd.getName());
		}

		List<String> expected = new ArrayList<String>(actual);
		expected.sort(String.CASE_INSENSITIVE_ORDER);

		if (!ascending) {
			Collections.reverse(expected);
		}

		Assert.assertEquals(actual, expected, "Product names are not in expected order.");
	}

	public static void assertListSortedByPrice(List<Product> products, boolean ascending) {
		List<BigDecimal> actual = new ArrayList<BigDecimal>();
		for (Product _prd : products) {
			actual.add(Utility.parsePriceToBigDecimal(_prd.getPrice()));
		}

		List<BigDecimal> expected = new ArrayList<>(actual);
		expected.sort(Comparator.naturalOrder());

		if (!ascending) {
			Collections.reverse(expected);
		}

		Assert.assertEquals(actual, expected, "Product prices are not in expected numeric order.");
	}

}

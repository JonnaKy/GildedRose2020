package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;


public class GildedRoseTest {

	@Test
	public void testTheTruth() {
		assertTrue(true);
	}
	@Test
	public void exampleTest() {
		//create an inn, add an item, and simulate one day
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.oneDay();
		
		//access a list of items, get the quality of the one set
		List<Item> items = inn.getItems();
		int quality = items.get(0).getQuality();
		
		//assert quality has decreased by one
		assertEquals("Failed quality for Dexterity Vest", 19, quality);
	}
//LOOP TESTING
	@Test
	public void emptyInn() {
		GildedRose inn = new GildedRose();
		inn.oneDay();
		assertTrue(inn.getItems().isEmpty());
	}
	
//NORMAL PRODUCTS
	
	@Test
	public void TestDecreasedQuality_OneDay() {

		GildedRose inn = new GildedRose();

		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));

		List<Item> items = inn.getItems();

		int quality = items.get(0).getQuality() - 1;
		
		inn.oneDay();
		
		assertEquals("Failed quality for Dexterity Vest", quality, items.get(0).getQuality());
		
	}
	
	@Test
	public void TestNormalProductQuality_IsNotNegative() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("+5 Dexterity Vest", 10, 0));
		List<Item> items = inn.getItems();
		inn.oneDay();
		
		assertEquals("Quality decreased to negative number", 0, items.get(0).getQuality());
	}
	
	@Test
	public void TestDecreasedQuality_AllButSpecialProducts() {

		GildedRose inn = new GildedRose();

		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.setItem(new Item("Elixir of the Mongoose", 5, 7));
		inn.setItem(new Item("Conjured Mana Cake", 3, 6));

		List<Item> items = inn.getItems();
		
		List<Integer> originalQualities = new ArrayList<>();
		
		for (int i = 0; i < items.size() ; i++) {
			originalQualities.add(items.get(i).getQuality());
		}
		
		inn.oneDay();
		
		for (int i = 0; i < items.size(); i++) {
			assertEquals("Failed quality common items", originalQualities.get(i) - 1, items.get(i).getQuality());
		}
			
	}
	
	@Test
	public void TestQualityDecreaseDoubles() {
		GildedRose inn = new GildedRose();
		
		inn.setItem(new Item("Elixir of the Mongoose", 0, 10));
		
		List<Item> items = inn.getItems();
		
		inn.oneDay();
		
		assertEquals("Failed to double the quality decrease", 8, items.get(0).getQuality());
	}
	
	
	@Test
	public void TestSellByDateToZero_NormalItems() {

		GildedRose inn = new GildedRose();

		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));

		List<Item> items = inn.getItems();
		
		
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 0);
		
		assertEquals("Failed to decrease the sellIn to zero", 0, items.get(0).getSellIn());
		
	}
	
	@Test
	public void TestQualityAfterBBD_NormalItem() {

			GildedRose inn = new GildedRose();

			inn.setItem(new Item("+5 Dexterity Vest", 10, 20));

			List<Item> items = inn.getItems();
				
			do {
				inn.oneDay();
			}while(items.get(0).getSellIn() > -1);
				
			assertEquals("Failed to decrease the quality", 8, items.get(0).getQuality());
				
			inn.oneDay();
				
			assertEquals("Failed to decrease the quality", 6, items.get(0).getQuality());
				
	}
	
	@Test
	public void TestNegativeQuality_ZeroSellIn_NoFunctionality() {
		GildedRose inn = new GildedRose();

		inn.setItem(new Item("+5 Dexterity Vest", 0, -1));

		List<Item> items = inn.getItems();
		
		inn.oneDay();
		
		assertEquals("Changes in quality", -1, items.get(0).getQuality());
	}
	
	@Test
	public void TestQualityIsZero() {
		GildedRose inn = new GildedRose();

		inn.setItem(new Item("+5 Dexterity Vest", 0, 0));

		List<Item> items = inn.getItems();
		
		inn.oneDay();
		
		assertEquals("Changes in quality", 0, items.get(0).getQuality());
	}
	
////BACKSTAGE	

	@Test
	public void TestQualityAfterBBD_Backstage() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20));
		
		List<Item> items = inn.getItems();
		
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 0);

		inn.oneDay();
		
		assertEquals("Failed to decrease the quality after concert is over", 0, items.get(0).getQuality());
	}
	
	@Test
	public void TestBackstageQuality_Over50() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 5, 50));
		
		List<Item> items = inn.getItems();
		
		inn.oneDay();
		
		assertEquals("Failed to limit quality increase", 50, items.get(0).getQuality());
	}
	
	@Test
	public void TestBackstageCorrectQualityIncrease() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 20, 0));
		
		List<Item> items = inn.getItems();
		
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 11);
		
		assertEquals("Failed to increase backstage quality correctly", 9, items.get(0).getQuality());
		
		//Testing the quality increase rate after 10 days
		int quality = items.get(0).getQuality();
		inn.oneDay();

		assertEquals("The increase rate of quality after 10 days is incorrect", quality + 2, items.get(0).getQuality());
		
		//Testing the quality increase rate when there are 5 days left to sell
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 6);
		
		quality = items.get(0).getQuality();
		inn.oneDay();

		assertEquals("The increase rate of quality after 5 days is incorrect", quality + 3, items.get(0).getQuality());
		
	}
	
////BRIE
	@Test
	public void TestBrieQuality_IsNotNegative() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 0, 0));
		List<Item> items = inn.getItems();
		
			inn.oneDay();

			assertEquals("Failed to change brie quality correctly", 3, items.get(0).getQuality());
	}
	
	@Test
	public void TestBrieQuality_IncreaseWhileSellInDecreased() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 20, 0));
		List<Item> items = inn.getItems();
		
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 11);
		
		assertEquals("Failed to increase brie quality correctly", 9, items.get(0).getQuality());
		
		//Testing the quality increase rate after 10 days
		int quality = items.get(0).getQuality();
		inn.oneDay();

		assertEquals("The increase rate of quality after 10 days is incorrect", quality + 2, items.get(0).getQuality());
		
		//Testing the quality increase rate when there are 5 days left to sell
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 6);
		
		quality = items.get(0).getQuality();
		inn.oneDay();

		assertEquals("The increase rate of quality after 5 days is incorrect", quality + 3, items.get(0).getQuality());
	}
	
	@Test
	public void TestBrieQuality_NotOVer50() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 12, 50));
		List<Item> items = inn.getItems();
		
		inn.oneDay();
		
		assertEquals("Failed to limit quality increase", 50, items.get(0).getQuality());
		
		GildedRose pub = new GildedRose();
		pub.setItem(new Item("Aged Brie", 12, 49));
		pub.oneDay();
		pub.oneDay();
		assertEquals("Failed to limit quality increase", 50, items.get(0).getQuality());
		
	}
	
////LEGENDARY
	
	@Test
	public void legendaryQualityNeverAlters() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
		List<Item> items = inn.getItems();
		
		inn.oneDay();
		
		assertEquals("Legendary items quality altered", 80, items.get(0).getQuality());
	}
	
	@Test
	public void legendarySellInBelowZero() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Sulfuras, Hand of Ragnaros", -1, 80));
		List<Item> items = inn.getItems();
		
		inn.oneDay();
		assertEquals("Legendary items quality altered", 80, items.get(0).getQuality());
	}
}

package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;



/*TESTATAAN:
 * - laatu heikkenee yhden päivän jälkeen
 * - myyntipäivät vähenevät ja voivat mennä yli nollan
 * - laatu ei mene yli nollan
 * - Laatu laskee kaksinkertaisella vauhdilla bbd:n mentyä
 * - TODO: Brien laatu kasvaa myyntipäivien vähentyessä
 * - TODO: Sulfuras ei myyntiä tai laadun laskemista
 * - TODO: Konserttilippujen laatu paranee vanhetessa
 * - TODO: Laatu erikoistuotteilla paranee kahdella kun myyntipäiviä on alle 10
 * - TODO: Laatu erikoistuotteilla paranee kolmella kun myyntipäiviä on alle 5
 * - TODO: Laatu laskee nollaan viimeisen myyntipäivän jälkeen
 */



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
	
	@Test
	public void TestDecreasedQuality_OneDay() {
		//konstruktorissa luodaan lista
		GildedRose inn = new GildedRose();
		//asetetaan listaan tavara
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		//asetetaan majatalon tuotteet listaan
		List<Item> items = inn.getItems();

		int quality = items.get(0).getQuality() - 1;
		
		inn.oneDay();
		
		assertEquals("Failed quality for Dexterity Vest", quality, items.get(0).getQuality());
		
	}
	
	@Test
	public void TestDecreasedQuality_AllButSpecialProducts() {
		//konstruktorissa luodaan lista
		GildedRose inn = new GildedRose();
		//asetetaan listaan tavara
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.setItem(new Item("Elixir of the Mongoose", 5, 7));
		inn.setItem(new Item("Conjured Mana Cake", 3, 6));
		//asetetaan majatalon tuotteet listaan
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
	public void TestSellByDateToZero_NormalItems() {
		//konstruktorissa luodaan lista
		GildedRose inn = new GildedRose();
		//asetetaan listaan tavara
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		//asetetaan majatalon tuotteet listaan
		List<Item> items = inn.getItems();
		
		
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 0);
		
		assertEquals("Failed to decrease the sellIn to zero", 0, items.get(0).getSellIn());
		
	}
	
	@Test
	public void TestQualityAfterBBD_NormalItems() {
		//konstruktorissa luodaan lista
				GildedRose inn = new GildedRose();
				//asetetaan listaan tavara
				inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
				//asetetaan majatalon tuotteet listaan
				List<Item> items = inn.getItems();
				
				
				do {
					inn.oneDay();
				}while(items.get(0).getSellIn() > -1);
				
				assertEquals("Failed to decrease the quality", 8, items.get(0).getQuality());
				
	}
	
	@Test
	public void TestBrieQuality_IncreaseAndMAX_VALUE() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 2, 0));
		
		List<Item> items = inn.getItems();
		
		do {
			inn.oneDay();
		}while(items.get(0).getQuality() < 50);
		
		assertEquals("Failed to increase brie quality to max", 50, items.get(0).getQuality());
	}
}

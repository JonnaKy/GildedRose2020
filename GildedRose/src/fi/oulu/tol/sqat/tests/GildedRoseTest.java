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
 * - Laatu laskee nollaan viimeisen myyntipäivän jälkeen konserttilipulla
 * -  Sulfuras ei myyntiä tai laadun laskemista 
 * 
 * 
 * - TODO: Konserttilippujen laatu paranee vanhetessa
 * - TODO: Laatu erikoistuotteilla paranee kahdella kun myyntipäiviä on alle 10, brie done
 * - TODO: Laatu erikoistuotteilla paranee kolmella kun myyntipäiviä on alle 5, brie done
 * - TODO: mitä brien laadulle käy kun sellIn < 0
 * 
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
	public void TestQualityAfterBBD_Backstage() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
		
		List<Item> items = inn.getItems();
		
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > -1);
		
		assertEquals("Failed to decrease the quality after concert is over", 0, items.get(0).getQuality());
	}
	
	@Test
	public void TestBrieQuality_IncreaseWhileSellInDecreased() {
		//SHOULD THE QUALITY DOUBLE ON THE TENTH DAY
		/*for example. quality is 9 on the 11th day
		 * should it be 11 on the 10th day or just 10
		 	and 12 on the 9th day
		 */
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 20, 0));
		List<Item> items = inn.getItems();
		
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 11);
		
		assertEquals("Failed to increase brie quality correctly", 9, items.get(0).getQuality());
		
		//Testing the quality increase rate after 10 days
		inn.oneDay();
		int quality = items.get(0).getQuality();
		inn.oneDay();

		//the quality increases by two on the ninth day
		assertEquals("The increase rate of quality after 10 days is incorrect", quality + 2, items.get(0).getQuality());
		
		//Testing the quality increase rate when there are 5 days left to sell
		do {
			inn.oneDay();
		}while(items.get(0).getSellIn() > 6);
		

		inn.oneDay();
		quality = items.get(0).getQuality();
		inn.oneDay();

		assertEquals("The increase rate of quality after 5 days is incorrect", quality + 3, items.get(0).getQuality());
	}
	
	@Test
	public void bireQualityAfterBBD() {
		//what should happen to brie after sellIn is < 0, keep increasing by three or decrease?
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 0, 20));
		List<Item> items = inn.getItems();
		
		inn.oneDay();
		
		System.out.println(items.get(0).getSellIn());//-1
		System.out.println(items.get(0).getQuality());//23
	}
	
	@Test
	public void legendaryQualityNeverAlters() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
		List<Item> items = inn.getItems();
		
		inn.oneDay();
		
		assertEquals("Legendary items quality altered", 80, items.get(0).getQuality());
	}
}

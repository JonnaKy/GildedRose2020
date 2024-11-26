package fi.oulu.tol.sqat;

import java.util.ArrayList;
import java.util.List;


public class GildedRose {

	private static List<Item> items = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        System.out.println("OMGHAI!");
		
        items = new ArrayList<Item>();
        items.add(new Item("+5 Dexterity Vest", 10, 20));
        items.add(new Item("Aged Brie", 2, 0));
        items.add(new Item("Elixir of the Mongoose", 5, 7));
        items.add(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
        items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
        items.add(new Item("Conjured Mana Cake", 3, 6));

        updateQuality();
}


	
    public static void updateQuality()
    {//quality is handled before the sellIn is decreased
        for (int i = 0; i < items.size(); i++)
        {
            if ((!"Aged Brie".equals(items.get(i).getName())) && !"Backstage passes to a TAFKAL80ETC concert".equals(items.get(i).getName())) 
            {
                if (items.get(i).getQuality() > 0)
                {
                    if (!"Sulfuras, Hand of Ragnaros".equals(items.get(i).getName()))
                    {
                        items.get(i).setQuality(items.get(i).getQuality() - 1);
                    }
                }
            }
            else//täällä kasvaa vain brie tai backstage
            {
                if (items.get(i).getQuality() < 50)
                {
                    items.get(i).setQuality(items.get(i).getQuality() + 1);
                    //tarviiko tätä tarkistusta
                    if ("Backstage passes to a TAFKAL80ETC concert".equals(items.get(i).getName()) || "Aged Brie".equals(items.get(i).getName()))
                    {
                        if (items.get(i).getSellIn() < 11 && items.get(i).getQuality() < 50) //this 11 or 12???
                        {
                                items.get(i).setQuality(items.get(i).getQuality() + 1);
                        }

                        if (items.get(i).getSellIn() < 6)
                        {
                            if (items.get(i).getQuality() < 50)
                            {
                                items.get(i).setQuality(items.get(i).getQuality() + 1);
                            }
                        }
                    }
                }
            }
            //VÄHENNETÄÄN MYYNTIPÄIVÄÄ
            if (!"Sulfuras, Hand of Ragnaros".equals(items.get(i).getName()))//joku muu kuin legendaarinen == myyntipäivä vähenee
            {
                items.get(i).setSellIn(items.get(i).getSellIn() - 1);
            }

            if (items.get(i).getSellIn() < 0)//myyntipäivät loppu
            {
                if (!"Aged Brie".equals(items.get(i).getName()))
                {
                    if (!"Backstage passes to a TAFKAL80ETC concert".equals(items.get(i).getName()))
                    {
                        if (items.get(i).getQuality() > 0)
                        {
                            if (!"Sulfuras, Hand of Ragnaros".equals(items.get(i).getName()))
                            {
                            	//VÄHENNETÄÄN LAATUA KAHDELLA KOSKA MYYNTIPÄIVÄT LOPPU
                                items.get(i).setQuality(items.get(i).getQuality() - 1);
                            }
                        }
                    }
                    else
                    {
                        items.get(i).setQuality(items.get(i).getQuality() - items.get(i).getQuality());
                    }
                }
                else
                {
                    if (items.get(i).getQuality() < 50)
                    {
                        items.get(i).setQuality(items.get(i).getQuality() );
                    }
                }
            }
        }
    }

    //constructor
    public GildedRose() {
    	items = new ArrayList<Item>();
    }
    
    //getter
    public List<Item> getItems() {
    	return items;
    }
    //setter
    public void setItem(Item item) {
    	items.add(item);
    }
    
    //update one day
    public void oneDay() {
    	updateQuality();
    }
}

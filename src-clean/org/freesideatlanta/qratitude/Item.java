package org.freesideatlanta.qratitude;

/**
 * Implements fields and methods for a Line Item that can be inputted into the
 * qratitude application. Format is structured to permit exporting and importing
 * of JSON objects using either Jackson or GSON
 * 
 * @author Cameron Kilgore
 */
public class Item {
	//Constructors
	public Item() {
		
	}
	
	//Methods
	/**
	 * Returns the Item Name
	 * @return The name of the item
	 */
	public String getItemName() {
		return itemName;
	}
	
	/**
	 * Returns the item description
	 * @return The item description
	 */
	public String getItemDescription() {
		return itemDescription;
	}
	
	/**
	 * Returns the condition of the item 
	 * @return The item condition
	 */
	public String getCondition() {
		return condition;
	}
	
	/**
	 * Returns the quantity of an item
	 * @return The quantity of an item
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Returns the Color of the item
	 * @return The item color
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * Sets the name of the item
	 * @param item The name of the item
	 */
	public void setItemName(String item) {
		itemName = item;
	}
	
	/**
	 * Sets the description of the item
	 * @param descript The description of the item
	 */
	public void setItemDescription(String descript) {
		itemDescription = descript;
	}
	
	/**
	 * Sets the quantity of the item
	 * @param quant The quantity of the item
	 */
	public void setQuantity(int quant) {
		quantity = quant;
	}
	
	/**
	 * Sets the condition of the item
	 * @param cond The condition of the item
	 */
	public void setCondition(String cond) {
		condition = cond;
	}
	
	/**
	 * Sets the color of the item
	 * @param col The color of the item
	 */
	public void setColor(String col) {
		color = col;
	}
	
	//Fields
	private String itemName;
	private String itemDescription;
	private int quantity;
	private String condition;
	private String color;
}

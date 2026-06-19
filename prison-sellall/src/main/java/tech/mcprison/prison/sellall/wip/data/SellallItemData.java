package tech.mcprison.prison.sellall.wip.data;

public class SellallItemData {
	
	private String name;
	private double price;
	private int amount;

	public SellallItemData( String name, double price ) {
		super();
		
		this.name = name;
		this.price = price;
		
		this.amount = 0;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}

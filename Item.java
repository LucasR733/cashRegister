import java.util.Random;

//Item class - type to hold a name of an item and the cost of it(also includes discount rate and actual discount percentage)
public class Item {

  //Variables
  private String name; // Item Name
  private double price; // Item Price
  int discountPercent = 0; // Percent Discount
  boolean discount; // Has a discount?
  int discountRate = 0; // 1 in __ chance of discount
  Random rand = new Random(); //used to create a random number for if item is discounted or not
  double tax = 0.0;
  double itemTaxTotal = 0.0;

  //Constructor
  public Item(String name, double price, int discountInput, int discountRateInput, double taxRate) {
    this.name = name;
    this.price = price;
    this.tax = taxRate;
    
    if(discountRateInput != 0)
      this.discountRate = discountRateInput;


    //Determines if the item can be discounted based on 1 / discountRate
    if(discountRate == 0){
      discount = false;
    } else if(discountRate == 100){
      discount = false;
    } else if(rand.nextInt(100) < discountRate) {
      this.discountPercent = discountInput;
      discount = true;
    } 

    //caculates tax for item based on if it has a 100% dicsount or not
    if(discountRate == 100 && discount)
      itemTaxTotal = 0.0;
    else
      itemTaxTotal = price * tax;

  }

  //get methods
  public String getName() {
    return this.name;
  }

  public double getPrice() {
    return this.price;
  }
  
  public int getDiscount() {
    return this.discountPercent;
  }

  public double getTaxTotal(){
    return this.itemTaxTotal;
  }
  //returns true if item has discont else false
  public boolean isDiscount() {
    return this.discount;
  }
  
  //Returns the item name and price
  @Override
  public String toString() {
    return this.name + "\n - $" + this.price;
  }
}
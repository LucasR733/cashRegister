//import statements
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;

//Class to handle a shopping cart of items(calculates cost of everything including tax, discounts, donations, etc.)
public class ShoppingCart {
  
//Instance variables  
  //Local Variables
  Item [] shoppingCart;
  Scanner input;
  double tax = 0.06;
  double donation = 0.0;
  double discountPercentage = 0.0;
  double runningTotal = 0.0; // total before discounts
  double discountTotal = 0.0; // total after discounts
  double taxTotal = 0.0;
  
  //Constants
  final static int QUARTER_VALUE = 25;
  final static int DIME_VALUE = 10;
  final static int NICKEL_VALUE = 5;
  final static int PENNY_VALUE = 1;
  final static int DOLLAR_VALUE = 100;

  
  //constructor
  public ShoppingCart(int discount, int discountRate, double taxRate, ArrayList<String> shoppingList) {
    
    //variables
    tax = taxRate;
    int indexCount = 0;
    shoppingCart = new Item[countItems(shoppingList)]; //initialize array with total ammount of lines in file that are formatted correctly
    double itemCost;
 
    //Takes input and fills in each item in the shopping cart
    for(int i = 0; i < shoppingList.size(); i++){

      //only runs if testItem returns true(Syntax for file input is correct)
      if(testItem(shoppingList.get(i))){

        //create item
        createItem(shoppingList.get(i), discount, discountRate, indexCount);
        
        itemCost = shoppingCart[indexCount].getPrice();//get item cost
        runningTotal = runningTotal + itemCost;//adds item cost to total cost
        taxTotal += shoppingCart[indexCount].getTaxTotal();//calculate tax for this item
        //System.out.println("TaxTotal: " + shoppingCart[indexCount].getTaxTotal()); // prints tax total for every item, used for testing
        
        //calculate Discount Total
        if(shoppingCart[indexCount].isDiscount()){
          discountTotal += (itemCost - (itemCost * (shoppingCart[indexCount].getDiscount()/100.0)));
        } else {
          discountTotal += itemCost;
        }    
        indexCount++;  
      }
    }
    //runningTotal += taxTotal;
    
  }

  //method to count how many items are actually items and not erros
  public int countItems(ArrayList<String> shoppingList){
    int count = 0;
    for(int i = 0; i < shoppingList.size(); i++) {
      if(shoppingList.get(i).contains("-")) 
        count++;
    }
    return count;
  }

  //tests string from file input to see if its formatted correctly
  public boolean testItem(String test){
    if(test.contains("-")) 
      return true;
    System.out.println("Line: \"" + test + "\" has an error in its syntax, it was skipped.");
    return false;
  }
  
  //method to handle one line of file - seperate item name and cost then create an item of it
  public void createItem(String fileLine, int discount, int discountRate, int itemIndex){

    //variables
    double cost;
    String name;
    int index;
    
    //takes index of '-' or skips line if no '-'
    if(fileLine.contains("-")) {
      index = fileLine.indexOf('-');//takes the index of '-' so we can seperate the item name and cost from the file
      
      //seperate itemName and itemCost
      name = fileLine.substring(0, index-1);
      cost = Double.valueOf(fileLine.substring(index + 2, fileLine.length()));

      //shortens item name to fit on receipt in uniform manner(only if name is longer than 25 characters)
      if(name.length() > 25) {
        name = name.substring(0, 22);
        name = name.concat("...");
      }

      //add item to shoppingcart
      shoppingCart[itemIndex] = new Item(name, cost, discount, discountRate, tax); 
    } else {
      System.out.println("Line: \"" + fileLine + "\" has an error in its syntax, it was skipped.");
    }
    
  }
  
  // Getter Methods
  public double getRunningTotal(){
    return runningTotal;
  }

  public double getDonationTotal() {
    return donation;
  }
  
  public double getDiscountTotal(){
    return discountTotal;
  }

  public double getTaxTotal(){
    return taxTotal;
  }

  //Rounds up to the next dollar to donate
  public double roundToDonate(double total) {
    if(Math.round(total) < total) return Math.round(total) + 1.0;
    else return Math.round(total);
  }  

  public double donate(double amount) {
    donation = amount;
    return discountTotal + taxTotal + amount;
  }


  //printReciept Method
  public void printReciept(double money){
    
    System.out.println("        Thank you for shopping with us!   ");
    System.out.println("Item --------------------- Cost -------- Tax -------- Discount ----");
    
    for(int i = 0; i < shoppingCart.length; i++){
      //one iteration on the reciept for each item, its cost, and its discount
      
      //name output
      System.out.print(" " + shoppingCart[i].getName());
      for(int j = shoppingCart[i].getName().length(); j < 25; j++){
        System.out.print(" ");
      }

      //cost output
      System.out.printf("   %,.2f", shoppingCart[i].getPrice());

      //formats cost in uniform manor
      if(shoppingCart[i].getPrice() >= 100000) {
        System.out.print("");
      } else if(shoppingCart[i].getPrice() >= 10000){
        System.out.print(" ");
      } else if (shoppingCart[i].getPrice() >= 1000) { // numbers > 100 print commas so output contains 1 less space than normal
        System.out.print("  ");
      } else if(shoppingCart[i].getPrice() >= 100){
        System.out.print("    ");
      } else if(shoppingCart[i].getPrice() >= 10){
        System.out.print("     ");
      } else {
        System.out.print("      ");
      }
      
      //tax output
      System.out.printf("   %,.2f", shoppingCart[i].getTaxTotal());

      //formats tax in uniform manor
      if(shoppingCart[i].getTaxTotal() >= 100000) {
        System.out.print("");
      } else if(shoppingCart[i].getTaxTotal() >= 10000){
        System.out.print(" ");
      } else if (shoppingCart[i].getTaxTotal() >= 1000) { // numbers > 100 print commas so output contains 1 less space than normal
        System.out.print("  ");
      } else if(shoppingCart[i].getTaxTotal() >= 100){
        System.out.print("    ");
      } else if(shoppingCart[i].getTaxTotal() >= 10){
        System.out.print("     ");
      } else {
        System.out.print("      ");
      }

      
      //discount output
      if(shoppingCart[i].getDiscount() > 0.0){
        System.out.println("   " + (int)(shoppingCart[i].getDiscount()) + "%");
      } else{
        System.out.println("   -" );
      }

    }
    
    //totals output
    System.out.printf("\n\nSubtotal: $%,.2f \n", runningTotal); //subtotal
    System.out.printf("You saved: $%,.2f \n", (runningTotal - discountTotal)); // total discounted
    System.out.printf("You Donated: $%,.2f\n", donation);
    System.out.printf("Total Taxed: $%,.2f \n", taxTotal); // total taxxed
    System.out.printf("Total: $%,.2f \n", discountTotal + taxTotal + donation); //actual total
    System.out.printf("Total Paid: $%,.2f\n\n", money);
    System.out.println(getChange(money));//prints change
  }
  
  
  //getChange method that calculates how much change the user has after entering in a cash value to pay with
  public String getChange(double money){
    
    //local variables
    int totalDollars = 0;
    int totalQuarters = 0;
    int totalDimes = 0;
    int totalNickels = 0;
    int totalPennies = 0;
    int moneyInt = (int)(money*100); // holds the ammount of money the user inputs to pay
    int changeTemp = moneyInt - ((int) ((discountTotal + taxTotal + donation)*100)); //holds the total ammount of change

    //calculation
    totalDollars = (changeTemp - (changeTemp % DOLLAR_VALUE)) / DOLLAR_VALUE;
    changeTemp = changeTemp - (totalDollars * DOLLAR_VALUE);
    totalQuarters = (changeTemp - (changeTemp % QUARTER_VALUE)) / QUARTER_VALUE;
    changeTemp = changeTemp - (totalQuarters * QUARTER_VALUE);
    totalDimes = (changeTemp - (changeTemp % DIME_VALUE)) / DIME_VALUE;
    changeTemp = changeTemp - (totalDimes * DIME_VALUE);
    totalNickels = (changeTemp - (changeTemp % NICKEL_VALUE)) / NICKEL_VALUE;
    changeTemp = changeTemp - (totalNickels * NICKEL_VALUE);
    totalPennies = changeTemp;

    //total change calculation
    double total = ((totalDollars * DOLLAR_VALUE) + (totalQuarters * QUARTER_VALUE) + (totalDimes * DIME_VALUE) + (totalNickels * NICKEL_VALUE) + (totalPennies * PENNY_VALUE)) / 100.0;    

    // Only return the given change, no change that has a value of 0
    String output = "Total Change: $" + total + "\n ";

    if(totalDollars > 0) {
      output = output.concat("Dollars: " + totalDollars + "\n ");
    }
    if(totalQuarters > 0) {
      output = output.concat("Quarters: " + totalQuarters + "\n ");
    }
    if(totalDimes > 0) {
      output = output.concat("Dimes: " + totalDimes + "\n ");
    }
    if(totalNickels > 0) {
      output = output.concat("Nickles: " + totalNickels + "\n ");
    }
    if(totalPennies > 0) {
      output = output.concat("Pennies: " + totalPennies);
    }

    return output;
  }
  
}
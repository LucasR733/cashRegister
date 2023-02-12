//Code Written by Lucas Routhier & Atom Olson
//1/22/23

//import statements
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.lang.*;


class Main {
  /*  Cash Register
    - fix donation method(does user want to round to next dollar to donate or donate another given ammount?)
  */
  
  static File myGroceriesFile = new File("Items.txt"); // File of grocery Items and their price
  static ArrayList<String> shoppingList = new ArrayList<>();
  static Scanner fileScan;
  
  //file reader method
  public static void readFile(){

    try {
      fileScan = new Scanner(myGroceriesFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println("File not Found");
    }  
    if(!fileScan.hasNextLine()) {
      System.out.println("\tFile is empty\nEnter item name into file, followed by \" - \", then the cost.(item - 0.99)");
    System.exit(0);
    }
    String text; // used to hold values of each line in file
    
    //take each line in the file and add it as a string to an array list
    while(fileScan.hasNextLine()){
      text = fileScan.nextLine();
      shoppingList.add(text);
    }


    fileScan.close();
  }
  
  public static void main(String[] args) {

    //local variables
    Scanner input = new Scanner(System.in);
    int discount;
    int discountRate = 0; //used to determine how often the discount is given
    int temp = 0; //used for exception handling in tax input
    Double userMoney = 0.0;
    ShoppingCart groceries;
    double tax = 0;

    //read file and every line to an arrayList
    readFile();
  
    //input

    //tax input
    System.out.println("What is the tax percentage? (Enter 0 if no, else 1 - 100).");
    temp = input.nextInt();
    tax = temp/100.0;
    
    //exception handling for tax input
    while(temp > 100 || temp < 0){
      System.out.println("Please enter a number between 0 and 100: ");
      temp = input.nextInt();
      tax = temp/100.0;
    }
    
    //discount input
    System.out.println("Is there a discount going on? (Enter 0 if no, else 1 - 100).");
    discount = input.nextInt();

    //Exception handling for discount input(Only numbers between 0-100(inclusive))
    while(discount > 100 || discount < 0){
      System.out.println("Please enter a number between 0 and 100: ");
      discount = input.nextInt();
    }
    
    if(discount != 0) {
      System.out.println("How often do items get discounted?(0%... 50%... etc.)");
      discountRate = input.nextInt();
    
    }
    groceries = new ShoppingCart(discount, discountRate, tax, shoppingList);

    String userInput;
    char charInput;
    double donateInput = 0.0;
    //donation input
    System.out.println("Would you like to round to next dollar?(y/n)");
    userInput = input.next();
    charInput = userInput.charAt(0);
    
    //exception handling for if they wanna round to dollar
    while(charInput != 'y' && charInput != 'n'){
      System.out.println("Please enter \'y\' or \'n\'.");
      userInput = input.next();
      charInput = userInput.charAt(0);
      
    }

    //if user wants to round to dollar
    if(charInput == 'y'){
      groceries.roundToDonate(groceries.getRunningTotal() + groceries.getTaxTotal());
    } else{ // if user doesnt wanna round to dollar
      System.out.println("How much would you like to donate?(Enter 0 if none, else enter a number greater than 0)");
      donateInput = input.nextDouble();

      //exception handling for input to donate 
      while(donateInput  < 0){
        System.out.println("Please enter a number equal to or greater than 0.");
        donateInput = input.nextDouble();
      }
      groceries.donate(donateInput);
    }
    
    //how much do you have to pay
    System.out.printf("Your total is: $%,.2f \n", (groceries.getDiscountTotal() + groceries.getTaxTotal() + groceries.getDonationTotal()));
    System.out.println("How much do you wish you pay?");
    userMoney = input.nextDouble();

    
    
    //exception handling for if user doesn't have enough money
    while(groceries.getDiscountTotal() > userMoney) {
      System.out.println("Not Enough Cash, Try Again.");
      System.out.println("How much do you wish you pay?");
      userMoney = input.nextDouble();
    }

    //output    
    groceries.printReciept(userMoney);

    
  }
  
}
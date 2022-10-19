import java.util.*;
public class UnoMilestone3{
    public static void main (String [] args){
    	
    	//Initialize Deck
    	Deck deck = new Deck();
    	//ArrayList<Card> deckArray = deck.getDeck();
    	deck.shuffle();
    
    	//Display Deck Before Hands
    	/*
    	System.out.println("Color" + "\t" + "Type" + "\t" + "Before");
    	for(int i = 0;i < deck.getCardCount();i++)
    		System.out.println(Card.cardReader(deckArray.get(i)));    	
    	*/
    	//Initialize Hands		
    	
    	Hand p1Hand= new Hand(deck);
        Hand p2Hand= new Hand(deck);
        Hand p3Hand= new Hand(deck);
        Hand p4Hand= new Hand(deck);
    	
        //Display Deck After Hands
        /*
        System.out.println("Color" + "\t" + "Type" + "\t" + "After");
    	for(int i=0;i<deck.getCardCount();i++) 
    		System.out.println(Card.cardReader(deckArray.get(i)));
		*/
    	//Display Hands
    
    	System.out.println("P1 Hand:");
    	ArrayList<Card> handArray = p1Hand.getHand();
    	for(int i=0;i<7;i++) 
    		System.out.println(Card.cardReader(handArray.get(i)));
    	System.out.println();
    	
    	System.out.println("P2 Hand:");
    	handArray = p2Hand.getHand();
    	for(int i=0;i<7;i++) 
    		System.out.println(Card.cardReader(handArray.get(i)));
    	System.out.println();
    	
    	System.out.println("P3 Hand:");
    	handArray = p3Hand.getHand();
    	for(int i=0;i<7;i++) 
    		System.out.println(Card.cardReader(handArray.get(i)));
    	System.out.println();
    	
    	System.out.println("P4 Hand:");
    	handArray = p4Hand.getHand();
    	for(int i=0;i<7;i++) 
    		System.out.println(Card.cardReader(handArray.get(i)));
    }
}

class Card{
	int typeId;
	int colorId;
	
	public Card(int type,int color){
	this.typeId=type;
	this.colorId=color;
	}
	
	static public String cardReader (Card input) {
		String color="";
		String type="";
		String cardValue;
		//Decrypt Type
		if(input.typeId < 10)
			type=String.valueOf(input.typeId);
		if (input.typeId == 10)
			type="Reverse";
		if (input.typeId == 11)
			type="Skip";
		if (input.typeId == 12)
			type="Draw 2";
		if (input.typeId == 13)
			type="Wild";
		if (input.typeId == 14)
			type="Wild Draw 4";
		if (input.typeId == -1)
			type = "NONE";
		//Decrypt Color
		if(input.colorId == 0)
			color="Red";
		if (input.colorId == 1)
			color="Yellow";
		if (input.colorId == 2)
			color="Green";
		if (input.colorId == 3)
			color="Blue";
		if (input.colorId == 4)
			color = "ALL";
		if (input.colorId == -1)
			color = "NONE";
		//Return Card
		cardValue= color + "\t" + type;
		return cardValue;
	}
}

class Hand{
	private ArrayList<Card> hand;
	
	public Hand(Deck deck) {
		hand = new ArrayList<Card>();
		for(int i = 0; i < 7; i++)
		hand.add(deck.draw(deck.getDeck()));
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
}

class Deck{
	private ArrayList<Card> deck;
	private int cardCount;

	public Deck(){
		deck = new ArrayList<Card>();
		//Type 0:0 1:1 2:2 3:3 4:4 5:5 6:6 7:7 8:8 9:9 R:10 S:11 D:12 W:13 4:14
		//Color 0:R 1:Y 2:G 3:B
		for(int typePlace=0;typePlace<15;typePlace++) { 
			if (typePlace==0) {
				for(int smallColorPlace=0;smallColorPlace<4;smallColorPlace++) {
					deck.add(new Card(typePlace,smallColorPlace));
				}
			}
			else if (typePlace==13 || typePlace==14) {
				for(int i=0;i<4;i++) {
					deck.add(new Card(typePlace,4));
				}
			}
			else {
				for(int colorPlace=0;colorPlace<4;colorPlace++) {
					for(int i=0;i<2;i++) {
					deck.add(new Card(typePlace,colorPlace));
					}
				}
			}
		}
		cardCount=deck.size();
	}

	public ArrayList<Card> getDeck(){
		return deck;
	}

	public void shuffle(){
		Random rand =new Random();
		for(int i = 0; i < (this.cardCount); i++) {
			int swap = rand.nextInt(this.cardCount);
			Card hold =this.deck.get(swap);
			this.deck.set(swap,this.deck.get(i));
			this.deck.set(i,hold);
		}
	}
	
	public Card draw(ArrayList<Card> inputDeck) {
		Card drawnCard = inputDeck.get(0);
		inputDeck.remove(0);
		this.cardCount=inputDeck.size();
		return drawnCard;
	}
	public int getCardCount() {
		return cardCount;
	}
}
//Gage Zahn
//I, Gage Zahn, confirm that the work for "UnoMilestone" 
//was solely undertaken by myself and that no help was 
//provided from other sources other than those allowed.

import java.util.*;
public class UnoMilestone{
	public static void main (String [] args){
		Match match1 = new Match();
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

	public static boolean isPlayable(Card card, Discard discard) {
		if (card.colorId == discard.colorId || card.typeId == discard.typeId || card.typeId == 14 || card.typeId == 13) {
			return true;
		}
		else
			return false;
	}
	public void play (Game game) {
		game.discard.discard.add(this);
		System.out.println(game.currentPlayer().name + " plays a " + Card.cardReader(this));
		game.discard.colorId = this.colorId;
		game.discard.typeId = this.typeId;
		System.out.println("--------------------------------");
	}
}

class Hand{
	private ArrayList<Card> hand;
	String name;
	public Hand(Deck deck, String name) {
		this.name = name;
		hand = new ArrayList<Card>();
		for(int i = 0; i < 7; i++)
		hand.add(deck.draw(deck.getDeck()));
	}
	public ArrayList<Card> getHand() {
		return hand;
	}
	public void play(Game game){
		System.out.println("--------------------------------");
		for (int i = 0; i < this.getHand().size(); i++ ) {
			if(Card.isPlayable(this.getHand().get(i), game.discard) == true) {
				this.getHand().get(i).play(game);
				this.getHand().remove(i);
				this.display();
				game.discard.actions(game);
				break;
			}
			if (game.turnDraw == true && i == this.getHand().size() - 1) {
				game.progress();
				System.out.println("--------------------------------");
			}
			else if (game.turnDraw == false && i == this.getHand().size() - 1){
				game.currentPlayer().getHand().add(game.deck.draw(game.deck.getDeck()));
				System.out.println(game.currentPlayer().name + " draws.");
				this.display();
				game.turnDraw=true;
				game.currentPlayer().play(game);
			}
		}
	}
	public void display() {
		System.out.println(this.name + "'s hand:");
    	ArrayList<Card> handArray = this.getHand();
    	for(int i = 0;i < this.getHand().size();i++)
    		System.out.println(Card.cardReader(handArray.get(i)));
    	System.out.println();
	}
	public int majorityColor(){
		int [] color = new int[4];
		int max = 0;
		int majority = 0;
		for(int i = 0; i < this.getHand().size(); i++) {
			if (this.getHand().get(i).colorId == 0)
				color[0]++;
			if (this.getHand().get(i).colorId == 1)
				color[1]++;
			if (this.getHand().get(i).colorId == 2)
				color[2]++;
			if (this.getHand().get(i).colorId == 3)
				color[3]++;
		}
		for(int i = 0; i < 4; i++) {
			if (max < color [i] ) {
				max=color[i];
				majority=i;
			}
		}
		return majority;
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
class Discard{	
	ArrayList<Card> discard;
	int typeId;
	int colorId;
	public Discard(Game game){
		discard = new ArrayList<Card>();
		discard.add(game.deck.draw(game.deck.getDeck()));
		while (discard.get(0).typeId == 14) {
			game.deck.getDeck().add(discard.get(0));
			discard.clear();
			game.deck.shuffle();
			discard.add(game.deck.draw(game.deck.getDeck()));
		}
		typeId = discard.get(0).typeId;
		colorId = discard.get(0).colorId;
			
	}
	public void shuffle(Game game) {
		Card topCard = game.discard.discard.get(game.discard.discard.size() - 1);
		game.discard.discard.remove(game.discard.discard.size() - 1);
		for(int i = 0;i < game.discard.discard.size(); i++) {
			game.deck.getDeck().add(discard.get(i));
		}
		game.discard.discard.clear();
		game.deck.shuffle();
		game.discard.discard.add(topCard);
		
	}
	public Card getTop() {
		return new Card(this.typeId,this.colorId);
	}
	public void actions(Game game) {
		if (this.typeId == 12) {
			game.nextPlayer().getHand().add(game.deck.draw(game.deck.getDeck()));
			game.nextPlayer().getHand().add(game.deck.draw(game.deck.getDeck()));
			System.out.println(game.nextPlayer().name + " draws 2!");
			game.nextPlayer().display();
			game.progress();
		
		}
		if (this.typeId == 14) {
			game.nextPlayer().getHand().add(game.deck.draw(game.deck.getDeck()));
			game.nextPlayer().getHand().add(game.deck.draw(game.deck.getDeck()));
			game.nextPlayer().getHand().add(game.deck.draw(game.deck.getDeck()));
			game.nextPlayer().getHand().add(game.deck.draw(game.deck.getDeck()));
			game.nextPlayer().display();
			game.discard.colorId = game.currentPlayer().majorityColor();
			System.out.println(game.currentPlayer().name + " chooses " + Card.cardReader(game.discard.getTop()));
			game.progress();
		}
		if (this.typeId == 13) {
			game.discard.colorId = game.currentPlayer().majorityColor();
			System.out.println(game.currentPlayer().name + " chooses " + Card.cardReader(game.discard.getTop()));	
		}
		if (this.typeId == 11) {
			System.out.println(game.nextPlayer().name + " was skipped!");
			game.progress();
			
		}
		if (this.typeId == 10) {
			game.direction = !game.direction;
			System.out.println("Reverse!");
		}
		if (game.disStart == true) {
			game.disStart = !game.disStart;
			game.turnDraw=false;
		}
		else
			game.progress();
	}
}
class Game{
	public boolean direction;
	public int player;
	Deck deck;
	Hand p1Hand;
	Hand p2Hand;
	Hand p3Hand;
	Hand p4Hand;
	Discard discard;
	boolean disStart;
	boolean turnDraw;
	boolean gameWon;
	public Game(){
		gameWon = false;
		turnDraw = false;
		disStart = false;
		player = 1;
		direction = true;
		deck = new Deck();
		deck.shuffle();
		p1Hand= new Hand(deck,"P1");
    	p2Hand= new Hand(deck,"P2" );
    	p3Hand= new Hand(deck,"P3");
    	p4Hand= new Hand(deck,"P4");
    	discard = new Discard(this);
    	if (this.discard.typeId == 12 ||this.discard.typeId == 14 ||this.discard.typeId == 13 ||this.discard.typeId == 11 ||this.discard.typeId == 10) {
			this.disStart = true;
			this.discard.actions(this);
		}
		System.out.println("Discard Pile: " + Card.cardReader(discard.discard.get(0)));
		System.out.println();
    	p1Hand.display();
    	p2Hand.display();
    	p3Hand.display();
    	p4Hand.display();
    	
	}
	public void progress(){
		if (this.currentPlayer().getHand().size() == 1)
			System.out.println(this.currentPlayer().name + " Says 'UNO!'");
		if (this.currentPlayer().getHand().size() == 0) {
			System.out.println(this.currentPlayer().name + " ran out of cards and wins this round!");
			System.out.println(this.currentPlayer().name + "'s round score: " + this.score());
			this.gameWon=true;
		}
		if (this.gameWon == false) {
		if(this.direction == true) {
			if (this.player == 4)
				this.player = 1;
			else 
				this.player++;
		}
		else if (this.player == 1)
			this.player = 4;
		else 
			this.player--;
		if(this.deck.getCardCount() < 5)
			this.discard.shuffle(this);
		this.turnDraw=false;
		}
	}
	public Hand nextPlayer() {
		if(this.direction == true) {
			if (this.player == 4)
				return p1Hand;
			if (this.player == 3)
				return p4Hand;
			if (this.player == 2)
				return p3Hand;
			if (this.player == 1)
				return p2Hand;
			else {
				System.out.println("FAIL");
				return p1Hand;
				
			}
		}
		else
			if (this.player == 4)
				return p3Hand;
			if (this.player == 3)
				return p2Hand;
			if (this.player == 2)
				return p1Hand;
			if (this.player == 1)
				return p4Hand;
			else {
				System.out.println("FAIL");
				return p1Hand;
			}
	}
	public Hand currentPlayer() {
		if (this.player == 4)
			return this.p4Hand;
		if (this.player == 3)
			return this.p3Hand;
		if (this.player == 2)
			return this.p2Hand;
		if (this.player == 1)
			return this.p1Hand;
		else {
			System.out.println("FAIL");
			return p1Hand;
		}
			
	}	
	public static int  tally(Hand hand) {
		int total = 0;
		for(int i = 0; i < hand.getHand().size(); i++) {
			if (hand.getHand().get(i).typeId < 10)
				total += hand.getHand().get(i).typeId;
			if (hand.getHand().get(i).typeId == 10)
				total += 20;
			if (hand.getHand().get(i).typeId == 11)
				total += 20;
			if (hand.getHand().get(i).typeId == 12)
				total += 20;
			if (hand.getHand().get(i).typeId == 13)
				total += 50;
			if (hand.getHand().get(i).typeId == 14)
				total += 50;
		}
		return total;
	}
	public int score() {
		int total = 0;
		if (this.currentPlayer() == p1Hand) {
			total = Game.tally(p2Hand) + Game.tally(p3Hand) + Game.tally(p4Hand);
			}
		if (this.currentPlayer() == p2Hand) {
			total = Game.tally(p1Hand) + Game.tally(p3Hand) + Game.tally(p4Hand);
			}
		if (this.currentPlayer() == p3Hand) {
			total = Game.tally(p2Hand) + Game.tally(p1Hand) + Game.tally(p4Hand);
			}
		if (this.currentPlayer() == p4Hand) {
			total = Game.tally(p2Hand) + Game.tally(p3Hand) + Game.tally(p1Hand);
			}
		return total;
	}
}
class Match{
	int p1Score;
	int p2Score;
	int p3Score;
	int p4Score;
	ArrayList<Game> game;
	public Match() {
		p1Score = 0;
		p2Score = 0;
		p3Score = 0;
		p4Score = 0;
		game = new ArrayList<Game>();
		int i = 0;
		while(p1Score < 500 && p2Score < 500 && p3Score < 500 && p4Score < 500) {
		game.add(new Game());
		while(game.get(i).gameWon == false) {
		game.get(i).currentPlayer().play(game.get(i));
		}
		this.scoreKeeper(game.get(i));
		i++;
		}
		this.announcer(p1Score,1);
		this.announcer(p2Score,2);
		this.announcer(p3Score,3);
		this.announcer(p4Score,4);
	}
	public void scoreKeeper(Game game) {
		if (game.currentPlayer().name == "P1") {
			p1Score += game.score();
		}
		if (game.currentPlayer().name == "P2") {
			p2Score += game.score();
		}
		if (game.currentPlayer().name == "P3") {
			p3Score += game.score();
		}
		if (game.currentPlayer().name == "P4") {
			p4Score += game.score();
		}
		System.out.println("Player 1 has " + p1Score + " points.");
		System.out.println("Player 2 has " + p2Score + " points.");
		System.out.println("Player 3 has " + p3Score + " points.");
		System.out.println("Player 4 has " + p4Score + " points.");
		
	}
	public void announcer(int score, int player) {
		if (score < 500)
			System.out.println("Player " + player + " ended the match with " + score + " points.");
			else
				System.out.println("Player " + player + " ended the match with " + score + " points, and won!");
	}
}

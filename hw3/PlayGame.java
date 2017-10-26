import java.util.*;

public class PlayGame{
	public static OldMaidComputer gameHost;

	public static void main(String[] argv){
		startGame();
		gameHost.dealCards();
		gameHost.dropCards();
		while(gameHost.isGameOver()!=1){
			gameHost.drawCards();
			//Scanner scanner = new Scanner(System.in);
			//String temp =  scanner.nextLine();
		}
	}

	private static void startGame(){
		System.out.printf("Which game do you want to play (1) Old-Maid (2) New-Maid (3) Draw Turtle: ");
		Scanner scanner = new Scanner(System.in);
		int gameType =  scanner.nextInt();
		try {
            if(gameType==1)
            	gameHost = new OldMaidComputer();
            else if(gameType==2)
            	gameHost = new NewMaidComputer();
            else if(gameType==3)
            	gameHost = new DrawTurtle();
        }
        catch (Exception ex) {
            System.out.println("Wrong game type");
        }
        
	}
}

class DrawTurtle extends OldMaidComputer{
	Card turtleCard;
	DrawTurtle(){
		playerPool = new ArrayList<Player>();
		player0 = new Player(0);
		player1 = new Player(1);
		player2 = new Player(2);
		player3 = new Player(3);
		playerPool.add(player0);
		playerPool.add(player1);
		playerPool.add(player2);
		playerPool.add(player3);
		whosTurn = 0;
	}

	void dealCards(){
		RandomIndex shuffledDesk = new RandomIndex();
		shuffledDesk.setSize(52);
		turtleCard = new Card(shuffledDesk.getNext());
		for(int i=0;i<13;i++)
			player0.getCard(shuffledDesk.getNext());
		for(int i=0;i<13;i++)
			player1.getCard(shuffledDesk.getNext());
		for(int i=0;i<13;i++)
			player2.getCard(shuffledDesk.getNext());
		for(int i=0;i<12;i++)
			player3.getCard(shuffledDesk.getNext());
		
		System.out.printf("Game start, the turtle card is %s. (players don't know)\n",turtleCard.card);
		System.out.println("Deal cards");
		player0.ShowCards();
		player1.ShowCards();
		player2.ShowCards();
		player3.ShowCards();
	}

	void dropCards(Player p){
		p.sortHand();
		Card tempCard,tempCard_next;
		for(int i=0;i<p.cardsInHand.size()-1;i++){
			tempCard = p.cardsInHand.get(i);
			tempCard_next = p.cardsInHand.get(i+1);
			if(tempCard.intValue==tempCard_next.intValue){
				p.discardCard(i+1);
				p.discardCard(i);
				i=-1;
			}
		}
		p.ShowCards();
	}
}

class NewMaidComputer extends OldMaidComputer{
	int superCard;	
	 NewMaidComputer(){
		playerPool = new ArrayList<Player>();
		player0 = new Player(0);
		player1 = new Player(1);
		player2 = new Player(2);
		player3 = new Player(3);
		playerPool.add(player0);
		playerPool.add(player1);
		playerPool.add(player2);
		playerPool.add(player3);
		whosTurn = 0;
		Random rgen = new Random();  
		superCard = rgen.nextInt(13);
		superCard += 2;
	}


	void dealCards(){
		System.out.printf("Game start, the super card is %d.\n",superCard);
		RandomIndex shuffledDesk = new RandomIndex();
		shuffledDesk.setSize(54);
		for(int i=0;i<14;i++)
			player0.getCard(shuffledDesk.getNext()-2);
		for(int i=0;i<14;i++)
			player1.getCard(shuffledDesk.getNext()-2);
		for(int i=0;i<13;i++)
			player2.getCard(shuffledDesk.getNext()-2);
		for(int i=0;i<13;i++)
			player3.getCard(shuffledDesk.getNext()-2);
			
		System.out.println("Deal cards");
		player0.ShowCards();
		player1.ShowCards();
		player2.ShowCards();
		player3.ShowCards();

	}

	void dropCards(Player p){
		p.sortHand();
		Card tempCard,tempCard_next;
		for(int i=0;i<p.cardsInHand.size()-1;i++){
			tempCard = p.cardsInHand.get(i);
			tempCard_next = p.cardsInHand.get(i+1);
			if (tempCard.intValue==tempCard_next.intValue && tempCard.intValue == superCard){
				if (tempCard.color == tempCard_next.color){
					p.discardCard(i+1);
					p.discardCard(i);
					i=-1;
				}
			}
			else if(tempCard.intValue==tempCard_next.intValue && tempCard.intValue!=0){
				p.discardCard(i+1);
				p.discardCard(i);
				i=-1;
			}
		}
		p.ShowCards();
	}

}

class OldMaidComputer{
	ArrayList<Player> playerPool;
	Player player0,player1,player2,player3;
	int whosTurn;

	OldMaidComputer(){
		playerPool = new ArrayList<Player>();
		player0 = new Player(0);
		player1 = new Player(1);
		player2 = new Player(2);
		player3 = new Player(3);
		playerPool.add(player0);
		playerPool.add(player1);
		playerPool.add(player2);
		playerPool.add(player3);
		whosTurn = 0;
	}

	void dealCards(){
		System.out.println("Game start");
		RandomIndex shuffledDesk = new RandomIndex();
		shuffledDesk.setSize(54);
		for(int i=0;i<14;i++)
			player0.getCard(shuffledDesk.getNext()-2);
		for(int i=0;i<14;i++)
			player1.getCard(shuffledDesk.getNext()-2);
		for(int i=0;i<13;i++)
			player2.getCard(shuffledDesk.getNext()-2);
		for(int i=0;i<13;i++)
			player3.getCard(shuffledDesk.getNext()-2);
			
		System.out.println("Deal cards");
		player0.ShowCards();
		player1.ShowCards();
		player2.ShowCards();
		player3.ShowCards();

	}
	
	void dropCards(){
		System.out.println("Drop cards");
		dropCards(player0);
		dropCards(player1);
		dropCards(player2);
		dropCards(player3);
	}

	void dropCards(Player p){
		p.sortHand();
		Card tempCard,tempCard_next;
		for(int i=0;i<p.cardsInHand.size()-1;i++){
			tempCard = p.cardsInHand.get(i);
			tempCard_next = p.cardsInHand.get(i+1);
			if(tempCard.intValue==tempCard_next.intValue && tempCard.intValue!=0){
				p.discardCard(i+1);
				p.discardCard(i);
				i=-1;
			}
		}
		p.ShowCards();
	}

	void drawCards(){
		Player p0 = playerPool.get(whosTurn%playerPool.size());
		Player p1 = playerPool.get((whosTurn+1)%playerPool.size());
		Random rgen = new Random();  
		int index = rgen.nextInt(p1.cardsInHand.size());
		Card pick = p1.cardsInHand.get(index);
		p0.getCard(pick.valueOfCard);
		p1.discardCard(index);
		System.out.printf("Player%d draws a card from Player%d %s\n",p0.playerNumber,p1.playerNumber,pick.card);
		dropCards(p0);
		dropCards(p1);
		whosTurn++;
	}

	//
	
	
	int isGameOver(){
		int i=0,winner=-1;
		for(i=0;i<playerPool.size();i++){
			if((playerPool.get(i)).isWin()){
				//	Record who's winning
				if(winner==-1)
					winner = i;
				//	Handle the situation when 2 players win simultaneously
				else {
					System.out.printf("Player%d and Player%d win\n",playerPool.get(winner).playerNumber,playerPool.get(i).playerNumber);
					playerPool.remove(i);
					playerPool.remove(winner);
					//	Reassign whosTurn to the player after the first winner
					//		ex: player 1 & 3 win, then whosTurn => player 2
					whosTurn = (winner%playerPool.size());
					if(playerPool.size()==2){
						System.out.println("Basic game over");
						System.out.println("Continue");
						return 0;
					}
					else if(playerPool.size()==1){
						System.out.println("Bonus game over");
						return 1;
					}
				}
			}
		}
		//	Handle the 1-player winning situation
		if(winner!=-1){
			System.out.printf("Player%d wins\n",playerPool.get(winner).playerNumber);
			playerPool.remove(winner);
			//	Reassign whosTurn to the player after the winner
			//		ex: player 1 win, then whosTurn => player 2		
			whosTurn = (winner%playerPool.size());
			if(playerPool.size()==3){
				System.out.println("Basic game over");
				System.out.println("Continue");
				return 0;
			}
			
			else if(playerPool.size()==1){
				System.out.println("Bonus game over");
				return 1;
			}
		}
		return 0;
	}
}

class Player{
	int playerNumber;
	List<Card> cardsInHand;

	// Initiate the player status
	Player(int num){
		playerNumber = num;
		cardsInHand = new ArrayList<Card>();
	}

	void getCard(int value){
		Card card = new Card(value);
		cardsInHand.add(card);
	}

	void discardCard(int index){
		//
		cardsInHand.remove(index);
	}

	//	Sort arraylist<Card>
	void sortHand(){
		Collections.sort(cardsInHand, new Comparator<Card>(){
			public int compare(Card card1, Card card2){
				return card1.valueOfCard-card2.valueOfCard;
			}
		});
	}

	void ShowCards(){
		sortHand();
		System.out.printf("Player%d: ",playerNumber);
		for(int i=0; i<cardsInHand.size();i++){
			System.out.printf("%s ", cardsInHand.get(i).card);
		}
		System.out.printf("\n");
	}

	boolean isWin(){
		if(cardsInHand.size()==0)
			return true;
		else return false;
	}
}

class Card{
	int valueOfCard;
	int intValue; // 0 for joker, 2~14 for others
	int color; // 0 for black, 1 for red
	String card = "";
	
	//	Ininiate the card status
	Card(int value){
		valueOfCard = value;
		card = ValueToSuitValue();
	}
	// Change card raw value into suit & value in string
	private String ValueToSuitValue(){
		//Jokers
		intValue = 0;
		if(valueOfCard==-2) return "R0";
		else if(valueOfCard==-1) return "B0";

		String suit = "";
		String value = "";

		switch(valueOfCard%4){
			case 0: suit = "C"; color = 0; break; 
			case 1: suit = "D"; color = 1; break; 
			case 2: suit = "H"; color = 1; break; 
			case 3: suit = "S"; color = 0; break;
		}

		switch(valueOfCard/4){
			case 0: value = "2"; intValue = 2; break; 
			case 1: value = "3"; intValue = 3; break; 
			case 2: value = "4"; intValue = 4; break; 
			case 3: value = "5"; intValue = 5; break;
			case 4: value = "6"; intValue = 6; break; 
			case 5: value = "7"; intValue = 7; break; 
			case 6: value = "8"; intValue = 8; break; 
			case 7: value = "9"; intValue = 9; break;
			case 8: value = "10"; intValue = 10; break; 
			case 9: value = "J"; intValue = 11; break; 
			case 10: value = "Q"; intValue = 12; break; 
			case 11: value = "K"; intValue = 13; break;
			case 12: value = "A"; intValue = 14; break;
		}
		return String.format("%s%s",suit,value);
	}
}

class RandomIndex{
    //DATA:
    int[] index;
    int count = 0;

    //ACTIONS:
    void setSize(int N){
		if (index == null || N != index.length){
		    index = new int[N];
		    initializeIndex();
		    permuteIndex();
		}
    }
    
    void initializeIndex(){
		for(int i=0;i<index.length;i++)
		    index[i] = i;
    }

    void permuteIndex(){
		Random rnd = new Random();
		for(int i=index.length-1;i>=0;i--){
		    int j = rnd.nextInt(i+1);
		    int tmp = index[j];
		    index[j] = index[i];
		    index[i] = tmp;
		}
    }

    int getNext(){
		int res = index[count++];
		if (count == index.length){
		    permuteIndex();
		    count = 0;
		}
		return res;
    }
}

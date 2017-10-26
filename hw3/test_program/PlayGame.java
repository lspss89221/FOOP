import java.util.*;


public class PlayGame{
	public static void main(String[] argv){
		GameComputer game = new GameComputer();
		game.dealCards();
		game.dropCards();
		while(game.isGameOver()!=1){
			game.drawCards();
		}
	}
}

class GameComputer{
	ArrayList<Player> playerPool;
	Player player0,player1,player2,player3;
	int whosTurn;

	GameComputer(){
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

		System.out.println("Game start");
	}

	void dealCards(){
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
		remainCards(player0);
		remainCards(player1);
		remainCards(player2);
		remainCards(player3);
		player0.ShowCards();
		player1.ShowCards();
		player2.ShowCards();
		player3.ShowCards();

	}

	void drawCards(){
		Player p0 = playerPool.get(whosTurn%playerPool.size());
		Player p1 = playerPool.get((whosTurn+1)%playerPool.size());
		Random rgen = new Random();  
		int index = rgen.nextInt(p1.cardsInHand.size());
		Card pick = p1.cardsInHand.get(index);
		p0.cardsInHand.add(pick);
		p1.cardsInHand.remove(index);
		remainCards(p0);
		remainCards(p1);
		System.out.printf("Player%d draws a card from Player%d %s\n",p0.playerNumber,p1.playerNumber,pick.card);
		p0.ShowCards();
		p1.ShowCards();
		whosTurn++;
	}

	void remainCards(Player p){
		p.sortHand();
		int temp,temp_next;
		for(int i=0;i<p.cardsInHand.size()-1;i++){
			temp = p.cardsInHand.get(i).valueOfCard;
			temp_next = p.cardsInHand.get(i+1).valueOfCard;
			if(temp/4==temp_next/4 && temp!=-1 && temp!=-2){
				p.cardsInHand.remove(i+1);
				p.cardsInHand.remove(i);
				i=-1;
			}
		}
	}
	
	int isGameOver(){
		int i=0,winner=-1;
		for(i=0;i<playerPool.size();i++){
			if((playerPool.get(i)).isWin()){
				if(winner==-1)
					winner = i;
				else {
					System.out.printf("Player%d and Player%d win\n",playerPool.get(winner).playerNumber,playerPool.get(i).playerNumber);
					playerPool.remove(i);
					playerPool.remove(winner);
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
		if(winner!=-1){
			System.out.printf("Player%d wins\n",playerPool.get(winner).playerNumber);
			playerPool.remove(winner);
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
	int intValue; // not needed?
	String card = "";
	
	//	Ininiate the card status
	Card(int value){
		valueOfCard = value;
		card = ValueToSuitValue();
	}
	// Change card raw value into suit & value in string
	private String ValueToSuitValue(){
		//Jokers
		if(valueOfCard==-2) return "R0";
		else if(valueOfCard==-1) return "B0";

		String suit = "";
		String value = "";

		switch(valueOfCard%4){
			case 0: suit = "C"; break; 
			case 1: suit = "D"; break; 
			case 2: suit = "H"; break; 
			case 3: suit = "S"; break;
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

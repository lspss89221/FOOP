import java.util.*;


public class POOCasino{
	public static void main(String argv[]){
		Computer casinoComputer = new Computer();
		casinoComputer.startGame();
		while(casinoComputer.isGameOver()!=true){
			casinoComputer.playOneRound();
			casinoComputer.round++;
		}
	}
}

class Computer{
	int round;
	Player player;

	// Initiate the game to round 1
	Computer(){
		round = 1;
	}

	void startGame(){
		System.out.println("POOCasino Jacks or better, written by b01902039 Kevin Shih");
		System.out.printf("Please enter your name: ");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
		player = new Player(str);
		System.out.printf("Welcome, %s.\n",player.name);
	}

	//	Each round
	void playOneRound(){
		System.out.printf("You have %d P-dollars now.\n", player.money);
		System.out.printf("Please enter your P-dollar bet for round %d (1-5 or 0 for quitting the game): ", round);
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		//	Input = 0
		if(str.charAt(0)== 48)
			GameOver();
		//	Get input of integer: 1~5. 
		//	Start this round of game!
		else if(str.charAt(0) > 48 && str.charAt(0) <= 53){
			int bet = Integer.valueOf(str);
			player.money -= bet;
			
			// Player gets the original 5 cards
			RandomIndex shuffledDesk = new RandomIndex();
			shuffledDesk.setSize(52);
			for(int i=0;i<5;i++){
				player.getCard(shuffledDesk.getNext());
			}
			player.showHand();
			
			//	Ask player which cards he wants to keep/discard and
			//		let player draw more cards form desk
			int drawMore = askKeepCards();
			for(int i=0;i<drawMore;i++)
				player.getCard(shuffledDesk.getNext());
			player.showNewHand();
			int payOff = countHandPayOff(bet,player.cardsInHand);
			player.money += payOff;
			player.clearHand();
		}
		//	Wrong input format.
		else{
			System.out.println("WRONG INPUT FORMAT, PLEASE CONTACT ME: 0935790665");
			playOneRound();
		}
	}

	//	Input: int:how much player bet, Arraylist<card>: player's hand
	int countHandPayOff(int betMoney, List<Card> hand){
		char suit[] = new char[5];
		int value[] = new int[5];
		int i,j;
		int match[] = new int[15];
	
		for(i=0;i<5;i++){
			suit[i] = hand.get(i).card.charAt(0);
			value[i] = hand.get(i).intValue;
		}
		//	Sequential value
		if (value[4]-value[3] == value[3]-value[2] &&
			value[3]-value[2] == value[2]-value[1] &&
			value[2]-value[1] == value[1]-value[0] &&
			value[1]-value[0] == 1){
			if(suit[4]==suit[3] && suit[3]==suit[2] && suit[2]==suit[1] && suit[1]==suit[0]){
				//Royal flush
				if(value[0]==10){	
					if(betMoney == 5){
						System.out.printf("You get a royal flush. The payoff is %d.\n", 4000);
						return 4000;
					}
					System.out.printf("You get a royal flush. The payoff is %d.\n", betMoney*250);
					return betMoney*250;
				}
				//	Straight flush
				else {
					System.out.printf("You get a straight flush. The payoff is %d.\n", betMoney*50);
					return betMoney*50;
				}
			}
			//	Straight
			else {
				System.out.printf("You get a straight. The payoff is %d.\n", betMoney*4);
				return betMoney*4;
			}
		}
		//	Flush
		if(suit[0]==suit[1] && suit[1]==suit[2] && suit[2]==suit[3] && suit[3]==suit[4]){		
			System.out.printf("You get a flush. The payoff is %d.\n", betMoney*6);
			return betMoney*6;
		}
		
		//	Initiate matching value
		for(i=2;i<15;i++)
			match[i]=1;

		//	Matching value
		for(i=0;i<5;i++){
			for(j=0;j<i;j++){
				if(value[i] == value[j]){
					match[value[i]]++;
					break;
				}
			}
		}
		for(i=2;i<15;i++){
			// Four of a kind
			if(match[i]==4)
				return betMoney*25;
			//	3-matching value
			else if(match[i]==3){
				//	Full House
				for(j=2;j<15;j++){
					if(match[j]==2){
						System.out.printf("You get a full House. The payoff is %d.\n", betMoney*9);
						return betMoney*9;
					}
				}
				//	Three of a kind
				System.out.printf("You get a three of a kind. The payoff is %d.\n", betMoney*3);
				return betMoney*3;
			}
			//	2-matching value
			else if(match[i]==2){
				for(j=2;j<15;j++){
					//	Full House
					if(match[j]==3){
						System.out.printf("You get a full House. The payoff is %d.\n", betMoney*9);
						return betMoney*9;
					}
					//	Two pair
					else if(match[j]==2 && i!=j){
						System.out.printf("You get a two pair. The payoff is %d.\n", betMoney*2);
						return betMoney*2;
					}
				}
				//	Jack or better
				if(i==11 || i==12 || i==13 || i==14){	
					System.out.printf("You get a Jack or better. The payoff is %d.\n", betMoney);
					return betMoney;
				}
			}
		}

		System.out.printf("You get nothing. The payoff is %d.\n", betMoney*0);
		return 0;
	}

	//	Ask player which cards he wants to keep/discard, and
	//		return the amounts of cards the player going to draw next
	int askKeepCards(){
		System.out.printf("Which cards do you want to keep? ");
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		int index;
		String discardMessage = "";

		// Detect wrong input format
		for(int i=0;i<str.length();i++){
			if(!(str.charAt(i)=='a'||str.charAt(i)=='b'||str.charAt(i)=='c'||str.charAt(i)=='d'||str.charAt(i)=='e')){
				System.out.println("Wront input format. Please enter the cards you want to keep again.");
				return askKeepCards();
			}
		}

		if(str.equals("abcde"))
			discardMessage = "Sorry, I don't want to discard anthing.";

		//What cards the player want to discard
		else {
			for(int i=4;i>=0;i--){
				char chr = (char)(i+97);
				index = str.indexOf(chr);
				if(index==-1) {
					discardMessage = String.format("(%s) %s %s", chr, player.cardsInHand.get(i).card, discardMessage.trim());
					player.discardCard(i);
				}

			}
			discardMessage = String.format("Okay. I will discard %s.",discardMessage);
		}
		System.out.printf("%s\n",discardMessage);
		return 5-str.length();
	}

	void GameOver(){
		System.out.printf("Good bye, %s. You played for %d round and have %d P-dollars now.\n", player.name, round, player.money);
		round = -1;
	}

	//	Return true if the game is over
	boolean isGameOver(){
		if(player.money < 0)
			return true;
		else if(round > 0)
			return false;
		else
			return true;
	}
}

class Player{
	String name;
	int money;
	List<Card> cardsInHand = new ArrayList<Card>();
	
	// Initiate the player status
	Player(String playerName){
		name = playerName;
		money = 1000;
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

	void showHand(){
		sortHand();
		System.out.printf("Your cards are (a) %s (b) %s (c) %s (d) %s (e) %s\n",
			cardsInHand.get(0).card,
			cardsInHand.get(1).card,
			cardsInHand.get(2).card,
			cardsInHand.get(3).card,
			cardsInHand.get(4).card);
	}

	void showNewHand(){
		sortHand();

		System.out.printf("Your new cards are %s %s %s %s %s\n",
			cardsInHand.get(0).card,
			cardsInHand.get(1).card,
			cardsInHand.get(2).card,
			cardsInHand.get(3).card,
			cardsInHand.get(4).card);
	}

	void clearHand(){
		for(int i=cardsInHand.size()-1;i>=0;i--){
			cardsInHand.remove(i);
		}
	}
}

//	Class of one card, containing the card value in raw int: 0~51 & in real value: 0~13 
//		and also there's a string representing the card such as: "C10"  
class Card{
	int valueOfCard;
	int intValue;
	String card = "";
	
	//	Ininiate the card status
	Card(int value){
		valueOfCard = value;
		card = ValueToSuitValue();
	}
	// Change card raw value into suit & value in string
	private String ValueToSuitValue(){
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

//	Shuffler
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

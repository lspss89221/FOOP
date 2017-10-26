package foop;
import java.util.*;
import java.lang.*;
import java.*;
import java.lang.reflect.*;
import foop.*;

class POOCasino{

	public static void main(String argv[]){
		POOCasino casinoComputer = new POOCasino(argv);
		casinoComputer.startGame();
		while(casinoComputer.isGameOver()!=true){
			casinoComputer.playOneRound();
		}
	}


	Player[] player = new Player[4];
	int round,nRound,numOfPlayer;
	String[] playerName = new String[4];
	double[] bet = new double[8];
	
	boolean[] doubleFlag = new boolean[8];
	boolean[] insuranceFlag = new boolean[4];
	boolean[] surrenderFlag = new boolean[4];
	boolean[] splitFlag = new boolean[4];
	boolean[] hit = new boolean[8];
	boolean[] burst = new boolean[8];
	boolean[] bankrupt = new boolean[4];
	
	int[] handValue = new int[9];
	ArrayList<Hand> playerHand = new ArrayList<Hand>();
	Hand dealerHand;
	ArrayList<Hand> table = new ArrayList<Hand>();
	RandomIndex shuffledDesk = new RandomIndex();


	int	total;
	int	numOfAce;

	int tmpCardValue;
	Card tmpCard;
	ArrayList<Card> tmpPlayerCard = new ArrayList<Card>();
	Hand tmpHand;
	
	public POOCasino(String input[]){
		try {
			numOfPlayer = input.length-2;
			System.out.printf("numOfPlayer is %d\n",numOfPlayer);

			for(int i=0;i<numOfPlayer;i++)
				player[i] = (Player)Class.forName(input[i+2]).getConstructor(Integer.TYPE).newInstance(Integer.valueOf(input[1]));
			nRound = Integer.valueOf(input[0]);
		}
		catch(ClassNotFoundException ex0){
			System.out.println("Class not found");	
		}
		catch(NoSuchMethodException ex1){
			System.out.println("No such method");	
		}
		catch(Exception ex){
			System.out.println("Something wrong in input.");	
		}
		shuffledDesk.setSize(52);
	}
	
	void startGame(){
		String greeting = "";
		for(int i=0;i<numOfPlayer;i++){
			playerName[i] = player[i].getClass().getName();
			playerName[i] = playerName[i].replace("foop.", "");
			greeting = String.format("%s %s(%d) ",greeting,playerName[i],i);	
		}
		greeting = greeting.trim();
		System.out.printf("Hello, %s.\n",greeting);

		for(int i=0;i<numOfPlayer;i++)
			System.out.printf("%s(%d) has %.1f chips now\n", playerName[i],i,player[i].get_chips());
	}

	void playOneRound(){
		round++;
		System.out.println();
		System.out.printf("--------This is round %d--------\n",round);
		makeBets();
		firstTwoCards();
		buyInsurance();
		surrender();
		split();
		doubleDown();
		playerHit();
		dealerAction();
		moneyTime();
	}

	void cardsValue(Hand pHand){
		total = 0;
		numOfAce = 0;
		tmpPlayerCard = pHand.getCards();
		for(int j=0;j<tmpPlayerCard.size();j++){
			switch(tmpPlayerCard.get(j).getValue()){
				case 10: case 11: case 12: case 13: 
					total += 10;
					break;
				case 1:
					total += 11;
					numOfAce += 1;
					break;
				default:
					total += tmpPlayerCard.get(j).getValue();
					break;
			}
		}
	}
	
	void countHandValue(){
		for(int i=0;i<9;i++)
			handValue[i]=0;
		for(int i=0; i<playerHand.size(); i++){
			if(playerHand.get(i).getCards().size()!=0){
				cardsValue(playerHand.get(i));
				for(int j=1;j<numOfAce;j++){
					if(total>21)
						total-=10;
					else{
						handValue[i]=total;	
						break;
					}
				}
				if(handValue[i]==0)
					handValue[i]=total;
			}
		}
		
		cardsValue(dealerHand);

		for(int j=1;j<numOfAce;j++){
			if(total>21)
				total-=10;
			else{
				handValue[8]=total;	
				break;
			}
		}
		if(handValue[8]==0)
			handValue[8]=total;
	}


	void moneyTime(){
		countHandValue();
		for(int i=0; i<playerHand.size(); i++)
			if(playerHand.get(i).getCards().size()!=0)
				System.out.printf("%s(%d-%d)(bet:%.1f)'s hand value is %d\n",playerName[i%4], i%4, i/4,bet[i],handValue[i]);
				
		System.out.printf("Dealer's hand value is %d\n",handValue[8]);
		for(int i=0; i<playerHand.size(); i++){
			if(playerHand.get(i).getCards().size()!=0){
				try{
					if(surrenderFlag[i%4]==true)
						player[i%4].increase_chips(bet[i]*0.5);
					else if(burst[i]==true){
						continue;
					}
					else if(handValue[i]==21){
						if(handValue[8]==21){
							player[i%4].increase_chips(bet[i]);
						}
						else{
							player[i%4].increase_chips(2.5*bet[i]);
						}
					}
					else if(handValue[i]!=21){
						if(handValue[8]>21){
							player[i%4].increase_chips(2*bet[i]);
						}
						else if(handValue[8]==21){
							if(insuranceFlag[i%4]==true)
								player[i%4].increase_chips(bet[i]);		
						}
						else if(handValue[i]>handValue[8]){
							player[i%4].increase_chips(2*bet[i]);
						}
						else if(handValue[i]==handValue[8]){
							player[i%4].increase_chips(bet[i]);
						}
					}
				}
				catch(Exception ex0){
					System.out.printf("%s has no chips\n",playerName[i]);
					System.out.println("Exception");
				}
			}
		}
		for(int i=0;i<numOfPlayer;i++){
			double chips = player[i].get_chips();
			System.out.printf("%s(%d) has %.1f chips now\n", playerName[i],i,chips);
			if(chips <= 0){
				System.out.printf("%s(%d) is bankrupt now\n", playerName[i],i);
				bankrupt[i] = true;	
			}
		}
	}

	void dealerAction(){
		System.out.println("--------Dealer action:--------");
		while(dealerHit()==true){
			System.out.println("Dealer hit");	
			tmpPlayerCard = dealerHand.getCards();;
			tmpCardValue = shuffledDesk.getNext();
			tmpCard = new Card((byte)(tmpCardValue%4+1),(byte)(tmpCardValue/4+1));
			tmpPlayerCard.add(tmpCard);
			dealerHand = new Hand(tmpPlayerCard);
			String suitName = valueToSuit(tmpCard.getSuit());
			suitName = String.format("%s_%d ",suitName,tmpCard.getValue());
			System.out.printf("Dealer get %s\n",suitName);
		}
		showCards();
	}

	void playerHit(){
		System.out.println("--------Player hit:--------");
		myTableToOthers();
		for(int i=0;i<8;i++)
			burst[i]=false;
		for(int i=0;i<playerHand.size();i++){
			if(playerHand.get(i).getCards().size()!=0 && surrenderFlag[i%4]!=true){
				while(isBurst(i)!=true){
					hit[i] = player[i%4].hit_me(playerHand.get(i), dealerHand.getCards().get(0), table);
					if(hit[i]==true)
						addCard2PlayerHand(shuffledDesk.getNext(),i);
					if(hit[i]==false)
						break;		
				}
				if(hit[i]==true){
					System.out.printf("%s(%d) is burst\n", playerName[i%4],i);
					burst[i]=true;
				}
			}
		}
		showCards();
	}

	boolean dealerHit(){
		tmpPlayerCard = dealerHand.getCards();
		int total = 0;
		int numOfAce = 0;
		for(int i=0;i<tmpPlayerCard.size();i++){
			switch(tmpPlayerCard.get(i).getValue()){
				case 10: case 11: case 12: case 13: 
					total += 10;
					break;
				case 1:
					total += 11;
					numOfAce += 1;
					break;
				default:
					total += tmpPlayerCard.get(i).getValue();
					break;
			}
		}
		while(numOfAce>1){
			total -= 10;
			numOfAce --;
		}
		if(total == 21)
			return false;
		else if (total == 17 && numOfAce>0)
			return true;
		else if(total <= 16)
			return true;
		else
			return false;
	}

	boolean isBurst(int position){
		countHandValue();
		if(handValue[position] > 21)
			return true;
		else
			return false;
	}

	void doubleDown(){
		System.out.println("--------Double down:--------");
		myTableToOthers();
		for(int i=0;i<playerHand.size();i++){
			if(playerHand.get(i).getCards().size()!=0 && surrenderFlag[i%4]!=true){
				doubleFlag[i] = player[i%4].do_double(playerHand.get(i), dealerHand.getCards().get(0), table);
				if(doubleFlag[i]==true && player[i%4].get_chips() > bet[i]){
					System.out.printf("%s(%d) double down\n", playerName[i%4],i);
					try{
						player[i%4].decrease_chips(bet[i]);
						bet[i] = 2*bet[i];
					}
					catch(Exception ex0){
						System.out.println("Exception");
					}
				}
			}
		}
		showCards();
	}

	void split(){
		System.out.println("--------Split:--------");
		myTableToOthers();
		for(int i=0;i<numOfPlayer;i++){
			if(playerHand.get(i).getCards().size()!=0 && surrenderFlag[i%4]!=true){
				if(playerHand.get(i).getCards().get(0).getValue()==playerHand.get(i).getCards().get(1).getValue()&&surrenderFlag[i]!=true){
					tmpPlayerCard = playerHand.get(i).getCards();
					splitFlag[i] = player[i].do_split(tmpPlayerCard, dealerHand.getCards().get(0), table);	
					if(splitFlag[i]==true && player[i%4].get_chips()>bet[i%4]){
						System.out.printf("%s(%d) splits\n", playerName[i],i);
						ArrayList<Card> tmpPlayerCard2 = new ArrayList<Card>();
						tmpPlayerCard2.add(tmpPlayerCard.get(1));
						tmpPlayerCard.remove(1);
						tmpHand= new Hand(tmpPlayerCard);
						playerHand.remove(i);
						playerHand.add(i,tmpHand);
						tmpHand= new Hand(tmpPlayerCard2);
						playerHand.remove(i+4);
						playerHand.add(i+4,tmpHand);
						bet[i+4] = bet[i];
						try{
							player[i].decrease_chips(bet[i]);
						}
						catch(Exception ex0){
							System.out.println("Exception");
						}
						addCard2PlayerHand(shuffledDesk.getNext(), i);
						addCard2PlayerHand(shuffledDesk.getNext(), i+4);
					}
				}
			}
		}
		showCards();
	}

	void surrender(){
		System.out.println("--------Surrender:--------");
		myTableToOthers();
		tmpCard = dealerHand.getCards().get(1);
		System.out.printf("The Dealer's face-down card is ");
		String suitName = valueToSuit(tmpCard.getSuit());
		System.out.printf("%s_%d\n",suitName, tmpCard.getValue());
		for(int i=0;i<numOfPlayer;i++){
			if(bankrupt[i]!=true){
				surrenderFlag[i] = player[i].do_surrender((playerHand.get(i).getCards()).get(0), dealerHand.getCards().get(0), table);
				if(surrenderFlag[i]==true)
					System.out.printf("%s(%d) surrenders\n", playerName[i],i);
			}
		}
	}

	void buyInsurance(){
		System.out.println("--------Buy insurance:--------");
		myTableToOthers();
		tmpCard = dealerHand.getCards().get(0);
		System.out.printf("The Dealer's face-up card is ");
		String suitName = valueToSuit(tmpCard.getSuit());
		System.out.printf("%s_%d\n",suitName, tmpCard.getValue());
		if (tmpCard.getValue()==1){
			for(int i=0;i<numOfPlayer;i++){
				if(bankrupt[i]!=true){
					insuranceFlag[i] = player[i].buy_insurance((playerHand.get(i).getCards()).get(0), dealerHand.getCards().get(0), table);
					if(insuranceFlag[i]==true && player[i%4].get_chips() > bet[i]*0.5){
						System.out.printf("%s(%d) buys insurance\n", playerName[i],i);
						try{
							player[i].decrease_chips(0.5*bet[i]);
						}
						catch(Exception ex0){
							System.out.println("Exception");
						}
					}
				}
			}
		}
	}

	void makeBets(){
		System.out.println("--------Make bets:--------");
		for(int i=0;i<8;i++){
			bet[i]=0;
			handValue[i]=0;
		}
		handValue[8]=0;
		for(int i=0;i<numOfPlayer;i++){
			if(bankrupt[i]!=true){
				playerHand.add(dealerHand);
				bet[i] = (double)player[i].make_bet(playerHand,numOfPlayer,i);
				try{
					if(player[i].get_chips() > bet[i])
						player[i].decrease_chips(bet[i]);
					else
						player[i].decrease_chips(player[i].get_chips());
				}
				catch(Exception ex0){
					System.out.println("Exception");
				}
				System.out.printf("%s(%d) makes bet: %.1f\n", playerName[i],i , bet[i]);
			}
		}
	}

	void initiatePlayerHand(){
		playerHand.clear();
		tmpPlayerCard.clear();
		tmpHand = new Hand(tmpPlayerCard);
		for(int i=0;i<8;i++)
			playerHand.add(tmpHand);
	}

	void firstTwoCards(){
		System.out.println("--------Deal first two cards:--------");
		initiatePlayerHand();
		//dealer's first 2 cards
		tmpCardValue = shuffledDesk.getNext();
		//tmpCard = new Card((byte)(tmpCardValue%4+1),(byte)1);
		tmpCard = new Card((byte)(tmpCardValue%4+1),(byte)(tmpCardValue/4+1));
		tmpPlayerCard.add(tmpCard);
		tmpCardValue = shuffledDesk.getNext();
		tmpCard = new Card((byte)(tmpCardValue%4+1),(byte)(tmpCardValue/4+1));
		tmpPlayerCard.add(tmpCard);
		dealerHand = new Hand(tmpPlayerCard);

		//players' first 2 cards
		for(int i=0;i<numOfPlayer;i++){
			if(bankrupt[i]!=true){
				tmpCardValue = shuffledDesk.getNext();
				addCard2PlayerHand(tmpCardValue, i);
				tmpCardValue = shuffledDesk.getNext();
				addCard2PlayerHand(tmpCardValue, i);
			}
		}
		showCards();
	}

	void addCard2PlayerHand(int cardValue, int position){
		tmpPlayerCard = playerHand.get(position).getCards();
		tmpCard = new Card((byte)(cardValue%4+1),(byte)(cardValue/4+1));
		String suitName = valueToSuit(tmpCard.getSuit());
		suitName = String.format("%s_%d ",suitName,tmpCard.getValue());
		System.out.printf("%s(%d) get %s\n",playerName[position%4],position,suitName);
		tmpPlayerCard.add(tmpCard);
		tmpHand= new Hand(tmpPlayerCard);
		playerHand.remove(position);
		playerHand.add(position,tmpHand);
	}

	void myTableToOthers(){
		table.clear();
		for(int i=0;i<playerHand.size();i++){
			if(playerHand.get(i).getCards().size()!=0)
				table.add(playerHand.get(i));
		}
		table.add(dealerHand);
	}


	void showCards(){
		System.out.println("--------Now players' hands--------");
		for(int i=0;i<playerHand.size();i++){
			if(playerHand.get(i).getCards().size()!=0){
				tmpHand = playerHand.get(i);
				tmpPlayerCard = tmpHand.getCards();
				int turn = i%4;
				int split = i/4;
				System.out.printf("%s(%d-%d)(bet: %.1f) has ",playerName[i%4], turn, split, bet[i]);
				for(int j=0;j<tmpPlayerCard.size();j++){
					int suit = tmpPlayerCard.get(j).getSuit();
					int value = tmpPlayerCard.get(j).getValue();
					String suitName = valueToSuit(suit);
					System.out.printf("%s_%d ",suitName,value );
				}
				System.out.println();
			}
		}
		tmpPlayerCard = dealerHand.getCards();
		System.out.printf("Dealer has ");	
		for(int i=0;i<tmpPlayerCard.size();i++){
			int suit = tmpPlayerCard.get(i).getSuit();
			int value = tmpPlayerCard.get(i).getValue();
			String suitName = valueToSuit(suit);
			System.out.printf("%s_%d ",suitName,value );
		}
		System.out.println();
	}
	
	String valueToSuit(int value){
		String suitName = "";
		switch(value){
			case 1: suitName = "CLUB"; break;
			case 2: suitName = "DIAMOND"; break;
			case 3: suitName = "HEART"; break;
			case 4: suitName = "SPADE"; break;
			default: break;
		}
		return suitName;
	}

	boolean isGameOver(){
		if(round%5==0)
			shuffledDesk.setSize(52);
		if(round == nRound)
			return true;
		return false;
	}

}

class RandomIndex{
    //DATA:
    int[] index;
    int count = 0;

    //ACTIONS:
    void setSize(int N){
		if (index == null || N != index.length){
		    index = new int[4*N];
		    initializeIndex();
		    permuteIndex();
		}
    }
    
    void initializeIndex(){
		for(int i=0;i<index.length;i++)
		    index[i] = i/4;
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

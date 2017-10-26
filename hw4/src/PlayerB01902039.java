package foop;
import java.util.*;
import foop.*;
import java.lang.*;

class PlayerB01902039 extends Player{
	int total = 0;
	int numOfAce = 0;
	ArrayList<Card> tmpPlayerCard = new ArrayList<Card>();

	public PlayerB01902039(int chips){
		super(chips);
		//using floor, cutting of the digits
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

	public boolean buy_insurance(Card my_open, Card dealer_open, ArrayList<Hand> current_table){
		//System.out.println("test PlayerB01902039 buy_insurance");
		return false;
	}
	public boolean	do_double(Hand my_open, Card dealer_open, ArrayList<Hand> current_table){
		cardsValue(my_open);
		int dealerCardValue = dealer_open.getValue();
		if(numOfAce==0)
			if(total==9 && dealerCardValue>=2 && dealerCardValue<=6)
				return true;
			else if((total==10 || total==11)&&total>dealerCardValue)
				return true;
		else if(numOfAce>0)
			if(total>=16 && total<=18)
				return true;
		return false;
	}
	public boolean	do_split(java.util.ArrayList<Card> my_open, Card dealer_open, ArrayList<Hand> current_table){
		int dealerCardValue = dealer_open.getValue();
		if(my_open.get(0).getValue()==8||my_open.get(0).getValue()==1)
			return true;
		else if(my_open.get(0).getValue()==2||my_open.get(0).getValue()==3||my_open.get(0).getValue()==6||my_open.get(0).getValue()==7||my_open.get(0).getValue()==9)
			if(dealerCardValue>=2 && dealerCardValue<=6)
				return true;			
		return false;
	}
	public boolean	do_surrender(Card my_open, Card dealer_open, ArrayList<Hand> current_table){
		//System.out.println("test PlayerB01902039 do_surrender");
		return false;	
	}
	public boolean	hit_me(Hand my_open, Card dealer_open, ArrayList<Hand> current_table){
		//System.out.println("test PlayerB01902039 hit_me");
		cardsValue(my_open);
		int dealerCardValue = dealer_open.getValue();
		if(numOfAce==0){
			if(total<=11)
				return true;
			else if(total>=12 && total<=16 && dealerCardValue>=2 && dealerCardValue<=6)
				return false;
			else if(total>=12 && total<=16 && ((dealerCardValue>=7 && dealerCardValue<=10)||dealerCardValue==1))
				return false;
			else if(total>=17 && total<=21)
				return false;
		}
		else if(numOfAce>0){
			if(total>=13 && total<=18)
				return true;
			else if(total>=19 && total <=21)
				return false;
		}
		return false;
	}

	public int	make_bet(ArrayList<Hand> last_table, int total_player, int my_position){
		//System.out.println("test PlayerB01902039 make_bet");
		return 1;
	}
	public String toString(){
		//String.format("PlayerB01902039 has %f chips",get_chips());
		return String.format("%f",get_chips()); 
	}
}
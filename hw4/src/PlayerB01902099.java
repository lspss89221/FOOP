package foop;
import java.*;
import foop.*;

class PlayerB01902099 extends Player{
	public PlayerB01902099(int chips){
		super(chips);
		System.out.println("Object created!! with " + chips + " chips.");
		System.out.println("Hahahaha!! I want to play a game...");
	}
	
	/*Get the number of chips that the player wants to bet.*/
	public int make_bet(java.util.ArrayList<Hand> last_table, int total_player, int my_position){
	
		double my_chips = get_chips();
		if(my_chips < 1)
			return 0;
		else if(my_chips < 20)
			return 1;
		else if(my_chips < 50)
			return 2;
		else if(my_chips < 100)
			return 4;
		else if(my_chips < 500)
			return 10;
		else if(my_chips < 1000)
			return 20;
		else if(my_chips < 5000)
			return 30;
		else if(my_chips < 10000)
			return 50;
		else if(my_chips >= 10000)
			return 100;
		return 10;
	}

	/*Ask whether the player wants to do surrender.*/
	public boolean	do_surrender(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		return false;
	}

	/*Ask whether the player wants to buy insurance.*/
	public boolean buy_insurance(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		return false;
	}

	public int calculateCardValueByHand(Hand h){
	
		int cardValue , value = 0, aceNumber = 0;
		int size = h.getCards().size();		
		for(int i = 0 ; i < size ; i++){
			cardValue = (int) h.getCards().get(i).getValue();
			if(cardValue > 10)
				cardValue = 10;
			else if(cardValue == 1)
				aceNumber += 1;
			value += cardValue;
		}
		
		while(aceNumber > 0 && value < 21){
			if(value + 10 <= 21){
				value += 10;
				aceNumber -= 1;
			}
			else
				break;
		}
		return value;
	}	
		
	/*Ask whether the player wants to double down.*/
	public boolean do_double(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		
		int dealerValue = dealer_open.getValue();
		int myValue = calculateCardValueByHand(my_open);
		java.util.ArrayList<Card> temp = my_open.getCards();
		
		int aceFlag = 0;
		int size = temp.size();
		for(int i = 0 ; i < size ; i++)
			if(temp.get(i).getValue() == 1)
				aceFlag = 1;
		
		if(dealerValue > 10)
			dealerValue = 10;
			
		if((myValue == 11 || myValue == 10) && dealerValue != 10 && dealerValue != 1){
			return true;
		}
		else if(aceFlag == 1){
			if(myValue >= 15 && myValue <= 17)
				if(dealerValue >= 3 && dealerValue <= 6)
					return true;
		}
		return false;
	}

	/*Ask whether the player wants to do split.*/
	public boolean do_split(java.util.ArrayList<Card> my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		
		int dealerValue = dealer_open.getValue();
		int myValue = my_open.get(0).getValue();
		if(dealerValue > 10)
			dealerValue = 10;
		if(myValue > 10)
			myValue = 10;
		
		if(myValue == 1 || myValue == 8){
			return true;
		}
		else{
			if(myValue == 7 || myValue == 6 || myValue == 2 || myValue == 3)
				if(dealerValue >= 2 && dealerValue <= 7)
					return true;
		}	
		return false;
	}

	/*Ask whether the player wants to hit.*/
	public boolean	hit_me(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
	
		int dealerValue = dealer_open.getValue();
		int myValue = calculateCardValueByHand(my_open);
		if(dealerValue == 1)
			dealerValue = 11;
		else if(dealerValue > 10)
			dealerValue = 10;
		
		if(myValue >= 4 && myValue <= 11){
			return true;
		}
		else if(myValue >= 12 && myValue <= 15){
			if(dealerValue >= 7 && dealerValue <= 11)
				return true;
		}
		else if(myValue == 16 || myValue == 17){
			if(my_open.getCards().size() == 2){
				int v1 = my_open.getCards().get(0).getValue();
				int v2 = my_open.getCards().get(1).getValue();
				if(v1 == 1 || v2 == 1)
					return true;
			}
		}
		return false;
	}
	
	/*Show the player's status.	*/
	public java.lang.String toString(){
		return "XDD";
	}

}
import java.util.*;

public class Player{
	public int playerNumber;
	public List<Integer> cardsInHand;
	public Player next;

	public Player(int num){
		playerNumber = num;
		cardsInHand = new ArrayList<Integer>();
	}

	public void ShowCards(){
		Collections.sort(cardsInHand);
		System.out.printf("Player%d: ",playerNumber);
		for(int i=0; i<cardsInHand.size();i++){
			System.out.printf("%s ", ToSuit(cardsInHand.get(i)-2));
		}
		System.out.printf("\n");
	}

	public static String ToSuit(int num){
		if(num==-2)
			return "R0";
		else if(num==-1)
			return "B0";

		String suit = "";
		String card = "";
				
		switch(num%4){
			case 0: suit = "C"; break; case 1: suit = "D"; break; case 2: suit = "H"; break; case 3: suit = "S"; break;
		}

		switch(num/4){
			case 0: card = "2"; break; case 1: card = "3"; break; case 2: card = "4"; break; case 3: card = "5"; break;
			case 4: card = "6"; break; case 5: card = "7"; break; case 6: card = "8"; break; case 7: card = "9"; break;
			case 8: card = "10"; break; case 9: card = "J"; break; case 10: card = "Q"; break; case 11: card = "K"; break;
			case 12: card = "A"; break;
		}

		return String.format("%s%s",suit,card);
	}

	public boolean isWin(){
		if(cardsInHand.size()==0)
			return true;
		else return false;
	}
	

}
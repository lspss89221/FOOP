import java.util.*;

public class PlayGame{

	public static ArrayList<Player> playerPool = new ArrayList<Player>();
	public static int whosTurn;

	public static void DealCards(Player p0,Player p1, Player p2, Player p3){
		Random rgen = new Random();  

		int i;
		int[] cards = new int[54];
		for(i=0; i<54; i++){
			cards[i] = i;
		}

		for (i=0; i<54; i++) {
		    int randomPosition = rgen.nextInt(54);
		    int temp = cards[i];
		    cards[i] = cards[randomPosition];
		    cards[randomPosition] = temp;
		}

		for(i=0; i<14; i++){
			p0.cardsInHand.add(cards[i]);
		}
		
		for(i=0; i<14; i++){
			p1.cardsInHand.add(cards[i+14]);
		}
		
		for(i=0; i<13; i++){
			p2.cardsInHand.add(cards[i+28]);
		}
			
		for(i=0; i<13; i++){
			p3.cardsInHand.add(cards[i+41]);
		}

		System.out.println("Deal cards");
		ShowCards(p0, p1, p2, p3);
	}
	
	public static void DropCards(Player p0,Player p1, Player p2, Player p3){
		RemainCards(p0);
		RemainCards(p1);
		RemainCards(p2);
		RemainCards(p3);

		System.out.println("Drop cards");
		ShowCards(p0, p1, p2, p3);
	}

	public static void DrawCards(Player p0, Player p1){
		Random rgen = new Random();  
		int index = rgen.nextInt(p1.cardsInHand.size());
		int card = p1.cardsInHand.get(index);
		p0.cardsInHand.add(card);
		p1.cardsInHand.remove(index);
		RemainCards(p0);
		RemainCards(p1);
		System.out.printf("Player%d draws a card from Player%d %s\n",p0.playerNumber,p1.playerNumber,Player.ToSuit(card-2));
		ShowCards(p0, p1);
	}

	public static void RemainCards(Player p){
		Collections.sort(p.cardsInHand);
		for(int i=0;i<p.cardsInHand.size()-1;i++){
			if((p.cardsInHand.get(i)-2)/4==(p.cardsInHand.get(i+1)-2)/4 && p.cardsInHand.get(i)!=0 && p.cardsInHand.get(i)!=1){
				p.cardsInHand.remove(i+1);
				p.cardsInHand.remove(i);
				i=-1;
			}
		}
	}
	
	public static void ShowCards(Player p0,Player p1, Player p2, Player p3){
		p0.ShowCards();
		p1.ShowCards();
		p2.ShowCards();
		p3.ShowCards();
	}
	
	public static void ShowCards(Player p0,Player p1){
		p0.ShowCards();
		p1.ShowCards();
	}
	
	public static int isGameOver(){
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

	public static void main(String[] argv){
		Player player0 = new Player(0);
		Player player1 = new Player(1);
		Player player2 = new Player(2);
		Player player3 = new Player(3);
		playerPool.add(player0);
		playerPool.add(player1);
		playerPool.add(player2);
		playerPool.add(player3);
	
		DealCards(player0, player1, player2, player3);
		DropCards(player0, player1, player2, player3);
		System.out.println("Game start");
	
		int i = 0;
		whosTurn = 0;
		while(isGameOver()!=1){
			DrawCards(playerPool.get(whosTurn%playerPool.size()),playerPool.get((whosTurn+1)%playerPool.size()));
			whosTurn++;
		}	
	}
}
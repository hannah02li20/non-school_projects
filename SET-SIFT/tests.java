import java.util.ArrayList;
import java.io.File;

// Everything in this file is my work

public class tests {
    public static void main(String[] args) {

        SetGame thisgame = new SetGame();

        ArrayList<SetGame.Card> test_list = new ArrayList<SetGame.Card>();
        File t = new File("input.txt");
        //File t = new File("set_test.txt");
        //File t = new File("test_2.txt");
        test_list = thisgame.convertFiletoList(t);

        ArrayList<ArrayList<SetGame.Card>> set_collection = thisgame.getAllSets(test_list);

        int setsize = set_collection.size();
        System.out.println(setsize);

        ArrayList<ArrayList<SetGame.Card>> disjoint_collection = new ArrayList<ArrayList<SetGame.Card>>();
        disjoint_collection = thisgame.largestDisjoint(set_collection);

        int disjoint_size = disjoint_collection.size();
        System.out.println(disjoint_size);
        System.out.println();
        for (int index = 0; index < disjoint_size; index++) {
            for (int card_index = 0; card_index < 3; card_index++) {
                SetGame.Card temp_card = disjoint_collection.get(index).get(card_index);
                String printthis = thisgame.toPrint(temp_card);	   
	            System.out.println(printthis);
            }
            System.out.println();
        }

    }
}

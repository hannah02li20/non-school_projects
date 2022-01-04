
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetGame {

    // code by Henrik Warne
    class Card {
        private int color; // (1) yellow, (2) blue, (3) green
        private int number; // the number of times the symbol appears, 1, 2, or 3
        private int symbol; // (1) A, (2) S, (3) H
        private int shading; // (1) lowercase a, s, h; (2) uppercase A, S, H; (3) symbolcase @, $, #

        public Card(int co, int nu, int sy, int sh) {
            color = co;
            number = nu;
            symbol = sy;
            shading = sh;
        }
    }

    // entirely my code
    // this takes the input.txt and turns it into a list of cards that I can use
    ArrayList<Card> convertFiletoList(File name) {
        ArrayList<Card> result = new ArrayList<Card>();
        
        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(name));
			String line = reader.readLine();
            // don't need the first line, skip it (it's just the number of lines)
            line = reader.readLine();

			while (line != null) {
                int c, sym, sha, num;
                // read the line & get color

                String yellow = line.substring(0, 6); // yellow = 1
                String blue = line.substring(0, 4); // blue = 2
                //String green = line.substring (0, 5); // green = 3

                if (yellow.equals("yellow")) {
                    c = 1;
                    // move on to the rest of the line
                    line = line.substring(7);
                } else if (blue.equals("blue")) {
                    c = 2;
                    // move on to the rest of the line
                    line = line.substring(5);
                } else { 
                    c = 3;
                    // move on to the rest of the line
                    line = line.substring(6);
                }

                // posibilities for the rest of the line: a, s, h, A, S, H, @, $, # in various numbers
                String low_a = "a";
                String low_s = "s";
                String low_h = "h";
                String up_a = "A";
                String up_s = "S";
                String up_h = "H";
                String sym_a = "@";
                String sym_s = "$";
                //String sym_h = "#";

                String index0 = line.substring(0, 1);
                if (index0.equals(low_a)) {
                    sym = 1; // a
                    sha = 1; // lowercase
                } else if (index0.equals(low_s)) {
                    sym = 2; // s
                    sha = 1; // lowercase
                } else if (index0.equals(low_h)) {
                    sym = 3; // h
                    sha = 1; // lowercase
                } else if (index0.equals(up_a)) {
                    sym = 1; // a
                    sha = 2; // uppercase
                } else if (index0.equals(up_s)) {
                    sym = 2; // s
                    sha = 2; // uppercase
                } else if (index0.equals(up_h)) {
                    sym = 3; // h
                    sha = 2; // uppercase
                } else if (index0.equals(sym_a)) {
                    sym = 1; // a
                    sha = 3; // symbolcase
                } else if (index0.equals(sym_s)) {
                    sym = 2; // s
                    sha = 3; // symbolcase
                } else { // #
                    sym = 3; // h
                    sha = 3; // symbolcase
                }
                num = line.length();
                // add to result
                Card temp_card = new Card(c, num, sym, sha);
                result.add(temp_card);

                // move to next  line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return result;
    }


    // code by Henrik Warne
    // returns whether or not 3 cards are a set 
    boolean isSet(Card a, Card b, Card c) {
        if (!((a.number == b.number) && (b.number == c.number) || 
                (a.number != b.number) && (a.number != c.number) && (b.number != c.number))) {
            return false;
        }
        if (!((a.symbol == b.symbol) && (b.symbol == c.symbol) ||
                (a.symbol != b.symbol) && (a.symbol != c.symbol) && (b.symbol != c.symbol))) {
            return false;
        }
        if (!((a.shading == b.shading) && (b.shading == c.shading) ||
                (a.shading != b.shading) && (a.shading != c.shading) && (b.shading != c.shading))) {
            return false;
        }
        if (!((a.color == b.color) && (b.color == c.color) ||
                (a.color != b.color) && (a.color != c.color) && (b.color != c.color))) {
            return false;
        }
        return true;
    }

    // code by Henrik Warne
    // this returns an ArrayList containing all the sets (can be used later to find disjoint collection)
    ArrayList<ArrayList<Card>> getAllSets(List<Card> cards) {
        ArrayList<ArrayList<Card>> result = new ArrayList<ArrayList<Card>>();
        if (cards == null) return result;
        int size = cards.size();
        for (int ai = 0; ai < size; ai++) {
            Card a = cards.get(ai);
            for (int bi = ai + 1; bi < size; bi++) {
                Card b = cards.get(bi);
                for (int ci = bi + 1; ci < size; ci++) {
                    Card c = cards.get(ci);
                    if (isSet(a, b, c)) {
                        ArrayList<Card> set = new ArrayList<Card>();
                        set.add(a);
                        set.add(b);
                        set.add(c);
                        result.add(set);
                    }
                }
            }
        }
        return result;
    }

    // entirely my code
    // this returns an ArrayList containing the largest disjoint set
    ArrayList<ArrayList<Card>> largestDisjoint(ArrayList<ArrayList<Card>> cards) {
        ArrayList<ArrayList<Card>> result = new ArrayList<ArrayList<Card>>();
        if (cards == null) return result;

        // while there are still cards
        while (cards.size() != 0) {
            int size = cards.size();
            // iterate through the whole set, find the set that invalidates the least amount of other sets
            int max_disjoint_sets = 0;
            int set_to_be_added = 0;
            for (int index = 0; index < size; index++) {
                // for each set, find the number of other sets it is disjoint with
                int temp_max_disjoint_sets = 0;
                for (int dis = 0; dis < size; dis++) {
                    boolean index_disjoint_dis = isThisDisjoint(cards.get(index), cards.get(dis));
                    if (index_disjoint_dis) { temp_max_disjoint_sets++; }
                }
                // if that number is greater than max_disjoint_sets
                if (temp_max_disjoint_sets > max_disjoint_sets) {
                    // then set_to_be_added = index
                    set_to_be_added = index;
                    max_disjoint_sets = temp_max_disjoint_sets;
                }
            }
            // add it to result and removes it from cards
            ArrayList<Card> to_be_added = cards.get(set_to_be_added);
            result.add(to_be_added);
            // remove the sets that it invalidates form cards
            for (int index = 0; index < cards.size(); index++) {
                if (set_to_be_added == index) { continue; }
                boolean disjointquestionmark = isThisDisjoint(to_be_added, cards.get(index));
                if (!disjointquestionmark) {
                    cards.remove(index);
                    index--;
                }
            }
            cards.remove(to_be_added);
        }
        return result;
    }

    // entirely my code
    // rreturns whether these two sets are disjoint or not
    public boolean isThisDisjoint (ArrayList<Card> set1, ArrayList<Card> set2) {
        Card card0 = set1.get(0);
        Card card1 = set1.get(1);
        Card card2 = set1.get(2);
        
        for (int index = 0; index < 3; index++) {
            if (set2.contains(card0) || set2.contains(card1) || set2.contains(card2)) { return false; }
        }
        return true;
    }

    // entirely my code
    public String toPrint(Card c) {
        int n = c.number;
        int sh = c.shading;
        int sy = c.symbol;
        int co = c.color;
        String result = "";

        // the number of times the symbol appears, 1, 2, or 3
        // symbol: (1) A, (2) S, (3) H
        // shading: (1) lowercase a, s, h; (2) uppercase A, S, H; (3) symbolcase @, $, #
        // color: (1) yellow, (2) blue, (3) green
        String color;

        if (co == 1) {
            color = "yellow";
        } else if (co == 2) {
            color = "blue";
        } else {
            color = "green";
        }

        String addthis = (color + " ");
        result += addthis;

        for (int index = 0; index < n; index++) {
            if (sy == 1 && sh == 1) {
                result += ("a");
            } else if (sy == 1 && sh == 2) {
                result += ("A");
            } else if (sy == 1 && sh == 3) {
                result += ("@");
            } else if (sy == 2 && sh == 1) {
                result += ("s");
            } else if (sy == 2 && sh == 2) {
                result += ("S");
            } else if (sy == 2 && sh == 3) {
                result += ("$");
            } else if (sy == 3 && sh == 1) {
                result += ("h");
            } else if (sy == 3 && sh == 2) {
                result += ("H");
            } else { //if (sy == 3 && sh == 3) {
                result += ("#");
            }
        }
        return result;
    }
}
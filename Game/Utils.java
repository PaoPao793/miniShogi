package Game;

import java.io.*;
import java.util.*;

public class Utils {

    static class InitialPosition {
        String piece;
        String position;

        public InitialPosition(String pc, String pos) {
            piece = pc;
            position = pos;
        }

        public String toString() {
            return piece + " " + position;
        }
    }

    static class TestCase {
        List<InitialPosition> initialPieces;
        List<String> upperCaptures;
        List<String> lowerCaptures;
        List<String> moves;

        public TestCase(List<InitialPosition> ip, List<String> uc, List<String> lc, List<String> m) {
            initialPieces = ip;
            upperCaptures = uc;
            lowerCaptures = lc;
            moves = m;
        }

        public String toString() {
            String str = "";

            str += "initialPieces: [\n";
            for (InitialPosition piece : initialPieces) {
                str += piece + "\n";
            }
            str += "]\n";

            str += "upperCaptures: [";
            for (String piece : upperCaptures) {
                str += piece + " ";
            }
            str += "]\n";

            str += "lowerCaptures: [";
            for (String piece : lowerCaptures) {
                str += piece + " ";
            }
            str += "]\n";

            str += "moves: [\n";
            for (String move : moves) {
                str += move + "\n";
            }
            str += "]";

            return str;
        }
    }

    public static TestCase parseTestCase(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine().trim();
        List<InitialPosition> initialPieces = new ArrayList<InitialPosition>();
        while (!line.equals("")) {
            String[] lineParts = line.split(" ");
            initialPieces.add(new InitialPosition(lineParts[0], lineParts[1]));
            line = br.readLine().trim();
        }

        line = br.readLine().trim();
        List<String> upperCaptures = Arrays.asList(line.substring(1, line.length() - 1).split(" "));
        line = br.readLine().trim();
        List<String> lowerCaptures = Arrays.asList(line.substring(1, line.length() - 1).split(" "));
        line = br.readLine().trim();
        line = br.readLine().trim();
        List<String> moves = new ArrayList<String>();

        while (line != null) {
            line = line.trim();
            moves.add(line);
            line = br.readLine();
        }

        br.close();

        return new TestCase(initialPieces, upperCaptures, lowerCaptures, moves);
    }

    public static Piece createPiece(String s) {
        char c1=' ';
        char c2=' ';
        Piece p;
        if(s.length()==2) {//if piece is promoted{
            c1=s.charAt(1);
            c2=s.charAt(0);
        }
        else if(s.length()==1) {
            c1=s.charAt(0);
        }

        if(Character.isLowerCase(c1)) {
            switch(c1)
            {
                case 'd':
                    p=new King("d"); //remember to set drive
                    break;
                case 'p':
                    p=new Pawn("p");
                    break;
                case 's':
                    p=new GoldGeneral("s");
                    break;
                case 'r':
                    p=new SilverGeneral("r");
                    break;
                case 'g':
                    p=new Bishop("g");
                    break;
                case 'n':
                    p=new Rook("n");
                    break;
                default:
                    return null;
            }
        } else {
            switch(c1) {
                case 'D':
                    p=new King("D"); //remember to set drive
                    break;
                case 'P':
                    p=new Pawn("P");
                    break;
                case 'S':
                    p=new GoldGeneral("S");
                    break;
                case 'R':
                    p=new SilverGeneral("R");
                    break;
                case 'G':
                    p=new Bishop("G");
                    break;
                case 'N':
                    p=new Rook("N");
                    break;
                default:
                    return null;
            }
        }
        
        if(c2!=' ') {
            p.promote();
        }

        return p;
    }

}

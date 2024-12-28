package Game;

import java.util.*;

public class Game {
    private Board board;
    private int moves;
    private boolean isOver;
    private Player lower;
    private Player upper;
    private Player curr_player;
    private Scanner sc;
    private String lastMove;

    // constructor for a Game -> init all the pieces necessary 
    public Game(boolean interactive, String file_path) {
        moves = 0;
        isOver = false;
        lower = new Player("Gote");
        upper = new Player("Sente");
        curr_player = lower;
        if (interactive) {
            board = new Board(true); 
            sc = new Scanner(System.in); 
            runInteractive();
        } else {
            board = new Board();
            runFileMode(file_path);
        }
    }

    private void setOver() { // mark game as ended 
        isOver = true;
    }

    private void runInteractive() {
        while(!isOver) {
            if (moves == 400) {
                System.out.println("Tie game. Too many moves.");
                setOver();
            }

            // print out info 
            System.out.print(board);
            upper.printCaptured();
            lower.printCaptured();
            System.out.println();
            System.out.println();

            if (isInCheck()) { // check if current player is in check situation
                System.out.println(curr_player.getName() + " player is in check!");
                System.out.println("Available moves: ");
                ArrayList<String> avail_moves = create_available_moves();
                for(String s : avail_moves) { // printing available moves
                    System.out.println(s);
                }

                if (avail_moves.size() == 0) {// If available moves = 0 i.e Checkmate
                    Player otherPlayer = get_other_player(curr_player);
                    otherPlayer.printWin("  Checkmate.");
                    setOver();
                    return;
                }
            }

            System.out.print(curr_player.getName() + "> ");
            String input = sc.nextLine();
            if (input.equals(" ")) { // no move made -> illegal
                endGameIllegalMove(lastMove);
                return;
            }

            lastMove = input; // Updating last move, for printing 

            System.out.println((curr_player).getName()+" player action: " + lastMove);
            
            String[] split = input.split(" "); //splits user input to identify type of move and positions
            
            if (split[0].equals("move") && split.length > 2) {
                makeMove(split);
            } else if (split[0].equals("drop") && split.length > 2) {
                dropMove(split);
            } else { // neither --> illegal move 
                System.out.println("NEITHER");
                endGameIllegalMove(lastMove);
                return;
            }

            // change player turns and increment move counter, also if move counter 200 then end
            moves++;
            curr_player = get_other_player(curr_player);
        }
    }

    private void makeMove(String[] split) {
        // check if they want to promote at the end of turn 
        boolean promote = false;
        if(split.length == 4 && split[3].equals("promote")) {
            promote = true;
        }

        String start = split[1];
        String end = split[2];
        Position initialPosition = new Position(start.charAt(0), Character.getNumericValue(start.charAt(1)));
        Position finalPosition = new Position(end.charAt(0), Character.getNumericValue(end.charAt(1)));

        //trying to move piece to same position -> illegal 
        if (initialPosition.equals(finalPosition)) { 
            System.out.println("Attempt to move to same piece");
            endGameIllegalMove(lastMove);
            return;
        }

        // checks if move is on the map and initial position is not empty 
        if(!board.isOnMap(initialPosition) || !board.isOnMap(finalPosition) || !board.isOccupied(initialPosition)) { 
            System.out.println("not on the map or initial empty");
            endGameIllegalMove(lastMove);
            return;
        }

        // If piece to be moved does not belong to current player then illegal
        Piece currPiece = board.getPiece(initialPosition);
        if (!currPiece.belongsTo(curr_player)) {
            this.endGameIllegalMove(lastMove);
            return;
        }

        if (currPiece.canMove(board, initialPosition, finalPosition)) { // If move is legal
            if (promote) { // If trying to promote piece
                if ((curr_player.isPiecePromotable(finalPosition) || curr_player.isPiecePromotable(initialPosition)) && currPiece.canPromote() && !currPiece.isPromoted()) { // Can be promoted if final or initial position in promoted row
                    currPiece.promote();
                } else {
                    endGameIllegalMove(lastMove); // illegal move,trying to promote in not promotable row
                    return;
                }
            }

            if (curr_player.isPiecePromotable(finalPosition) && currPiece instanceof Pawn && !currPiece.isPromoted()) { //Preview piece should be promoted forcibly if in promote row 
                currPiece.promote(); // force a promotion on Preview
            }

            if(!board.isOccupied(finalPosition)) { // if final position not occupied
                board.removePiece(initialPosition);
                board.setPiece(finalPosition, currPiece);
                if(isInCheck()) { // if this move caused check on own player then IT IS ILLEGAL AND GAME OVER
                    board.setPiece(initialPosition, currPiece);
                    board.removePiece(finalPosition);
                    endGameIllegalMove(lastMove);
                }
            } else { // if final position occupied
                Piece end_piece=board.getPiece(finalPosition);
                if(end_piece.belongsTo(curr_player)) { // If final spot contained a piece from same player
                    endGameIllegalMove(lastMove);
                    return;
                }

                end_piece.dePromote(); // handles case if not promoted already
                // Is there an opp? 
                end_piece.getCaptued();
                curr_player.capturePiece(end_piece);
                board.removePiece(finalPosition);
                board.removePiece(initialPosition);
                board.setPiece(finalPosition, currPiece);

                // if this move caused check on own player then IT IS ILLEGAL AND GAME OVER
                if (isInCheck()) {
                    board.setPiece(initialPosition, currPiece);
                    board.setPiece(finalPosition, end_piece);
                    endGameIllegalMove(lastMove);
                }
            }
        } else { // illegal move
            endGameIllegalMove(lastMove);
        }
    }

    private void dropMove(String[] split) {
        Piece to_drop = curr_player.capturedPieces(split[1]);
        if (to_drop == null) {
            endGameIllegalMove(lastMove);
            return;
        }

        Position pos = new Position(split[2].charAt(0),Character.getNumericValue(split[2].charAt(1)));
        if(!board.isOnMap(pos) || board.isOccupied(pos)) {
            endGameIllegalMove(lastMove);
            return;
        }

        // check if position is legal to place dropped piece
        if (to_drop instanceof Pawn) { // -> special circumstances for preview
            if (curr_player.isPiecePromotable(pos)) { // If trying to drop in promotion row
                endGameIllegalMove(lastMove);
                return;
            }

            // Check for existing unpromoted Box Preview pieces in the same column
            for (int row = 0; row < 5; row++) {
                Piece temp = board.getPiece(row, pos.getCol());
                if (temp != null && temp instanceof Pawn && !temp.isPromoted() && temp.belongsTo(curr_player)) {
                    endGameIllegalMove(lastMove);
                    return;
                }
            }

            int temp_col = pos.getRow() - 1;
            while (temp_col >= 0) {
                Piece temp = board.getPiece(pos.getCol(), temp_col);
                if (temp != null && temp.belongsTo(curr_player)&& temp instanceof Pawn && !temp.isPromoted()) {
                    endGameIllegalMove(lastMove);
                    return;
                }
                temp_col--;
            }

            temp_col = pos.getRow() + 1;
            while (temp_col < 5) {
                Piece temp = board.getPiece(pos.getCol(), temp_col);
                if(temp != null && temp.belongsTo(curr_player) && temp instanceof Pawn && !temp.isPromoted()) {
                    endGameIllegalMove(lastMove);
                    return;
                }
                temp_col++;
            }

            // does the preview cause checkmate?
            board.setPiece(pos,to_drop);

            // try moving the player move to check if the enemy got a check
            curr_player = get_other_player(curr_player);
            if (isInCheck() && create_available_moves().size() == 0) {
                board.removePiece(pos);
                curr_player = get_other_player(curr_player);
                endGameIllegalMove(lastMove);
                return;
            }
            curr_player = get_other_player(curr_player);
        } 

        board.setPiece(pos, to_drop);
        curr_player.rmCaptured(to_drop);
    }

    private void runFileMode(String path) {
        try {
            Utils.TestCase temp = Utils.parseTestCase(path);

            // fill in initial positions for pieces from file 
            for (Utils.InitialPosition ipiece: temp.initialPieces) { 
                Piece p = Utils.createPiece(ipiece.piece);
                Position pos =new Position(ipiece.position);
                board.setPiece(pos,p);
            }

            // fill in upper captures
            for (String upper_captured : temp.upperCaptures) { 
                if (upper_captured.equals("")) {
                    break;
                }
                Piece p = Utils.createPiece(upper_captured);
                upper.capturePiece(p);
            }

            // fill in lower captures 
            for (String lower_captured : temp.lowerCaptures) {
                if (lower_captured.equals("")) {
                    break;
                }
                Piece p = Utils.createPiece(lower_captured);
                lower.capturePiece(p);
            }

            // go through moves 
            for (String move : temp.moves) {
                if (moves == 400) {
                    System.out.println("Tie game. Too many moves.");
                    setOver();
                }
                lastMove = move;
                String[] split = move.split(" ");
                if (split[0].equals("move")) {
                    makeMove(split);
                } else if (split[0].equals("drop")) {
                    dropMove(split);
                }

                lastMove = move;
                if (isOver) {
                    return;
                }

                curr_player = get_other_player(curr_player);
                moves++;  //move ends here
            }

            System.out.print(get_other_player(curr_player).getName() + " player action: ");
            printOutStuff();

            if (isInCheck()) {
                ArrayList<String> avail_moves = create_available_moves();

                if (avail_moves.size() == 0) { // last move checkmate check
                    Player other_player = get_other_player(curr_player);
                    other_player.printWin("  Checkmate.");
                    this.setOver();
                    return;
                }

                //If not checkmate then check if moves = 400
                if (moves == 400) {
                    System.out.println("Tie game.  Too many moves.");
                    this.setOver();
                    return;
                } else {
                    System.out.println(curr_player.getName()+" player is in check!");
                    System.out.println("Available moves: ");
                    
                    for(String s : avail_moves) {
                        System.out.println(s);
                    }
                }
            }
            
            if (moves == 400) {
                System.out.println("Tie game.  Too many moves.");
                this.setOver();
            } else {
                System.out.println(curr_player.getName()+">");
            }

        } catch(Exception e) {
            System.out.println("Error with opening filepath");
        }
    }

    private void printOutStuff() {
        System.out.println(lastMove);
        System.out.print(board);
        upper.printCaptured();
        lower.printCaptured();
        System.out.println();
    }

    private boolean isInCheck() { //checks if current player is in check
        Position DrivePosForCurrPlayer = board.findKing(curr_player);
        Player otherPlayer; 
        if (curr_player.getName().equals("UPPER")) {
            otherPlayer = lower;
        } else {
            otherPlayer = upper;
        }

        ArrayList<Position> allTargets = otherPlayer.allPossMoves(board); // all poss moves the other player can make rn 
        return allTargets.contains(DrivePosForCurrPlayer);
    }

    private ArrayList<String> create_available_moves() {
        Set<String> save_moves = new LinkedHashSet<>();
        Position current_drive_pos = board.findKing(curr_player);
        Piece drive = board.getPiece(current_drive_pos);
        Player other_player = get_other_player(curr_player);

        ArrayList<Position> all_targets = other_player.allPossMoves(board); // all the moves the other player can make rn 
        
        // First find where drive can go 
        drive.possibleMoves(board, current_drive_pos);
        ArrayList<Position> drive_moves = drive.allMoves();

        for (Position move : drive_moves) {
            if (!all_targets.contains(move)) { // make it move to where it opp can't get it 
                // move drive to see if it avoids check
                Piece maybe_enemy = board.getPiece(move);
                if (maybe_enemy != null && !(maybe_enemy.belongsTo(curr_player))) { // if position contains enemy
                    Piece enemy_piece = board.getPiece(move);
                    board.setPiece(move, drive);
                    board.removePiece(current_drive_pos);
                    if(!isInCheck()) {
                        String str = "move " + current_drive_pos + " " + move;
                        save_moves.add(str);
                    }
                    board.setPiece(current_drive_pos, drive);
                    board.setPiece(move, enemy_piece);
                } else { // if position is empty then simulate
                    board.setPiece(move, drive);
                    board.removePiece(current_drive_pos);
                    if(!isInCheck()) { // if it would take player out of check -> its a valid move 
                        String str = "move " + current_drive_pos + " " + move;
                        save_moves.add(str);
                    }
                    board.setPiece(current_drive_pos, drive); // move it back and del simulation 
                    board.removePiece(move);
                }
            }
        }

        // For the case when a piece can come in the way of check or capture piece causing check
        for (int i = 0; i < 5; i++) { 
            for (int j = 0; j < 5; j++) {
                Piece pieceTemp = board.getPiece(i,j);
                Position pos = new Position(i,j);
                
                if (pieceTemp != null && pieceTemp.belongsTo(curr_player)) {
                    pieceTemp.possibleMoves(board, pos);
                    ArrayList<Position> allMoves = pieceTemp.allMoves();

                    for (Position move : allMoves) {
                        Piece maybe_enemy = board.getPiece(move);
                        if (maybe_enemy != null && !(maybe_enemy.belongsTo(curr_player))) {
                            Piece enemy_piece = board.getPiece(move);
                            board.setPiece(move, pieceTemp);
                            board.removePiece(move); // changed from pos 

                            if (isInCheck()) {
                                String str = "move " + pos + " " + move;
                                save_moves.add(str);
                            }

                            board.setPiece(pos, pieceTemp);
                            board.setPiece(move, enemy_piece);
                        }

                        // Simulate moving the piece and check for check again
                        if (all_targets.contains(move)) {
                            board.setPiece(move, pieceTemp);
                            board.removePiece(pos);
                            if(!isInCheck()) {
                                String str = "move " + pos + " " + move;
                                save_moves.add(str);
                            }
                            board.setPiece(pos, pieceTemp);
                            board.removePiece(move);
                        }
                    }
                }
            }
        }

        // finding available drop moves
        for(Piece p : curr_player.getCaptured()) { 
            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 5; j++) {
                    Position pos = new Position(i,j);
                    if(!board.isOccupied(pos)) {
                        board.setPiece(pos,p);
                        if(!isInCheck()) {
                            char c=Character.toLowerCase(p.toString().charAt(0));
                            String str = "drop " + c + " " + pos;
                            save_moves.add(str);
                        }
                        board.removePiece(pos);
                    }
                }
            }
        }

        ArrayList<String> ans = new ArrayList<>(save_moves);
        Collections.sort(ans); //to match required case
        return ans;
    }

    private Player get_other_player(Player pl) {
        if (pl.getName().equals("UPPER")) {
            return this.lower;
        } 
        else {
            return this.upper;
        }
    }
    
    private void endGameIllegalMove(String last_move) {
        System.out.print(curr_player.getName() + " player action: ");
        printOutStuff();
        Player other_player = get_other_player(curr_player);
        other_player.printWin("  Illegal move.");
        this.setOver();
    }
}
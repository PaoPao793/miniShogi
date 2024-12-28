# Mini Shogi

- [Game Rules](#game-rules)
   - [Overview](#overview)
   - [Objective](#objective)
   - [The Players](#the-players)
   - [The Pieces](#the-pieces)
   - [The Board](#the-board)
   - [Capturing](#capturing)
   - [Promotion](#promotion)
   - [Drops](#drops)
   - [Game End](#game-end)
- [Game Interface](#game-interface)


##### Running the Game
- First compile the code by running `javac Game/Main.java` from the project root directory
- Then, run `java Game/Main.java -i` or `java Game/Main.java -f` (depending on the mode desired, more of this in ([Game Interface](#game-interface)))


## Game Rules

### Overview

Mini Shogi is a variant of Japanese chess played on a 5x5 board between two players. 

The game has two players, **Gote** and **Sente**. Each player aims to capture their opponent's main piece (King). 


### The Players


The  **Gote** player starts on the bottom side of the board, and their pieces are represented by lower-case characters. This player moves first


The **Sente** player starts on the top side of the board, and their pieces are represented by upper-case characters.


### The Pieces

Each player starts with six pieces, each with different movement capabilities.

The **[King] (d/D)** piece, king in the original game, can move one square in any direction:

The **[ROOK] (n/N)** piece, rook in the original game, can move any number of squares along rows or columns (orthogonal directions):

The **[Bishop] (g/G) piece** piece, bishop in the original game, can move any number of squares along diagonal directions:

The **[Gold General] (s/S)** piece, gold general in the original game, can move one square in any direction except its backward diagonals:

The **[Silver General] (r/R)** piece, silver general in the original game, can move one square in any direction except sideways or directly backward:

A **[Pawn] (p/P)** piece, pawn in the original game, can move one space forward:

*Note: The Rook piece or the Bishop piece cannot jump over pieces in its path of movement.*

### The Board

The board is a grid of 5 rows by 5 columns. We will call each location on the board a *square*.

This is the starting board state:
```
5 | R| B| S| G| K|
4 |__|__|__|__| P|
3 |__|__|__|__|__|
2 | p|__|__|__|__|
1 | k| g| s| b| r|
   a  b  c  d  e
```


### Capturing


A player can capture an opponent's piece by moving their piece onto the same square as an opponent's piece. The captured piece leaves the board, and can be later dropped onto the board by the player who captured it (more on this under *Drops* below). A player cannot capture their own pieces.


### Promotion


A piece may (but does not have to) be **promoted** when it moves into, within, or out of the **promotion zone**.


The promotion zone is the row of the board furthest from each player's starting position:
* For the Gote player, the promotion zone is the top row of the board.
* For the Sente player, the promotion zone is the bottom row of the board.

A piece that has been promoted should gain a plus symbol "+" before its letter showing on the board.

Pieces promote as follows:
* The **King** piece cannot be promoted.
* The **Gold General** piece cannot be promoted.
* The **Promoted Silver General** piece (+r/+R) moves the same way as the **Gold General** piece.
* The **Promoted Bishop** (+g/+G) piece can move like the **Bishop** piece or the **King** piece.
* The **Promoted Rook** piece (+n/+N) can move like the **Rook** piece or the **King** piece.
* The **Promoted Pawn** piece (+p/+P) moves like the **Gold General** piece.

*Note: The Pawn **must** be promoted once they reach the furthest row (otherwise they would not have any legal moves on the next turn).*


### Drops

Pieces that a player has captured can be dropped back onto the board under the capturing player's control. Dropping a piece takes your entire turn.


You cannot drop a piece onto a square that contains another piece.


All dropped pieces must start unpromoted (even if they were captured as promoted pieces and/or are dropped into the promotion zone).


The Pawn piece may not be dropped into the promotion zone or onto a square that results in an immediate checkmate.
* Note: other pieces *can* be dropped into the promotion zone or onto a square that results in an immediate checkmate.


Two unpromoted Pawn pieces may not lie in the same column when they belong to the same player (e.g. If you already have a Pawn piece in the third column, you cannot drop another Pawn piece into that column).


### Game End


#### Move Limit


For simplicity, the game ends in a tie once each player has made 200 moves. When a game ends in a tie, output the message "Tie game.  Too many moves." instead of the move prompt.


#### Checkmate


When a player is in a position where their King piece could be captured on their opponent's next move, they are in **check**.
That player **must** make a move to get out of check by doing one of the following:
* remove their King piece from danger
* capture the piece that threatens their King piece
* put another piece between the King piece and the piece that threatens it


If a player has no moves that they could make to avoid capture, they are in **checkmate** and lose the game.


When a player wins via checkmate, output the message "<Sente/Gote> player wins.  Checkmate." instead of the move prompt.


## Game Interface
There are two ways to play the game: 
- **Interactively**, where the players enter keyboard commands to play moves 
```
$ java Main -i
```

- **Through a file**, where the plays are writted in a file and then passed in. First, write out the game state and then the moves to make. 
```
$ java Main -f <filePath>
```

#### Move Format

**move <from> <to> [promote]**
To move a piece, enter `move` followed by the location of the piece to be moved, the location to move to, and (optionally) the word `promote` if the piece should be promoted at the end of the turn.
* `move a2 a3` moves the piece at square a2 to square a3.
* `move a4 a5 promote` moves the piece at square a4 to square a5 and promotes it at the end of the turn.


**drop <piece> <to>**
To drop a piece, enter `drop` followed by the lowercase character representing the piece to drop and the location to drop the piece.  Pieces are always lower-case, no matter which player is performing the drop.
* `drop s c3` drops a captured **Gold General** piece at square c3.
* `drop g a1` drops a captured **Bishop** piece at square a1.


### File Mode
Some more details about File Mode

**File mode** is very similar to **interactive mode**, except the input can be a partial game.


The input file will contain:
* each piece's current position
* an array of pieces captured by Sente
* an array of pieces captured by Gote
* moves to make with one move per line

Example input (this file begins in the middle of a game and does not complete the game so afterwards, it puts you in interactive mode): 
```
d a1
s b1
r c1
g d1
n e1
p a2
D e5
S d5
R c5
G b5
N a5
P e4


[]
[]


move a2 a3
```

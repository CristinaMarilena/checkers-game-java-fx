# Checkers

## Game Basics

Checkers is played by two players. Each player begins the game with 12 colored discs.
In our game, one piece is white and the other is black. 
Each player places his or her pieces on the 12 dark squares closest to him or her. 
White moves first. Players then alternate moves.

The board consists of 64 squares, alternating between 32 dark and 32 light squares.

A player wins the game when the opponent cannot make a move. 
In most cases, this is because all of the opponent's pieces have been captured.

### Rules of the Game

* Moves are allowed only on the dark squares, so pieces always move diagonally. Single pieces are always limited to forward moves (toward the opponent).
* A piece making a non-capturing move (not involving a jump) may move only one square.
* A piece making a capturing move (a jump) leaps over one of the opponent's pieces, landing in a straight diagonal line on the other side. Only one piece may be captured in a single jump; however, multiple jumps are allowed during a single turn.
* When a piece is captured, it is removed from the board.
* If a player is able to make a capture, there is no option; the jump must be made. If more than one capture is available, the player is free to choose whichever he or she prefers.
* When a piece reaches the furthest row from the player who controls that piece, it is crowned and becomes a king. One of the pieces which had been captured is placed on top of the king so that it is twice as high as a single piece.
* Kings are limited to moving diagonally but may move both forward and backward. (Remember that single pieces, i.e. non-kings, are always limited to forward moves.)
* Kings may combine jumps in several directions, forward and backward, on the same turn. Single pieces may shift direction diagonally during a multiple capture turn, but must always jump forward (toward the opponent).

# Checkers Application

## Technology

The application is implemented in JavaFx.

## Implementation

The _Main_ class of the application is CheckersApp that extends Application from JavaFX.
The main purpose of the class is to load all of the UI resources
including the gameboard and the minimal menu.

_BoardSquare_ class represents a black or white square on the gameboard that may have or not a
pawn on it.

_GameBoard_ class populates the gameboard with the necessary pawns
 and executes all the valid moves when each and every players'r turn comes.
 
 _Pawn_ and _Move_ are simple java models that represent the piece of the game and it's specific move.
 
 _PawnType_ and _MoveType_ are java enumerations to represent different kind of pawns(RED_PAWN, WHITE_PAWN,
 RED_KING, WHITE_KING) and moves(INVALID, STEP, JUMP).
 
 The main difference beetwen the simple red and white pawn is the direction of the movements 
 on the gameboard. 
 
 As for the moves:
 
* INVALID moves means that the piece is trying to be moved in an inaccessible location
          or the player doesn't have a right to.
* STEP is non-capturing move.
* JUMP is the move where a playes captures the other player's pawn.

_GameSearch_ class implements the most primitive algorithm ever invented( :) ) 
that returns a list of all the pawns that have accessible moves on the board, no matter the player ol the selected pawn.
We mainly need that for presenting the status of the game.

## Things to be implemented

* Unit Tests
* Making a player to execute a jump if it is available.
* Present the player the possible moves for the selected pawn.
* Not ending a player's turn until he does all the possible jumps.
* Make a king combine jumps in different directions.

![screenshot from 2018-07-23 05-50-05](https://user-images.githubusercontent.com/23499989/43054918-7fbad950-8e3c-11e8-9d60-b358354dacdd.png)


# NIM-Game


### General Description:
a game board consisting of 4x4 cells, each of which contains a piece of stone.
Players move in turn.
In a move, a player must choose a (non empty) row or column and take away 1, 2, 3, or 4 stones from it.
If 2 or more stones are removed, they must be adjacent. 
The winner of the game is the player who makes the last move
    
### Storing Information
The following information are stored in database (JPA):
the date and time when the game was started,
the name of the players, the number of moves made by the players during the game,
and the name of the winner.

import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
      int strategicMove = -1;
      strategicMove = iCanWin();
      if (strategicMove == -1) 
      {
          strategicMove = theyCanWin();
          if (strategicMove == -1) 
          {
              strategicMove = randomMove();
          }
      }
      moveOnColumn(strategicMove);
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }

    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    
    public int iCanWin()
    {
       return winningMove(iAmRed);
    }
    
    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin()
    {
       return winningMove(!iAmRed);
    }

    /**
     * Returns true if and only if tokenA, tokenB and tokenB are red coloured tokens.
     * 
     * @param tokenA first token that needs checking
     * @param tokenB second token that needs checking
     * @param tokenC third token that needs checking
     * @param redToken the colour of the token
     * 
     * @return true if three red coloured were identified.
     */
    public boolean checkTokenCombination(Connect4Slot tokenA, Connect4Slot tokenB, Connect4Slot tokenC, boolean redToken)
    {
        if(tokenA.getIsFilled() && 
           tokenB.getIsFilled() && 
           tokenC.getIsFilled()) 
        {
               if(tokenA.getIsRed()==redToken && 
                  tokenB.getIsRed()==redToken && 
                  tokenC.getIsRed()==redToken) 
               {
                  return true;
               }
        }
        return false;
    }
    
    /**
     * Returns the position of the token.
     * 
     * @param column the column position of token
     * @param slot the row position of token
     * 
     * @return the Connect4Slot at that position.
     */
    public Connect4Slot tokenPosition(int column, int slot)
    {
        return myGame.getColumn(column).getSlot(slot);
    }
    
    /**
     * Returns the column that would allow anyone to win.
     * 
     * @param myToken the colour of the token
     * 
     * @return the winning column.
     */
    public int winningMove(boolean myToken)
    {
        int totalColumn = myGame.getColumnCount();
        int totalRow = myGame.getRowCount();
        
        for(int col = 0; col < totalColumn; col++) 
        {
            int valid = getLowestEmptyIndex(myGame.getColumn(col));
            if(valid > -1) 
            {
                // Checks for descending diagonal (\), starting column = 0, starting row = 0, empty position = first token
                if(col < totalColumn - 3 && valid < totalRow - 3) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col+1, valid+1),
                     tokenPosition(col+2, valid+2),
                     tokenPosition(col+3, valid+3),
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for descending diagonal (\), starting column = 1, starting row = 1, empty position = second token
                if(col > 0 && col < totalColumn - 2  && valid > 0 && valid < totalRow - 2) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col-1, valid-1),
                     tokenPosition(col+1, valid+1),
                     tokenPosition(col+2, valid+2),
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for descending diagonal (\), starting column = 2, starting row = 2, empty position = third token
                if(col > 1 && col < totalColumn - 1 && valid > 1 && valid < totalRow - 1) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col-2, valid-2),
                     tokenPosition(col-1, valid-1),
                     tokenPosition(col+1, valid+1),
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for descending diagonal (\), starting column = 3, starting row = 3, empty position = fourth token
                if(col > 2 && col < totalColumn && valid > 2 && valid < totalRow) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col-3, valid-3),
                     tokenPosition(col-2, valid-2),
                     tokenPosition(col-1, valid-1),
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for ascending diagonal (/), starting column = 3, starting row = 0, empty position = first token
                if(col > 2 && col < totalColumn && valid >= 0 && valid < totalRow - 3) 
                { 
                    if(checkTokenCombination
                    (tokenPosition(col-3, valid+3),
                     tokenPosition(col-2, valid+2),
                     tokenPosition(col-1, valid+1),
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for ascending diagonal (/), starting column = 2, starting row = 1, empty position = second token
                if(col > 1 && col < totalColumn - 1 && valid > 0 && valid < totalRow - 2) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col-2, valid+2),
                     tokenPosition(col-1, valid+1),
                     tokenPosition(col+1, valid-1),
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for ascending diagonal (/), starting column = 1, starting row = 2, empty position = third token
                if(col > 0 && col < totalColumn - 2 && valid > 1 && valid < totalRow - 1) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col-1, valid+1),
                     tokenPosition(col+1, valid-1),
                     tokenPosition(col+2, valid-2),
                     myToken))
                    {
                        return col;
                    }
                }
                
                // Checks for ascending diagonal (/), starting column = 0, starting row = 3, empty position = fourth token
                if(col >= 0 && col < totalColumn - 3 && valid > 2 && valid < totalRow) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col+1, valid-1),
                    tokenPosition(col+2, valid-2),
                    tokenPosition(col+3, valid-3), 
                    myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for vertical combinations, starting row = 0
                if(valid < totalRow - 3) 
                { 
                    
                    if(myGame.getColumn(col).getSlot(valid+1).getIsRed()==myToken 
                    && myGame.getColumn(col).getSlot(valid+2).getIsRed()==myToken
                    && myGame.getColumn(col).getSlot(valid+3).getIsRed()==myToken) 
                    {
                        return col;
                    }
                }
                
                // Checks for horizontal combinations, starting column = 0, empty position = second token [X.0.0.0]
                if(col < totalColumn - 3) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col+1, valid), 
                     tokenPosition(col+2, valid), 
                     tokenPosition(col+3, valid), 
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for horizontal combinations, starting column = 1, empty position = second token [0.X.0.0]
                if(col > 0 && col < totalColumn - 2) 
                { 
                    if(checkTokenCombination
                    (tokenPosition(col-1, valid),
                     tokenPosition(col+1, valid),
                     tokenPosition(col+2, valid),
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for horizontal combinations, starting column = 2, empty position = third token [0.0.X.0]
                if(col > 1 && col < totalColumn - 1) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col-2, valid),
                     tokenPosition(col-1, valid),
                     tokenPosition(col+1, valid),
                     myToken)) 
                    {
                        return col;
                    }
                }
                
                // Checks for horizontal combinations, starting column = 3, empty position = fourth token [0.0.0.X]
                if(col > 2) 
                {
                    if(checkTokenCombination
                    (tokenPosition(col-3, valid),
                     tokenPosition(col-2, valid),
                     tokenPosition(col-1, valid),
                     myToken)) 
                    {
                        return col;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "Firdause Rocks!";
    }
}

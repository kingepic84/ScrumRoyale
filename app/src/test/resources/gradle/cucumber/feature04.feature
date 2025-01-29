Feature: Play the Phase 4 version of the game.
  
  The user should be able to play the Phase 4 version of the game.


  Scenario: The user runs the program in "phase4" mode.
    When the program is run as "phase4"
    Then the game log should be accurate
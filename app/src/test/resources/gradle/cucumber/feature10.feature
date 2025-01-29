Feature: Play the Phase 10 version of the game.
  
  The user should be able to play the Phase 10 version of the game.


  Scenario: The user runs the program in "phase10" mode.
    When the program is run as "phase10"
    Then the game log should be accurate
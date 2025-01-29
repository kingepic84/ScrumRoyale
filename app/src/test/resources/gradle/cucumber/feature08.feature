Feature: Play the Phase 8 version of the game.
  
  The user should be able to play the Phase 8 version of the game.


  Scenario: The user runs the program in "phase8" mode.
    When the program is run as "phase8"
    Then the game log should be accurate
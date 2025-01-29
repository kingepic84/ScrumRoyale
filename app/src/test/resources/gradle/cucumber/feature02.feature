Feature: Play the Phase 2 version of the game.
  
  The user should be able to play the Phase 2 version of the game.


  Scenario: The user runs the program in "phase2" mode.
    When the program is run as "phase2"
    Then the game log should be accurate
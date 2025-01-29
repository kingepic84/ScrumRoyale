Feature: Play the Phase 0 version of the game.
  
  The user should be able to play the Phase 0 version of the game.


  Scenario: The user runs the program in "phase0" mode.
    When the program is run as "phase0"
    Then the game log should be accurate
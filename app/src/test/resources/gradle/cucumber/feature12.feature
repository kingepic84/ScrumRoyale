Feature: Play the Phase 12 version of the game.
  
  The user should be able to play the Phase 12 version of the game.


  Scenario: The user runs the program in "phase12" mode.
    When the program is run as "phase12"
    Then the game log should be accurate
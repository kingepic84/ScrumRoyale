Feature: Play the Phase 6 version of the game.
  
  The user should be able to play the Phase 6 version of the game.


  Scenario: The user runs the program in "phase6" mode.
    When the program is run as "phase6"
    Then the game log should be accurate
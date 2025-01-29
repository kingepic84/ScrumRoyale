Feature: Play the Phase 7 version of the game.
  
  The user should be able to play the Phase 7 version of the game.


  Scenario: The user runs the program in "phase7" mode.
    When the program is run as "phase7"
    Then the game log should be accurate
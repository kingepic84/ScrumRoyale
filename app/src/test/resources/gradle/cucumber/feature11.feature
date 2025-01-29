Feature: Play the Phase 11 version of the game.
  
  The user should be able to play the Phase 11 version of the game.


  Scenario: The user runs the program in "phase11" mode.
    When the program is run as "phase11"
    Then the game log should be accurate
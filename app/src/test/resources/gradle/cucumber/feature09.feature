Feature: Play the Phase 9 version of the game.
  
  The user should be able to play the Phase 9 version of the game.


  Scenario: The user runs the program in "phase9" mode.
    When the program is run as "phase9"
    Then the game log should be accurate
Feature: Play the Phase 5 version of the game.
  
  The user should be able to play the Phase 5 version of the game.


  Scenario: The user runs the program in "phase5" mode.
    When the program is run as "phase5"
    Then the game log should be accurate
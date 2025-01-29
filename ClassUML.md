```mermaid
classDiagram
    class Vegas {
        +getCurrentPlayer() int
    }

    class Player {
    }

    class Casino {
    }

    class Dice {
    }

    class Die {
    }

    class Dealer {

    }

    class ConsoleUI {

    }
    
    class RNG {
    }

    class Tuple {
    }

    Vegas "1" --> "many" Player : "has"
    Vegas "1" --> "many" Casino : "has"
    Vegas "1" --> "1" ConsoleUI : "has"
    Vegas "1" --> "1" Dealer : "has"

    Player "1" --> "1" Dice : "has"
    Casino "1" --> "1" Dice : "has"
    
    Dice "1" --> "1" Die: "has"

# Unite Centrale

L'unite centrale (UC) permet a la station sol (*Ground Station Control* : (GCS)) de communiquer efficacement avec l'autopilote (AP).

# Overview

La GCS envoit une commande sous le format :

    {
        "id" : int,
        "x" : float,
        "y" : float,
        "z" : float,
        "buttonLeftPressed" : boolean,
        "buttonRightPressed" : boolean,
        "buttonTopPressed" : boolean,
        "buttonBotPressed" : boolean,
        "isNewPos" : boolean
    }

L'UC se charge alors de reformater le paquet pour le transmettre a l'autopilote (plus precisement l'APMessager), elle envoit donc :

    {
        "id" : int,
        "command" : string,
        "metadata" : {
            "x" : float,
            "y" : float,
            "z" : float
        }
    }

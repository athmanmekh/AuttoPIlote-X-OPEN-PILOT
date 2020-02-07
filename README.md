# AutoPilote X OPEN PROJECT DRONE
----

#Overview
L'Autopilote permet d'intègrer la communication avec la GCS (station sol), il permet de calculer la trajectoire (en continue même si le drone est stable) à partir des paquet provenant des capteurs (position actuel) et les paquets provenant du la GCS (destination).

Dans un premier temps, l'autopilote se met dans un état d'attente.
L'autopilote attends alors de recevoir une commande depuis l'unité centrale (UC),
il peut recevoir les commande suivantes:
* GOTO : une position (x,y,z) est alors donné en plus de la commande afin de déplacer le drone a ces coordonees.
* FORWARD : si le drone n'est pas trop proche d'un obstacle, alors le drone s'avancera
* BACKWARD : si le drone n'est pas trop proche d'un obstacle, alors le drone reculera
* LEFT : si le drone n'est pas trop proche d'un obstacle, alors le drone se deplacera vers la gauche
* RIGHT : si le drone n'est pas trop proche d'un obstacle, alors le drone se deplacera vers la droit
* UP : si le drone n'est pas trop proche d'un obstacle, alors le drone montera
* DOWN : si le drone n'est pas trop proche d'un obstacle, alors le drone descendra
* Peut-etre d'autres commandes dans le futur ?

Une fois la commande lue, l'autopilote agira de la manière suivante:
1. On récupères les infos des capteurs (seuelement ceux nécessaire au bon fonctionnement de la commande).
2. L'autopilote calcule ensuite les commandes a executer (s'avancer, reculer, monter, descendre ...).
3. L'autopilote depose ensuite les commandes, que la maquette doit executer, sur le bus.
4. Si les conditions de la commande sont completee, alors l'autopilote repasse dans l'état d'attente initial.

#Implementation

*A chaque attribut sera associé un getter et setter*

----
## class "APMessager"
### Attributes
- class AP

### Methods
* JSONObject sendMSG()
* void getMSG()
* void

## class "AP" (AutoPilote)
### Attributes
- class Capteur #1
- class Capteur #2
- class Capteur #3 ... (le nombre de capteurs nécessaire quoi)

### Methods
* void computeMSG()
* JSONObject createMSG()

## class "Capteur"
### Attributes
*Tout ces attributs sont des tableaux d'une taille fixée a la création des classes filles (plus de détails dans le segment "Capteurs")*
- float[] data
- float[] target
- float[] diff

### Methods
*avant d'utiliser ces fonctions il faut avoir remplis au moins le champs data et (target ou diff)*

* void computeDifference()

#Capteurs
Chaque classe capteur hérite de la classe "Capteur" composé de 3 champs :
- data : groupement de données. Par exemple : la coordonée, l'altitude ou l'assiette
- target  les meme donnees que pour la data, seulement ces valeurs sont celles que l'ont cherche a obtenir
- diff : encore les meme donnees mais celle-ci servent a savoir la difference (positive ou negative) entre data et target

La taille de ces champs sera initialisé dependant le composant, comme vous pourrez le remarquer dans les classes du meme package. Ces classes capteurs seront invoques par la classe AP en fonction de la commande a execute.


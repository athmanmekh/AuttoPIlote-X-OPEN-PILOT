# AutoPilote X OPEN PROJECT DRONE 

*A chaque attribut sera associé un getter et setter*

----
# class "APMessager"
## Attributes
- class AP

## Methods
* JSONObject sendMSG()
* void getMSG()
* void

----
# interface/abstract "AP" (AutoPilote)
## Attributes
- class Capteur#1
- class Capteur#2
- class Capteur#3 ... (le nombre de capteurs nécessaire quoi)

## Methods
* void computeMSG()
* JSONObject createMSG()

*plutot que de faire ces deux méthodes on peut avoir __APMessager__ qui recupere les infos des classes "capteurs" *
Et du coup pour la classe capteur on peut faire une classe mere qui ressemblerait à ça :

----
# class "Capteur"
## Attributes
- data : groupement de données. Par exemple la coordonée, l'altitude ou l'assiette
- target : les meme donnees que pour la data, seulement ces valeurs sont celles que l'ont cherche a obtenir
- diff : encore les meme donnees mais celle-ci servent a savoir la difference (positive ou negative) entre data et target

## Methods
*avant d'utiliser ces fonctions il faut avoir remplis au moins le champs data et (target ou diff)*

* void computeDifference()

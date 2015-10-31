#!/bin/bash
echo "---------Starting Script---------"
echo
echo "---------Running kNN---------"

java kNN 3 lenses.training lenses.testing
java KNN 3 crx.training.processed crx.testing.processed

java kNN 5 lenses.training lenses.testing
java KNN 10 crx.training.processed crx.testing.processed
echo "---------FINISHED kNN---------"
clear
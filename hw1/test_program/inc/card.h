#ifndef CARD_H
#define CARD_H

#include <string>
#include <iostream>

class Card
{
public:
    int num, suit;

    Card(int _num = 0, int _suit = 0): num(_num), suit(_suit) { }
    std::string toString();

    bool operator<(const Card& c) const;
    bool operator==(const Card& c) const;
    bool operator!=(const Card& c) const;
};

#endif

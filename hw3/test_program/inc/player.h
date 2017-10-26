#ifndef PLAYER_H
#define PLAYER_H

#include <set>

#include "card.h"

class Player
{
public:
    int id;
    std::set<Card> card;

    Player() { }
    void drop();
    bool compare_card(const std::set<Card>& c);
    bool remove_card(const Card& c);
    bool add_card(const Card& c);
};

#endif

#include <set>
#include <vector>

#include "player.h"
#include "card.h"

using namespace std;

void Player::drop()
{
    vector<Card> tmp(card.begin(), card.end());

    card.clear();
    for( int i = 0; i < (int)tmp.size(); i++ )
    {
        if( i + 1 < (int)tmp.size() && tmp[i].num == tmp[i + 1].num && tmp[i].num != 0 ) i++;
        else card.insert(tmp[i]);
    }
    return;
}

bool Player::compare_card(const set<Card>& c)
{
    if( card.size() != c.size() ) return false;

    for( set<Card>::const_iterator it1 = card.begin(), it2 = c.begin(); it1 != card.end() && it2 != c.end(); ++it1, ++it2 )
        if( (*it1) != (*it2) )
            return false;
    return true;
}

bool Player::remove_card(const Card& c)
{
    if( card.count(c) == 0 ) return false;
    card.erase(c);
    return true;
}

bool Player::add_card(const Card& c)
{
    if( card.count(c) != 0 ) return false;
    card.insert(c);
    return true;
}

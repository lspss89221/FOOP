#include <string>
#include "player.h"
#include "card.h"

using namespace std;

extern string num_table[15];
extern string suit_table[6];

string Card::toString()
{
    return suit_table[suit] + num_table[num];
}

bool Card::operator<(const Card& c) const
{
    if( num != c.num ) return (num < c.num);
    return (suit < c.suit);
}

bool Card::operator==(const Card& c) const
{
    return (num == c.num && suit == c.suit);
}

bool Card::operator!=(const Card& c) const
{
    return !((*this) == c);
}

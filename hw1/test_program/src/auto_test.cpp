#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <iostream>
#include <set>
#include <map>
#include <vector>
#include <string>
#include <algorithm>

#include "auto_test.h"
#include "message.h"
#include "card.h"
#include "player.h"
#include "table.h"

using namespace std;

template<class T>
bool compare_vec(const vector<T>& v1, const vector<T>& v2)
{
    if( v1.size() != v2.size() ) return false;
    for( int i = 0; i < (int)v1.size(); i++ )
        if( v1[i] != v2[i] ) return false;
    return true;
}

void CheckPlayerCard(Message& msg, Player* players, int i)
{
    vector<Card> get, get2;
    if( !msg.ParsePlayerCardInfoMessage(i, get) )
        msg.error("(Drop card) Player" + to_string(i) + " card info format error");

    get2 = get;
    sort(get2.begin(), get2.end());
    if( !compare_vec(get, get2) )
        msg.error("(Drop card) Player" + to_string(i) + " card info error: wrong card order");

    if( !players[i].compare_card(set<Card>(get2.begin(), get2.end())) )
        msg.error("(Drop card) Player" + to_string(i) + " drop card process error");
}

vector<int> CheckWinner(Player* players, int F, int S)
{
    vector<int> ret;
    if( players[F].card.size() == 0 ) ret.push_back(F);
    if( players[S].card.size() == 0 ) ret.push_back(S);
    return ret;
}

int findNext(Player* players, int idx)
{
    while( players[idx].card.size() == 0 ) idx = (idx + 1) % 4;
    return idx;
}

int main()
{
    Message msg;

    msg.input();
    init_table();

    // Deal cards
    if( !msg.hasNext() || !msg.equal("Deal cards") )
        msg.error("Message \"Deal cards\" missed");

    Player players[4];
    set<Card> deck;

    for( int i = 0; i < 4; i++ )
    {
        if( !msg.hasNext() )
            msg.error("(Deal card) Player" + to_string(i) + " card info missed");

        vector<Card> get, get2;
        if( !msg.ParsePlayerCardInfoMessage(i, get) )
            msg.error("(Deal card) Player" + to_string(i) + " card info format error");

        get2 = get;
        sort(get2.begin(), get2.end());
        if( !compare_vec(get, get2) )
            msg.error("(Deal card) Player" + to_string(i) + " card info error: wrong card order");

        players[i].id = i;
        players[i].card = set<Card>(get.begin(), get.end());

        if( !((i <= 1 && players[i].card.size() == 14) || (i >= 2 && players[i].card.size() == 13)) )
            msg.error("(Deal card) Player" + to_string(i) + " card info error: number of cards is not 14 or 13");

        for( auto it = players[i].card.begin(); it != players[i].card.end(); ++it ) deck.insert(*it);
    }

    if( deck.size() != 54 )
        msg.error("(Deal card) Duplicate card");

    // Drop cards
    if( !msg.hasNext() || !msg.equal("Drop cards") )
        msg.error("Message \"Drop cards\" missed");

    for( int i = 0; i < 4; i++ )
        players[i].drop();

    for( int i = 0; i < 4; i++ )
    {
        if( !msg.hasNext() )
            msg.error("(Drop card) Player" + to_string(i) + " card info missed");

        CheckPlayerCard(msg, players, i);
    }

    // Game start
    if( !msg.hasNext() || !msg.equal("Game start") )
        msg.error("Message \"Game start\" missed");

    // Basic game
    int curr = 0, next = 1;
    vector<int> winner = CheckWinner(players, curr, next);
    while( winner.size() == 0 )
    {
        if( !msg.hasNext() )
            msg.error("(Basic game) message missed");
        next = (curr + 1) % 4;

        Card take;
        if( !msg.ParseDrawCardMessage(curr, next, take) )
            msg.error("(Basic game) Draw card error (format error or player id error)");

        if( !players[next].remove_card(take) )
            msg.error("(Basic game) Draw card error: Player" + to_string(next) + " doesn't have card " + take.toString());

        if( !players[curr].add_card(take) )
            msg.error("(Basic game) Draw card error: Player" + to_string(curr) + " already has card " + take.toString());

        players[curr].drop();

        if( !msg.hasNext() )
            msg.error("(Basic game) Miss player info after drawing");
        CheckPlayerCard(msg, players, curr);

        if( !msg.hasNext() )
            msg.error("(Basic game) Miss player info after drawing");
        CheckPlayerCard(msg, players, next);

        winner = CheckWinner(players, curr, next);
        curr = (curr + 1) % 4;
    }

    if( !msg.hasNext() )
        msg.error("Winner(s) message missed");

    if( !msg.CheckWinnerMessage(winner) )
        msg.error("Winner(s) message error");

    // Basic game over
    if( !msg.hasNext() || !msg.equal("Basic game over") )
        msg.error("Message \"Basic game over\" missed");

    cout << "PASS basic game test" << endl;

    // Continue
    if( !msg.hasNext() || !msg.equal("Continue") )
        msg.error("Message \"Continue\" missed");

    // Bonus game
    curr = findNext(players, curr);
    next = findNext(players, (curr + 1) % 4);
    while( curr != next )
    {
        if( !msg.hasNext() )
            msg.error("(Bonus game) message missed");

        Card take;
        if( !msg.ParseDrawCardMessage(curr, next, take) )
            msg.error("(Bonus game) Draw card error (format error or player id error)");

        if( !players[next].remove_card(take) )
            msg.error("(Bonus game) Draw card error: Player" + to_string(next) + " doesn't have card " + take.toString());

        if( !players[curr].add_card(take) )
            msg.error("(Bonus game) Draw card error: Player" + to_string(curr) + " already has card " + take.toString());

        players[curr].drop();

        if( !msg.hasNext() )
            msg.error("(Bonus game) Miss player info after drawing");
        CheckPlayerCard(msg, players, curr);

        if( !msg.hasNext() )
            msg.error("(Bonus game) Miss player info after drawing");
        CheckPlayerCard(msg, players, next);

        winner = CheckWinner(players, curr, next);
        if( winner.size() != 0 )
        {
            if( !msg.hasNext() )
            msg.error("Winner(s) message missed");

            if( !msg.CheckWinnerMessage(winner) )
                msg.error("Winner(s) message error");
        }

        curr = findNext(players, next);
        next = findNext(players, (curr + 1) % 4);
    }

    // Bonus game over
    if( !msg.hasNext() || !msg.equal("Bonus game over") )
        msg.error("Message \"Bonus game over\" missed");

    cout << "PASS bonus game test" << endl;

    if( msg.hasNext() )
        msg.error("Output Limited Exceeded");

    cout << endl;

    return 0;
}

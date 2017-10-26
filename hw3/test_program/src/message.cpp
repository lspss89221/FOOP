#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cassert>
#include <algorithm>
#include <iostream>
#include <string>
#include <vector>
#include <unistd.h>

#include "card.h"
#include "message.h"
#include "table.h"

using namespace std;

extern map<char, int> suit_inv_table;
extern map<string, int> num_inv_table;

void Message::input()
{
    string in;
    buf.clear();
    all = 0;
    line_num = -1;
    while( getline(cin, in) )
    {
        buf.push_back(in);
        ++all;
    }
    return;
}

bool Message::hasNext()
{
    ++line_num;
    return (line_num < all);
}

void Message::error(const string& strErr)
{
    cout << "[Line " << line_num + 1 << "]: ";
    cout << strErr << endl;

    FILE* fp = fopen("output.txt", "w");
    for( int i = 0; i < all; i++ )
        fprintf(fp, "%s\n", buf[i].c_str());
    fclose(fp);
    exit(1);
}

bool Message::equal(const string& str)
{
    return (str == buf[line_num]);
}

bool Message::ParsePlayerCardInfoMessage(const int& id, vector<Card>& cvec)
{
    static char tmp[7122];
    char *ptr;

    cvec.clear();
    strcpy(tmp, buf[line_num].c_str());
    ptr = strtok(tmp, " ");

    if( ptr == NULL || string(ptr) != "Player" + to_string(id) + ":" )
        return false;

    ptr = strtok(NULL, " ");
    while( ptr != NULL )
    {
        int len = strlen(ptr);
        if( !(len >= 1 && len <= 3) )
            return false;

        string num_str = string(ptr + 1, ptr + len);
        if( num_inv_table.count(num_str) == 0 || suit_inv_table.count(ptr[0]) == 0 )
            return false;

        cvec.push_back(Card(num_inv_table[num_str], suit_inv_table[ptr[0]]));
        ptr = strtok(NULL, " ");
    }

    return true;
}

bool Message::ParseDrawCardMessage(const int& F, const int& S, Card& take)
{
    string p1, p2, mid, card_str;
    if( buf[line_num].size() != 36 && buf[line_num].size() != 37 )
        return false;

    p1 = buf[line_num].substr(0, 7);
    mid = buf[line_num].substr(7, 19);
    p2 = buf[line_num].substr(26, 7);
    card_str = buf[line_num].substr(34, buf[line_num].size() == 37 ? 3 : 2);

    if( mid != " draws a card from " || p1.substr(0, 6) != "Player" || p2.substr(0, 6) != "Player" )
        return false;

    if( F != p1[6] - '0' || S != p2[6] - '0' )
        return false;

    if( num_inv_table.count(card_str.substr(1, card_str.size() - 1)) == 0 || suit_inv_table.count(card_str[0]) == 0 )
        return false;

    take = Card(num_inv_table[card_str.substr(1, card_str.size() - 1)], suit_inv_table[card_str[0]]);
    return true;
}

bool Message::CheckWinnerMessage(vector<int>& winner)
{
    string p1, mid, p2, ed;
    assert(winner.size() > 0 && winner.size() <= 2);
    if( winner.size() == 1 )
    {
        if( buf[line_num].size() != 12 ) return false;
        p1 = buf[line_num].substr(0, 7);
        ed = buf[line_num].substr(7, 5);
        return (ed == " wins" && p1.substr(0, 6) == "Player" && winner[0] == p1[6] - '0');
    }
    else
    {
        if( buf[line_num].size() != 23 ) return false;
        if( winner[0] > winner[1] ) swap(winner[0], winner[1]);
        p1 = buf[line_num].substr(0, 7);
        mid = buf[line_num].substr(7, 5);
        p2 = buf[line_num].substr(12, 7);
        ed = buf[line_num].substr(19, 4);
        return (mid == " and " && ed == " win" && p1.substr(0, 6) == "Player" && p2.substr(0, 6) == "Player" &&
                winner[0] == p1[6] - '0' && winner[1] == p2[6] - '0');
    }
}

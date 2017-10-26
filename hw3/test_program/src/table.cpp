#include <map>
#include "table.h"

using namespace std;

map<char, int> suit_inv_table;
map<string, int> num_inv_table;
string num_table[15] = {"0", ".", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
string suit_table[6] = {"R", "B", "C", "D", "H", "S"};

void init_table()
{
    suit_inv_table['R'] = 0;
    suit_inv_table['B'] = 1;
    suit_inv_table['C'] = 2;
    suit_inv_table['D'] = 3;
    suit_inv_table['H'] = 4;
    suit_inv_table['S'] = 5;

    num_inv_table["0"] = 0;
    num_inv_table["2"] = 2;
    num_inv_table["3"] = 3;
    num_inv_table["4"] = 4;
    num_inv_table["5"] = 5;
    num_inv_table["6"] = 6;
    num_inv_table["7"] = 7;
    num_inv_table["8"] = 8;
    num_inv_table["9"] = 9;
    num_inv_table["10"] = 10;
    num_inv_table["J"] = 11;
    num_inv_table["Q"] = 12;
    num_inv_table["K"] = 13;
    num_inv_table["A"] = 14;

    return;
}

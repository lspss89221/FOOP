#ifndef MESSAGE_H
#define MESSAGE_H

#include <vector>
#include <string>

#include "card.h"

class Message
{
public:
    int line_num, all;
    std::vector<std::string> buf;

    Message(): line_num(0), all(0) { }

    void input();
    bool hasNext();
    void error(const std::string& strErr);
    bool equal(const std::string& str);
    bool ParsePlayerCardInfoMessage(const int& id, std::vector<Card>& cvec);
    bool ParseDrawCardMessage(const int& F, const int& S, Card& take);
    bool CheckWinnerMessage(std::vector<int>& winner);
};

#endif

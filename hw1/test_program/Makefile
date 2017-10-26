TARGET = auto_test

BIN = bin
SRC = src
INC = inc
OBJ = obj

TEST_OBJS = message.o table.o card.o player.o auto_test.o

OBJS = $(foreach n, $(TEST_OBJS), $(OBJ)/$(n))

CXX_LIB =
CXXFLAGS = -Wall -O2 -Iinc -std=c++11
CXX = g++

.PHONY: all clean

############################################################

all: $(foreach n, $(TARGET), $(BIN)/$(n)) Makefile

clean:
	-@rm -rf bin/* obj/*

############################################################

$(BIN)/auto_test: $(foreach n, $(TEST_OBJS), $(OBJ)/$(n))
	@echo '   CXX       $@'
	@$(CXX) $^ $(CXXFLAGS) $(CXX_LIB) $(DEF) -o $@


############################################################

$(OBJS): $(OBJ)/%.o: $(SRC)/%.cpp $(INC)/%.h
	@echo '   CXX       $@'
	@$(CXX) $< $(CXXFLAGS) $(CXX_LIB) $(DEF) -c -o $@

make
javac PlayGame.java
while [ 1 ];
do
    java PlayGame | ./bin/auto_test
    if [ "$?" -ne "0" ]; then
        exit 1
    fi
done

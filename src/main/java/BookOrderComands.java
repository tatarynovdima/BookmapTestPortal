package main.java;

import java.io.IOException;

public interface BookOrderComands {
    void proceedOrder(String str);
    void proceedUpdate(String str);
    void proceedQuery(String str) throws IOException;

    void getBestBid() throws IOException;

    void getBestAsk() throws IOException;
    void removeBestBid(int num);

    void removeBestAsk(int num);
}
package org.example;

import java.util.*;

public class OrderBook {
    private TreeMap<Integer, Integer> bidOrders;  // TreeMap to store bid orders with price as key
    private TreeMap<Integer, Integer> askOrders;  // TreeMap to store ask orders with price as key

    public OrderBook() {
        bidOrders = new TreeMap<>(Collections.reverseOrder());  // Highest bid price first
        askOrders = new TreeMap<>();  // Lowest ask price first
    }

    public void updateOrder(String[] orderParams) {
        int price = Integer.parseInt(orderParams[1]);
        int size = Integer.parseInt(orderParams[2]);

        if (orderParams[3].equals("bid")) {
            bidOrders.put(price, size);
        } else if (orderParams[3].equals("ask")) {
            askOrders.put(price, size);
        }
        // Ensure bid prices are lower than ask prices
        validateOrderBook();
    }
    private void validateOrderBook() {
        if (!bidOrders.isEmpty() && !askOrders.isEmpty()) {
            int highestBidPrice = bidOrders.firstKey();
            int lowestAskPrice = askOrders.firstKey();
            if (highestBidPrice >= lowestAskPrice) {
                throw new IllegalStateException("Invalid order book: Bid price is higher than or equal to ask price.");
            }
        }
    }

    public String queryBestBid() {
        if (!bidOrders.isEmpty()) {
            Map.Entry<Integer, Integer> bestBid = bidOrders.firstEntry();
            return bestBid.getKey() + "," + bestBid.getValue();
        }
        return "";
    }

    public String querySizeAtPrice(int price) {
        int bidSize = bidOrders.getOrDefault(price, 0);
        int askSize = askOrders.getOrDefault(price, 0);
        if (bidSize > 0 || askSize > 0) {
            return  bidSize + "\n" + askSize;
        }
        return "spread";
    }

    public void processBuyOrder(int size) {
        while (size > 0 && !askOrders.isEmpty()) {
            Map.Entry<Integer, Integer> lowestAsk = askOrders.firstEntry();
            int askPrice = lowestAsk.getKey();
            int askSize = lowestAsk.getValue();

            if (askSize <= size) {
                size -= askSize;
                askOrders.remove(askPrice);
            } else {
                askOrders.put(askPrice, askSize - size);
                size = 0;
            }
        }
    }

    public void processSellOrder(int size) {
        while (size > 0 && !bidOrders.isEmpty()) {
            Map.Entry<Integer, Integer> highestBid = bidOrders.firstEntry();
            int bidPrice = highestBid.getKey();
            int bidSize = highestBid.getValue();

            if (bidSize <= size) {
                size -= bidSize;
                bidOrders.remove(bidPrice);
            } else {
                bidOrders.put(bidPrice, bidSize - size);
                size = 0;
            }
        }
    }
}

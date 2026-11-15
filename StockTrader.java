// Stock Trader Case Study
//
// Part 2: lowest and highest prices
// File                  Lowest price  When      Highest price When
// tiny.txt              $30.00        Mar 2020  $70.00        Jul 2020
// rising.txt            $20.00        Jan 2010  $48.14        Dec 2014
// falling.txt           $30.36        Dec 2014  $100.00       Jan 2010
// zigzag.txt            $35.00        Jul 2010  $65.00        Mar 2010
// aapl_2000_2010.txt    $7.07         Mar 2003  $223.02       Mar 2010
// amzn_2000_2010.txt    $5.97         Sep 2001  $135.91       Nov 2009
// msft_2000_2010.txt    $15.81        Feb 2009  $43.22        Mar 2000
// ibm_2000_2010.txt     $53.01        Sep 2002  $130.32       Dec 2009
// goog_2004_2010.txt    $102.37       Aug 2004  $707.00       Oct 2007
// sbux_2007_2016.txt    $4.09         Nov 2008  $62.16        Oct 2015
// sp500_1925_1945.txt   $4.77         Jun 1932  $31.30        Sep 1929
// sp500_1990_2025.txt   $307.12       Oct 1990  $6853.03      Dec 2025
//
// Part 2 questions
// 1. In msft_2000_2010.txt the highest price ($43.22, Mar 2000) is before the lowest price ($15.81, Feb 2009). So if you bought Microsoft in 2000 you bought basically at the top and then it dropped about 63% over the next 9 years. Even in the last month of the file it was $28.80, which is still less than the $39.81 it started at in Jan 2000. So after 10 years you'd still be losing money. Buy and hold finished at $7,237.50.
// 2. The lowest month for sbux_2007_2016.txt is Nov 2008, which is right in the middle of the 2008 financial crisis (Lehman Brothers went bankrupt in Sep 2008 and the whole stock market crashed). The lowest month for sp500_1925_1945.txt is Jun 1932, which is near the bottom of the Great Depression after the 1929 crash. The highest month in that file is Sep 1929, right before the crash, and the price dropped about 85% from there. So yes, the timing makes sense for both.
//
// Step E: final values
// File                  Buy and hold   Dip buyer      Moving average My strategy
// tiny.txt              $12,975.00     $14,975.00     $10,000.00     $10,000.00
// rising.txt            $24,031.86     $10,000.00     $20,411.92     $21,007.72
// falling.txt           $3,095.64      $3,159.28      $10,000.00     $10,000.00
// zigzag.txt            $12,101.39     $38,071.99     $9,848.78      $9,848.78
// aapl_2000_2010.txt    $85,865.80     $36,610.49     $114,231.31    $95,997.06
// amzn_2000_2010.txt    $19,886.04     $14,192.52     $38,170.87     $48,881.28
// msft_2000_2010.txt    $7,237.50      $49,273.15     $7,208.23      $9,062.98
// ibm_2000_2010.txt     $12,467.97     $27,998.86     $7,549.75      $9,553.66
// goog_2004_2010.txt    $54,398.54     $13,655.41     $18,706.20     $22,187.52
// sbux_2007_2016.txt    $36,280.60     $15,759.50     $67,500.23     $56,302.18
// sp500_1925_1945.txt   $16,362.00     $11,483.45     $37,195.81     $44,756.46
// sp500_1990_2025.txt   $198,868.74    $40,836.68     $199,113.67    $227,663.54
//
// Step E questions
// 1. The dip buyer was the best on the most files (4: tiny, zigzag, msft, ibm). My strategy was best on 3 (amzn, sp500_1925_1945, sp500_1990_2025), the moving average on 2 (aapl, sbux), and buy and hold on 2 (rising, goog). On falling.txt the moving average and my strategy tied at $10,000.00 since they both just stayed in cash. No strategy was the best on every file.
// 2. The dip buyer did really badly on aapl_2000_2010.txt ($36,610.49 vs $85,865.80 for buy and hold). It bought in May 2000 at $21.00 and sold in Jun 2000 at $26.19 because of the 10% rule, and it kept doing quick trades like that. In Jul 2003 it sold at $10.54 and didn't buy again until Apr 2005 at $36.06, so it sat in cash while Apple went way up. Selling at only 10% cuts the winners short, and it only buys after two down months, so it can miss a long climb. Every trade also costs $10.
// 3. The moving average beat buy and hold on amzn_2000_2010.txt ($38,170.87 vs $19,886.04). Amazon dropped from $64.56 in Jan 2000 down to $5.97 in Sep 2001 (the dot-com crash) before coming back up to $128.82. Buy and hold just rode the whole drop. The moving average stayed in cash until Jan 2002, bought at $14.19 after the crash, and sold at $43.01 in Feb 2004.
// 4. My strategy beat buy and hold on 7 of the 12 files (falling, aapl, amzn, msft, sbux, and both S&P 500 files). It lost on rising and goog, where the price just kept going up, because it buys late and sells on small dips. It also lost on zigzag and ibm, where the price keeps crossing back and forth over the average, and on tiny.txt where it never trades since there are only 8 months. I didn't tune it for just one file. I tried averages from 6 to 12 months on all the files and picked 10 because it did best overall. It beat my 12-month version on 7 files, was worse on 2 (aapl, sbux), and tied on 3. But I picked it after looking at these same files, so a real test would be new stocks it hasn't seen.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("tiny.txt"));
        String symbol = in.next();
        int n = in.nextInt();

        int[] years = new int[n];
        int[] months = new int[n];
        double[] prices = new double[n];

        loadData(in, years, months, prices);
        printData(years, months, prices);

        int lowest = indexOfLowest(prices);
        int highest = indexOfHighest(prices);
        System.out.printf("%s lowest:  $%.2f in %s %d%n", symbol, prices[lowest],
                monthName(months[lowest]), years[lowest]);
        System.out.printf("%s highest: $%.2f in %s %d%n", symbol, prices[highest],
                monthName(months[highest]), years[highest]);

        double result = simulate(years, months, prices);
        System.out.printf("Final value: $%,.2f%n", result);
    }

    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
    }

    public static String monthName(int month) {
        String[] names = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return names[month - 1];
    }

    public static void printData(int[] years, int[] months, double[] prices) {
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("%s %d: $%.2f%n", monthName(months[i]), years[i], prices[i]);
        }
    }

    public static int indexOfLowest(double[] prices) {
        int lowest = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < prices[lowest]) {
                lowest = i;
            }
        }
        return lowest;
    }

    public static int indexOfHighest(double[] prices) {
        int highest = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[highest]) {
                highest = i;
            }
        }
        return highest;
    }

    public static final double START_CASH = 10000.0;
    public static final double FEE = 10.0;

    public static double simulate(int[] years, int[] months, double[] prices) {
        double cash = START_CASH;
        int shares = 0;
        double boughtAt = 0.0;

        for (int i = 0; i < prices.length; i++) {
            double[] history = Arrays.copyOf(prices, i + 1);
            String action = decide(history, shares > 0, boughtAt);

            if (action.equals("BUY") && shares == 0) {
                int canBuy = (int) ((cash - FEE) / prices[i]);
                if (canBuy > 0) {
                    shares = canBuy;
                    cash = cash - shares * prices[i] - FEE;
                    boughtAt = prices[i];
                    System.out.printf("%s %d: BUY %d shares at $%.2f%n",
                            monthName(months[i]), years[i], shares, prices[i]);
                }
            } else if (action.equals("SELL") && shares > 0) {
                cash = cash + shares * prices[i] - FEE;
                System.out.printf("%s %d: SELL %d shares at $%.2f%n",
                        monthName(months[i]), years[i], shares, prices[i]);
                shares = 0;
                boughtAt = 0.0;
            }
        }
        return cash + shares * prices[prices.length - 1];
    }

    public static String decide(double[] history, boolean holding, double boughtAt) {
        return buyAndHold(history, holding, boughtAt);
    }

    public static String buyAndHold(double[] history, boolean holding, double boughtAt) {
        return "BUY";
    }

    public static String dipBuyer(double[] history, boolean holding, double boughtAt) {
        int n = history.length;
        double today = history[n - 1];

        if (!holding) {
            if (n >= 3 && today < history[n - 2] && history[n - 2] < history[n - 3]) {
                return "BUY";
            }
        } else if (today >= boughtAt * 1.10) {
            return "SELL";
        }
        return "HOLD";
    }

    public static String movingAverage(double[] history, boolean holding, double boughtAt) {
        if (history.length < 12) {
            return "HOLD";
        }
        double today = history[history.length - 1];
        double average = averageOfLast(history, 12);

        if (!holding && today > average) {
            return "BUY";
        }
        if (holding && today < average) {
            return "SELL";
        }
        return "HOLD";
    }

    public static double averageOfLast(double[] history, int count) {
        double sum = 0.0;
        for (int i = history.length - count; i < history.length; i++) {
            sum += history[i];
        }
        return sum / count;
    }

    // Rules: if there are fewer than 10 prices, hold. If I don't own shares and today's price is above the average of the last 10 months, buy. If I own shares and today's price is below that average, sell. Otherwise hold.
    // Why: the average shows the trend. Price above its average means the stock is going up, so I want to own it. Price below its average means it's going down, so I'd rather be in cash and dodge the crash. I used 10 months instead of 12 because it reacts a little sooner, and when I tried both, 10 did better overall.
    public static String myStrategy(double[] history, boolean holding, double boughtAt) {
        if (history.length < 10) {
            return "HOLD";
        }
        double today = history[history.length - 1];
        double average = averageOfLast(history, 10);

        if (!holding && today > average) {
            return "BUY";
        }
        if (holding && today < average) {
            return "SELL";
        }
        return "HOLD";
    }
}

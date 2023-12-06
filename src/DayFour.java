import java.util.*;
import java.util.regex.*;

/**
 * https://adventofcode.com/2023/day/4
 */
public class DayFour {
    // Example line
    // Card   1: 24 12 26 39 19 98 74 16 82 77 | 80 11 51  1 74 60 77 68 42 35 39 78 21 12 29 19 25 98 65 91 33 17 59 24 31

    public static final Pattern PATTERN = Pattern.compile("Card\\s+(\\d+):\\s+(.*)");

    public static void main(String[] args) {
        List<String> lines = FileUtils.readInputFile("resources/scratchcards.txt");
        List<Card> cards = parseLinesToCards(lines);
        System.out.println("total points from scratch card is : " + calculateTotalPoints(cards));
    }

    private static double calculateTotalPoints(List<Card> cards) {
        return cards.stream()
                .map(DayFour::calculatePoints)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private static List<Card> parseLinesToCards(List<String> lines) {
        List<Card> cards = new ArrayList<>();

        for(String line : lines) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.find()) {
                String cardId = matcher.group(1);
                String numbers = matcher.group(2);

                String[] numSets = numbers.split("\\|");
                List<Integer> winningNums = parseNumbers(numSets[0]);
                List<Integer> actualNums = parseNumbers(numSets[1]);

                Card card = new Card(cardId);
                card.winningNums = winningNums;
                card.actualNums = actualNums;

                cards.add(card);
            }
        }
        return cards;
    }

    private static double calculatePoints(Card card) {
        long count = getMatchingNumberCount(card);
        return count > 0 ? Math.pow(2, count - 1) : 0;
    }

    private static long getMatchingNumberCount(Card card) {
        List<Integer> winningNums = card.winningNums;
        List<Integer> actualNums = card.actualNums;
        return winningNums.stream()
                .filter(actualNums::contains)
                .count();
    }

    private static List<Integer> parseNumbers(String numbers) {
        return Arrays.stream(numbers.trim().split("\\s+"))
                .map(Integer::valueOf)
                .toList();
    }
}

class Card{
    String cardId;
    List<Integer> winningNums;
    List<Integer> actualNums;

    int count;

    List<String> wonCopyCards;

    public Card(String cardId) {
        this.cardId = cardId;
        this.winningNums = new ArrayList<>();
        this.actualNums = new ArrayList<>();
    }
}

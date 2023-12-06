import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

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
        System.out.println("total points from scratch card is : " + totalWonCards(cards));
    }

    private static List<Card> parseLinesToCards(List<String> lines) {
        List<Card> cards = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.find()) {
                Integer cardId = Integer.parseInt(matcher.group(1));
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

    // Part 1
    private static double calculateTotalPoints(List<Card> cards) {
        return cards.stream()
                .map(DayFour::calculatePoints)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private static double calculatePoints(Card card) {
        long count =card.getMatchingNumberCount();
        return count > 0 ? Math.pow(2, count - 1) : 0;
    }

    private static List<Integer> parseNumbers(String numbers) {
        return Arrays.stream(numbers.trim().split("\\s+"))
                .map(Integer::valueOf)
                .toList();
    }

    // Part 2
    // Getting part 2 to work -> to try later : store the cardIds won for each card and count using that
    private static int totalWonCards(List<Card> cards) {
        Map<Integer, Integer> cardCountMap = cards.stream()
                .collect(
                        Collectors.toMap(c -> c.cardId, c -> 1));

        for (Card card : cards) {
            int count = (int) card.getMatchingNumberCount();
            int currentCardCount = cardCountMap.computeIfAbsent(card.cardId, c -> 1);

            // For each card won add currentCardCount
            IntStream.rangeClosed(1, count)
                    .mapToObj(id -> card.cardId + id)
                    .forEach(wonCardId ->
                            cardCountMap.merge(wonCardId, currentCardCount, Integer::sum));
        }

        return cardCountMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}

class Card{
    Integer cardId;
    List<Integer> winningNums;
    List<Integer> actualNums;

    public Card(Integer cardId) {
        this.cardId = cardId;
        this.winningNums = new ArrayList<>();
        this.actualNums = new ArrayList<>();
    }

    public long getMatchingNumberCount() {
        List<Integer> winningNums = this.winningNums;
        List<Integer> actualNums = this.actualNums;
        return winningNums.stream()
                .filter(actualNums::contains)
                .count();
    }

}

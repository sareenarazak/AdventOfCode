import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class DayThree {

    public static int ROW_SIZE;
    public static int COL_SIZE;

    public static void main(String[] args) {
        List<String> lines = FileUtils.readInputFile("resources/engineparts.txt");
        List<Symbol> symbols = parseLinesToSymbols(lines);
        List<NumberAndRange> numberAndRanges = parseLinesToNumberRanges(lines);
        ROW_SIZE = lines.size();
        COL_SIZE = lines.get(0).length();

        System.out.println(getSumOfParts1(numberAndRanges, symbols));
        System.out.println(getSumOfParts2(numberAndRanges, symbols));

    }

    private static int getSumOfParts1(List<NumberAndRange> numberAndRanges, List<Symbol> symbols) {
        return numberAndRanges.stream()
                    .filter(nr -> nr.hasSymbolNeighbor(symbols))
                    .mapToInt(NumberAndRange::getNumber)
                    .sum();
    }

    private static int getSumOfParts2(List<NumberAndRange> numberAndRanges, List<Symbol> symbols) {
        return symbols.stream()
                .filter(s -> s.symbol == '*')
                .mapToInt(star -> calculateProduct(star, numberAndRanges))
                .sum();
    }
    private static int calculateProduct(Symbol star, List<NumberAndRange> numberAndRanges) {
        List<NumberAndRange> touchingNumbers = numberAndRanges.stream()
                .filter(star::isNeighborToNum)
                .toList();

        return (touchingNumbers.size() == 2) ?
                touchingNumbers.get(0).getNumber() * touchingNumbers.get(1).getNumber() :
                0;
    }

    private static List<Symbol> parseLinesToSymbols(List<String> lines) {
        Pattern validSymbolPattern = Pattern.compile("[^\\d.]");
        return IntStream.range(0, lines.size())
                .boxed()
                .flatMap(row ->
                        validSymbolPattern.matcher(lines.get(row)).results()
                                .map(matchResult ->
                                        new Symbol(matchResult.group().charAt(0), row, matchResult.start())))
                .toList();
    }

    private static List<NumberAndRange> parseLinesToNumberRanges(List<String> lines) {
        return IntStream.range(0, lines.size())
                .mapToObj(rowIndex -> parseToNumberWithRange(lines.get(rowIndex), rowIndex))
                .flatMap(List::stream)
                .toList();
    }

    private static List<NumberAndRange> parseToNumberWithRange(String line, int index) {
        Pattern numPattern = Pattern.compile("\\d+");
        Matcher matcher = numPattern.matcher(line);
        List<NumberAndRange> numbers = new ArrayList<>();

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end() - 1;
            Integer number = Integer.valueOf(matcher.group());
            NumberAndRange numRange = new NumberAndRange(number, index, start, end);
            numbers.add(numRange);
        }

        return numbers;
    }
}

class Range {
    int rowIndex;
    int colStart;
    int colEnd;

    public Range(int rowIndex, int colStart, int colEnd) {
        this.rowIndex = rowIndex;
        this.colStart = colStart;
        this.colEnd = colEnd;
    }
}
class NumberAndRange {
    private final int number;
    private final Range range;
    public NumberAndRange(int number, int rowIndex, int start, int end) {
        this.number = number;
        this.range = new Range(rowIndex,start,end);
    }

    public int getNumber() {
        return this.number;
    }

    public boolean hasSymbolNeighbor(List<Symbol> symbols) {
        List<Range> neighborCells = getNeighborRanges();
        return neighborCells.stream()
                .anyMatch(range ->
                        symbols.stream()
                                .anyMatch(s -> s.isInRange(range)));
    }

    public List<Range> getNeighborRanges() {
        List<Range> neighbors = new ArrayList<>();
        int start = Math.max(0, range.colStart - 1);
        int end = Math.min(range.colEnd + 1, DayThree.COL_SIZE - 1);
        int rowIndex = range.rowIndex;
        // above
        if (rowIndex > 0) {
            Range rangeAbove = new Range(rowIndex - 1, start, end);
            neighbors.add(rangeAbove);
        }

        if (rowIndex < DayThree.ROW_SIZE - 1) {
            Range rangeBelow = new Range(rowIndex + 1, start, end);
            neighbors.add(rangeBelow);
        }
        // same line
        if (start > 0) neighbors.add(new Range(rowIndex, start, start ));
        if (end < DayThree.COL_SIZE - 1) neighbors.add(new Range(rowIndex, end, end));

        return neighbors;
    }

    @Override
    public String toString() {
        return "Number: " + number + ", Row Index: " + range.rowIndex + ", Start: " + range.colStart + ", End: " + range.colEnd;
    }
}

class Symbol {
    public final char symbol;
    private final int[] pos;
    Set<NumberAndRange> numberAndRangeSet;

    public Symbol(char symbol, int row, int col) {
        this.symbol = symbol;
        this.pos = new int[]{row, col};
        numberAndRangeSet = new HashSet<>();
    }

    public boolean isNeighborToNum(NumberAndRange numberAndRange) {
        return numberAndRange.getNeighborRanges().stream()
                .anyMatch(this::isInRange);
    }
    public boolean isInRange(Range range) {
        return range.rowIndex == pos[0] && range.colStart <= pos[1] && range.colEnd >= pos[1];
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/2
 */
public class DayTwo {

    public static void  main(String[] args) {
        List<String> lines = FileUtils.readInputFile("resources/games.txt");

        List<Game> games = parseLinesToGames(lines);

        //part 1
        System.out.println("Sum of possible games values is " + possibleGameIdSums(games));

        // part 2
        System.out.println("sum of power of cubes " + calculatePowerOfCubesSum(games));

       /* System.out.println(getGameId("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"));
        System.out.println(getGameRounds("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"));
        System.out.println(possibleGameIdSums(List.of("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")));*/
    }

    private static int possibleGameIdSums(List<Game> games) {
        return games.stream()
                .filter(Game::isGamePossible)
                .mapToInt(Game::getGameId)
                .sum();
    }

    private static Integer calculatePowerOfCubesSum(List<Game> games) {
        return games.stream()
                .mapToInt(Game::calculatePowerOfCubes)
                .sum();
    }

    private static List<Game> parseLinesToGames(List<String> lines) {
        List<Game> games = new ArrayList<>();
        for(String line : lines) {
            int gameId = parseGameId(line);
            Game game = new Game(gameId);
            List<String> gameRounds = parseGameRounds(line);
            populateColorMaxCountMap(gameRounds, game.getColorMaxCountMap());

            games.add(game);
        }
        return games;
    }

    private static int parseGameId(String input) {
        String gameIdString = input.split(":")[0];
        return Integer.parseInt(gameIdString.split(" ")[1]);
    }

    private static List<String> parseGameRounds(String input) {
        String gameRounds = input.split(":")[1];
        return Arrays.asList(gameRounds.split(";"));
    }

    private static void populateColorMaxCountMap(List<String> rounds, Map<String, Integer> colorMaxCountMap) {
        for(String round : rounds) {
            String[] countAndColors = round.split(",");

            for(String countColor : countAndColors) {
                String[] splitCountColor = countColor.trim().split(" ");

                Integer count = Integer.valueOf(splitCountColor[0]);
                String color = splitCountColor[1];

                colorMaxCountMap.put(color, Math.max(count, colorMaxCountMap.getOrDefault(color, 0)));
            }
        }
    }
}

class Game {
    private static final Map<String, Integer> COLOR_COUNT_MAP = Map.of(
            "red", 12,
            "green", 13,
            "blue", 14);

    private int gameId;
    private Map<String, Integer> colorMaxCountMap;

    public Game(int gameId) {
        this.gameId = gameId;
        this.colorMaxCountMap = new HashMap<>();
    }

    public int getGameId() {
        return this.gameId;
    }

    public Map<String, Integer> getColorMaxCountMap() {
        return this.colorMaxCountMap;
    }
    public int calculatePowerOfCubes() {
       return  this.colorMaxCountMap.values()
               .stream()
               .reduce(1, (a, b) -> a * b);
    }

    public boolean isGamePossible() {
        return this.colorMaxCountMap.entrySet().stream()
                .allMatch(e -> e.getValue() <= COLOR_COUNT_MAP.getOrDefault(e.getKey(), 0));
    }

    @Override
    public String toString() {
        return "GameId: " + gameId + " " + colorMaxCountMap.toString();
    }
}
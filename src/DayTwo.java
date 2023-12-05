import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/2
 */
public class DayTwo {
    public static final Map<String, Integer> COLOR_COUNT_MAP = Map.of("red", 12, "green", 13, "blue", 14);

    public static void  main(String[] args) {

        List<String> lines = readInputFile("resources/games.txt");
        //part 1
        int sum = possibleGameIdSums(lines);
        System.out.println("Sum of possible games values is " + sum);

        // part 2
        System.out.println("sum of power of cubes " + powerOfCubes(lines));

       /* System.out.println(getGameId("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"));
        System.out.println(getGameRounds("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"));
        System.out.println(possibleGameIdSums(List.of("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")));*/
    }

    private static List<String> readInputFile(String filePath) {
        Path currentWorkingDir = Paths.get(System.getProperty("user.dir"));

        Path path = currentWorkingDir.resolve(filePath);
        try {
            return Files.readAllLines(path);
        } catch (IOException ex) {
            throw new RuntimeException("Could not read text file at " + path);
        }
    }

    private static Integer powerOfCubes(List<String> lines) {
        List<Game> games = createGamesFromLine(lines);
        int powerSum = 0;
        for(Game game : games) {
            powerSum += game.colorMaxCountMap.values().stream().reduce(1, (a, b) -> a * b);
        }
        return powerSum;
    }

    private static int possibleGameIdSums(List<String> lines) {
        List<Game> games = createGamesFromLine(lines);
        int sum = 0;

        for(Game game : games) {
            if(isPossible(game)) sum += game.gameId;
        }

        return sum;
    }

    private static boolean isPossible(Game game) {
        for (Map.Entry<String, Integer> colorAndCount : game.colorMaxCountMap.entrySet()) {
              String color = colorAndCount.getKey();
              Integer count = colorAndCount.getValue();
              if(count > COLOR_COUNT_MAP.get(color)) return false;
        }
        return true;
    }

    private static List<Game> createGamesFromLine(List<String> lines) {
        List<Game> games = new ArrayList<>();
        for(String line : lines) {
            int gameId = getGameId(line);
            Game game = new Game(gameId);

            createColorMaxCountMap(getGameRounds(line), game.colorMaxCountMap);

            games.add(game);
        }
        return games;
    }

    private static int getGameId(String input) {
        String gameIdString = input.split(":")[0];
        return Integer.parseInt(gameIdString.split(" ")[1]);
    }

    private static List<String> getGameRounds(String input) {
        String gameRounds = input.split(":")[1];
        return Arrays.asList(gameRounds.split(";"));
    }

    private static void createColorMaxCountMap(List<String> rounds, Map<String, Integer> colorMaxCountMap) {
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
    public int gameId;
    public Map<String, Integer> colorMaxCountMap;

    public Game(int gameId) {
        this.gameId = gameId;
        this.colorMaxCountMap = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameId : ").append(this.gameId);
        sb.append(colorMaxCountMap.toString());
        return sb.toString();
    }
}
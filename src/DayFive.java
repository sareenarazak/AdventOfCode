import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * https://adventofcode.com/2023/day/5
 * Brute force - so many loopa
 * is there a better algo
 *
 */
public class DayFive {
    public static void main(String[] args) {
        //  System.out.println(parseSeedIds("seeds: 79 14 55 13"));
        String almanacDataString = FileUtils.readFileToString("resources/almanac.txt");
        System.out.println("minimum location values is :  " + getMinLocationNumber(almanacDataString));
    }


    private static Long getMinLocationNumber(String fileInputData) {

        AlmanacData almanacData = parseAlmanacData(fileInputData);
        Long minLocationId = Long.MAX_VALUE;

        for (Long seed : almanacData.seedIds) {
            Long currentId = seed;

            //Go through the maps in order of mapNames list
            for (int g = 0; g < almanacData.mapNames.size(); g++) {
                String currentMapName = almanacData.mapNames.get(g);
                List<RangeData> rangeDataList = almanacData.mapNameToRangeDataMap.getOrDefault(currentMapName, List.of());

                for (RangeData rangeData : rangeDataList) {
                    // if It is in range get the nextId for next map
                    // if not in range Id remains the same
                    if (rangeData.isInSourceRange(currentId)) {
                        currentId = rangeData.dest + (currentId - rangeData.source);
                        break;
                    }
                }
            }
            minLocationId = Math.min(minLocationId, currentId);
        }
        return minLocationId;
    }

    private static AlmanacData parseAlmanacData(String almanacDataString) {
        Pattern groupsPattern = Pattern.compile("\n\n");
        String[] dataGroups = groupsPattern.split(almanacDataString);

        List<Long> seedIds = parseSeedIds(dataGroups[0]);

        AlmanacData almanacData = new AlmanacData(seedIds);

        for (int i = 1; i < dataGroups.length; i++) {
            String[] dataForGroup = dataGroups[i].split("\n");

            String groupName = dataForGroup[0].split(" ")[0];

            almanacData.mapNames.add(i - 1, groupName);

            List<RangeData> rangeDataForGroup = new ArrayList<>();

            for (int j = 1; j < dataForGroup.length; j++) {
                String[] rangeDataArray = dataForGroup[j].split("\\s+");

                RangeData rangeData = new RangeData(
                        Long.parseLong(rangeDataArray[1]),
                        Long.parseLong(rangeDataArray[0]),
                        Long.parseLong(rangeDataArray[2]));

                rangeDataForGroup.add(rangeData);
            }
            almanacData.mapNameToRangeDataMap.put(groupName, rangeDataForGroup);
        }
        return almanacData;

    }

    private static List<Long> parseSeedIds(String seedLine) {
        Pattern seedPattern = Pattern.compile("seeds:\\s+([\\d\\s]+)");
        Matcher seedMatcher = seedPattern.matcher(seedLine);

        return seedMatcher.find() ?
                Stream.of(seedMatcher.group(1).split("\\s+"))
                        .map(Long::valueOf)
                        .toList() : List.of();
    }
}

class AlmanacData {
    List<Long> seedIds;
    List<String> mapNames;
    Map<String, List<RangeData>> mapNameToRangeDataMap;

    public AlmanacData(List<Long> seedIds) {
        this.seedIds = seedIds;
        this.mapNameToRangeDataMap = new HashMap<>();
        this.mapNames = new ArrayList<>();
    }
}
class RangeData {
    Long source;
    Long dest;
    Long range;
    public RangeData(Long source, Long dest, Long range) {
        this.source = source;
        this.dest = dest;
        this.range = range;
    }
    public boolean isInSourceRange(Long number) {
        return this.source <= number && this.source + range - 1 >= number;
    }
}
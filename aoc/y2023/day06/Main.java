import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static class Record {
        long time;
        long distance;

        Record(long time, long distance) {
            this.time = time;
            this.distance = distance;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<ArrayList<String>> nums = parseNums(in);
        System.out.println("Part 1: " + solvePart1(nums));
        System.out.println("Part 2: " + solvePart2(nums));
    }

    static long solvePart1(ArrayList<ArrayList<String>> nums) {
        ArrayList<Record> records = toRecordsPart1(nums);
        long ans = 1;
        for (Record record : records) {
            ans *= solve(record);
        }
        return ans;
    }

    static long solvePart2(ArrayList<ArrayList<String>> nums) {
        Record record = toRecordPart2(nums);
        long ans = solve(record);
        return ans;
    }

    static long solve(Record record) {
        long vBest = getBestStartSpeed(record.time);
        long vLow = getLowestWinningSpeed(record.time, record.distance, 1, vBest + 1);
        long vHigh = getHighestWinningSpeed(record.time, record.distance, vBest, record.time);
        return vHigh - vLow + 1;
    }

    static long getDistance(long v, long t) {
        return v * (t - v);
    }

    static long getBestStartSpeed(long t) {
        // Find best start speed v, i.e. maximize the getDistance function, which is quadratic.
        long vLow = 1;
        long vHigh = t;

        while (vLow < vHigh) {
            long vMid = (vLow + vHigh) / 2;
            if (getDistance(vMid, t) > getDistance(vMid + 1, t)) {
                vHigh = vMid;
            } else {
                vLow = vMid + 1;
            }
        }

        return vLow;
    }

    static long getLowestWinningSpeed(long t, long dRecord, long vLow, long vHigh) {
        // Find the lowest winning speed on the monotonically decreasing interval [vLow, vHigh)
        // of the distance function.
        long ans = 0;
        while (vLow < vHigh) {
            long vMid = vLow + (vHigh - vLow) / 2;
            if (getDistance(vMid, t) > dRecord) {
                ans = vMid;
                vHigh = vMid;
            } else {
                vLow = vMid + 1;
            }
        }
        if (ans == -1) {
            throw new RuntimeException();
        }
        return ans;
    }

    static long getHighestWinningSpeed(long t, long dRecord, long vLow, long vHigh) {
        // Find the highest winning speed on the monotonically increasing interval [vLow, vHigh)
        // of the distance function.
        long ans = 0;
        while (vLow < vHigh) {
            long vMid = vLow + (vHigh - vLow) / 2;
            if (getDistance(vMid, t) > dRecord) {
                ans = vMid;
                vLow = vMid + 1;
            } else {
                vHigh = vMid;
            }
        }
        if (ans == -1) {
            throw new RuntimeException();
        }
        return ans;
    }

    static ArrayList<ArrayList<String>> parseNums(Scanner in) {
        ArrayList<ArrayList<String>> nums = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            Pattern pattern = Pattern.compile("(\\d+)");
            Matcher matcher = pattern.matcher(line);
            ArrayList<String> lineNums = new ArrayList<>();
            while (matcher.find()) {
                lineNums.add(matcher.group(1));
            }
            nums.add(lineNums);
        }
        return nums;
    }

    static ArrayList<Record> toRecordsPart1(ArrayList<ArrayList<String>> nums) {
        ArrayList<Record> records = new ArrayList<>();
        for (int i = 0; i < nums.get(0).size(); i++) {
            long time = Long.parseLong(nums.get(0).get(i));
            long distance = Long.parseLong(nums.get(1).get(i));
            records.add(new Record(time, distance));
        }
        return records;
    }

    static Record toRecordPart2(ArrayList<ArrayList<String>> nums) {
        StringBuilder time = new StringBuilder();
        StringBuilder distance = new StringBuilder();
        for (int i = 0; i < nums.get(0).size(); i++) {
            time.append(nums.get(0).get(i));
            distance.append(nums.get(1).get(i));
        }
        return new Record(
            Long.parseLong(time.toString()),
            Long.parseLong(distance.toString())
        );
    }
}

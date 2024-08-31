import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.lang.Character;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        char[][] grid = parseGrid(in);
        ArrayList<Num> nums = extractNums(grid);
        System.out.println("Part 1: " + solvePart1(grid, nums));
        System.out.println("Part 2: " + solvePart2(grid, nums));
    }

    static int solvePart1(char[][] grid, ArrayList<Num> nums) {
        int ans = 0;
        for (Num num : nums) {
            if (getAdjacentSymbols(grid, num).size() > 0) {
                ans += num.val;
            }
        }
        return ans;
    }

    static int solvePart2(char[][] grid, ArrayList<Num> nums) {
        TreeMap<Symbol, ArrayList<Num>> adjacentNumsOfGear = new TreeMap<>(
                (symbol1, symbol2) -> {
                    if (symbol1.row == symbol2.row) {
                        return Integer.compare(symbol1.col, symbol2.col);
                    }
                    return Integer.compare(symbol1.row, symbol2.row);
                });
        for (Num num : nums) {
            ArrayList<Symbol> adjacentSymbols = getAdjacentSymbols(grid, num);
            for (Symbol symbol : adjacentSymbols) {
                if (symbol.chr != '*') {
                    continue;
                }
                if (adjacentNumsOfGear.get(symbol) == null) {
                    adjacentNumsOfGear.put(symbol, new ArrayList<>());
                }
                adjacentNumsOfGear.get(symbol).add(num);
            }
        }
        int ans = 0;
        for (Map.Entry<Symbol, ArrayList<Num>> entry : adjacentNumsOfGear.entrySet()) {
            ArrayList<Num> adjacentNums = entry.getValue();
            if (adjacentNums.size() == 2) {
                ans += adjacentNums.get(0).val * adjacentNums.get(1).val;
            }
        }
        return ans;
    }

    static char[][] parseGrid(Scanner in) {
        ArrayList<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            lines.add(in.nextLine());
        }
        char[][] grid = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    static ArrayList<Symbol> getAdjacentSymbols(char[][] grid, Num num) {
        ArrayList<Symbol> symbols = new ArrayList<>();
        int height = grid.length;
        int width = grid[0].length;
        // left
        if (num.startCol - 1 >= 0 && grid[num.row][num.startCol - 1] != '.') {
            symbols.add(parseSymbol(grid, num.row, num.startCol - 1));
        }
        // right
        if (num.endCol + 1 < width && grid[num.row][num.endCol + 1] != '.') {
            symbols.add(parseSymbol(grid, num.row, num.endCol + 1));
        }
        // top
        if (num.row > 0) {
            int firstCol = Math.max(0, num.startCol - 1);
            int lastCol = Math.min(num.endCol + 1, width - 1);
            for (int col = firstCol; col <= lastCol; col++) {
                if (grid[num.row - 1][col] != '.') {
                    symbols.add(parseSymbol(grid, num.row - 1, col));
                }
            }
        }
        // bottom
        if (num.row < height - 1) {
            int firstCol = Math.max(0, num.startCol - 1);
            int lastCol = Math.min(num.endCol + 1, width - 1);
            for (int col = firstCol; col <= lastCol; col++) {
                if (grid[num.row + 1][col] != '.') {
                    symbols.add(parseSymbol(grid, num.row + 1, col));
                }
            }
        }
        return symbols;
    }

    static ArrayList<Num> extractNums(char[][] grid) {
        ArrayList<Num> nums = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            int startCol = -1;
            for (int col = 0; col < grid[0].length; col++) {
                char chr = grid[row][col];
                if (!Character.isDigit(chr)) {
                    if (startCol != -1) {
                        nums.add(parseNum(grid, row, startCol, col - 1));
                        startCol = -1;
                    }
                } else if (startCol == -1) {
                    startCol = col;
                }
            }
            if (startCol != -1) {
                nums.add(parseNum(grid, row, startCol, grid[0].length - 1));
            }
        }
        return nums;
    }

    static Num parseNum(char[][] grid, int row, int startCol, int endCol) {
        int val = 0;
        for (int col = startCol; col <= endCol; col++) {
            val *= 10;
            val += grid[row][col] - '0';
        }
        return new Num(row, startCol, endCol, val);
    }

    static Symbol parseSymbol(char[][] grid, int row, int col) {
        return new Symbol(row, col, grid[row][col]);
    }

    static class Num {
        int row;
        int startCol;
        int endCol;
        int val;

        Num(int row, int startCol, int endCol, int val) {
            this.row = row;
            this.startCol = startCol;
            this.endCol = endCol;
            this.val = val;
        }
    }

    static class Symbol {
        int row;
        int col;
        char chr;

        Symbol(int row, int col, char chr) {
            this.row = row;
            this.col = col;
            this.chr = chr;
        }
    }
}
import java.util.Scanner;

class Sudoku {
    private static final int EMPTY = -1;

    private final int dim;
    private final int sdim;
    private final int[][] cells;
    private int backtracking;

    public Sudoku (int[][] cells) {
        this.dim = cells.length;
        this.sdim = (int)Math.sqrt(dim);
        this.cells = cells;
        this.backtracking = 0;
    }

    int getBacktracking () { return backtracking; }

    public boolean isBlank (int row, int col) {
        if(cells[col][row] == EMPTY){
            return true;
        }
        return false;
    }

    public boolean isValid (int val, int row, int col) {
        return checkCol(val, col) && checkRow(val, row) && checkBlock(val, row, col);
    }

    private boolean checkRow (int val, int row) {
        for (int i = 0; i < cells[0].length; i++) {
            if(cells[i][row] == val){
                return false;
            }
        }
        return true;
    }

    private boolean checkCol (int val, int col) {
        for (int i = 0; i < cells.length; i++) {
            if(cells[col][i] == val){
                return false;
            }
        }
        return true;
    }

    private boolean checkBlock (int val, int row, int col) {
        row = row - row % sdim;
        col = col - col % sdim;
        for (int i = row; i < row + sdim; i++) {
            for (int j = col; j < col + sdim; j++) {
                if(cells[j][i] == val){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean solve () {
        return tryColumn(0);
    }

    private boolean tryColumn (int col) {
        if(col == dim) { return true; }
        return tryCell(col, 0);
    }

    private boolean tryCell (int col, int row) {
        if(row == dim) { return tryColumn(col+1); }
        if(!isBlank(row, col)) { return tryCell(col, row + 1); }

        for (int i = 1; i <= dim; i++) {
            if(isValid(i, row, col)){
                cells[col][row] = i;
                if(tryCell(col, row + 1)) { return true; }
                else{
                    cells[col][row] = EMPTY;
                    backtracking++;
                }
            }
        }
        return false;
    }

    public String toString () {
        StringBuilder res = new StringBuilder();
        for (int j=0; j<dim; j++) {
            if (j % sdim == 0) res.append("――――――――――――――――――――――\n");
            for (int i=0; i<dim; i++) {
                if (i % sdim == 0) res.append("│");
                int c = cells[i][j];
                res.append(c == EMPTY ? "." : c);
                res.append(" ");
            }
            res.append("│\n");
        }
        res.append("――――――――――――――――――――――\n");
        return res.toString();
    }

    //------------------------------------------------------------

    public static Sudoku read (Scanner s) {
        int dim = s.nextInt();
        int[][] cells = new int[dim][dim];
        for (int j = 0; j < dim; j++)
            for (int i = 0; i < dim; i++) {
                String c = s.next();
                cells[i][j] = c.equals(".") ? EMPTY : Integer.parseInt(c);
            }
        return new Sudoku(cells);
    }
}

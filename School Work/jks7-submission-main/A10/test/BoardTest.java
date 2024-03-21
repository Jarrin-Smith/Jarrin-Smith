import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

class BoardTest {
    @Test
    void tile () throws InterruptedException {
        Board.Tile tile = new Board.Tile('k', 0, 0);
        Board.Tile.TilePanel panel = new Board.Tile.TilePanel(tile,50);

        JFrame jf = new JFrame("Board.Tile");
        jf.add(panel);
        jf.pack();
        jf.setVisible(true);
        Thread.sleep(4000);
    }

    @Test
    void slow () throws IOException, InterruptedException {
        List<String> words = Files.readAllLines(Paths.get("commonwords.txt"));

        int size = 10;
        Board.Tile[][] tiles = new Board.Tile[size][size];
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                tiles[r][c] = new Board.Tile(r, c);
        Board board = new Board(tiles, new WordList(words));

        JFrame jf = new JFrame("Board");
        JScrollPane panel = new JScrollPane(new Board.BoardPanel(board));
        jf.setLayout(new BorderLayout());
        jf.add(BorderLayout.CENTER, panel);
        jf.pack();
        jf.setVisible(true);

        long t0 = System.currentTimeMillis();
        int i = 0;
        for (String w : board.findWords()) {
            i++;
            System.out.println(w);
        }
        long t1 = System.currentTimeMillis();
        System.out.printf("Found %d words in %d ms!%n", i, t1 - t0);
        Thread.sleep(4000);
    }

    @Test
    void compare () throws IOException, InterruptedException {
        List<String> words = Files.readAllLines(Paths.get("commonwords.txt"));
        Scanner scanner = new Scanner(Paths.get("commonwords.txt"));
        Trie trie = new Trie();
        while (scanner.hasNext()) trie.insert(scanner.next());

        int size = 3;
        String s = "valentine";
        Board.Tile[][] tiles = new Board.Tile[size][size];
        for (int r=0; r<size; r++)
            for (int c=0; c<size; c++)
                tiles[r][c] = new Board.Tile(s.charAt(r*3+c),r,c);
        Board board1 = new Board(tiles,new WordList(words));
        Board board2 = new Board(tiles,trie);

        JFrame jf = new JFrame("Board");
        JScrollPane panel = new JScrollPane(new Board.BoardPanel(board2));
        jf.setLayout(new BorderLayout());
        jf.add(BorderLayout.CENTER, panel);
        jf.pack();
        jf.setVisible(true);

        long t0 = System.currentTimeMillis();
        int i = 0;
        for (String w : board1.findWords()) {
            i++;
            System.out.println(w);
        }
        long t1 = System.currentTimeMillis();
        System.out.printf("slow --- Found %d words in %d ms!%n", i, t1-t0);

        t0 = System.currentTimeMillis();
        i = 0;
        for (String w : board2.findWords()) {
            i++;
            System.out.println(w);
        }
        t1 = System.currentTimeMillis();
        System.out.printf("fast --- Found %d words in %d ms!%n", i, t1-t0);
        Thread.sleep(4000);
    }

    @Test
    void myTest() throws IOException, InterruptedException {
        // The search function uses getNeighbors as well. 
        List<String> words = Files.readAllLines(Paths.get("commonwords.txt"));
        Scanner scanner = new Scanner(Paths.get("commonwords.txt"));
        Trie trie = new Trie();
        while (scanner.hasNext()) trie.insert(scanner.next());

        int size = 3;
        // changed word
        String s = "abilities";
        Board.Tile[][] tiles = new Board.Tile[size][size];
        for (int r=0; r<size; r++)
            for (int c=0; c<size; c++)
                tiles[r][c] = new Board.Tile(s.charAt(r*3+c),r,c);
        Board board1 = new Board(tiles,new WordList(words));
        Board board2 = new Board(tiles,trie);

        JFrame jf = new JFrame("Board");
        JScrollPane panel = new JScrollPane(new Board.BoardPanel(board2));
        jf.setLayout(new BorderLayout());
        jf.add(BorderLayout.CENTER, panel);
        jf.pack();
        jf.setVisible(true);

        long t0 = System.currentTimeMillis();
        int i = 0;
        for (String w : board1.findWords()) {
            i++;
            System.out.println(w);
        }
        long t1 = System.currentTimeMillis();
        System.out.printf("slow --- Found %d words in %d ms!%n", i, t1-t0);

        t0 = System.currentTimeMillis();
        i = 0;
        for (String w : board2.findWords()) {
            i++;
            System.out.println(w);
        }
        t1 = System.currentTimeMillis();
        System.out.printf("fast --- Found %d words in %d ms!%n", i, t1-t0);
        Thread.sleep(4000);
    }
}
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordListTest {

    @Test
    public void simple () {
        WordList t = new WordList(Arrays.asList(
                "to", "tea", "ted", "ten", "in", "inn", "A"));
        assertTrue(t.contains("ten"));
        assertTrue(t.contains("in"));
        assertTrue(t.contains("inn"));
        assertFalse(t.contains("tenn"));
        assertFalse(t.contains("te"));
        assertTrue(t.possiblePrefix(""));
        assertTrue(t.possiblePrefix("t"));
        assertTrue(t.possiblePrefix("to"));
        assertTrue(t.possiblePrefix("te"));
        assertTrue(t.possiblePrefix("i"));
        assertFalse(t.possiblePrefix("tu"));
    }

    @Test
    public void dict () throws IOException {
        List<String> list = Files.readAllLines(Paths.get("commonwords.txt"));
        WordList words = new WordList(list);

        assertTrue(words.contains("abandon"));
        assertTrue(words.contains("abandoned"));
        assertTrue(words.contains("abandonment"));
        assertFalse(words.contains("abandonmenth"));
        assertFalse(words.contains("abando"));
        assertFalse(words.contains("aband"));
        assertFalse(words.contains("aban"));
        assertFalse(words.contains("aba"));
        assertFalse(words.contains("ab"));
        assertFalse(words.contains("a"));
        assertTrue(words.possiblePrefix("abando"));
        assertTrue(words.possiblePrefix("aband"));
        assertTrue(words.possiblePrefix("aban"));
        assertTrue(words.possiblePrefix("aba"));
        assertTrue(words.possiblePrefix("ab"));
        assertTrue(words.possiblePrefix("a"));
        assertTrue(words.possiblePrefix("abandon"));
    }

    @Test
    public void myTest() {
        WordList t = new WordList(Arrays.asList(
                "so", "soap", "sea", "see", "seen", "bob", "bobb"));
        assertTrue(t.contains("so"));
        assertTrue(t.contains("soap"));
        assertTrue(t.contains("sea"));
        assertFalse(t.contains("seee"));
        assertFalse(t.contains("bo"));
        assertTrue(t.possiblePrefix(""));
        assertTrue(t.possiblePrefix("b"));
        assertTrue(t.possiblePrefix("s"));
        assertTrue(t.possiblePrefix("so"));
        assertTrue(t.possiblePrefix("bo"));
        assertFalse(t.possiblePrefix("bu"));
    }
}
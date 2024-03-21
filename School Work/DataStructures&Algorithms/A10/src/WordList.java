import java.util.List;

public record WordList (List<String> words) implements WordCollection {

    /**
     * See WordListTest for the expected behavior of these two methods.
     */
    public boolean contains(String w) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equals(w)) {
                return true;
            }
        }
        return false;
    }

    public boolean possiblePrefix(String w) {
        for (String word : words) {
            if (word.startsWith(w)) return true;
        }
        return false;
    }
}

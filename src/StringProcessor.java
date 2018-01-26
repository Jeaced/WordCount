import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class StringProcessor implements Runnable, Parser {

    String resource;
    ConcurrentHashMap<String, Integer> sharedHashMap;
    String[] words;
    AtomicBoolean stopCondition;

    public StringProcessor(String resource, ConcurrentHashMap<String, Integer> sharedHashMap, AtomicBoolean stop) {
        this.resource = resource;
        this.sharedHashMap = sharedHashMap;
        stopCondition = stop;
    }

    @Override
    public void run() {
        parse();
    }

    private boolean checkWord(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!isCyrillic(word.charAt(i))) {
                return false;
            }
        }
        return true;

    }

    boolean isCyrillic(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            stopCondition.set(true);
        }
        return Character.UnicodeBlock.CYRILLIC.equals(Character.UnicodeBlock.of(c));
    }

    @Override
    public void parse() {
        resource.replace("\n", "");
        resource = resource.trim().replaceAll(" +", " ");
        resource= resource.toLowerCase();
        words = resource.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (stopCondition.get() == true) {
                break;
            }
            words[i] = words[i].replaceAll("\\p{Punct}+$", "");
            if (checkWord(words[i])) {
                sharedHashMap.compute(words[i], (k, v) -> v == null ? 1 : v + 1);
                System.out.println(words[i] + " " + sharedHashMap.get(words[i]));
            }
        }
    }
}

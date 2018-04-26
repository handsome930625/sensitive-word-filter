import com.chaser.engine.FileResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * description:测试有多少敏感词
 *
 * @author wangyj on 2018/4/26
 */
public class WordTest {
    public static void main(String[] args) {
        Set<String> words = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(FileResource.class.getClassLoader().getResourceAsStream("word.txt")))) {
            for (String buf; (buf = br.readLine()) != null; ) {
                if ("".equals(buf.trim())) {
                    continue;
                }
                words.add(buf.substring(0, 1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(words);
    }
}

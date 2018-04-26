package com.chaser.engine;

import com.chaser.plugin.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * description:文件型资源抓取器
 *
 * @author wangyj on 2018/4/25
 */
public class FileResource implements Resource {
    /**
     * 资源路径
     */
    private String path;

    FileResource(String path) {
        this.path = path;
    }

    @Override
    public List<String> readWords() {
        List<String> words;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(FileResource.class.getClassLoader().getResourceAsStream(path)))) {
            words = new ArrayList<>(1200);
            for (String buf; (buf = br.readLine()) != null; ) {
                if ("".equals(buf.trim())) {
                    continue;
                }
                words.add(buf);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return words;
    }
}

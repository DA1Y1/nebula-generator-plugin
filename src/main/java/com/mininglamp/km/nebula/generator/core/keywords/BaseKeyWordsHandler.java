package com.mininglamp.km.nebula.generator.core.keywords;

import com.mininglamp.km.nebula.generator.core.config.IKeyWordsHandler;

import java.util.List;
import java.util.Locale;

/**
 * 基类关键字处理
 *
 * @author nieqiurong 2020/5/8.
 * @since 3.3.2
 */
public abstract class BaseKeyWordsHandler implements IKeyWordsHandler {

    public List<String> keyWords;

    public BaseKeyWordsHandler(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public List<String> getKeyWords() {
        return keyWords;
    }

    @Override
    public boolean isKeyWords(String columnName) {
        return getKeyWords().contains(columnName.toUpperCase(Locale.ENGLISH));
    }
}

package com.leunya.waylocation.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * @author pengli
 * @create 2022-02-23 11:13
 */
public class SpiltCharAnalyzer extends Analyzer {


    @Override
    protected TokenStreamComponents createComponents (String s) {

        Tokenizer token=new SpiltChar();

        return new TokenStreamComponents(token);
    }
}

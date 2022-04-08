package com.leyuna.waylocation.command;

import com.leyuna.waylocation.util.SpiltCharAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.StringReader;

/**
 * 测试分词器
 * 分词器工做流程
 * 1.切分，将须要分词的内容进行切分红每一个单词或者词语
 * 2.去除停用词，有些词在文本中出现的频率很是高，可是对文本所携带的信息基本不产生影响，例如英文的“a、an、the、of”，或中文的“的、了、着、是”，以及各类标点符号等，
 * 这样的词称为停用词（stop word）。文本通过分词以后，停用词一般被过滤掉，不会被进行索引。在检索的时候，用户的查询中若是含有停用词，
 * 检索系统也会将其过滤掉（由于用户输入的查询字符串也要进行分词处理）。排除停用词能够加快创建索引的速度，减少索引库文件的大小。
 * 3.对于英文字母，转为小写，由于搜索的时候不区分大小写
 *
 * @author kencery
 */
public class AnalyzerTest {

    /**
     * StandardAnalyzer分词法测试,对中文支持不是很好,将中文分词成1个字(单字分词)
     *
     * @throws Exception
     */
    public void StandardAnalyzerTest () throws Exception {
        //英文测试
        String text = "get Way Best";
        Analyzer analyzer = new StandardAnalyzer();
        displayTokens(analyzer, text);
    }

    public void myAnalyzerTest() throws Exception {
        String text="getWay";
        Analyzer analyzer=new SpiltCharAnalyzer();
        displayTokens(analyzer,text);
    }

    /**
     * CJKAnalyzerTest分词法测试,对中文支持不是很好，将中文分词成2个字(二分法分词)
     *
     * @throws Exception
     */
    public void CJKAnalyzerTest () throws Exception {
        //英文测试
        String text = "get way best";
        Analyzer analyzer = new CJKAnalyzer();
        displayTokens(analyzer, text);
    }

    /**
     * 使用指定的分词器对指定的文本进行分词，并打印出分出的词,测试分词法的方法
     * 备注说明：这里注意版本问题，暂无方法解决
     *
     * @param analyzer
     * @param text
     * @throws Exception
     */
    public static void displayTokens (Analyzer analyzer, String text) throws Exception {
        System.out.println("当前使用的分词器：" + analyzer.getClass().getName());
        //分词流，即将对象分词后所得的Token在内存中以流的方式存在，也说是说若是在取得Token必须从TokenStream中获取，而分词对象能够是文档文本，也能够是查询文本。
        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
        //表示token的首字母和尾字母在原文本中的位置。好比I'm的位置信息就是(0,3)，须要注意的是startOffset与endOffset的差值并不必定就是termText.length()，
        //由于可能term已经用stemmer或者其余过滤器处理过；
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        //这个有点特殊，它表示tokenStream中的当前token与前一个token在实际的原文本中相隔的词语数量，用于短语查询。好比： 在tokenStream中[2:a]的前一个token是[1:I'm ]，
        //它们在原文本中相隔的词语数是1，则token="a"的PositionIncrementAttribute值为1；
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.addAttribute(PositionIncrementAttribute.class);

        //问题说明：这里须要使用jdk1.7,若是使用jdk1.8或者jdk1.6则会出现报错信息
        //>>若是你们谁有相应的解决方案，请提交到git上我将会合并或者添加个人QQ咱们互相讨论
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        //表示token词典类别信息，默认为“Word”，好比I'm就属于<APOSTROPHE>，有撇号的类型；
        TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);
        tokenStream.reset();

        int position = 0;
        while (tokenStream.incrementToken()) {
            int increment = positionIncrementAttribute.getPositionIncrement();
            if (increment > 0) {
                position = position + increment;
            }
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String term = "输出结果为：" + charTermAttribute.toString();
            System.out.println("第" + position + "个分词，分词内容是:[" + term + "]" + "，分词内容的开始结束位置为：(" + startOffset + "-->" + endOffset + ")，类型是：" + typeAttribute.type());
        }
        tokenStream.close();
    }
}
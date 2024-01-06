package com.leyunone.waylocal.util;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;

/**
 * @author leyunone
 * @create 2022-02-23 11:03
 * 自定义分词器  -  单个字符分词
 */
public class SpiltChar extends Tokenizer {

    public SpiltChar (AttributeFactory factory) {
        super(factory);
    }

    public SpiltChar () {

    }

    public SpiltChar(boolean useSmart){

    }

    private int offset = 0, bufferIndex = 0, dataLen = 0;
    private final static int MAX_WORD_LEN = 255;
    private final static int IO_BUFFER_SIZE = 1024;
    private final char[] buffer = new char[MAX_WORD_LEN];
    private final char[] ioBuffer = new char[IO_BUFFER_SIZE];
    private int length;
    private int start;
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

    private final void push (char c) {
        if (length == 0) {
            start = offset - 1;
        }            // start of token
        buffer[length++] = Character.toLowerCase(c);  // buffer it

    }

    private final boolean flush () {

        if (length > 0) {
            //System.out.println(new String(buffer, 0,
            //length));
            termAtt.copyBuffer(buffer, 0, length);
            offsetAtt.setOffset(correctOffset(start), correctOffset(start + length));
            return true;
        }
        return false;
    }

    @Override
    public boolean incrementToken () throws IOException {
        clearAttributes();
        length = 0;
        start = offset;
        while (true) {
            final char c;
            offset++;
            if (bufferIndex >= dataLen) {
                dataLen = input.read(ioBuffer);
                bufferIndex = 0;
            }

            if (dataLen == -1) {
                offset--;
                return flush();
            } else {
                c = ioBuffer[bufferIndex++];
            }
            switch (Character.getType(c)) {

                case Character.DECIMAL_DIGIT_NUMBER://注意此部分只是滤一些熟悉或者字母
                case Character.LOWERCASE_LETTER://注意此部分
                case Character.UPPERCASE_LETTER://注意此部分
//	                push(c);
//	                if (length == MAX_WORD_LEN) return flush();
//	                break;
                case Character.OTHER_LETTER:
                    if (length > 0) {
                        bufferIndex--;
                        offset--;
                        return flush();
                    }
                    push(c);
                    return flush();
                default:
                    if (length > 0) {
                        return flush();
                    }
                    break;
            }
        }
    }

    @Override
    public final void end () {
        // set final offset
        final int finalOffset = correctOffset(offset);
        this.offsetAtt.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void reset () throws IOException {
        super.reset();
        offset = bufferIndex = dataLen = 0;
    }

}

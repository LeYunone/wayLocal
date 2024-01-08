package com.leyunone.waylocal.support.lucene;

import com.alibaba.fastjson.JSON;
import com.leyunone.waylocal.constant.enums.PathEnum;
import com.leyunone.waylocal.common.ServerConstant;
import com.leyunone.waylocal.bean.dto.ClassDTO;
import com.leyunone.waylocal.bean.dto.LuceneDTO;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.util.ParamsUtil;
import com.leyunone.waylocal.util.SpiltCharAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leyunone
 * @create 2021-02-22
 * <p>
 * Lucene 操作指令
 */
@Service
public class SearchDbBuildService {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建方法索引库文档
     */
    public void addMethodDir (List<MethodInfoDTO> methods) {
        IndexWriter indexWriter = null;
        try {
            List<Document> documents = new ArrayList<>();
            //拿写出流
            indexWriter = generateindexWriter(PathEnum.PATH_METHOD_DIR.getValue());
            for (MethodInfoDTO method : methods) {
                Document document = new Document();
                //填充文档
                document.add(new TextField("methodId", method.getMethodId(), Field.Store.YES));
                document.add(new TextField("methodName", method.getMethodName(), Field.Store.YES));
                //不分词的全类名
                document.add(new StringField("className", method.getClassName(), Field.Store.YES));
                //出入参不进行分词

                document.add(new StringField("value", JSON.toJSONString(method), Field.Store.YES));
                documents.add(document);
            }
            //一次处理
            indexWriter.addDocuments(documents);
            //关闭输出流
        } catch (IOException e) {
            logger.error("waylocal : Error Lucene error");
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    logger.error("waylocal : Error Lucene error");
                }
            }
        }
    }

    /**
     * 创建类的搜索库
     */
    public void addClassDir () {
        IndexWriter indexWriter = null;
        try {
            List<Document> documents = new ArrayList<>();
            indexWriter = generateindexWriter(PathEnum.PATH_CLASS_DIR.getValue());
            for (String className : ServerConstant.ClassName) {
                Document document = new Document();
                document.add(new StringField("className", className, Field.Store.YES));
                //只留最后的类名当key
                className = className.substring(className.lastIndexOf('.') + 1);
                document.add(new TextField("key", className, Field.Store.YES));
                documents.add(document);
            }
            //一次处理
            indexWriter.addDocuments(documents);
            //关闭输出流
        } catch (IOException e) {
            logger.error("waylocal : Error Lucene error");
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private IndexWriter generateindexWriter (String path) throws IOException {
        //创建索引库位置
        Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(path));
        //IK 分词器
        Analyzer analyzer = new SpiltCharAnalyzer();
        //创建输出流 write
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        return new IndexWriter(directory, indexWriterConfig);
    }

    /**
     * 删除文件索引库文档
     */
    public void deleteDir () {
        //删方法搜索库
        deleteFile(PathEnum.PATH_METHOD_DIR.getValue());
        //删类搜索库
        deleteFile(PathEnum.PATH_CLASS_DIR.getValue());
    }

    private void deleteFile (String path) {
        File file = new File(path);
        if (file.exists()) {
            //删除文件下所有文件
            File[] files = file.listFiles();
            for (File f : files) {
                f.delete();
            }
            //删除文件夹
            file.delete();
        }
    }
}

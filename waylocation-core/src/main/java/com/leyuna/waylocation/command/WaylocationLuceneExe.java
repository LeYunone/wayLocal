package com.leyuna.waylocation.command;

import com.alibaba.fastjson.JSON;
import com.leyuna.waylocation.constant.enums.PathEnum;
import com.leyuna.waylocation.constant.global.ServerConstant;
import com.leyuna.waylocation.dto.ClassDTO;
import com.leyuna.waylocation.dto.LuceneDTO;
import com.leyuna.waylocation.dto.MethodInfoDTO;
import com.leyuna.waylocation.util.ParamsUtil;
import com.leyuna.waylocation.util.SpiltCharAnalyzer;
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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @create 2021-02-22
 * <p>
 * Lucene 操作指令
 */
@Service
public class WaylocationLuceneExe {

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
            e.printStackTrace();
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
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

    public LuceneDTO getClassDir (String value, Integer size) {
        LuceneDTO luceneDTO = new LuceneDTO();
        IndexReader indexReader = null;
        try {
            List<ClassDTO> result = new ArrayList();
            Analyzer analyzer = new SpiltCharAnalyzer();
            //关键词
            QueryParser qp = new QueryParser("key", analyzer);
            Query query = qp.parse("key:" + value);

            //高亮关键字
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

            //打开索引库输入流
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(PathEnum.PATH_CLASS_DIR.getValue()));
            indexReader = DirectoryReader.open(directory);
            //索引库搜索指令
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            //分页
            TopDocs topDocs = indexSearcher.search(query, size);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            //只取前十条
            long len = scoreDocs.length;
            for (int i = 0; i < len; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                ClassDTO classDTO = new ClassDTO();
                //获得对应的文档
                Document doc = indexSearcher.doc(scoreDoc.doc);
                String className = doc.get("className");
                classDTO.setValue(className);
                //暂不高亮处理
                TokenStream tokenStream = analyzer.tokenStream("className", new StringReader(className));
                classDTO.setHightLineKey(highlighter.getBestFragment(tokenStream, className));
                result.add(classDTO);
            }
            luceneDTO.setListData(result);
            luceneDTO.setTotole(topDocs.totalHits);
        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return luceneDTO;
    }

    /**
     * 关键词搜索
     *
     * @param key  方法名
     * @param size
     * @return
     */
    public LuceneDTO getMethodDir (String key, String value, Integer size) {
        LuceneDTO luceneDTO = new LuceneDTO();
        IndexReader indexReader = null;
        try {
            List<MethodInfoDTO> result = new ArrayList();
            Analyzer analyzer = new SpiltCharAnalyzer();
            //关键词
            QueryParser qp = new QueryParser(key, analyzer);
            Query query = qp.parse(key + ":" + value);

            //高亮关键字
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

            //打开索引库输入流
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(PathEnum.PATH_METHOD_DIR.getValue()));
            indexReader = DirectoryReader.open(directory);
            //索引库搜索指令
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            //分页
            TopDocs topDocs = indexSearcher.search(query, size);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            //只取前十条
            long len = scoreDocs.length;
            for (int i = 0; i < len; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                //获得对应的文档
                Document doc = indexSearcher.doc(scoreDoc.doc);
                //还原方法名
                String methodName = doc.get("methodName");
                //高亮处理
                TokenStream tokenStream = analyzer.tokenStream("methodName", new StringReader(methodName));
                MethodInfoDTO method = new MethodInfoDTO();
                String json = doc.get("value");
                method = JSON.parseObject(json, MethodInfoDTO.class);
                String returnName = "void";
                if (null != method.getReturnParams()) {
                    returnName = method.getReturnParams().getName();
                }

                method.setLineKey(returnName + "  " + methodName + "(" + ParamsUtil.getParams(method.getParams()) + ")");
                //页面展示
                methodName = highlighter.getBestFragment(tokenStream, methodName);
                method.setHightLineKey(returnName + "  " + methodName + "(" + ParamsUtil.getParams(method.getParams()) + ")");
                result.add(method);
            }
            luceneDTO.setListData(result);
            luceneDTO.setTotole(topDocs.totalHits);
        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return luceneDTO;
    }

    public LuceneDTO getMethodDirBooleanQuery (String className, String methodName) {
        LuceneDTO luceneDTO = new LuceneDTO();
        IndexReader indexReader = null;
        try {
            List<MethodInfoDTO> result = new ArrayList();
            Analyzer analyzer = new SpiltCharAnalyzer();
            //关键词
            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
            QueryParser query = new QueryParser("methodName", analyzer);
            Query parse = query.parse(methodName);
            Query query2 = new TermQuery(new Term("className", className));

            booleanQueryBuilder.add(parse, BooleanClause.Occur.MUST);
            booleanQueryBuilder.add(query2, BooleanClause.Occur.SHOULD);
            BooleanQuery booleanQuery = booleanQueryBuilder.build();

            //高亮关键字
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(booleanQuery));

            //打开索引库输入流
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(PathEnum.PATH_METHOD_DIR.getValue()));
            indexReader = DirectoryReader.open(directory);
            //索引库搜索指令
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            //分页
            TopDocs topDocs = indexSearcher.search(booleanQuery, 10);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            //只取前十条
            long len = scoreDocs.length;
            for (int i = 0; i < len; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                //获得对应的文档
                Document doc = indexSearcher.doc(scoreDoc.doc);
                //还原方法名
                methodName = doc.get("methodName");
                //高亮处理
                TokenStream tokenStream = analyzer.tokenStream("methodName", new StringReader(methodName));
                MethodInfoDTO method = new MethodInfoDTO();
                String json = doc.get("value");
                method = JSON.parseObject(json, MethodInfoDTO.class);
                String returnName = "void";
                if (null != method.getReturnParams()) {
                    returnName = method.getReturnParams().getName();
                }
                method.setLineKey(returnName + "  " + methodName + "(" + ParamsUtil.getParams(method.getParams()) + ")");
                //页面展示
                methodName = highlighter.getBestFragment(tokenStream, methodName);
                method.setHightLineKey(returnName + "  " + methodName + "(" + ParamsUtil.getParams(method.getParams()) + ")");
                result.add(method);
            }
            luceneDTO.setListData(result);
            luceneDTO.setTotole(topDocs.totalHits);
        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return luceneDTO;
    }
}
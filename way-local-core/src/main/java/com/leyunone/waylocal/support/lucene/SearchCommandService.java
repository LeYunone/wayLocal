package com.leyunone.waylocal.support.lucene;

import com.leyunone.waylocal.bean.dto.LuceneDTO;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * :)
 *
 * @Author leyunone
 * @Date 2024/1/9 11:06
 */
@Service
public class SearchCommandService {


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
            logger.error("waylocal : Error Lucene error");
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
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
            logger.error("waylocal : Error Lucene error");
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                    logger.error("waylocal : Error Lucene error");
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
            Query parse = query.parse("methodName:"+methodName);
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
                String json = doc.get("value");
                MethodInfoDTO method = JSON.parseObject(json, MethodInfoDTO.class);
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
            logger.error("waylocal : Error Lucene error");
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                }
            }
        }
        return luceneDTO;
    }
}

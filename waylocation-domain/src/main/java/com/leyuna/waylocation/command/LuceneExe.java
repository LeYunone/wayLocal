package com.leyuna.waylocation.command;

import com.leyuna.waylocation.bean.dto.LuceneDTO;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.constant.enums.PathEnum;
import com.leyuna.waylocation.custom.SpiltCharAnalyzer;
import com.leyuna.waylocation.util.StringResoleUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
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

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @create 2021-02-22
 *
 * Lucene 操作指令
 */
@Service
public class LuceneExe {

    /**
     * 创建方法索引库文档
     */
    public void addMethodDir(List<MethodInfoDTO> methods) {
        try {
            List<Document> documents=new ArrayList<>();
            //创建索引库位置
            Directory directory= FSDirectory.open(FileSystems.getDefault().getPath(PathEnum.PATH_METHOD_DIR.getValue()));
            //IK 分词器
            Analyzer analyzer = new SpiltCharAnalyzer();
            //创建输出流 write
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);

            FieldType typeYes=new FieldType();
            typeYes.setStored(true);//可存储
            typeYes.setTokenized(true);//设置分词
            FieldType typeNo=new FieldType();
            typeNo.setStored(true);
            typeNo.setTokenized(false);

            for(MethodInfoDTO method:methods){
                Document document=new Document();
                //填充文档
                document.add(new TextField("methodId",method.getMethodId(), Field.Store.YES));
                //处理方法名 遇到大写前空格分隔，符合分词器规律
                document.add(new TextField("methodName",method.getMethodName(), Field.Store.YES));
                document.add(new TextField("className",method.getClassName(), Field.Store.YES));
                //出入参不进行分词
                document.add(new StringField("params",method.getParams(), Field.Store.YES));
                document.add(new StringField("returnParams",method.getReturnParams(), Field.Store.YES));
                documents.add(document);
            }
            //一次处理
            indexWriter.addDocuments(documents);
            //关闭输出流
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关键词搜索
     * @param key 方法名
     * @param size
     * @return
     */
    public LuceneDTO getMethodDirByMethodName(String key, Integer size){
        LuceneDTO luceneDTO=new LuceneDTO();
        try {
            List<MethodInfoDTO> result=new ArrayList();
            Analyzer analyzer=new SpiltCharAnalyzer();
            //关键词
            QueryParser qp = new QueryParser("methodName",analyzer);
            Query query=qp.parse(key);

            //高亮关键字
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

            //打开索引库输入流
            Directory directory=FSDirectory.open(FileSystems.getDefault().getPath(PathEnum.PATH_METHOD_DIR.getValue()));
            IndexReader indexReader = DirectoryReader.open(directory);
            //索引库搜索指令
            IndexSearcher indexSearcher=new IndexSearcher(indexReader);

            //从上一页最后一条数据开始查询  达到分页的目的
            TopDocs topDocs = indexSearcher.search(query, size);
            long totle=topDocs.totalHits;

            ScoreDoc[] scoreDocs=topDocs.scoreDocs;
            //只取前十条
            long len=size>10?10:size;
            for(int i=0;i<len;i++){
                ScoreDoc scoreDoc=scoreDocs[i];
                //获得对应的文档
                Document doc = indexSearcher.doc(scoreDoc.doc);
                //还原方法名
                String methodName=StringResoleUtil.synthesisWord(doc.get("methodName"));
                //高亮处理
                TokenStream tokenStream = analyzer.tokenStream("methodName", new StringReader(methodName));

                MethodInfoDTO method=new MethodInfoDTO();
                method.setMethodName(highlighter.getBestFragment(tokenStream,methodName));
                method.setParams(doc.get("params"));
                method.setReturnParams(doc.get("returnParams"));
                method.setClassName(doc.get("className"));
                method.setMethodId(doc.get("methodId"));
                result.add(method);
            }
            luceneDTO.setListData(result);
            luceneDTO.setTotole(totle);

            indexReader.close();
        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return luceneDTO;
    }

    /**
     * 关键词搜索 根据类名
     * @param className
     * @return
     */
    public LuceneDTO getMethodDirByClassName(String className){
        LuceneDTO luceneDTO=new LuceneDTO();
        try {
            List<MethodInfoDTO> result=new ArrayList();
            Analyzer analyzer=new StandardAnalyzer();
            //关键词
            QueryParser qp = new QueryParser("className",analyzer);
            Query query=qp.parse(className);

            //打开索引库输入流
            Directory directory=FSDirectory.open(FileSystems.getDefault().getPath(PathEnum.PATH_METHOD_DIR.getValue()));
            IndexReader indexReader = DirectoryReader.open(directory);
            //索引库搜索指令
            IndexSearcher indexSearcher=new IndexSearcher(indexReader);

            //只查一条出来
            TopDocs topDocs = indexSearcher.search(query,1);
            long totle=topDocs.totalHits;

            long len=totle>10?10:totle;
            for(ScoreDoc scoreDoc:topDocs.scoreDocs){
                //获得对应的文档
                Document doc = indexSearcher.doc(scoreDoc.doc);

                MethodInfoDTO method=new MethodInfoDTO();
                method.setMethodName(doc.get("methodName"));
                method.setParams(doc.get("params"));
                method.setReturnParams(doc.get("returnParams"));
                method.setClassName(doc.get("className"));
                method.setMethodId(doc.get("methodId"));
                result.add(method);
            }
            luceneDTO.setListData(result);
            luceneDTO.setTotole(totle);
            indexReader.close();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return luceneDTO;
    }
}

package cn.eg.controller;

import cn.eg.Response.ResponseData;
import cn.eg.entity.TbItem;
import cn.eg.mapper.TbItemMapper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FullSearchController {

    private static final Logger logger = LoggerFactory.getLogger("");

    private static final String IR = "E:\\ssmTbItemIndex\\data";

    @Autowired
    private TbItemMapper itemMapper;

    @RequestMapping("/createIndex")
    public ResponseData createIndex() {

        ResponseData resp = ResponseData.success();
        List<TbItem> items = itemMapper.selectAll();
        List<Document> docList = new ArrayList<>();
        for (TbItem product : items) {
            Document document = new Document();

            // store:如果是yes，则说明存储到文档域中
            /*Field id = new TextField("id", product.getId().toString(), Field.Store.YES);
            Field title = new TextField("title", product.getTitle(), Field.Store.YES);
            Field sellPoint = new TextField("sellPoint", product.getSellPoint(), Field.Store.YES);
            Field barcode = new TextField("barcode", product.getBarcode()==null?"":product.getBarcode(), Field.Store.YES);
            Field image = new TextField("image", product.getImage(), Field.Store.YES);
            Field price = new TextField("price", product.getPrice().toString(), Field.Store.YES);
            Field cid = new TextField("cid", product.getCid().toString(), Field.Store.YES);
            Field num = new TextField("num", product.getNum()+"", Field.Store.YES);
            Field status = new TextField("status", product.getStatus()+"", Field.Store.YES);
            Field created = new TextField("created", product.getCreated().toString(), Field.Store.YES);
            Field updated = new TextField("updated", product.getUpdated().toString(), Field.Store.YES);*/


            // 商品id 不分词直接索引存储
            Field id = new StringField("id", product.getId().toString(), Field.Store.YES);
            // 商品名称 分词索引存储
            Field title = new TextField("title", product.getTitle(), Field.Store.YES);
            // 商品卖点 分词索引存储
            Field sellPoint = new TextField("sellPoint", product.getSellPoint(), Field.Store.YES);
            // 商品条形码 不分词直接索引存储
            Field barcode = new StringField("barcode", product.getBarcode() == null ? "" : product.getBarcode(), Field.Store.YES);
            // 商品图片地址 不分词不索引存储
            Field image = new StoredField("image", product.getImage());
            // 商品价格 不分词不索引存储
            Field price = new StoredField("price", product.getPrice());
            // 商品类别id 不分词不索引存储
            Field cid = new StoredField("cid", product.getCid());
            // 商品数量 不分词不索引存储
            Field num = new StoredField("num", product.getNum());
            // 商品状态 不分词不索引存储
            Field status = new StoredField("status", product.getStatus());
            // 商品创建日期 不分词不索引存储
            Field created = new StoredField("created", product.getCreated().toString());
            // 商品更新日期 不分词不索引存储
            Field updated = new StoredField("updated", product.getUpdated().toString());


            // 将field域设置到Document对象中
            document.add(id);
            document.add(title);
            document.add(sellPoint);
            document.add(barcode);
            document.add(image);
            document.add(price);
            document.add(cid);
            document.add(num);
            document.add(status);
            document.add(created);
            document.add(updated);

            docList.add(document);
        }

        // 创建分词器，标准分词器
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        // 创建IndexWriter
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 指定索引库的地址
        Path indexFile = Paths.get(IR);
        IndexWriter indexWriter = null;
        try{
            Directory directory = FSDirectory.open(indexFile);
            indexWriter = new IndexWriter(directory, config);

            // 通过IndexWriter对象将Document写入到索引库中
            for (Document document : docList) {
                indexWriter.addDocument(document);
            }


        } catch (Exception e) {
            logger.error("生成索引异常!",e);
            return  ResponseData.fail("生成索引异常!");
        } finally {
            // 关闭indexWriter
            try {
                if (indexWriter != null)
                    indexWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return resp;

    }
    @RequestMapping("searchIndex")
    public ResponseData searchIndex(String keyword) throws Exception {
        if(StringUtils.isEmpty(keyword)){
            return ResponseData.fail("关键字为空!");
        }
        ResponseData resp = ResponseData.success();
        List<TbItem> items = new ArrayList<>();
        resp.putKeyAndValue("items",items);
        // 创建query对象
        // 使用QueryParser搜索时，需要指定分词器，搜索时的分词器要和索引时的分词器一致
        // 第一个参数：默认搜索的域的名称
        //QueryParser parser = new QueryParser("title", new StandardAnalyzer());
        QueryParser parser = new QueryParser("sellPoint", new IKAnalyzer());


        // 通过queryparser来创建query对象
        // 参数：输入的lucene的查询语句(关键字一定要大写)
        Query query = parser.parse(keyword);

        //精确的词项查询 TermQuery
        //Query query = new TermQuery(new Term("id", "998692"));

        //范围查询
        //Query query = new TermRangeQuery("id", new BytesRef("123455"), new BytesRef("123457"), true, true);

        /**
         * 组合查询
         * MUST和MUST表示“与”的关系，即“交集”。
         * MUST和MUST_NOT前者包含后者不包含。
         * MUST_NOT和MUST_NOT没意义。
         * SHOULD与MUST表示MUST，SHOULD失去意义。
         * SHOUlD与MUST_NOT相当于MUST与MUST_NOT。
         * SHOULD与SHOULD表示“或”的概念。
         *
         */
        /*Query query1 = new TermQuery(new Term("barcode", "323233"));
        Query query2 = new TermQuery(new Term("sellPoint", "手"));

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(query1, BooleanClause.Occur.MUST);
        builder.add(query2, BooleanClause.Occur.SHOULD);
        BooleanQuery query = builder.build();*/


        // 默认搜索多个域名
        //String[] fields = {"title", "sellPoint"};
        //MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
        //Query query = parser.parse("手"); // 相当于title:手 OR sellPoint:手


        /*Query query1 = new TermQuery(new Term("title", "电"));
        Query query2 = new TermQuery(new Term("sellPoint", "手"));

        // 利用BoostQuery包装Query
        BoostQuery query3 = new BoostQuery(query2, 100f);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(query3, BooleanClause.Occur.MUST);
        builder.add(query2, BooleanClause.Occur.MUST);
        BooleanQuery query = builder.build();*/

        /*String[] fields = {"title", "sellPoint"};
        Map<String, Float> boosts = new HashMap<>();
        boosts.put("title", 100f);
        // 利用MultiFieldQueryParser构造函数传参
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer(), boosts);
        Query query = parser.parse("通");*/



        // 创建IndexSearcher
        // 指定索引库的地址
        Path indexFile = Paths.get(IR);
        Directory directory = FSDirectory.open(indexFile);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        // 通过searcher来搜索索引库
        // 第二个参数：指定需要显示的顶部记录的N条
        TopDocs topDocs = indexSearcher.search(query, 2);

        // 根据查询条件匹配出的记录
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取文档的ID
            int docId = scoreDoc.doc;

            // 通过ID获取文档
            Document doc = indexSearcher.doc(docId);
            TbItem item = new TbItem();
            long id = Long.parseLong(doc.getField("id").getCharSequenceValue().toString());
            System.out.println(id);
            item.setId(id);
            items.add(itemMapper.selectByPrimaryKey(item));

        }

        // 关闭资源
        reader.close();
        return resp;
    }

    @Test
    public void updateIndex() throws Exception {
        // 创建分词器，标准分词器
        Analyzer analyzer = new StandardAnalyzer();
        // 创建IndexWriter
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        Path indexFile = Paths.get("E:\\search-learn\\data");
        Directory directory = FSDirectory.open(indexFile);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 第一个参数：指定查询条件
        // 第二个参数：修改之后的对象
        // 修改时如果根据查询条件可以查出结果，则将其删掉，并进行覆盖新的doc；否则直接新增一个doc
        Document doc = new Document();
        doc.add(new TextField("name","pingpong", Field.Store.YES));
        indexWriter.updateDocument(new Term("name", "xiaoming"), doc);

        indexWriter.close();
    }


    @Test
    public void deleteByCondition() throws Exception {
        // 创建分词器，标准分词器
        Analyzer analyzer = new StandardAnalyzer();
        // 创建IndexWriter
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        Path indexFile = Paths.get("E:\\search-learn\\data");
        Directory directory = FSDirectory.open(indexFile);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // Terms
        indexWriter.deleteDocuments(new Term("barcode", "323233"));
        indexWriter.close();
    }
}

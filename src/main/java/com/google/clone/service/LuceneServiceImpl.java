package com.google.clone.service;

import com.google.clone.model.Index;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhuziv on 25-Sep-15.
 */
@Component
public class LuceneServiceImpl implements LuceneService {

    private final String SEARCH_ID_FIELD = "id";
    private final String SEARCH_TITLE_FIELD = "title";
    private final String SEARCH_TEXT_FIELD = "text";
    private final String SEARCH_URL_FIELD = "url";
    private final int HITS_PER_PAGE = 10;

    public List<Index> search(List<Index> indexList, String query) {
        List<Index> returnList = new ArrayList<>();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try (IndexWriter w = new IndexWriter(directory, config)) {
            for(Index index : indexList) {
                Document doc = new Document();
                doc.add(new StringField(SEARCH_ID_FIELD, index.getId(), Field.Store.YES));
                doc.add(new TextField(SEARCH_TITLE_FIELD, index.getTitle(), Field.Store.YES));
                doc.add(new TextField(SEARCH_TEXT_FIELD, index.getText(), Field.Store.YES));
                doc.add(new StringField(SEARCH_URL_FIELD, index.getUrl(), Field.Store.YES));
                w.addDocument(doc);
            }
            w.close();

            IndexReader reader = DirectoryReader.open(directory);
            org.apache.lucene.search.Query titleQuery = new QueryParser(SEARCH_TITLE_FIELD, analyzer).parse(query);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(HITS_PER_PAGE);
            searcher.search(titleQuery, collector);
            ScoreDoc[] titleHits = collector.topDocs().scoreDocs;

            for (int i = 0; i < titleHits.length; ++i) {
                int docId = titleHits[i].doc;
                Document d = searcher.doc(docId);
                returnList.add(new Index(d.get(SEARCH_URL_FIELD), d.get(SEARCH_TITLE_FIELD), d.get(SEARCH_TITLE_FIELD)));
            }

            org.apache.lucene.search.Query textQuery = new QueryParser(SEARCH_TEXT_FIELD, analyzer).parse(query);
            collector = TopScoreDocCollector.create(HITS_PER_PAGE);
            searcher.search(textQuery, collector);
            ScoreDoc[] textHits = collector.topDocs().scoreDocs;

            for (int i = 0; i < textHits.length; ++i) {
                int docId = textHits[i].doc;
                Document d = searcher.doc(docId);
                returnList.add(new Index(d.get(SEARCH_URL_FIELD), d.get(SEARCH_TITLE_FIELD), d.get(SEARCH_TITLE_FIELD)));
            }
            reader.close();
            returnList.subList(0, 10);
        } catch(IndexOutOfBoundsException e) {
            return returnList;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }
}

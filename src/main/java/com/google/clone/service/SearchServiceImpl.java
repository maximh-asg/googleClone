package com.google.clone.service;

import com.google.clone.dao.IndexDao;
import com.google.clone.model.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mhuziv on 24-Sep-15.
 */
@Component
public class SearchServiceImpl implements SearchService {

    @Autowired
    IndexDao indexDao;

    @Autowired
    LuceneService luceneService;

    public List<Index> doSearch(String query){
        return luceneService.search(indexDao.findAll(), query);
    }
}

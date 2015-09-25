package com.google.clone.service;

import com.google.clone.model.Index;

import java.util.List;

/**
 * Created by mhuziv on 25-Sep-15.
 */
public interface LuceneService {
    public List<Index> search(List<Index> indexList, String query);
}

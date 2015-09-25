package com.google.clone.service;

import com.google.clone.model.Index;

import java.util.List;

/**
 * Created by mhuziv on 24-Sep-15.
 */
public interface SearchService {
    public List<Index> doSearch(String query);
}

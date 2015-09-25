package com.google.clone.dao;

import com.google.clone.model.Index;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mhuziv on 24-Sep-15.
 */
public interface IndexDao extends MongoRepository<Index,String> {
}

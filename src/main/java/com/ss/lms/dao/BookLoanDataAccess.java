package com.ss.lms.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.ss.lms.entity.BookLoan;
import com.ss.lms.entity.BookLoanCompositeKey;

@Component
public interface BookLoanDataAccess extends CrudRepository<BookLoan, BookLoanCompositeKey> 
{

}

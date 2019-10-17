package com.ss.lms.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookLoan;
import com.ss.lms.entity.BookLoanCompositeKey;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.LibraryBranch;
import com.ss.lms.entity.Publisher;
import com.ss.lms.service.AdminService;

@RestController
@RequestMapping(value = "/lms/admin*")
public class AdminController
{
	@Autowired
	AdminService admin;
	
	/*************************************************
	 * 
	 * ALL CREATE OPERATIONS
	 * 
	 *************************************************/

	@PostMapping(path = "/author", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
								consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) 
	{
		// make sure the id is null and other fields aren't
		if(author.getAuthorId() != null || author.getAuthorName() == null || "".contentEquals(author.getAuthorName()) ) 
		{
			return new ResponseEntity<Author>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Author>(admin.saveAuthor(author), HttpStatus.CREATED);
	}

	@PostMapping(path = "/publisher", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
								consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) 
	{
		// make sure the id is null, and the other fields aren't
		if(publisher.getPublisherId() != null || publisher.getPublisherName() == null || "".contentEquals(publisher.getPublisherName())
											|| publisher.getPublisherAddress() == null || "".contentEquals(publisher.getPublisherAddress())
											|| publisher.getPublisherPhone() == null || "".contentEquals(publisher.getPublisherPhone())) 
		{
			return new ResponseEntity<Publisher>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Publisher>(admin.savePublisher(publisher), HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/book", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
							consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Book> createBook(@RequestBody Book book)
	{
		// make sure the id is null, and the other fields aren't
		if(book.getBookId() != null || book.getTitle() == null || "".contentEquals(book.getTitle())
									|| book.getAuthorId() == null || book.getPublisherId() == null)
		{
			return new ResponseEntity<Book>(HttpStatus.BAD_REQUEST);
		}
		
		// check author exists
		if(!admin.readAuthorById(book.getAuthorId()).isPresent()) 
		{
			return new ResponseEntity<Book>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		// check publisher exists
		if(!admin.readPublisherById(book.getPublisherId()).isPresent())
		{
			return new ResponseEntity<Book>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		// create the entity
		return new ResponseEntity<Book>(admin.saveBook(book), HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/branch", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
								consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<LibraryBranch> createLibraryBranch(@RequestBody LibraryBranch libraryBranch)
	{
		// make sure the id is null, and the other fields aren't
		if(libraryBranch.getBranchId() != null || libraryBranch.getBranchName() == null || "".contentEquals(libraryBranch.getBranchName())
									|| libraryBranch.getBranchAddress() == null || "".contentEquals(libraryBranch.getBranchAddress()))
		{
			return new ResponseEntity<LibraryBranch>(HttpStatus.BAD_REQUEST);
		}
		
		// create the entity
		return new ResponseEntity<LibraryBranch>(admin.saveLibraryBranch(libraryBranch), HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/borrower", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
								consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Borrower> createBorrower(@RequestBody Borrower borrower)
	{
		// make sure the id is null, and the other fields aren't
		if(borrower.getCardNo() != null || borrower.getName() == null || "".contentEquals(borrower.getName())
										|| borrower.getAddress() == null || "".contentEquals(borrower.getAddress())
										|| borrower.getPhone() == null || "".contentEquals(borrower.getPhone()))
		{
			return new ResponseEntity<Borrower>(HttpStatus.BAD_REQUEST);
		}
		
		// create the entity
		return new ResponseEntity<Borrower>(admin.saveBorrower(borrower), HttpStatus.CREATED);
	}
	
	/*************************************************
	 * 
	 * ALL READ OPERATIONS
	 * 
	 *************************************************/

	@GetMapping(value = "/author/{authorId}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Author> readAuthorById(@PathVariable Integer authorId)
	{
		Optional<Author> result = admin.readAuthorById(authorId);
		
		// 200 regardless of if we found it or not, the query was successful, it means they can keep doing it
		if(!result.isPresent()) 
		{
			return new ResponseEntity<Author>(HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<Author>(result.get(), HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/author", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Iterable<Author>> readAuthorAll()
	{
		Iterable<Author> result = admin.readAuthorAll();

		// 200 regardless of if we found it or not, the query was successful, it means they can keep doing it
		if(!result.iterator().hasNext()) 
		{
			return new ResponseEntity<Iterable<Author>>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Iterable<Author>>(result, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/publisher/{publisherId}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Publisher> readPublisherById(@PathVariable Integer publisherId)
	{
		Optional<Publisher> result = admin.readPublisherById(publisherId);
		
		if(!result.isPresent()) 
		{
			return new ResponseEntity<Publisher>(HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<Publisher>(result.get(), HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/publisher", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Iterable<Publisher>> readPublisherAll()
	{
		Iterable<Publisher> result = admin.readPublisherAll();
		
		// 200 regardless of if we found it or not, the query was successful, it means they can keep doing it
		if(!result.iterator().hasNext()) 
		{
			return new ResponseEntity<Iterable<Publisher>>(result, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Iterable<Publisher>>(result, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/book/{bookId}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Book> readBookById(@PathVariable Integer bookId)
	{
		Optional<Book> result = admin.readBookById(bookId);
		
		if(!result.isPresent()) 
		{
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<Book>(result.get(), HttpStatus.OK);
		}
	}

	@GetMapping(value = "/book", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Iterable<Book>> readBookAll()
	{
		Iterable<Book> result = admin.readBookAll();
		
		// 200 regardless of if we found it or not, the query was successful, it means they can keep doing it
		if(!result.iterator().hasNext()) 
		{
			return new ResponseEntity<Iterable<Book>>(result, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Iterable<Book>>(result, HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/branch/{branchId}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<LibraryBranch> readLibraryBranchById(@PathVariable Integer branchId)
	{
		Optional<LibraryBranch> result = admin.readLibraryBranchById(branchId);
		
		if(!result.isPresent()) 
		{
			return new ResponseEntity<LibraryBranch>(HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<LibraryBranch>(result.get(), HttpStatus.OK);
		}
	}

	@GetMapping(value = "/branch", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Iterable<LibraryBranch>> readLibraryBranchAll()
	{
		Iterable<LibraryBranch> result = admin.readLibraryBranchAll();
		
		// 200 regardless of if we found it or not, the query was successful, it means they can keep doing it
		if(!result.iterator().hasNext()) 
		{
			return new ResponseEntity<Iterable<LibraryBranch>>(result, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Iterable<LibraryBranch>>(result, HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/borrower/{cardNo}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Borrower> readBorrowerById(@PathVariable Integer cardNo)
	{
		Optional<Borrower> result = admin.readBorrowerById(cardNo);
		
		if(!result.isPresent()) 
		{
			return new ResponseEntity<Borrower>(HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<Borrower>(result.get(), HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/borrower", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Iterable<Borrower>> readBorrowerByAll()
	{
		Iterable<Borrower> result = admin.readBorrowerAll();
		
		// 200 regardless of if we found it or not, the query was successful, it means they can keep doing it
		if(!result.iterator().hasNext()) 
		{
			return new ResponseEntity<Iterable<Borrower>>(result, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Iterable<Borrower>>(result, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/loan/borrower/{cardNo}/branch/{branchId}/book/{bookId}", 
				produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<BookLoan> readBookLoanByAllId(@PathVariable("cardNo") Integer cardNo, @PathVariable("branchId") Integer branchId, @PathVariable("bookId") Integer bookId)
	{
		Optional<BookLoan> result = admin.readBookLoanById(new BookLoanCompositeKey(cardNo,branchId,bookId));
		
		if(!result.isPresent()) 
		{
			return new ResponseEntity<BookLoan>(HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<BookLoan>(result.get(), HttpStatus.OK);
		}
	}

	@GetMapping(value = "/loan", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Iterable<BookLoan>> readBookLoanAll()
	{
		Iterable<BookLoan> result = admin.readBookLoanAll();
		
		// 200 regardless of if we found it or not, the query was successful, it means they can keep doing it
		if(!result.iterator().hasNext()) 
		{
			return new ResponseEntity<Iterable<BookLoan>>(result, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Iterable<BookLoan>>(result, HttpStatus.OK);
		}
	}
	
	/*************************************************
	 * 
	 * ALL UPDATE OPERATIONS
	 * 
	 *************************************************/
	
	@PutMapping(value = "/author/{authorId}", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
										produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Author> updateAuthor(@PathVariable Integer authorId, @RequestBody Author author)
	{
		if(author.getAuthorId() != null || author.getAuthorName() == null || "".contentEquals(author.getAuthorName())) 
		{
			System.out.println("i made it to the null check !");
			return new ResponseEntity<Author>(HttpStatus.BAD_REQUEST);
		}
		
		if(!admin.readAuthorById(authorId).isPresent()) 
		{
			return new ResponseEntity<Author>(HttpStatus.NOT_FOUND);
		}
		
		author.setAuthorId(authorId);
		
		return new ResponseEntity<Author>(admin.saveAuthor(author),HttpStatus.OK);
	}
	
	@PutMapping(value = "/publisher/{publisherId}",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
											consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Publisher> updatePublisher(@PathVariable Integer publisherId, @RequestBody Publisher publisher)
	{
		if(publisher.getPublisherId() != null || publisher.getPublisherName() == null || "".contentEquals(publisher.getPublisherName())
				|| publisher.getPublisherAddress() == null || "".contentEquals(publisher.getPublisherAddress())
				|| publisher.getPublisherPhone() == null || "".contentEquals(publisher.getPublisherPhone())) 
		{
			return new ResponseEntity<Publisher>(HttpStatus.BAD_REQUEST);
		}
		
		if(!admin.readPublisherById(publisherId).isPresent()) 
		{
			return new ResponseEntity<Publisher>(HttpStatus.NOT_FOUND);
		}
		
		publisher.setPublisherId(publisherId);

		return new ResponseEntity<Publisher>(admin.savePublisher(publisher), HttpStatus.OK);
	}
	
	@PutMapping(value = "/book/{bookId}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
									consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Book> updateBook(@PathVariable Integer bookId, @RequestBody Book book)
	{
		if(book.getBookId() != null || book.getTitle() == null || "".contentEquals(book.getTitle())
				|| book.getAuthorId() == null || book.getPublisherId() == null)
		{
			return new ResponseEntity<Book>(HttpStatus.BAD_REQUEST);
		}
		
		// check the entity exists
		if(!admin.readBookById(bookId).isPresent())
		{
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
		}
		
		// check new author exists
		if(!admin.readAuthorById(book.getAuthorId()).isPresent()) 
		{
			return new ResponseEntity<Book>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		// check new publisher exists
		if(!admin.readPublisherById(book.getPublisherId()).isPresent())
		{
			return new ResponseEntity<Book>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		book.setBookId(bookId);
		
		return new ResponseEntity<Book>(admin.saveBook(book), HttpStatus.OK);
	}
	
	@PutMapping(value = "/branch/{branchId}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
										consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<LibraryBranch> updateLibraryBranch(@PathVariable Integer branchId, @RequestBody LibraryBranch libraryBranch)
	{
		if(libraryBranch.getBranchId() != null || libraryBranch.getBranchName() == null || "".contentEquals(libraryBranch.getBranchName())
				|| libraryBranch.getBranchAddress() == null || "".contentEquals(libraryBranch.getBranchAddress()))
		{
			return new ResponseEntity<LibraryBranch>(HttpStatus.BAD_REQUEST);
		}
		
		if(!admin.readLibraryBranchById(branchId).isPresent()) 
		{
			return new ResponseEntity<LibraryBranch>(HttpStatus.NOT_FOUND);
		}
		
		libraryBranch.setBranchId(branchId);

		return new ResponseEntity<LibraryBranch>(admin.saveLibraryBranch(libraryBranch), HttpStatus.OK);
	}
	
	@PutMapping(value = "/borrower/{cardNo}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
									consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Borrower> updateBorrower(@PathVariable Integer cardNo, @RequestBody Borrower borrower)
	{
		if(borrower.getCardNo() != null || borrower.getName() == null || "".contentEquals(borrower.getName())
				|| borrower.getAddress() == null || "".contentEquals(borrower.getAddress())
				|| borrower.getPhone() == null || "".contentEquals(borrower.getPhone()))
		{
			return new ResponseEntity<Borrower>(HttpStatus.BAD_REQUEST);
		}
		
		if(!admin.readBorrowerById(cardNo).isPresent()) 
		{
			return new ResponseEntity<Borrower>(HttpStatus.NOT_FOUND);
		}
		
		borrower.setCardNo(cardNo);
		
		return new ResponseEntity<Borrower>(admin.saveBorrower(borrower), HttpStatus.OK);
	}
	
	@PutMapping(value = "/loan/borrower/{cardNo}/branch/{branchId}/book/{bookId}", 
			produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
			consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<BookLoan> updateBookLoan(@PathVariable("cardNo") Integer cardNo, @PathVariable("branchId") Integer branchId, @PathVariable("bookId") Integer bookId, @RequestBody BookLoan bookLoan)
	{
		// all IDs must be null in the body, and then assigned from the URI
		// dateOut must also be null, and is to be filled in using the existing data in the DB
		if(bookLoan.getCardNo() != null || bookLoan.getBranchId() != null || bookLoan.getBookId() != null || bookLoan.getDateOut() != null) 
		{
			return new ResponseEntity<BookLoan>(HttpStatus.BAD_REQUEST);
		}
		
		// once each ID exists, we need to check that entry exists and we need to retrieve the existing dateOut data
		Optional<BookLoan> existingData = admin.readBookLoanById(new BookLoanCompositeKey(cardNo,branchId,bookId));
		
		if(!existingData.isPresent()) 
		{
			return new ResponseEntity<BookLoan>(HttpStatus.NOT_FOUND);
		}
		
		bookLoan.setCardNo(cardNo);
		bookLoan.setBranchId(branchId);
		bookLoan.setBookId(bookId);
		bookLoan.setDateOut(existingData.get().getDateOut());

		return new ResponseEntity<BookLoan>(admin.saveBookLoan(bookLoan), HttpStatus.OK);
	}
	
	/*************************************************
	 * 
	 * ALL DELETE OPERATIONS
	 * 
	 *************************************************/
	
	@DeleteMapping(value = "/author/{authorId}")
	public ResponseEntity<HttpStatus> deleteAuthor(@PathVariable Integer authorId)
	{
		if(!admin.readAuthorById(authorId).isPresent()) 
		{
			return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		else 
		{
			admin.deleteAuthorById(authorId);
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping(value = "/publisher/{publisherId}")
	public ResponseEntity<HttpStatus> deletePublisher(@PathVariable Integer publisherId)
	{
		if(!admin.readPublisherById(publisherId).isPresent()) 
		{
			return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		else 
		{
			admin.deletePublisherById(publisherId);
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping(value = "/book/{bookId}")
	public ResponseEntity<HttpStatus> deleteBook(@PathVariable Integer bookId)
	{
		if(!admin.readBookById(bookId).isPresent()) 
		{
			return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		else 
		{
			admin.deleteBookById(bookId);
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping(value = "/branch/{branchId}")
	public ResponseEntity<HttpStatus> deleteLibraryBranch(@PathVariable Integer branchId)
	{
		if(!admin.readLibraryBranchById(branchId).isPresent()) 
		{
			return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		else 
		{
			admin.deleteLibraryBranchById(branchId);
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping(value = "/borrower/{cardNo}")
	public ResponseEntity<HttpStatus> deleteBorrower(@PathVariable Integer cardNo)
	{
		if(!admin.readBorrowerById(cardNo).isPresent())
		{
			return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		else 
		{
			admin.deleteBorrowerById(cardNo);
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		}
	}
}

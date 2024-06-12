package Tuan2.TranTanPhat.Services;

import Tuan2.TranTanPhat.Entities.Book;

import Tuan2.TranTanPhat.Repositories.IBookRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class BookService {
    private final IBookRepository bookRepository;
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    public List<Book> getAllBooks(Integer pageNo,
                                  Integer pageSize,
                                  String sortBy) {
        return bookRepository.findAllBooks(pageNo, pageSize, sortBy);
    }
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    public void addBook(Book book) {
        bookRepository.save(book);
    }
    public void updateBook(@NotNull Book book) {
        Book existingBook = bookRepository.findById(book.getId())
                .orElse(null);
        Objects.requireNonNull(existingBook).setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setCategory(book.getCategory());
        bookRepository.save(existingBook);}

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
    public List<Book> searchByTitleOrAuthor(Integer pageNo,
                                            Integer pageSize,
                                            String sortBy,
                                            String keyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return bookRepository.searchBook(keyword, pageable);
    }
    public List<Book> getAllBooksSorted(Integer pageNo,
                                        Integer pageSize,
                                        String sortBy) {
        // Check if sortBy is valid, otherwise default to "id"
        if (!Arrays.asList("id", "title", "author", "price", "category").contains(sortBy)) {
            sortBy = "id";
        }
        return bookRepository.findAllBooks(pageNo, pageSize, sortBy);
    }
}

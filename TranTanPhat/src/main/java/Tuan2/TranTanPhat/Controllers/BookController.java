package Tuan2.TranTanPhat.Controllers;
import Tuan2.TranTanPhat.daos.Item;
import Tuan2.TranTanPhat.Entities.Book;
import Tuan2.TranTanPhat.Entities.Category;
import Tuan2.TranTanPhat.Services.BookService;
import Tuan2.TranTanPhat.Services.CartService;
import Tuan2.TranTanPhat.Services.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import  java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService ;
    private final CartService cartService ;

    @GetMapping
    public String showAllBooks(
            @NotNull Model model,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Book> books=bookService.getAllBooks(pageNo, pageSize, sortBy);;
        model.addAttribute("books", books);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("totalPages",bookService.getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);

        return "Book/list";
    }
    @GetMapping("/add")
    public String addForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categories);
        return "Book/add";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book,
                          @NotNull BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories",
                    categoryService.getAllCategories());
            return "book/add";
        }
        bookService.addBook(book);
        return "redirect:/books";
    }
    @GetMapping("/edit/{id}")
    public String editBookForm(@NotNull Model model, @PathVariable long id)
    {
        var book = bookService.getBookById(id).orElse(null);
        model.addAttribute("book", book != null ? book : new Book());
        return "Book/edit";
    }
    @PostMapping("/edit")
    public String editBook(@ModelAttribute("book") Book book) {
        bookService.updateBook(book);
        return "redirect:/books";
    }
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        if (bookService.getBookById(id).isPresent())
            bookService.deleteBookById(id);
        return "redirect:/books";
    }
    @GetMapping("/detail/{id}")
    public String showBookDetail(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "Book/detail";
    }
    @GetMapping("/search")
    public String searchBooks(@RequestParam("keyword") String keyword,
                              @RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              @RequestParam(defaultValue = "id") String sortBy,
                              Model model) {
        List<Book> books = bookService.searchByTitleOrAuthor(pageNo, pageSize, sortBy, keyword);
        // Sắp xếp danh sách sách theo tiêu chí được chọn
        if ("id".equals(sortBy)) {
            books.sort(Comparator.comparing(Book::getId));
        } else if ("price".equals(sortBy)) {
            books.sort(Comparator.comparing(Book::getPrice));
        } else if ("title".equals(sortBy)) {
            books.sort(Comparator.comparing(Book::getTitle));
        } else if ("author".equals(sortBy)) {
            books.sort(Comparator.comparing(Book::getAuthor));
        } else if ("category".equals(sortBy)) {
            books.sort(Comparator.comparing(book -> book.getCategory().getName()));
        }

        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("totalPages", books.size() / pageSize);
        return "Book/list";
    }


    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
                            @RequestParam long id,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam(defaultValue = "1") int quantity)
    {
        var cart = cartService.getCart(session);
        cart.addItems(new Item(id, name, price, quantity));
        cartService.updateCart(session, cart);
        return "redirect:/books";
    }
}
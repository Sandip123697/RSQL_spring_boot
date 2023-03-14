package com.behl.dolores.service;

import static com.behl.dolores.utility.CommonUtil.isEmpty;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.behl.dolores.dto.RsqlSearchRequestDto;
import com.behl.dolores.dto.SearchResponseDto;
import com.behl.dolores.entity.Book;
import com.behl.dolores.properties.PaginationProperties;
import com.behl.dolores.repository.BookRepository;
import com.behl.dolores.rsql.CustomRsqlVisitor;
import com.behl.dolores.utility.PageableUtil;
import com.behl.dolores.utility.ResponseBuilder;
import com.behl.dolores.utility.SortingUtil;

import cz.jirutka.rsql.parser.RSQLParser;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@EnableConfigurationProperties(value = PaginationProperties.class)
public class BookService {

    private final PaginationProperties paginationProperties;
    private final BookRepository bookRepository;
    private final RSQLParser rsqlParser;
    private final CustomRsqlVisitor<Book> bookRsqlVisitor = new CustomRsqlVisitor<Book>();

    public SearchResponseDto retreive(final RsqlSearchRequestDto rsqlSearchRequestDto) {
        Page<Book> result;
        final String query = rsqlSearchRequestDto.getQuery();
        final Integer count = rsqlSearchRequestDto.getCount();
        final Integer page = rsqlSearchRequestDto.getPage();
        final String sort = rsqlSearchRequestDto.getSort();
        final Integer DEFAULT_COUNT = paginationProperties.getPagination().getDefaultCount();

        if (isEmpty(query)) {
            result = bookRepository.findAll(PageRequest.of(PageableUtil.getPageNumber(null, count),
                    PageableUtil.getCount(null, DEFAULT_COUNT), SortingUtil.build(sort)));
        } else {
            Specification<Book> specification = rsqlParser.parse(query).accept(bookRsqlVisitor);
            result = bookRepository.findAll(specification, PageRequest.of(PageableUtil.getPageNumber(page, count),
                    PageableUtil.getCount(count, DEFAULT_COUNT), SortingUtil.build(sort)));

            if (PageableUtil.exceedsTotalPageCount(result))
                result = bookRepository.findAll(specification,
                        PageRequest.of(PageableUtil.getPageNumber(result.getTotalPages(), count),
                                PageableUtil.getCount(count, DEFAULT_COUNT), SortingUtil.build(sort)));
        }
        return ResponseBuilder.build(result);
    }

}
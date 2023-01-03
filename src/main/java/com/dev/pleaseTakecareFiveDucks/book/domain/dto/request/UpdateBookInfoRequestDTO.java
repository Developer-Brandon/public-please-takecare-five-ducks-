package com.dev.pleaseTakecareFiveDucks.book.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateBookInfoRequestDTO {
    Integer bookNo;
    Integer madeNatureNo;
    Integer bookTypeNo;
    String title;
    String author;
    String link;
    String bookRegDt;
}

package com.dev.pleaseTakecareFiveDucks.book.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class SelectBookInfoRequestDTO {
    Integer bookNo;
}

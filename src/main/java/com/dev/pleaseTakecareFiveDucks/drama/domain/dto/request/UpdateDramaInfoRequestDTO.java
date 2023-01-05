package com.dev.pleaseTakecareFiveDucks.drama.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UpdateDramaInfoRequestDTO {
    Integer dramaNo;
    Integer madeNatureNo;
    String title;
    String author;
    String filePath;
    String fileName;
    Integer pagePerDramaCnt;
    String dramaRegDt;
}

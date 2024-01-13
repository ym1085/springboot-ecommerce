package com.shoppingmall.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProductFilesVO {
    private Long productFileId;
    private String originFileName;
    private String storedFileName;
    private String storedThumbNail;
    private String delegateThumbNail;
    private int fileSize;
    private LocalDateTime createDate;
    private String delYn;
    private int productId;

    @Builder
    public ProductFilesVO(
            Long productFileId,
            String originFileName,
            String storedFileName,
            String storedThumbNail,
            String delegateThumbNail,
            int fileSize,
            LocalDateTime createDate,
            String delYn,
            int productId
    ) {
        this.productFileId = productFileId;
        this.originFileName = originFileName;
        this.storedFileName = storedFileName;
        this.storedThumbNail = storedThumbNail;
        this.delegateThumbNail = delegateThumbNail;
        this.fileSize = fileSize;
        this.createDate = createDate;
        this.delYn = delYn;
        this.productId = productId;
    }
}

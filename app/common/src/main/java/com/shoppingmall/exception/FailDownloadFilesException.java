package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailDownloadFilesException extends CustomException {

    public FailDownloadFilesException(ErrorCode errorCode) {
        super(errorCode);
    }
}

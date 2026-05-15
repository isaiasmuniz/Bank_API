package com.muniz.isaias.bank_Api_restFull.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {
}

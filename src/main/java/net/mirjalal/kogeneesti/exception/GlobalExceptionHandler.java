package net.mirjalal.kogeneesti.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.mirjalal.kogeneesti.model.dto.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidFileException(InvalidFileException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(400, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(400, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponseDto(404, ex.getMessage(), LocalDateTime.now()));
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
    //     return ResponseEntity.internalServerError().body(new ErrorResponseDto(500, ex.getMessage(), LocalDateTime.now()));
    // }
}

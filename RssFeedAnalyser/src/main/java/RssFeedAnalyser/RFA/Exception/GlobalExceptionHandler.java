package RssFeedAnalyser.RFA.Exception;

import RssFeedAnalyser.RFA.Response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = SizeLimitMinimumException.class)
    public ResponseEntity<?> handleSizeLimitMinimumException(SizeLimitMinimumException exception, WebRequest request)
    {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CustomInternalException.class)
    public ResponseEntity<?> handleCustomInternalException(CustomInternalException exception, WebRequest request)
    {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NoAnalysisIdException.class)
    public ResponseEntity<?> handleNoAnalysisIdException(NoAnalysisIdException exception, WebRequest request)
    {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}

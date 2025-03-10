package technikal.task.fishmarket.exceptions.handler;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import technikal.task.fishmarket.exceptions.ProcessImageException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        return buildResponse(400, ex.getMessage(), ex, model);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException ex, Model model) {
        return buildResponse(404, ex.getMessage(), ex, model);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(Exception ex, Model model) {
        return buildResponse(404, "Сторінку не знайдено", ex, model);
    }

    @ExceptionHandler(ProcessImageException.class)
    public String handleProcessImage(ProcessImageException ex, Model model) {
        return buildResponse(500, ex.getMessage(), ex, model);
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        return buildResponse(500, "На серверi cталася помилка", ex, model);
    }

    private String buildResponse(int status, String message, Exception ex, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("status", status);
        log.error(ex.getMessage(), ex);
        return "error-page";
    }
}

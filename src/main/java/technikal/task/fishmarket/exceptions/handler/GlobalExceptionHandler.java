package technikal.task.fishmarket.exceptions.handler;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import technikal.task.fishmarket.exceptions.ProccessImageException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 404);
        log.error(ex.getMessage(), ex);
        return "error-page";
    }

    @ExceptionHandler(ProccessImageException.class)
    public String handleSavingImage(ProccessImageException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 500);
        log.error(ex.getMessage(), ex);
        return "error-page";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 400);
        log.error(ex.getMessage(), ex);
        return "error-page";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(Exception ex, Model model) {
        model.addAttribute("message", "Сторінку не знайдено");
        model.addAttribute("status", 404);
        log.error(ex.getMessage(), ex);
        return "error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("message", "Сталася помилка: " + ex.getMessage());
        model.addAttribute("status", 500);
        log.error(ex.getMessage(), ex);
        return "error-page";
    }
}

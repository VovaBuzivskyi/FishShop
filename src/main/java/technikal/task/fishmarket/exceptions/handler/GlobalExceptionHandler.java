package technikal.task.fishmarket.exceptions.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import technikal.task.fishmarket.exceptions.SavingImageException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 404);
        return "error-page";
    }

    @ExceptionHandler(SavingImageException.class)
    public String handleSavingImage(SavingImageException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 500);
        return "error-page";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 400);
        return "error-page";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(Exception ex, Model model) {
        model.addAttribute("message", "Сторінку не знайдено");
        model.addAttribute("status", 404);
        return "error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("message", "Сталася помилка: " + ex.getMessage());
        model.addAttribute("status", 500);
        return "error-page";
    }
}

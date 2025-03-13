package technikal.task.fishmarket.controllers;


import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import technikal.task.fishmarket.dtos.FishDto;
import technikal.task.fishmarket.services.FishService;

@Controller
@RequestMapping("/fish")
public class FishController {

    private final FishService fishService;

    public FishController(FishService fishService) {
        this.fishService = fishService;
    }

    @GetMapping({"", "/"})
    public String showFishList(Model model) {
        model.addAttribute("fishlist", fishService.getAllFishes());
        return "index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("fishDto", new FishDto());
        return "createFish";
    }

    @PostMapping("/create")
    public String addFish(@Valid @ModelAttribute FishDto fishDto, BindingResult result) {
        if (result.hasErrors()) {
            return "createFish";
        }

        try {
            fishService.saveFish(fishDto);
        } catch (IllegalArgumentException e) {
            result.addError(new FieldError("fishDto", "imageFiles", e.getMessage()));
            return "createFish";
        }

        return "redirect:/fish";
    }

    @GetMapping("/delete")
    public String deleteFish(@RequestParam int id) {
        fishService.deleteFish(id);
        return "redirect:/fish";
    }
}

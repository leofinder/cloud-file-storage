package com.craftelix.filestorage.controller;

import com.craftelix.filestorage.dto.user.UserSignupDto;
import com.craftelix.filestorage.exception.UserAlreadyExistException;
import com.craftelix.filestorage.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signin")
    public String showSigninForm() {
        return "user/signin";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("userSignupDto", new UserSignupDto());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String processSignupForm(@Valid @ModelAttribute("userSignupDto") UserSignupDto userSignupDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        try {
            userService.save(userSignupDto);
        } catch (UserAlreadyExistException e) {
            bindingResult.reject("userExists", e.getMessage());
            return "user/signup";
        }
        redirectAttributes.addFlashAttribute("successMessage", "You've successfully sign up. Please sign in to our app!");
        return "redirect:/signin";
    }

}

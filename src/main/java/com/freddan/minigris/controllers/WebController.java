package com.freddan.minigris.controllers;

import com.freddan.minigris.entities.User;
import com.freddan.minigris.services.UserService;
import com.freddan.minigris.services.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    private WebService webService;
    private UserService userService;

    @Autowired
    public WebController(WebService webService, UserService userService) {
        this.webService = webService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerAccount(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(User user) {
        String result = userService.createUser(user);
        if (result.equals("User was saved")) {
            return "redirect:/mydetails";
        } else {
            return result;
        }
    }

    @GetMapping("/mydetails")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String getMyDetails(Model model) {
        User userDetails = userService.findMyDetails();
        model.addAttribute("userinfo", userDetails);
        return "mydetails";
    }


}

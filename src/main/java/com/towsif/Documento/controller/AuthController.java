package com.towsif.Documento.controller;

import com.towsif.Documento.entity.UserEntity;
import com.towsif.Documento.repository.UserEntityRepository;
import com.towsif.Documento.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController
{
    private final UserEntityService userEntityService;

    private final UserEntityRepository userEntityRepository;

    public AuthController(UserEntityService userEntityService, UserEntityRepository userEntityRepository)
    {
        this.userEntityService = userEntityService;
        this.userEntityRepository = userEntityRepository;
    }

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model)
    {
        UserEntity user = new UserEntity();
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute UserEntity user,
                           BindingResult result,
                           Model model)
    {
        System.out.println("registration request");

        if(userEntityRepository.existsByEmail(user.getEmail()))
        {
            return "redirect:/register?fail";
        }

        if(result.hasErrors())
        {
            model.addAttribute("user", user);

            return "register";
        }

        System.out.println("user = " + user);

        userEntityService.saveUser(user);

        return "redirect:/documents?success";
    }
}

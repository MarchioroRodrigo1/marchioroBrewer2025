package com.marchioro.brewer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


import com.marchioro.brewer.dto.MenuItem;
import com.marchioro.brewer.service.MenuService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private MenuService menuService;

    @ModelAttribute("menu")
    public List<MenuItem> menu() {

       
        return menuService.getMenu();
    }
}
package ru.practicum.shareit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FavIconController {
    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
}
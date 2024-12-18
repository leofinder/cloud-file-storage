package com.craftelix.filestorage.controller;

import com.craftelix.filestorage.security.CustomUserDetails;
import com.craftelix.filestorage.service.BreadcrumbService;
import com.craftelix.filestorage.service.DataInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final DataInfoService dataInfoService;

    private final BreadcrumbService breadcrumbService;

    @GetMapping("/")
    public String showMainForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestParam(value = "path", defaultValue = "/") String path,
                               Model model) {

        model.addAttribute("dataInfoList", dataInfoService.findByPath(path, userDetails.getId()));
        model.addAttribute("breadcrumbs", breadcrumbService.build(path));
        return "main";
    }
}

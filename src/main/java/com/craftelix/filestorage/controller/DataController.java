package com.craftelix.filestorage.controller;

import com.craftelix.filestorage.dto.DataRenameRequestDto;
import com.craftelix.filestorage.dto.DataRequestDto;
import com.craftelix.filestorage.dto.DataStreamResponseDto;
import com.craftelix.filestorage.security.CustomUserDetails;
import com.craftelix.filestorage.service.FileManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {

    private final FileManagerService fileManagerService;

    @PostMapping
    public String create(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @Valid @ModelAttribute DataRequestDto dataRequestDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("validationErrors", getErrors(bindingResult));
            redirectAttributes.addFlashAttribute("validationAlertTitle", "Error Occurred While Creating Folder");
            return redirectToRefererOrHome(request);
        }

        fileManagerService.createFolder(dataRequestDto, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @PatchMapping("/rename")
    public String rename(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @Valid @ModelAttribute DataRenameRequestDto dataRenameRequestDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("validationErrors", getErrors(bindingResult));
            redirectAttributes.addFlashAttribute("validationAlertTitle", "Error Occurred While Rename File / Folder");
            return redirectToRefererOrHome(request);
        }

        fileManagerService.rename(dataRenameRequestDto, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @DeleteMapping
    public String delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @Valid @ModelAttribute DataRequestDto dataRequestDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("validationErrors", getErrors(bindingResult));
            redirectAttributes.addFlashAttribute("validationAlertTitle", "Error Occurred While Delete File / Folder");
            return redirectToRefererOrHome(request);
        }

        fileManagerService.delete(dataRequestDto, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @PostMapping("/upload/files")
    public String handleFilesUpload(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestParam("files") MultipartFile[] files,
                                    @RequestParam("parentPath") String parentPath,
                                    RedirectAttributes redirectAttributes,
                                    HttpServletRequest request) {

        if (areFilesNotValid(files)) {
            String validationError = "File validation failed: ensure file have valid name, are not empty, and meet size requirements.";
            redirectAttributes.addFlashAttribute("validationErrors", Collections.singletonList(validationError));
            redirectAttributes.addFlashAttribute("validationAlertTitle", "Error Occurred While Upload Files");
            return redirectToRefererOrHome(request);
        }

        fileManagerService.uploadFiles(files, parentPath, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @PostMapping("/upload/folder")
    public String handleFolderUpload(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestParam("folderFiles") MultipartFile[] files,
                                     @RequestParam("parentPath") String parentPath,
                                     RedirectAttributes redirectAttributes,
                                     HttpServletRequest request) {

        if (areFilesNotValid(files)) {
            String validationError = "Files validation failed: ensure all files have valid names, are not empty, and meet size requirements.";
            redirectAttributes.addFlashAttribute("validationErrors", Collections.singletonList(validationError));
            redirectAttributes.addFlashAttribute("validationAlertTitle", "Error Occurred While Upload Folder");
            return redirectToRefererOrHome(request);
        }

        fileManagerService.uploadFiles(files, parentPath, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @Valid @ModelAttribute DataRequestDto dataRequestDto) {

        DataStreamResponseDto dataStreamResponseDto = fileManagerService.download(dataRequestDto, userDetails.getId());
        String filename = UriUtils.encode(dataStreamResponseDto.getFilename(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dataStreamResponseDto.getResource());
    }

    private boolean areFilesNotValid(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return true;
        }

        for (MultipartFile file : files) {
            if (file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private String redirectToRefererOrHome(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    private List<String> getErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
    }
}

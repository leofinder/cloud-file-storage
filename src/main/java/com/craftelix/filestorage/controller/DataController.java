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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

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
            redirectAttributes.addFlashAttribute("errors", getErrors(bindingResult));
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
            redirectAttributes.addFlashAttribute("errors", getErrors(bindingResult));
            return redirectToRefererOrHome(request);
        }

        fileManagerService.rename(dataRenameRequestDto, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @DeleteMapping
    public String delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @Valid @ModelAttribute DataRequestDto dataRequestDto,
                         HttpServletRequest request) {

        fileManagerService.delete(dataRequestDto, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @PostMapping("/upload/files")
    public String handleFilesUpload(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @RequestParam("files") MultipartFile[] files,
                                   @RequestParam("parentPath") String parentPath,
                                   HttpServletRequest request) {

        fileManagerService.uploadFiles(files, parentPath, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @PostMapping("/upload/folder")
    public String handleFolderUpload(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @RequestParam("folderFiles") MultipartFile[] files,
                                   @RequestParam("parentPath") String parentPath,
                                   HttpServletRequest request) {

        fileManagerService.uploadFiles(files, parentPath, userDetails.getId());
        return redirectToRefererOrHome(request);
    }

    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @Valid @ModelAttribute DataRequestDto dataRequestDto) {

        DataStreamResponseDto dataStreamResponseDto = fileManagerService.download(dataRequestDto, userDetails.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + dataStreamResponseDto.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dataStreamResponseDto.getResource());
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

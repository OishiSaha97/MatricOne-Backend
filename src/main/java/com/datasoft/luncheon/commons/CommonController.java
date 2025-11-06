package com.datasoft.luncheon.commons;
import com.datasoft.luncheon.commons.model.filter.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @PostMapping("/{source}/upload-excel")
    public ResponseEntity<?> uploadExcel(@PathVariable String source, @RequestParam MultipartFile file,
                                         @RequestParam(required = false) Integer id) {
        return commonService.uploadExcel(source, file, id);
    }

    @PostMapping("/{source}/list")
    public ResponseEntity<?> getList(@PathVariable String source, @RequestBody SearchFilter searchFilter) {
        return commonService.getList(source, searchFilter);
    }

    @PostMapping("/{source}/get")
    public ResponseEntity<?> findById(@PathVariable String source, @RequestParam Integer id) {
        return commonService.findById(source, id);
    }

    @PostMapping("/{source}/update")
    public ResponseEntity<?> update(@PathVariable String source, @RequestBody Map<String, Object> data) {
        return commonService.update(source, data);
    }

    @PostMapping("/{source}/download-table")
    public void downloadTable(@PathVariable String source, @RequestBody SearchFilter searchFilter, HttpServletResponse response) {
        commonService.downloadTable(source, searchFilter, response);
    }


}

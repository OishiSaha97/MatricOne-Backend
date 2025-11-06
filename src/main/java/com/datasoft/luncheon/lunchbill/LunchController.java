package com.datasoft.luncheon.lunchbill;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/lunch-bill")
@RequiredArgsConstructor
public class LunchController {

    private final LunchBillService lunchBillService;

    @PostMapping("/get")
    public ResponseEntity<?> getLunchBill(@RequestParam String year, @RequestParam String month) {
        return new ResponseEntity<>(lunchBillService.getLunchBill(year, month, null), HttpStatus.OK);
    }

    @PostMapping("/getMealList")
    public ResponseEntity<?> getMealList(@RequestParam String users) {
        return new ResponseEntity<>(lunchBillService.getLunchList(users), HttpStatus.OK);
    }

    @PostMapping("/getVendorMeal")
    public ResponseEntity<?> getVendorMeal(@RequestParam String users, @RequestParam(required = false) String date) {
        return new ResponseEntity<>(lunchBillService.getVendorList(users, date), HttpStatus.OK);
    }

    @PostMapping("/downloadList")
    public void downloadLunch(@RequestParam String users, HttpServletResponse response) {
        lunchBillService.downloadLunch(users,response);
    }

    @PostMapping("/download")
    public void downloadBill(@RequestParam String year, @RequestParam String month,@RequestParam String users, HttpServletResponse response) {
        lunchBillService.downloadBill(year, month, users,response);
    }

    @PostMapping("/self/download")
    public void downloadMyBill(@RequestParam String year, @RequestParam String month,  HttpServletResponse response) {
        lunchBillService.downloadMyBill(year, month, response);
    }


}
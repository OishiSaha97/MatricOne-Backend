package com.datasoft.luncheon.guest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/guest")
@AllArgsConstructor
public class GuestController {
    private final GuestService guestService;


    @PostMapping("/meal/save")
    public ResponseEntity<?> saveGuest(@RequestBody List<Guest> guest ,@RequestParam String date) throws ParseException {
        return guestService.saveGuest( date, guest);
    }

    @GetMapping("/meal/show")
    public ResponseEntity<List<Guest>> showGuest(@RequestParam("date") Date date){
        return guestService.showGuest(date);
    }

}

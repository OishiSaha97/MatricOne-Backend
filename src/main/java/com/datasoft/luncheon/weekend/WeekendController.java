package com.datasoft.luncheon.weekend;

import com.datasoft.luncheon.commons.CommonController;
import com.datasoft.luncheon.commons.model.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/weekend")
public class WeekendController {

    private final WeekendService weekendService;
    private final CommonController commonService;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse> insertHoliday(@RequestBody List<Weekend> weekend){
        return weekendService.insertHoliday(weekend);
    }

    @GetMapping("/show")
    public ResponseEntity<List<Weekend>> showHoliday(){
        return weekendService.showHoliday();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteHoliday( @RequestParam Integer id){
         return weekendService.deleteHoliday(id);
    }
}




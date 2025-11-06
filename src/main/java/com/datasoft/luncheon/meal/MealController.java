package com.datasoft.luncheon.meal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping("/today")
    public ResponseEntity<?> findCurrentMeal(){
        return new ResponseEntity<>(mealService.findCurrentMeal(), HttpStatus.OK);
    }
    @GetMapping("/findByDate")
    public ResponseEntity<?> findMealDate(@RequestParam Date date){
        return new ResponseEntity<>(mealService.findMealDate(date), HttpStatus.OK);
    }
    @PostMapping("/{source}/update")
    public ResponseEntity<?> updateCurrentMeal(@PathVariable String source, @RequestParam String value){
        return mealService.updateCurrentMeal(source, value);
    }

    @PostMapping("/updateIdea")
    public ResponseEntity<?>updateCurrentIdea(@RequestParam String value){
        return mealService.updateCurrentIdea(value);
    }

    @GetMapping("/showIdea")
    public ResponseEntity<?>showCurrentIdea() {
        return mealService.showCurrentIdea();
    }


    @GetMapping("/lunch/menu")
    public ResponseEntity<?> findLunchMenu(){
        return mealService.findLunchMenu();
    }
    @PostMapping("/lunch/calender")
    public ResponseEntity<?> findBookedLunch(@RequestParam String startDate, @RequestParam String endDate){
        return mealService.findBookedLunch(startDate, endDate);
    }

//    @PostMapping("/lunch/book")
//    public ResponseEntity<?> bookLunch(@RequestBody List<Lunch> lunch){
//
//        return mealService.bookLunch(lunch);
//    }

    @PostMapping("/lunch/book")
    public ResponseEntity<?> bookLunch(@RequestBody Map<String, List<Lunch>> lunchMap) {
        // Access the three lists from the Map
        List<Lunch> updatedItems = lunchMap.get("updatedItems");
        List<Lunch> deletedItems = lunchMap.get("deletedItems");
        List<Lunch> newlyAddedItems = lunchMap.get("newlyAddedItems");

        // Call the service to handle the logic
        return mealService.bookLunch(updatedItems, deletedItems, newlyAddedItems);
    }

    @PostMapping("/lunch/update")
    public ResponseEntity<?> updateLunch(@RequestBody  Lunch lunch){
        return mealService.updateLunch(lunch);
    }

    @PostMapping("/lunch/adminFind")
    public ResponseEntity<?> findAdminLunch(@RequestParam String user, @RequestParam String date){
        return new ResponseEntity<>(mealService.findAdminLunch(user, date), HttpStatus.OK);
    }

    @PostMapping("/lunch/adminUpdate")
    public ResponseEntity<?> updateAdminLunch(@RequestParam String user,@RequestParam  String lunch, @RequestParam String date){
        return new ResponseEntity<>(mealService.updateAdminLunch(user,lunch, date), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteMeal( @RequestParam String date){
        return mealService.deleteMeal(date);
    }


    @GetMapping("/{year}/{month}/configured-lunch")
    public ResponseEntity<?> configuredLunch(@PathVariable String year, @PathVariable String month){
        return mealService.configuredLunch(year, month);
    }



}

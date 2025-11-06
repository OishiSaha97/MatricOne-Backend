package com.datasoft.luncheon.feedback;

import com.datasoft.luncheon.commons.model.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> insertFeedback(@RequestBody Feedback feedback){
        return feedbackService.insertFeedback(feedback);
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse> checkFeedbackForToday(){
        return feedbackService.checkFeedbackForToday();
    }

}

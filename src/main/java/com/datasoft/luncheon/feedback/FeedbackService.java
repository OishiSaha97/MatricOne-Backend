package com.datasoft.luncheon.feedback;

import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final JdbcFunctionDao jdbcFunctionDao;
    private final UserService userService;
    public ResponseEntity<ApiResponse> insertFeedback(Feedback feedback) {
        feedback.setCreatedBy(userService.getCurrentUserId());
        feedbackRepository.save(feedback);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse> checkFeedbackForToday() {
        return new ResponseEntity<>(new ApiResponse(200, "Fetched", jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT TIME(now()) > '14:00' AND (SELECT COUNT(id) FROM feedback WHERE DATE(created_at) = DATE(now()) AND LOWER(created_by) = ?) = 0",
                Boolean.class,
                userService.getCurrentUserId()
        )), HttpStatus.OK);
    }
}

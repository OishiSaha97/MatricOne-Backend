package com.datasoft.luncheon.guest;

import com.datasoft.luncheon.commons.CommonController;
import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final UserService userService;
    private final JdbcFunctionDao jdbcFunctionDao;
    private final JdbcTemplate jdbcTemplate;



    @Transactional
    public ResponseEntity<?> saveGuest(String dateString, List<Guest> guests) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateString);
        Date today = new Date();
        today.setTime(0); // Reset time to midnight for comparison

        if (date.before(today)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = userService.getCurrentUserId();
        Integer user = getUserId(userId);
        List<Guest> existingGuests = guestRepository.findByDateAndUserId(date, userId);

        // Handle Deletions
        if (existingGuests != null && !existingGuests.isEmpty()) {
            List<Integer> deletedGuestIds = guests.stream()
                    .filter(guest -> "deleted".equals(guest.getStatus()))
                    .map(Guest::getId)
                    .collect(Collectors.toList());

            if (!deletedGuestIds.isEmpty()) {
                insertIntoHistory(deletedGuestIds, user, date);
                deleteAllByIds(deletedGuestIds);
            }
        }

        if (Objects.nonNull(guests) && !guests.isEmpty()) {
            // Handle New Guests
            List<Guest> newGuests = guests.stream()
                    .filter(guest -> "new".equals(guest.getStatus()))
                    .peek(guest -> {
                        guest.setUserId(userId);
                        guest.setDate(date);
                        guest.setCreatedBy(user);
                    })
                    .collect(Collectors.toList());

            if (!newGuests.isEmpty()) {
                guestRepository.saveAll(newGuests);
            }

            // Handle Updated Guests
            List<Guest> updatedGuests = guests.stream()
                    .filter(guest -> "updated".equals(guest.getStatus()))
                    .collect(Collectors.toList());

            if (!updatedGuests.isEmpty()) {
                updatedGuests.forEach(guest -> updateGuestById(guest.getId(), guest.getLunch(), user));
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void insertIntoHistory(List<Integer> deletedGuestIds, Integer user, Date date) {
        String ids = deletedGuestIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String sql = "INSERT INTO his_guest (guest_id, guest_name, date, user_id, lunch, created_by, created_at, updated_by, updated_at, action_type, action_by) " +
                "SELECT id, guest_name, date, user_id, lunch, created_by, created_at, updated_by, updated_at, 'DELETED' action_type, ? as action_by\n"+
                "FROM guest WHERE id IN (" + ids + ")";

        jdbcTemplate.update(sql,user);
    }

    private void deleteAllByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        String sql = "DELETE FROM guest WHERE id IN (" +
                ids.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";
        jdbcTemplate.update(sql);
    }

    public ResponseEntity<List<Guest>> showGuest(Date date) {
        return new ResponseEntity<>(guestRepository.findByDateAndUserId(date, userService.getCurrentUserId()),HttpStatus.OK);
    }

    public void updateGuestById(Integer guestId, String lunch, Integer updatedBy) {
        String sql = "UPDATE guest SET lunch = ?, updated_by = ? WHERE id = ?";
        jdbcTemplate.update(sql, lunch, updatedBy, guestId);
    }


    public Integer getUserId(String userId) {
        return jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Integer.class,
                userId
        );
    }


//    public ResponseEntity<?> deleteGuest(Integer id) {
//        return new ResponseEntity<>(guestRepository.findById(id),HttpStatus.OK);
//    }
//    public ResponseEntity<?> deleteGuestsByDate(String date) {
//       return guestRepository.deleteByDate(date);
//    }
}

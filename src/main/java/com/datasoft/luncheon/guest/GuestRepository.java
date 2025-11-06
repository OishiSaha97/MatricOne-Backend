package com.datasoft.luncheon.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {


    //void deleteByDateAndUserId( Date date,Integer currentUserId);
    void deleteAllByDate(Date date);
    void deleteAllByDateAndUserId(Date date, String currentUserId);

    List<Guest> findByDateAndUserId(Date date, String currentUserId);

    //   ResponseEntity<?> deleteByDate(String date);

//    void deleteByDateAndUserId(List<Guest> guests);
}

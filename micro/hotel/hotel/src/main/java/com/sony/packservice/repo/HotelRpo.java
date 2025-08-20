package com.sony.packservice.repo;

import com.sony.packservice.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRpo extends JpaRepository<Hotel,Integer> {
}

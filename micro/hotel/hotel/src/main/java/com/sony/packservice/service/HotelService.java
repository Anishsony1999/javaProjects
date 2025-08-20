package com.sony.packservice.service;

import com.sony.packservice.dto.HotelDto;
import com.sony.packservice.model.Hotel;
import com.sony.packservice.repo.HotelRpo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    @Autowired
    HotelRpo repo;

    public void addHotel(Hotel hotel) {
        repo.save(hotel);
    }

    public List<Hotel> getAllHotels() {
        return repo.findAll();
    }

    public Hotel getHotelById(int id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Hotel Not Found"));
    }

    public void updateHotel(int id, Hotel hotel) {
        repo.save(hotel);
    }

    public void deleteHotel(int id) {
        repo.deleteById(id);
    }


}

package com.sony.packservice.controller;



import com.sony.packservice.model.Hotel;
import com.sony.packservice.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    @Autowired
    HotelService hotelService;

    // Add new hotel
    @PostMapping
    public ResponseEntity<String> addHotel(@RequestBody Hotel hotel) {
        hotelService.addHotel(hotel);
        return new ResponseEntity<>("Hotel added successfully", HttpStatus.CREATED);
    }

    // Get all hotels
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    // Get hotel by ID
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable int id) {
        Optional<Hotel> hotel = Optional.ofNullable(hotelService.getHotelById(id));
        return hotel.map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // Update hotel by ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updateHotel(@PathVariable int id, @ModelAttribute Hotel hotel) {
        try {
            hotelService.updateHotel(id, hotel);
            return new ResponseEntity<>("Hotel updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Hotel not found", HttpStatus.NOT_FOUND);
        }
    }

    // Delete hotel by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable int id) {
        try {
            hotelService.deleteHotel(id);
            return new ResponseEntity<>("Hotel deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Hotel not found", HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<Hotel>> searchHotels(@RequestParam(value = "city", required = false) String city,@RequestParam(value = "name", required = false) String name) {
//        List<Hotel> filteredHotels = hotelService.searchHotels(city,name);
//        return new ResponseEntity<>(filteredHotels, HttpStatus.OK);
//    }
}

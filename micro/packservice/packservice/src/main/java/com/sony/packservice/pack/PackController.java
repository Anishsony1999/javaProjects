package com.sony.packservice.pack;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/pack")
public class PackController {

    @Autowired
    private PackService service;

    @GetMapping()
    public ResponseEntity<List<Package>> getAllPack(){
        return new ResponseEntity<>(service.allPackage(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Package> createPack(@RequestBody Package pack){
        return new ResponseEntity<>(service.createPackage(pack),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackById(@PathVariable int id){
        return new ResponseEntity<>(service.packageById(id), HttpStatus.OK);
    }

}

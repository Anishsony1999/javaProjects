package com.sony.packservice.pack;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PackService {
    List<Package> allPackage();
    Package createPackage(Package pack);
    Package packageById(int id);
    Package packageUpdateById(int id,Package pack);
    void packDeleteById(int id);
}

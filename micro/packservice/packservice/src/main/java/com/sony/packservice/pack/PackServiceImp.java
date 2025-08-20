package com.sony.packservice.pack;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackServiceImp implements PackService{

    @Autowired
    private PackageRepo repo;

    @Override
    public List<Package> allPackage() {
        return repo.findAll();
    }

    @Override
    public Package createPackage(Package pack) {
        return repo.save(pack);
    }

    @Override
    public Package packageById(int id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Package packageUpdateById(int id, Package pack) {
        Package currentPack = repo.findById(id).orElse(null);
        if (currentPack !=null){
            currentPack.setPackName(pack.getPackName());
            currentPack.setDays(pack.getDays());
            return repo.save(currentPack);
        }
        return null;
    }

    @Override
    public void packDeleteById(int id) {
            repo.deleteById(id);
    }


}

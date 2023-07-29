package com.example.crowdfundingapp.campaigns;

import com.example.crowdfundingapp.campaigns.Campaign;
import com.example.crowdfundingapp.campaigns.CampaignRepository;
import com.example.crowdfundingapp.exceptions.UserDoesntExistException;
import com.example.crowdfundingapp.user.User;
import com.example.crowdfundingapp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    private final CampaignRepository entityRepo;
    private final UserRepository userRepo;

    @Autowired
    public CampaignService(CampaignRepository entityRepo, UserRepository userRepo) {
        this.entityRepo = entityRepo;
        this.userRepo = userRepo;
    }
    
    public List<Campaign> getAll() {
        return entityRepo.findAll();
    }

    public Optional<Campaign> getById(Long id) {
        return entityRepo.findById(id);
    }

    public Campaign add(Campaign c, Long ownerId) {
        User owner = userRepo.findById(ownerId).orElseThrow(() -> new UserDoesntExistException());
//        User owner = userRepo.findById(1l).get();
        c.setOwner(owner);
        return entityRepo.save(c);
    }

    public Campaign update(Campaign c) {
        return entityRepo.save(c);
    }

    public void deleteById(Long id) {
        entityRepo.deleteById(id);
    }
}

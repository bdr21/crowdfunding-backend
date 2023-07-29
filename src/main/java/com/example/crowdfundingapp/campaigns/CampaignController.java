package com.example.crowdfundingapp.campaigns;

import com.example.crowdfundingapp.campaigns.Campaign;
import com.example.crowdfundingapp.campaigns.CampaignService;
import com.example.crowdfundingapp.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/campaigns")
@CrossOrigin(origins = "http://localhost:4200")
public class CampaignController {

    private final CampaignService entityService;

    @Autowired
    public CampaignController(CampaignService entityService) {
        this.entityService = entityService;
    }

//    @Operation(security = { @SecurityRequirement(name = "BearerJWT") })
    @GetMapping
    public ResponseEntity<List<Campaign>> getAll() {
        List<Campaign> list = entityService.getAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getOne(@PathVariable Long id) {
        Optional<Campaign> existingInstance = entityService.getById(id);
//        return existingInstance.map(u -> ResponseEntity.ok(c))
        return existingInstance.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PostMapping("/x")
//    public ResponseEntity<Campaign> postOne() {
////        Long id = userDetails.getUserId();
//        Campaign c = new Campaign();
//        System.out.println("called");
//        Long id = 5l;
//        Campaign createdInstance = entityService.add(c,id);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstance);
//    }

    @PostMapping("")
    public ResponseEntity<Campaign> postOne(@RequestBody Campaign c, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getUserId();
        Campaign createdInstance = entityService.add(c,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateOne(@PathVariable Long id, @RequestBody Campaign c) {
//        Optional<Campaign> existingInstance = entityService.getById(id);
        c.setId(id);
        Campaign updatedInstance = entityService.update(c);
        return ResponseEntity.ok(updatedInstance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Campaign> deleteOne(@PathVariable Long id) {
        entityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

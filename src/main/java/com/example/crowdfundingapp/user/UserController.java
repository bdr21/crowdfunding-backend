package com.example.crowdfundingapp.user;

import com.example.crowdfundingapp.dto.BasicResponse;
import com.example.crowdfundingapp.dto.LoginRequest;
import com.example.crowdfundingapp.dto.ResponseWithToken;
import com.example.crowdfundingapp.dto.SignUpRequest;
import com.example.crowdfundingapp.responseMessages.ControllerMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService entityService;

    @Autowired
    public UserController(UserService entityService) {
        this.entityService = entityService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> list = entityService.getAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable Long id) {
        Optional<User> existingInstance = entityService.getById(id);
//        return existingInstance.map(u -> ResponseEntity.ok(u))
        return existingInstance.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> postOne(@RequestBody User u) {
        User createdInstance = entityService.add(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateOne(@PathVariable Long id, @RequestBody User u) {
//        Optional<User> existingInstance = entityService.getById(id);
        u.setId(id);
        User updatedInstance = entityService.update(u);
        return ResponseEntity.ok(updatedInstance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteOne(@PathVariable Long id) {
        entityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<BasicResponse> signUp(@RequestBody @Validated SignUpRequest signUpRequest) {
        entityService.signUp(signUpRequest);

        BasicResponse response = new BasicResponse(HttpStatus.OK, ControllerMessages.SIGN_UP_SUCCESS_MESSAGE);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWithToken> login(@RequestBody @Validated LoginRequest loginRequest , HttpServletResponse res) throws Exception {
        String token = entityService.login(loginRequest,res);
        ResponseWithToken response = new ResponseWithToken(HttpStatus.OK, ControllerMessages.LOG_IN_SUCCESS_MESSAGE, token);
        ResponseEntity<ResponseWithToken> re = new ResponseEntity<>(response, response.getStatus());
        return re;
    }
}

package reservAMF.Controller;

import dev.project.VinewoodStreet.config.TokenConfig;
import dev.project.VinewoodStreet.dto.request.LoginRequest;
import dev.project.VinewoodStreet.dto.request.RegisterRequest;
import dev.project.VinewoodStreet.dto.response.LoginResponse;
import dev.project.VinewoodStreet.dto.response.RegisterUserResponse;
import dev.project.VinewoodStreet.models.UserModel;
import dev.project.VinewoodStreet.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UsuarioRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.senha()); // autentica o usuário usando o email e senha

        Authentication authentication = authenticationManager.authenticate(userAndPass);

        UserModel user = (UserModel) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));


    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserModel newUser = new UserModel();
        newUser.setSenha(passwordEncoder.encode(request.senha()));
        newUser.setEmail(request.email());
        newUser.setNome(request.nome());

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(newUser.getNome(), newUser.getEmail()));

    }

}

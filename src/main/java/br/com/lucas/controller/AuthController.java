package br.com.lucas.controller;


import br.com.lucas.dto.AuthRequestDTO;
import br.com.lucas.dto.AuthResponseDTO;
import br.com.lucas.dto.RegisterRequestDTO;
import br.com.lucas.entity.Role;
import br.com.lucas.entity.User;
import br.com.lucas.repository.UserRepository;
import br.com.lucas.security.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints de login e registro")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)  // ou ADMIN se for admin
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponseDTO(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody @Valid AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponseDTO(jwtToken));
    }
}
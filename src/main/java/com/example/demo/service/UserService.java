package com.example.demo.service;

import com.example.demo.entity.Portfolio;
import com.example.demo.entity.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Value("${my.secret.key}")
    private String secretKey;

    private SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 새로운 회원을 등록합니다.
     *
     * @param user 등록할 회원 정보를 담고 있는 User 객체입니다.
     * @return 등록된 회원을 위한 JWT 토큰을 반환합니다.
     */
    public String signUp(User user) {
        validateNewUser(user);
        validateEmptyFields(user);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return createToken(user);
    }

    /**
     * 주어진 회원의 사용자 ID와 비밀번호, 닉네임이 비어있지 않은지 검증합니다.
     *
     * @param user 검증할 회원. 이는 등록할 회원 정보를 담고 있는 User 객체입니다.
     * @throws IllegalArgumentException 사용자 ID, 비밀번호, 닉네임이 비어있는 경우 예외를 발생시킵니다.
     */
    private void validateEmptyFields(User user) {
        String eMessage = "회원 가입 중 에러가 발생했습니다: ";
        String type = "";
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            type = "Username cannot be empty";
        } else if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            type = "User Password cannot be empty";
        } else if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
            type = "Nickname cannot be empty";
        } else {
            return;
        }
        throw new IllegalArgumentException(eMessage + type);
    }

    /**
     * 새로 가입하려는 회원의 유효성을 검증합니다.
     *
     * @param user 검증할 회원. 이는 가입하려는 회원의 정보를 담고 있는 객체입니다.
     * @throws IllegalStateException 이미 존재하는 사용자 ID일 경우 발생합니다.
     */
    private void validateNewUser(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalStateException("회원 가입 중 에러가 발생했습니다: User ID already exists");
        });
    }

    /**
     * 사용자 이름과 비밀번호를 이용하여 로그인합니다.
     *
     * @param username 사용자 아이디입니다.
     * @param password 사용자 비밀번호입니다.
     * @return 로그인한 회원을 위한 JWT 토큰을 반환합니다.
     * @throws IllegalStateException 사용자 이름 또는 비밀번호가 유효하지 않은 경우 예외를 발생시킵니다.
     */
    public String logIn(String username, String password) {
        User foundUser = validateUser(username);
        validatePassword(foundUser, password);
        String token = createToken(foundUser);
        return token;
    }

    /**
     * 주어진 회원의 비밀번호를 검증합니다.
     *
     * @param foundUser 검증할 회원객체
     * @param password 검증할 비밀번호
     * @throws IllegalStateException 회원이 존재하지 않거나 비밀번호가 일치하지 않을 경우 발생합니다.
     */
    private void validatePassword(User foundUser, String password) {
        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            throw new IllegalStateException("로그인 중 에러가 발생했습니다: Invalid password");
        }
    }

    /**
     * 회원을 위한 JWT 토큰을 생성합니다.
     *
     * @param user 토큰을 생성할 회원 정보를 담고 있는 User 객체입니다.
     * @return 생성된 JWT 토큰을 반환합니다.
     */
    private String createToken(User user){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(getSecretKeySpec())
                .compact();
    }

    /**
     * 주어진 사용자 이름과 관련된 포트폴리오를 찾습니다.
     *
     * @param username 사용자 이름
     * @return 사용자와 관련된 포트폴리오 목록
     */
    public List<Portfolio> findPortfolios(String username){
        User user = validateUser(username);
        return portfolioRepository.findByUser(user);
    }

    /**
     * 주어진 사용자 이름을 가진 사용자가 존재하는지 검증합니다.
     *
     * @param username 사용자 이름
     * @return 찾은 사용자
     * @throws NoSuchElementException 주어진 사용자 이름을 가진 사용자를 찾을 수 없는 경우
     */
    private User validateUser(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User does not exist"));
    }
}
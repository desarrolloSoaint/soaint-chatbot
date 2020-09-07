package com.soaint.controller;

import com.soaint.DTO.Mensaje;
import com.soaint.DTO.JwtDto;
import com.soaint.DTO.LoginUser;
import com.soaint.DTO.NewUser;
import com.soaint.entity.Rol;
import com.soaint.entity.UserPrincipal;
import com.soaint.entity.Users;
import com.soaint.security.jwt.JwtProvider;
import com.soaint.service.RolService;
import com.soaint.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "USERS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    //Save Usuario
    @PostMapping("/create")
    @ApiOperation(value = "Crear el Regitro de un Nuevo Usuario", notes = "Crea un Nuevo Usuario")
    public ResponseEntity<?> create(@Valid @RequestBody NewUser newUser, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados o email invalido"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(newUser.getEmail()))
            return new ResponseEntity(new Mensaje("El email ya existe"), HttpStatus.BAD_REQUEST);
        Users user =
                new Users(newUser.getEmail(),
                        passwordEncoder.encode(newUser.getPassword()), newUser.getRoles());
        user.setCreated_at(newUser.getCreated_at());
        user.setRoles(newUser.getRoles());
        userService.save(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }

    //Hacer Login Usuario Registro
    @PostMapping("/login")
    @ApiOperation(value = "Login de un Usuario Registrado", notes = "Iniciar Sesion con Credenciales")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUser loginUser, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!userService.existsByEmail(loginUser.getEmail()))
            return new ResponseEntity(new Mensaje("No autorizado"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        Integer idUser = UserPrincipal.getIdUser();
        JwtDto jwtDto = new JwtDto(idUser, jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }


}

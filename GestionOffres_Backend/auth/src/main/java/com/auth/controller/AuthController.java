package com.auth.controller;

import com.auth.dto.AuthenticationRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.auth.dto.ChangePasswordDto;
import com.auth.dto.EntrepriseDto;
import com.auth.dto.RoleDto;
import com.auth.dto.SignupRequest;
import com.auth.dto.UserDto;
import com.auth.entity.DemandeAjoutEntreprise;
import com.auth.entity.DemandeRejoindreEntreprise;
import com.auth.entity.Entreprise;
import com.auth.entity.Notification;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.exceptions.UserNotFoundException;
import com.auth.repository.UserRepository;
import com.auth.services.auth.AuthService;
import com.auth.services.auth.DemandeAjoutService;
import com.auth.services.auth.DemandeRejointService;
import com.auth.services.auth.EntrepriseService;
import com.auth.services.auth.NotificationService;
import com.auth.services.auth.RoleService;
import com.auth.services.jwt.UserDetailsServiceImpl;
import com.auth.utils.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpHeaders;

import org.springframework.format.annotation.DateTimeFormat;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	@Autowired
    private final NotificationService notificationService;
	@Autowired
    private EntrepriseService entrepriseService;
	  private final AuthenticationManager authenticationManager;
	  @Autowired
	    private final AuthService authService;
	  @Autowired
	    private final UserDetailsServiceImpl userDetailsService; // Inject UserDetailsService
	  @Autowired
	    private final JwtUtil jwtUtil;
	  @Autowired
	    private RoleService roleService;
	  @Autowired
	  private UserRepository userRepository;
	  @Autowired
	  private DemandeAjoutService entrepriseRequestService;
	  @Autowired 
	  private DemandeRejointService joinRequestService;
	 
	@PostMapping("/login")

	  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {

	      try {

	          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

	      } catch (BadCredentialsException e) {

	          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");

	      }



	      final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

	      Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());

	      if (optionalUser.isPresent()) {

	          User user = optionalUser.get();

	          if (!user.isEmailVerified()) {

	              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email not verified");

	          }



	          if (authService.checkIfPasswordNeedsUpdate(user)) {

	              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Password update required");

	          }



	          List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

	          final String jwt = jwtUtil.generateToken(user.getEmail(), roles);



	          // Set JWT to the response header

	          response.setHeader("Authorization", "Bearer " + jwt);



	          try {

	              // Convert byte array to Base64 string, handle null image

	              String base64Image = null;

	              if (user.getImg() != null) {

	                  base64Image = Base64.getEncoder().encodeToString(user.getImg());

	              }



	              JSONObject jsonResponse = new JSONObject();

	              jsonResponse.put("jwt", jwt); 

	              jsonResponse.put("userId", user.getId());

	              jsonResponse.put("roles", roles);

	              jsonResponse.put("userImage", base64Image); // Send Base64 string



	              return ResponseEntity.ok().headers(new HttpHeaders()).body(jsonResponse.toString());

	          } catch (JSONException e) {

	              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating JSON response");

	          }

	      } else {

	          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

	      }

	  }


	  @PostMapping(value = "/signup", consumes = "multipart/form-data")
	  public ResponseEntity<?> signupUser(
	            @RequestParam(value = "email", required = true) String email,
	            @RequestParam(value = "name", required = true) String name,
	            @RequestParam(value = "prenom", required = true) String prenom,
	            @RequestParam(value = "password", required = true) String password,
	            @RequestParam(value = "cin", required = true) String cin,
	            @RequestParam(value = "datenais", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date datenais,
	            @RequestParam(value = "lieunais", required = true) String lieunais,
	            @RequestParam(value = "img", required = true) MultipartFile img) {
	      try {
	          SignupRequest signupRequest = new SignupRequest();
	          signupRequest.setEmail(email);
	          signupRequest.setName(name);
	          signupRequest.setPrenom(prenom);
	          signupRequest.setPassword(password);
	          signupRequest.setCin(cin);
	          signupRequest.setDatenais(datenais);
	          signupRequest.setLieunais(lieunais);
	          signupRequest.setImg(img.getBytes()); // Convert MultipartFile to byte[]

	          UserDto userDto = authService.createUser(signupRequest);

	          return ResponseEntity.ok(userDto);
	      } catch (IOException e) {
	          return ResponseEntity.status(500).body("Error processing image");
	      } catch (Exception e) {
	          return ResponseEntity.status(500).body("Error creating user");
	      }
	  }
	  @GetMapping("/requestsbyuserid/{userId}")
	    public List<DemandeAjoutEntreprise> getRequestsByUserId(@PathVariable UUID userId) {
	        return entrepriseRequestService.getRequestsByUserId(userId);
	    }
   
	  @DeleteMapping("/delete/{id}")
	    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
	        try {
	            boolean deleted = authService.deleteUserById(id);
	            if (!deleted) {
	                return ResponseEntity.badRequest().body("User could not be deleted.");
	            }
	            return ResponseEntity.ok().body("User deleted successfully.");
	        } catch (UserNotFoundException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    @GetMapping("/roles")
	    public ResponseEntity<List<RoleDto>> getAllRoles() {
	        List<RoleDto> roles = roleService.getAllRoles();
	        return ResponseEntity.ok(roles);
	    }
	    @GetMapping("/user/{id}")
	    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
	        UserDto userDto = authService.getUserById(id);
	        return ResponseEntity.ok(userDto);
	    }

	    @PostMapping("/make-admin/{id}")
	    public ResponseEntity<?> makeAdmin(@PathVariable UUID id) {
	        UserDto userDto = authService.makeAdmin(id);
	        return userDto != null ? ResponseEntity.ok(userDto) : ResponseEntity.notFound().build();
	    }
	   
	    @PostMapping("/make-user/{id}")
	    public ResponseEntity<?> makeUser(@PathVariable UUID id) {
	        UserDto userDto = authService.makeUser(id);
	        return userDto != null ? ResponseEntity.ok(userDto) : ResponseEntity.notFound().build();
	    }

	    @GetMapping("/users")
	    public ResponseEntity<List<UserDto>> getAllUsers() {
	        List<UserDto> users = authService.getAllUsers();
	        return ResponseEntity.ok(users);
	    }
	    @GetMapping("/users/{name}")
	    public ResponseEntity<List<UserDto>> getUsersByName(@PathVariable String name) {
	        List<UserDto> users = authService.searchUsersByName(name);
	        return ResponseEntity.ok(users);
	    }
	    @PutMapping(value = "/updateuser/{id}", consumes = "multipart/form-data")
	    public ResponseEntity<?> updateUserById(
	            @PathVariable UUID id,
	            @RequestParam("email") String email,
	            @RequestParam("name") String name,
		          @RequestParam("prenom") String prenom,
	            @RequestParam("cin") String cin,
	            @RequestParam("datenais") @DateTimeFormat(pattern = "yyyy-MM-dd") Date datenais,
	            @RequestParam("lieunais") String lieunais,
	            @RequestParam(value = "img", required = false) MultipartFile img) {
	        try {
	            UserDto userDto = new UserDto();
	            userDto=authService.getUserById(id);
	            userDto.setId(id);
	            userDto.setEmail(email);
	            userDto.setName(name);
	            userDto.setPrenom(prenom);
	            userDto.setCin(cin);
	            userDto.setDatenais(datenais);
	            userDto.setLieunais(lieunais);
	            if (img != null && !img.isEmpty()) {
	                userDto.setImg(img.getBytes()); // Update with new image
	            } else {
	                userDto.setImg(userDto.getImg()); // Keep existing image
	            } // Convert MultipartFile to byte[]
	            String base64Image = null;
	            if (userDto.getImg() != null) {
	                base64Image = Base64.getEncoder().encodeToString(userDto.getImg());
	            }
	            ResponseEntity<?> updatedUser = authService.updateUserById(userDto);

	            JSONObject jsonResponse = new JSONObject();
	            jsonResponse.put("userId", userDto.getId());
	            jsonResponse.put("email", userDto.getEmail());
	            jsonResponse.put("name", userDto.getName());
	            jsonResponse.put("prenom", userDto.getPrenom());
	            jsonResponse.put("cin", userDto.getCin());
	            jsonResponse.put("datenais", userDto.getDatenais());
	            jsonResponse.put("lieunais", userDto.getLieunais());
	            jsonResponse.put("userImage", base64Image); // Send Base64 string

	            return ResponseEntity.ok(updatedUser);
	        } catch (IOException e) {
	            return ResponseEntity.status(500).body("Error processing image");
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("Error updating user");
	        }
	    }
	    @PostMapping("/utilisateur/update-password")
	    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
	        return authService.updatePasswordByEmail(changePasswordDto);
	    }


	    @PostMapping(value = "/user/{userId}/add-entreprise", consumes = "multipart/form-data", produces = "application/json")
	    public ResponseEntity<Map<String, String>> addEntrepriseToUser(
	            @PathVariable UUID userId,
	            @RequestParam("name") String name,
	            @RequestParam("adresse") String adresse,
	            @RequestParam("secteuractivite") String secteuractivite,
	            @RequestParam("Matricule") String Matricule,
	            @RequestParam("idcategorie") String idcategorie,
	            @RequestParam("ville") String ville,
	            @RequestParam("siegesociale") String siegesociale,
	            @RequestParam("codeTVA") String codeTVA,
	            @RequestParam("logo") MultipartFile logo,
	            @RequestParam("codetvadocument") MultipartFile codetvadocument,
	            @RequestParam("status") MultipartFile status) {

	        Map<String, String> response = new HashMap<>();
	        try {
	            // Create an instance of EntrepriseDto and set its fields
	            EntrepriseDto entrepriseDto = new EntrepriseDto();
	            entrepriseDto.setName(name);
	            entrepriseDto.setAdresse(adresse);
	            entrepriseDto.setSecteuractivite(secteuractivite);
	            entrepriseDto.setIdcategorie(idcategorie);
	            entrepriseDto.setMatricule(Matricule);
	            entrepriseDto.setVille(ville);
	            entrepriseDto.setSiegesociale(siegesociale);
	            entrepriseDto.setCodeTVA(codeTVA);
	            if (codetvadocument != null && !codetvadocument.isEmpty()) {
	                if (!codetvadocument.getContentType().equals("application/pdf")) {
	                    response.put("error", "Document must be a PDF file.");
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	                }
	                byte[] returnedDoc = codetvadocument.getBytes();
	                entrepriseDto.setCodetvadocument(returnedDoc);
	            }
	            if (status != null && !status.isEmpty()) {
	                if (!status.getContentType().equals("application/pdf")) {
	                    response.put("error", "Document must be a PDF file.");
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	                }
	                byte[] returnedDoc = status.getBytes();
	                entrepriseDto.setCodetvadocument(returnedDoc);
	            }
	            if (logo != null && !logo.isEmpty()) {
	                try {
	                    byte[] returnedImg = logo.getBytes(); // Converts the logo to a byte array
	                    entrepriseDto.setReturnedImg(returnedImg);
	                } catch (IOException e) {
	                    response.put("error", "Error reading logo file: " + e.getMessage());
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	                }
	            }

	            authService.addEntrepriseToUser(userId, entrepriseDto); // assuming this is the method name in AuthService
	            response.put("message", "Entreprise added successfully");
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("error", "Error adding entreprise: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	    }
	    @DeleteMapping("/entreprise/{id}")
	    public ResponseEntity<String> deleteEntreprise(@PathVariable UUID id) {
	        try {
	            System.out.println("Deleting enterprise with ID: " + id); // Log the ID
	            entrepriseService.deleteEntreprise(id);
	            return ResponseEntity.ok("Entreprise deleted successfully");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entreprise not found");
	        } catch (Exception e) {
	            System.out.println("Error deleting enterprise: " + e.getMessage()); // Log the error
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting entreprise");
	        }
	    }


	    @PutMapping("/entreprise/{id}")
	    public ResponseEntity<Map<String, String>> updateEntreprise(
	            @PathVariable UUID id,
	            @RequestParam("name") String name,
	            @RequestParam("adresse") String adresse,
	            @RequestParam("secteuractivite") String secteuractivite,
	            @RequestParam("Matricule") String Matricule,
	            @RequestParam("ville") String ville,
	            @RequestParam("idcategorie") String idcategorie,
	            @RequestParam("siegesociale") String siegesociale,
	            @RequestParam("codeTVA") String codeTVA,
	            @RequestParam(value="logo", required = false) MultipartFile logo,
	            @RequestParam(value="codetvadocument", required = false) MultipartFile codetvadocument,
	            @RequestParam(value="status", required = false) MultipartFile status) {

	        Map<String, String> response = new HashMap<>();
	        try {
	            // Fetch the existing Entreprise entity and convert to DTO
	            EntrepriseDto existingEntrepriseDto = entrepriseService.getEntrepriseById(id);
	            if (existingEntrepriseDto == null) {
	                response.put("error", "Entreprise not found");
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	            }

	            // Update the fields of the DTO
	            existingEntrepriseDto.setName(name);
	            existingEntrepriseDto.setIdcategorie(idcategorie);
	            existingEntrepriseDto.setAdresse(adresse);
	            existingEntrepriseDto.setSecteuractivite(secteuractivite);
	            existingEntrepriseDto.setMatricule(Matricule);
	            existingEntrepriseDto.setVille(ville);
	            existingEntrepriseDto.setSiegesociale(siegesociale);
	            existingEntrepriseDto.setCodeTVA(codeTVA);
	            if (codetvadocument != null && !codetvadocument.isEmpty()) {
	                if (!codetvadocument.getContentType().equals("application/pdf")) {
	                    response.put("error", "Document must be a PDF file.");
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	                }
	                byte[] returnedDoc = codetvadocument.getBytes();
	                existingEntrepriseDto.setCodetvadocument(returnedDoc);
	            }else {
	                // Retain the original logo if no new logo is provided
	                existingEntrepriseDto.setCodetvadocument(existingEntrepriseDto.getCodetvadocument());
	            }

	            if (status != null && !status.isEmpty()) {
	                if (!status.getContentType().equals("application/pdf")) {
	                    response.put("error", "Document must be a PDF file.");
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	                }
	                byte[] returnedDoc = status.getBytes();
	                existingEntrepriseDto.setStatus(returnedDoc);
	            }else {
	                // Retain the original logo if no new logo is provided
	                existingEntrepriseDto.setStatus(existingEntrepriseDto.getStatus());
	            }

	            if (logo != null && !logo.isEmpty()) {
	                try {
	                    byte[] returnedImg = logo.getBytes(); // Converts the logo to a byte array
	                    existingEntrepriseDto.setReturnedImg(returnedImg);
	                } catch (IOException e) {
	                    response.put("error", "Error reading logo file: " + e.getMessage());
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	                }
	            } else {
	                // Retain the original logo if no new logo is provided
	                existingEntrepriseDto.setReturnedImg(existingEntrepriseDto.getLogo());
	            }

	            // Update the entreprise
	            entrepriseService.updateEntreprise(id, existingEntrepriseDto);
	            response.put("message", "Entreprise updated successfully");
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("error", "Error updating entreprise: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	    }


	    @GetMapping("/entreprises")
	    public ResponseEntity<List<EntrepriseDto>> getAllEntreprises() {
	        List<EntrepriseDto> entreprises = entrepriseService.getAllEntreprises();
	        return ResponseEntity.ok(entreprises);
	    }
	    @GetMapping("/user/{userId}/entreprises")
	    public ResponseEntity<?> getEntreprisesByUserId(@PathVariable UUID userId) {
	        try {
	            List<EntrepriseDto> entreprises = authService.getEntreprisesByUserId(userId);
	            return ResponseEntity.ok(entreprises);
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        }
	    }
	    @GetMapping("/entreprise/{id}")
	    public ResponseEntity<?> getEntrepriseById(@PathVariable UUID id) {
	        try {
	            EntrepriseDto entrepriseDto = entrepriseService.getEntrepriseById(id);
	            return ResponseEntity.ok(entrepriseDto);
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entreprise not found: " + e.getMessage());
	        }
	    }


	    @PostMapping(value = "/user/{userId}/request-entreprise", consumes = "multipart/form-data", produces = "application/json")
	    public ResponseEntity<Map<String, String>> requestToAddEntreprise(
	            @PathVariable UUID userId,
	            @RequestParam("name") String name,
	            @RequestParam("adresse") String adresse,
	            @RequestParam("secteuractivite") String secteuractivite,
	            @RequestParam("Matricule") String Matricule,
	            @RequestParam("idcategorie") String idcategorie,
	            @RequestParam("ville") String ville,
	            @RequestParam("siegesociale") String siegesociale,
	            @RequestParam("codeTVA") String codeTVA,
	            @RequestParam("logo") MultipartFile logo,
	            @RequestParam("codetvadocument") MultipartFile codetvadocument,
	            @RequestParam("status") MultipartFile status) {

	        Map<String, String> response = new HashMap<>();
	        try { String matriculeRegex = "^[0-9]{7}[A-Za-z]{3}[0-9]{3}$";
	        if (!Matricule.matches(matriculeRegex)) {
	            response.put("error", "Invalid Matricule format.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	            // Create an instance of DemandeAjoutEntreprise and set its fields
	            DemandeAjoutEntreprise demandeAjout = new DemandeAjoutEntreprise();
	            demandeAjout.setNom(name);
	            demandeAjout.setAdresse(adresse);
	            demandeAjout.setSecteuractivite(secteuractivite);
	            demandeAjout.setIdcategorie(idcategorie);
	            demandeAjout.setMatricule(Matricule);
	            demandeAjout.setVille(ville);
	            demandeAjout.setSiegesociale(siegesociale);
	            demandeAjout.setCodeTVA(codeTVA);// Set the initial status as NEW
	            demandeAjout.setUserId(userId);	   
	            if (codetvadocument != null && !codetvadocument.isEmpty()) {
	                if (!codetvadocument.getContentType().equals("application/pdf")) {
	                    response.put("error", "Document must be a PDF file.");
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	                }
	                byte[] returnedDoc = codetvadocument.getBytes();
	                demandeAjout.setCodetvadocument(returnedDoc);
	            }
	            if (status != null && !status.isEmpty()) {
	                if (!status.getContentType().equals("application/pdf")) {
	                    response.put("error", "Document must be a PDF file.");
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	                }
	                byte[] returnedDoc = status.getBytes();
	                demandeAjout.setStatus(returnedDoc);
	            }
	            if (logo != null && !logo.isEmpty()) {
	                byte[] logoBytes = logo.getBytes(); // Converts the logo to a byte array
	                demandeAjout.setLogo(logoBytes);
	            }

	            entrepriseRequestService.saveRequest(demandeAjout); // assuming this is the method name in EnterpriseRequestService
	            response.put("message", "Entreprise request submitted successfully");
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("error", "Error submitting enterprise request: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	    }
	    @PostMapping("/approve-request/{userId}/{requestId}")
	    public ResponseEntity<Map<String, String>> approveRequest(@PathVariable UUID userId, @PathVariable UUID requestId) {
	        Map<String, String> response = new HashMap<>();
	        try {
	            DemandeAjoutEntreprise request = entrepriseRequestService.approveRequest(requestId);
	            
	            if (request != null ) {
	                EntrepriseDto entrepriseDto = new EntrepriseDto();
	                entrepriseDto.setName(request.getNom());
	                entrepriseDto.setAdresse(request.getAdresse());
	                entrepriseDto.setSecteuractivite(request.getSecteuractivite());
	                entrepriseDto.setMatricule(request.getMatricule());
	                entrepriseDto.setVille(request.getVille());
	                entrepriseDto.setSiegesociale(request.getSiegesociale());
	                entrepriseDto.setIdcategorie(request.getIdcategorie());
	                entrepriseDto.setCodeTVA(request.getCodeTVA());
	                entrepriseDto.setReturnedImg(request.getLogo()); // Assuming logo is stored as byte[] in request
	                entrepriseDto.setCodetvadocument(request.getCodetvadocument());
	                entrepriseDto.setStatus(request.getStatus());
	                authService.addEntrepriseToUser(userId, entrepriseDto); 
	                entrepriseRequestService.deleteRequest(requestId);
	                roleService.addRoleToUser(userId, "ROLE_ADMIN");
	                response.put("message", "Enterprise approved and added successfully");
	                return ResponseEntity.ok(response);
	            } else {
	                response.put("error", "Request could not be found or was not in a valid state for approval.");
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	            }
	        } catch (Exception e) {
	            response.put("error", "Error approving enterprise request: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    } @GetMapping("/verify")
	    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
	        User user = userRepository.findByEmailVerificationToken(token);
	        if (user == null) {
	            return ResponseEntity.badRequest().body("Invalid token");
	        }

	        Date now = new Date();
	        long differenceInMillis = now.getTime() - user.getTokenCreationDate().getTime();
	        long differenceInMinutes = differenceInMillis / (60 * 1000);

	        if (differenceInMinutes > 30) {
	            // Token expired
	            user.setEmailVerificationToken(authService.generateVerificationToken());
	            user.setTokenCreationDate(new Date());
	            userRepository.save(user);
	            authService.sendVerificationEmail(user);
	            return ResponseEntity.badRequest().body("Token expired. A new verification email has been sent.");
	        }

	        user.setEmailVerified(true);
	        user.setEmailVerificationToken(null); // clear the token
	        userRepository.save(user);

	        return ResponseEntity.ok("Email verified successfully");
	    }

	  @GetMapping("/download/codetva/{id}")
public ResponseEntity<byte[]> downloadCodeTVADocument(@PathVariable UUID id) {
    Optional<DemandeAjoutEntreprise> demandeOptional = entrepriseRequestService.getRequestById(id);
    if (demandeOptional.isPresent()) {
        DemandeAjoutEntreprise demande = demandeOptional.get();
        if (demande.getCodetvadocument() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"CodeTVA_" + id.toString() + ".pdf\"")
                    .body(demande.getCodetvadocument());
        } else {
            return ResponseEntity.noContent().build();
        }
    } else {
        return ResponseEntity.notFound().build();
    }
}
@GetMapping("/utilisateur/{userId}/password-change-necessity")
public ResponseEntity<Integer> informUserOfPasswordChangeNecessity(@PathVariable UUID userId) {
    User user = authService.findUserById(userId);
    if (user == null) {
        return ResponseEntity.notFound().build(); // Return 404 if user not found
    }

    int remainingDays = authService.getRemainingDaysForPasswordChange(user);
    return ResponseEntity.ok(remainingDays);
}
@GetMapping("/download/status/{id}")
public ResponseEntity<byte[]> downloadStatusDocument(@PathVariable UUID id) {
    Optional<DemandeAjoutEntreprise> demandeOptional = entrepriseRequestService.getRequestById(id);
    if (demandeOptional.isPresent()) {
        DemandeAjoutEntreprise demande = demandeOptional.get();
        if (demande.getStatus() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Status_" + id.toString() + ".pdf\"")
                    .body(demande.getStatus());
        } else {
            return ResponseEntity.noContent().build();
        }
    } else {
        return ResponseEntity.notFound().build();
    }
}

	    @PostMapping("/rejectrequest/{id}")
	    public ResponseEntity<Void> rejectRequest(@PathVariable UUID id) {
	    	entrepriseRequestService.rejectRequest(id);
	    	entrepriseRequestService.deleteRequest(id);
	        return ResponseEntity.ok().build();
	    }
	    @PostMapping("/deleterequest/{id}")
	    public ResponseEntity<Void> deleterequest(@PathVariable UUID id) {
	    	entrepriseRequestService.deleteRequest(id);
	        return ResponseEntity.ok().build();
	    }
	    @GetMapping("createrequests")
	    public ResponseEntity<List<DemandeAjoutEntreprise>> getAllPendingRequests() {
	        return ResponseEntity.ok(entrepriseRequestService.getAllPendingRequests());
	    }
	    @PostMapping("/user/{userId}/join-request")
	    public ResponseEntity<?> requestJoinEntreprise(@PathVariable UUID userId, @RequestParam String entrepriseMatricule) {
	        try {
	            joinRequestService.createJoinRequest(userId, entrepriseMatricule);
	            return ResponseEntity.ok().body("Join request sent successfully.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error submitting join request: " + e.getMessage());
	        }
	    }

	    @PostMapping("/approve-join-request/{requestId}")
	    //@PreAuthorize("hasAuthority('ROLE_ADMIN')") 
	    public ResponseEntity<?> approveJoinRequest(@PathVariable UUID requestId) {
	        try {
	            joinRequestService.approveJoinRequest(requestId);
	            joinRequestService.deleteRequest(requestId);
	            return ResponseEntity.ok().body("Join request approved.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error approving join request: " + e.getMessage());
	        }
	    }

	    @PostMapping("/reject-join-request/{requestId}")
	   // @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
	    public ResponseEntity<?> rejectJoinRequest(@PathVariable UUID requestId) {
	        try {
	            joinRequestService.rejectJoinRequest(requestId);
	            joinRequestService.deleteRequest(requestId);
	            return ResponseEntity.ok().body("Join request rejected.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error rejecting join request: " + e.getMessage());
	        }
	    }
	    @GetMapping("/join-requests")
	    public ResponseEntity<List<DemandeRejoindreEntreprise>> getAllJoinRequests(@RequestParam String userId) {
	        List<DemandeRejoindreEntreprise> joinRequests = joinRequestService.getAllJoinRequests(userId);
	        return ResponseEntity.ok(joinRequests);
	    }

	    @GetMapping("/creation-requests")
	    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	    public ResponseEntity<List<DemandeAjoutEntreprise>> getAllCreationRequests() {
	        List<DemandeAjoutEntreprise> creationRequests = entrepriseRequestService.getAllPendingRequests();
	        return ResponseEntity.ok(creationRequests);
	    }
  //Notificationsssssssss
	    
	    @PostMapping("/notification")
	    public ResponseEntity<Notification> addNotification(@RequestBody Notification notification) {
	        // Ensure the entreprise is set
	        Entreprise entreprise = entrepriseService.findById(notification.getEntreprise().getId());
	        if (entreprise != null) {
	            notification.setEntreprise(entreprise);
	        }
	        
	        Notification createdNotification = notificationService.addNotification(notification);
	        return ResponseEntity.status(201).body(createdNotification);
	    }
	    @DeleteMapping("/notification/appeloffre/{idAppelOffre}")
	    public void deleteNotificationsByAppelOffreId(@PathVariable UUID idAppelOffre) {
	        notificationService.deleteNotificationsByAppelOffreId(idAppelOffre);
	    }
	    @DeleteMapping("/notification/{id}")
	    public ResponseEntity<Void> deleteNotification(@PathVariable UUID id) {
	        notificationService.deleteNotification(id);
	        return ResponseEntity.noContent().build();
	    }
	    @GetMapping("/notification/user")
	    public ResponseEntity<List<Notification>> getNotificationsByCategorieIdAndNotEntrepriseId(
	            @RequestParam UUID categorieId,
	            @RequestParam UUID entrepriseId) {
	        List<Notification> notifications = notificationService.getNotificationsByCategorieIdAndNotEntrepriseId(categorieId, entrepriseId);
	        return ResponseEntity.ok(notifications);
	    }
	    @GetMapping("/notification/appeloffre/{idAppelOffre}")
	    public List<Notification> getNotificationsByAppelOffreId(@PathVariable UUID idAppelOffre) {
	        return notificationService.getNotificationsByAppelOffreId(idAppelOffre);
	    }
}

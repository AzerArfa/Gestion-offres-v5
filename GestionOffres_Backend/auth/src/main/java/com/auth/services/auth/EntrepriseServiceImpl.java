package com.auth.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.dto.EntrepriseDto;
import com.auth.dto.UserDto;
import com.auth.entity.Entreprise;
import com.auth.entity.User;
import com.auth.repository.EntrepriseRepository;
import com.auth.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EntrepriseServiceImpl implements EntrepriseService {

    @Autowired
    private EntrepriseRepository entrepriseRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Entreprise createEntreprise(EntrepriseDto entrepriseDto) {
        // Check if an enterprise with the same matricule already exists
        Optional<Entreprise> existingEntreprise = entrepriseRepository.findByMatricule(entrepriseDto.getMatricule());
        if (existingEntreprise.isPresent()) {
            throw new IllegalArgumentException("An enterprise with the same matricule already exists");
        }

        // If no existing enterprise is found, proceed to create a new one
        Entreprise entreprise = new Entreprise(); // Assume constructor or conversion logic handles this
        entreprise.setNom(entrepriseDto.getName());
        entreprise.setAdresse(entrepriseDto.getAdresse());
        entreprise.setSecteuractivite(entrepriseDto.getSecteuractivite());
        entreprise.setLogo(entrepriseDto.getReturnedImg());
        entreprise.setIdcategorie(entrepriseDto.getIdcategorie());
        entreprise.setCodetvadocument(entrepriseDto.getCodetvadocument());
        entreprise.setStatus(entrepriseDto.getStatus());
        entreprise.setMatricule(entrepriseDto.getMatricule());
        entreprise.setVille(entrepriseDto.getVille());
        entreprise.setCodeTVA(entrepriseDto.getCodeTVA());
        entreprise.setSiegesociale(entrepriseDto.getSiegesociale());
        
        return entrepriseRepository.save(entreprise);
    }


//    @Override
//    public EntrepriseDto updateEntreprise(UUID id, EntrepriseDto entrepriseDto) {
//        Entreprise entreprise = entrepriseRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("Entreprise not found"));
//        updateEntrepriseFromDto(entreprise, entrepriseDto);
//        entreprise = entrepriseRepository.save(entreprise);
//        return convertToDto(entreprise);
//    }

    @Override
    @Transactional
    public void deleteEntreprise(UUID entrepriseId) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
            .orElseThrow(() -> new IllegalArgumentException("Entreprise not found"));

        // Remove the association from users
        List<User> users = userRepository.findByEntreprisesContains(entreprise);
        for (User user : users) {
            user.getEntreprises().remove(entreprise);
            userRepository.save(user);
        }

        // Delete the entreprise
        entrepriseRepository.delete(entreprise);
    }
    @Override
    @Transactional
    public void updateEntreprise(UUID entrepriseId, EntrepriseDto entrepriseDto) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
            .orElseThrow(() -> new IllegalArgumentException("Entreprise not found"));

        entreprise.setNom(entrepriseDto.getName());
        entreprise.setAdresse(entrepriseDto.getAdresse());
        entreprise.setSecteuractivite(entrepriseDto.getSecteuractivite());
        entreprise.setMatricule(entrepriseDto.getMatricule());
        entreprise.setIdcategorie(entrepriseDto.getIdcategorie());
        entreprise.setVille(entrepriseDto.getVille());
        entreprise.setSiegesociale(entrepriseDto.getSiegesociale());
        entreprise.setCodeTVA(entrepriseDto.getCodeTVA());
        entreprise.setLogo(entrepriseDto.getReturnedImg());
        entreprise.setCodetvadocument(entrepriseDto.getCodetvadocument());
        entreprise.setStatus(entrepriseDto.getStatus());
        entrepriseRepository.save(entreprise);
    }


    @Override
    public EntrepriseDto getEntrepriseById(UUID id) {
        Entreprise entreprise = entrepriseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entreprise not found"));
        return convertToDto(entreprise);
    }

    @Override
    public List<EntrepriseDto> getAllEntreprises() {
        return entrepriseRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    private EntrepriseDto convertToDto(Entreprise entreprise) {
        EntrepriseDto dto = new EntrepriseDto();
        dto.setId(entreprise.getId());
        dto.setName(entreprise.getNom());
        dto.setAdresse(entreprise.getAdresse());
        dto.setLogo(entreprise.getLogo());
        dto.setSecteuractivite(entreprise.getSecteuractivite());
        dto.setIdcategorie(entreprise.getIdcategorie());
        dto.setMatricule(entreprise.getMatricule());
        dto.setVille(entreprise.getVille());
        dto.setCodeTVA(entreprise.getCodeTVA());
        dto.setSiegesociale(entreprise.getSiegesociale());
        dto.setCodetvadocument(entreprise.getCodetvadocument());
        dto.setStatus(entreprise.getStatus());
        if (entreprise.getUsers() != null) {
            List<UserDto> userDtos = entreprise.getUsers().stream()
                                        .map(this::convertUserToDto)
                                        .collect(Collectors.toList());
            dto.setUsers(userDtos);
        }
        return dto;
    }

    private void updateEntrepriseFromDto(Entreprise entreprise, EntrepriseDto dto) {
        entreprise.setNom(dto.getName());
        entreprise.setAdresse(dto.getAdresse());
        entreprise.setSecteuractivite(dto.getSecteuractivite());
        entreprise.setMatricule(dto.getMatricule());
        entreprise.setVille(dto.getVille());
        entreprise.setIdcategorie(dto.getIdcategorie());
        entreprise.setCodetvadocument(dto.getCodetvadocument());
        entreprise.setStatus(dto.getStatus());
    }

    private UserDto convertUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPrenom(user.getPrenom());
        dto.setImg(user.getImg());
        // Add other fields if necessary
        return dto;
    }
 
}

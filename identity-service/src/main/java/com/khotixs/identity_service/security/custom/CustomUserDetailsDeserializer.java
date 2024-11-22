package com.khotixs.identity_service.security.custom;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.khotixs.identity_service.domain.Authority;
import com.khotixs.identity_service.domain.Role;
import com.khotixs.identity_service.domain.User;
import com.khotixs.identity_service.domain.UserRole;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CustomUserDetailsDeserializer extends JsonDeserializer<CustomUserDetails> {

    private static final TypeReference<Set<Authority>> AUTHORITY_SET_TYPE = new TypeReference<>() {};
    private static final TypeReference<Set<Role>> ROLE_SET_TYPE = new TypeReference<>() {};

    private static final TypeReference<Set<UserRole>> SIMPLE_GRANTED_AUTHORITY_LIST = new TypeReference<>() {
    };
    @Override
    public CustomUserDetails deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode rootNode = mapper.readTree(p);

        // Read "user" node
        JsonNode userJsonNode = readJsonNode(rootNode, "user");
        if (userJsonNode.isMissingNode()) {
            log.error("Missing 'user' field in JSON");
            throw new JsonMappingException(p, "Missing 'user' field in JSON");
        }

        // Extract fields from JSON
        Long id = readJsonNode(userJsonNode, "id").asLong();
        String uuid = readJsonNode(userJsonNode, "uuid").asText();
        String username = readJsonNode(userJsonNode, "username").asText();
        String email = readJsonNode(userJsonNode, "email").asText();
        String password = readJsonNode(userJsonNode, "password").asText();
        boolean isEnabled = readJsonNode(userJsonNode, "isEnabled").asBoolean();
        boolean accountNonExpired = readJsonNode(userJsonNode, "accountNonExpired").asBoolean();
        boolean credentialsNonExpired = readJsonNode(userJsonNode, "credentialsNonExpired").asBoolean();
        boolean accountNonLocked = readJsonNode(userJsonNode, "accountNonLocked").asBoolean();

        String familyName = readJsonNode(userJsonNode, "familyName").asText();
        String givenName = readJsonNode(userJsonNode, "givenName").asText();
        String dobString = readJsonNode(userJsonNode, "dob").asText(null);
        LocalDate dob = (dobString != null && !dobString.isEmpty()) ? parseDate(dobString) : null;

        String gender = readJsonNode(userJsonNode, "gender").asText(null);
        String profileImage = readJsonNode(userJsonNode, "profileImage").asText(null);
        boolean emailVerified = readJsonNode(userJsonNode, "emailVerified").asBoolean();
        String ipAddress = readJsonNode(userJsonNode, "ipAddress").asText(null);
        String phoneNumber = readJsonNode(userJsonNode, "phoneNumber").asText(null);

        // Deserialize roles and authorities
//        Set<Role> roles = mapper.convertValue(readJsonNode(userJsonNode, "roles"), new TypeReference<Set<Role>>() {});
//        Set<Authority> userAuthorities = new HashSet<>();
//        roles.forEach(role -> userAuthorities.addAll(role.getAuthorities()));

        Set<UserRole> userRoles = mapper.convertValue(
                rootNode.get("userRoles"),
                SIMPLE_GRANTED_AUTHORITY_LIST
        );

        // Populate User object
        User user = new User();
        user.setId(id);
        user.setUuid(uuid);
        user.setUsername(username);
        user.setUserRoles(userRoles);
        user.setEmail(email);
        user.setPassword(password);
        user.setFamilyName(familyName);
        user.setGivenName(givenName);
        user.setProfileImage(profileImage);
        user.setDob(dob);
        user.setGender(gender);
        user.setPhoneNumber(phoneNumber);
        user.setEmailVerified(emailVerified);
        user.setIsEnabled(isEnabled);
        user.setCredentialsNonExpired(credentialsNonExpired);
        user.setAccountNonLocked(accountNonLocked);
        user.setAccountNonExpired(accountNonExpired);
        user.setIpAddress(ipAddress);

        log.info("User details deserialized: {}", user);

        // Populate CustomUserDetails
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);

        return customUserDetails;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

    private LocalDate parseDate(String date) {
        try {
            return date != null ? LocalDate.parse(date) : null;
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", date, e);
            return null;
        }
    }

    private LocalDateTime parseDateTime(String dateTime) {
        try {
            return dateTime != null ? LocalDateTime.parse(dateTime) : null;
        } catch (DateTimeParseException e) {
            log.error("Invalid datetime format: {}", dateTime, e);
            return null;
        }
    }
}

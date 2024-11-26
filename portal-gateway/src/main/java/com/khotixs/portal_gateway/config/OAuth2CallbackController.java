//package com.khotixs.portal_gateway.config;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class OAuth2CallbackController {
//
//    private final OAuth2AuthorizedClientService authorizedClientService;
//
//    public OAuth2CallbackController(OAuth2AuthorizedClientService authorizedClientService) {
//        this.authorizedClientService = authorizedClientService;
//    }
//
//    @GetMapping("/login/oauth2/code/nextjs")
//    public String handleGoogleCallback(@RequestParam("code") String authorizationCode) {
//        // OAuth2 Access Token is automatically exchanged by Spring Security.
//        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
//                authentication.getAuthorizedClientRegistrationId(), authentication.getName());
//
//        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
//        // You can now use the access token to make authenticated API calls.
//        // For example: Make a call to your backend API using the access token
//
//        return "redirect:/dashboard"; // Redirect to the secured page after successful login
//    }
//}

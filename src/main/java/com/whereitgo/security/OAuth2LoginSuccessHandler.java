// package com.whereitgo.security;

// import java.io.IOException;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
// import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;
// import org.springframework.beans.factory.annotation.Autowired;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;


// @Component
// public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

//     @Autowired
//     private OAuth2AuthorizedClientService clientService;

//     // @Autowired
//     // private TokenService tokenService;

//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request,
//                                         HttpServletResponse response,
//                                         Authentication authentication) throws IOException {

//         OAuth2AuthenticationToken token =
//                 (OAuth2AuthenticationToken) authentication;

//         OAuth2AuthorizedClient client =
//                 clientService.loadAuthorizedClient(
//                         token.getAuthorizedClientRegistrationId(),
//                         token.getName()
//                 );

//         String accessToken = client.getAccessToken().getTokenValue();
//         String refreshToken = client.getRefreshToken() != null
//                 ? client.getRefreshToken().getTokenValue()
//                 : null;
//         System.out.println("Access token " + accessToken);
//         // tokenService.saveToken(token.getName(), accessToken, refreshToken);

//         response.sendRedirect("/success");
//     }
// }
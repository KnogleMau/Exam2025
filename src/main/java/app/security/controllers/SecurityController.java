package app.security.controllers;

import app.config.HibernateConfig;
import app.exceptions.ApiException;
import app.exceptions.ValidationException;
import app.security.daos.ISecurityDAO;
import app.security.daos.SecurityDAO;
import app.security.entities.User;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.bugelhartmann.TokenSecurity;
import dk.bugelhartmann.TokenVerificationException;
import dk.bugelhartmann.UserDTO;

import io.javalin.http.*;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityController implements ISecurityController {
    ISecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());
    ObjectMapper objectMapper = new Utils().getObjectMapper();
    TokenSecurity tokenSecurity = new TokenSecurity();

    @Override
    public Handler login(){
        return (Context ctx) -> {
            User user = ctx.bodyAsClass(User.class);
            try {
                User verified = securityDAO.getVerifiedUser(user.getUsername(), user.getPassword());
                Set<String> stringRoles = verified.getRoles()
                        .stream()
                        .map(role->role.getRoleName())
                        .collect(Collectors.toSet());
                UserDTO userDTO = new UserDTO(verified.getUsername(), stringRoles);
                String token = createToken(userDTO);

                ObjectNode on = objectMapper
                        .createObjectNode()
                        .put("token",token)
                        .put("username", userDTO.getUsername());
                ctx.json(on).status(200);

            } catch(ValidationException ex){
                ObjectNode on = objectMapper.createObjectNode().put("msg","login failed. Wrong username or password");
                ctx.json(on).status(401);
            }
        };
    }

    @Override
    public Handler register() {
        return ctx -> {
            User user = ctx.bodyAsClass(User.class);
            String username = user.getUsername();
            String password = user.getPassword();

            try {

                if(username.contains("admin")){
                    securityDAO.createUser(username, password);
                    securityDAO.addUserRole(username,"Admin");
                }else {
                    securityDAO.createUser(username, password);
                    securityDAO.addUserRole(username, "User");
                }
                User verified = securityDAO.getVerifiedUser(username, password);

                Set<String> stringRoles = verified.getRoles()
                        .stream()
                        .map(role->role.getRoleName())
                        .collect(Collectors.toSet());
                UserDTO userDTO = new UserDTO(verified.getUsername(), stringRoles);
                String token = createToken(userDTO);

                ObjectNode on = objectMapper
                        .createObjectNode()
                        .put("token",token)
                        .put("username", userDTO.getUsername());
                ctx.json(on).status(200);

            } catch(ValidationException ex){
                ObjectNode on = objectMapper.createObjectNode().put("msg","login failed.");
                ctx.json(on).status(401);
            }
        };
    }

    @Override
    public Handler authenticate() {

        return (Context ctx) -> {
            // This is a preflight request => no need for authentication
            if (ctx.method().toString().equals("OPTIONS")) {
                ctx.status(200);
                return;
            }
            // If the endpoint is not protected with roles or is open to ANYONE role, then skip
            Set<String> allowedRoles = ctx.routeRoles().stream().map(role -> role.toString().toUpperCase()).collect(Collectors.toSet());
            if (isOpenEndpoint(allowedRoles))
                return;

            // If there is no token we do not allow entry
            UserDTO verifiedTokenUser = validateAndGetUserFromToken(ctx);
            ctx.attribute("user", verifiedTokenUser); // -> ctx.attribute("user") in ApplicationConfig beforeMatched filter
        };
    }

    private boolean isOpenEndpoint(Set<String> allowedRoles) {
        // If the endpoint is not protected with any roles:
        if (allowedRoles.isEmpty())
            return true;

        // 1. Get permitted roles and Check if the endpoint is open to all with the ANYONE role
        if (allowedRoles.contains("ANYONE")) {
            return true;
        }
        return false;
    }

    @Override
    public Handler authorize() {
        return (Context ctx) -> {
            Set<String> allowedRoles = ctx.routeRoles()
                    .stream()
                    .map(role -> role.toString().toUpperCase())
                    .collect(Collectors.toSet());

            // 1. Check if the endpoint is open to all (either by not having any roles or having the ANYONE role set
            if (isOpenEndpoint(allowedRoles))
                return;
            // 2. Get user and ensure it is not null
            UserDTO user = ctx.attribute("user");
            if (user == null) {
                throw new ForbiddenResponse("No user was added from the token");
            }
            // 3. See if any role matches
            if (!userHasAllowedRole(user, allowedRoles))
                throw new ForbiddenResponse("User was not authorized with roles: " + user.getRoles() + ". Needed roles are: " + allowedRoles);
        };
    }

    private static boolean userHasAllowedRole(UserDTO user, Set<String> allowedRoles) {
        return user.getRoles().stream()
                .anyMatch(role -> allowedRoles.contains(role.toUpperCase()));
    }

    private String createToken(UserDTO user) {
        try {
            String ISSUER;
            String TOKEN_EXPIRE_TIME;
            String SECRET_KEY;

            if (System.getenv("DEPLOYED") != null) {
                ISSUER = System.getenv("ISSUER");
                TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME");
                SECRET_KEY = System.getenv("SECRET_KEY");
            } else {
                ISSUER = Utils.getPropertyValue("ISSUER", "public/config.properties");
                TOKEN_EXPIRE_TIME = Utils.getPropertyValue("TOKEN_EXPIRE_TIME", "public/config.properties");
                SECRET_KEY = Utils.getPropertyValue("SECRET_KEY", "public/config.properties");
            }
            return tokenSecurity.createToken(user, ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY);
        } catch (Exception e) {
            throw new ApiException(500, "Could not create token");
        }
    }

    private static String getToken(Context ctx) {
        String header = ctx.header("Authorization");
        if (header == null) {
            throw new UnauthorizedResponse("Authorization header is missing"); // UnauthorizedResponse is javalin 6 specific but response is not json!
        }

        // If the Authorization Header was malformed, then no entry
        String token = header.split(" ")[1];
        if (token == null) {
            throw new UnauthorizedResponse("Authorization header is malformed"); // UnauthorizedResponse is javalin 6 specific but response is not json!
        }
        return token;
    }

    private UserDTO verifyToken(String token) {
        boolean IS_DEPLOYED = (System.getenv("DEPLOYED") != null);
        String SECRET = IS_DEPLOYED ? System.getenv("SECRET_KEY") : Utils.getPropertyValue("SECRET_KEY", "public/config.properties");

        try {
            if (tokenSecurity.tokenIsValid(token, SECRET) && tokenSecurity.tokenNotExpired(token)) {
                return tokenSecurity.getUserWithRolesFromToken(token);
            } else {
                throw new UnauthorizedResponse("Token not valid");
            }
        } catch (ParseException | TokenVerificationException e) {
            // logger.error("Could not create token", e);
            throw new ApiException(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized. Could not verify token");
        }
    }

    private UserDTO validateAndGetUserFromToken(Context ctx) {
        String token = getToken(ctx);
        UserDTO verifiedTokenUser = verifyToken(token);
        if (verifiedTokenUser == null) {
            throw new UnauthorizedResponse("Invalid user or token"); // UnauthorizedResponse is javalin 6 specific but response is not json!
        }
        return verifiedTokenUser;
    }

    public void healthCheck(@NotNull Context ctx) {
        ctx.status(200).json("{\"msg\": \"API is up and running\"}");
    }
}

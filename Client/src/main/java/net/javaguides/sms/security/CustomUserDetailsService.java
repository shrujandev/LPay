package net.javaguides.sms.security;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.javaguides.sms.entity.User;
import net.javaguides.sms.repository.UserRepository;
import org.springframework.web.client.RestTemplate;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    private final String upi_server = "http://localhost:8070";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JSONObject usrobj = new JSONObject();
        usrobj.put("username", username);

        RestTemplate myRest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(usrobj.toString(), headers);
        ResponseEntity<String> respEntity = myRest.postForEntity(upi_server + "/getusrcreds", request, String.class);
        if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200)){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                User user = objectMapper.readValue(respEntity.getBody(), User.class);
                if(user == null){
                    throw new UsernameNotFoundException("User not found");
                }
                return new CustomUserDetails(user);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


        }else{
            System.out.println(respEntity.getBody());
            throw new UsernameNotFoundException("No response from UPI Server");
        }

    }

}
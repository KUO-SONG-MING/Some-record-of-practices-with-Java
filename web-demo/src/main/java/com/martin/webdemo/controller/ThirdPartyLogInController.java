package com.martin.webdemo.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.martin.webdemo.dto.LineAccessTokenResponse;
import com.martin.webdemo.dto.LineProfileResponse;
import com.martin.webdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import com.auth0.jwt.JWT;
@Controller
public class ThirdPartyLogInController {

    @Autowired
    UserService userService;

    @GetMapping("/thirdparty/login")
    public String thirdPartyLogin(Model model)
    {
        //line login endpoint
        String leinLoginUrl = "https://access.line.me/oauth2/v2.1/authorize?";
        String responseType = "response_type=code";
        //自己的channelId
        String clientId = "client_id=channelId";
        //call back uri
        String redirectUri = "redirect_uri=http://localhost:8080/line/login";
        //防止跨站攻擊的驗證碼
        String state = "state=1234abcd";
        //希望之後的id_token JWT回傳哪些使用者資訊
        String scope = "scope=openid%20profile%20email";
        //組合line api url
        String Url = leinLoginUrl + responseType +"&"+ clientId +"&"+ redirectUri +"&"+ state +"&"+scope;
        model.addAttribute("lineLogin",Url);
        return "thirdPartyLogin";
    }

    @GetMapping("/line/login")
    public String lineLogin(Model model,
                            @RequestParam String code,
                            @RequestParam String state)
    {
        //檢查驗證碼是否正確
        if(!state.equals("1234abcd"))
            return "error";

        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/x-www-form-urlencoded");
        MultiValueMap<String, String> bodyPair = new LinkedMultiValueMap();
        //authorize api回傳的臨時驗證code
        bodyPair.add("code",code);
        //自己的channelId
        bodyPair.add("client_id","channelId");
        //自己的channel secret
        bodyPair.add("client_secret","channelSecret");
        bodyPair.add("grant_type","authorization_code");
        //要跟呼叫authorize api的call back uri一致
        bodyPair.add("redirect_uri","http://localhost:8080/line/login");

        //帶入header與body到請求實體
        HttpEntity requestEntity = new HttpEntity(bodyPair,header);
        //執行http request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LineAccessTokenResponse> accessTokenResponse = restTemplate.exchange(
            "https://api.line.me/oauth2/v2.1/token",
                HttpMethod.POST,
                requestEntity,
                LineAccessTokenResponse.class
        );

        LineAccessTokenResponse accessData = accessTokenResponse.getBody();
        //解析 json web token
        DecodedJWT jwt = JWT.decode(accessData.getIdToken());
        //在聲明map中用關鍵字找到使用者資訊
        String lineUserName = jwt.getClaims().get("name").toString().replace("\"","");
        String lineUserPicture = jwt.getClaims().get("picture").toString().replace("\"","");
        String lineUserEmail = jwt.getClaims().get("email").toString().replace("\"","");

        //用accessToken呼叫profile一樣可以得到使用者資訊帶沒有email，
        HttpHeaders accessHeader = new HttpHeaders();
        accessHeader.set("Authorization",accessData.getTokenType() + " " + accessData.getAccessToken());
        ResponseEntity<LineProfileResponse> profileResponse = restTemplate.exchange("https://api.line.me/v2/profile",
                                                                                     HttpMethod.GET,
                                                                                     new HttpEntity(accessHeader),
                                                                                     LineProfileResponse.class);
        LineProfileResponse lineProfileResponse = profileResponse.getBody();
        userService.lineLogin(lineUserEmail, lineProfileResponse.getUserId());

        //把照片與使用者名稱秀到前端
        model.addAttribute("LineProfileResponse",lineProfileResponse);
        return "lineLogin";
    }

}

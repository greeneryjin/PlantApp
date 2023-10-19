package com.example.demo.headerconfig;

import com.example.demo.security.token.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//토큰 응답메세지
public class ResponseResult extends HttpServlet {

    private ObjectMapper ob = new ObjectMapper();

    //액세스토큰 재발급
    public void reCreateToken(HttpServletResponse response, String tokens, JwtProperties jwtProperties) throws ServletException, IOException {

        response.addHeader(jwtProperties.HEADER_STRING, jwtProperties.TOKEN_PREFIX);

        //상태코드
        response.setStatus(HttpServletResponse.SC_OK);

        //application/json : json 형식
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //한글로 인코딩
        response.setCharacterEncoding("utf-8");

        //결과 반환.
        Result res = new Result("success", "토큰이 다시 발급되었습니다.", tokens);

        //응답 작성
        String result = ob.writeValueAsString(res);
        response.getWriter().write(result);
    }

    //재로그인 요청
    public void againLogin(HttpServletResponse response) throws IOException {
        //상태코드
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //application/json : json 형식
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //한글로 인코딩
        response.setCharacterEncoding("utf-8");

        //결과 반환.
        Result res = new Result("success", "번거롭게 해서 죄송합니다. 다시 로그인 해주세요");

        //응답 작성
        String result = ob.writeValueAsString(res);
        response.getWriter().write(result);
    }

    //로그아웃
    public void logoutResponse(HttpServletResponse response) throws IOException {

        //상태코드
        response.setStatus(HttpServletResponse.SC_OK);

        //application/json : json 형식
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //한글로 인코딩
        response.setCharacterEncoding("utf-8");

        //결과 반환.
        Result res = new Result("success", "로그아웃 되었습니다.");

        //응답 작성
        String result = ob.writeValueAsString(res);
        response.getWriter().write(result);
    }
}

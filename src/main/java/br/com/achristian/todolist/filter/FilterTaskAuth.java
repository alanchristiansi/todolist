package br.com.achristian.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.achristian.todolist.user.IUserRespository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRespository userRespository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

        var servletPath = request.getServletPath();
        //if (servletPath.equals("/tasks/")) {
        //Foi alterado para startWith pois começamos a utilizar o ID na URL apos a task, se usar o equals, não encontra o parâmetro do ID
        if (servletPath.startsWith("/tasks/")) {
            //Pega Usuario e senha
            var authorization = request.getHeader("Authorization");
            System.out.println("authorization");
            System.out.println(authorization);

            var authEncoded = authorization.substring("Basic".length()).trim();
            
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            
            var authString = new String(authDecode);

            //Confirmando no terminal os dados decodificados
            System.out.println("authEncoded");
            System.out.println(authEncoded);
            System.out.println("authDecode");
            System.out.println(authDecode);
            System.out.println("authString");
            System.out.println(authString);

            // Exemplo: ["achristian", "12345"]
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            //Confirma Usuario e Senha
            System.out.println("Usuário e Senha");
            System.out.println(username);
            System.out.println(password);

            //Valida Usuario
            var user = this.userRespository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuário sem autorização");
            } else {
                //Valida Senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    //Finaliza processo
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(403);
                }
                
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
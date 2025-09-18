package br.com.achristian.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name ="tb_users")
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    //Valida que o nome do usuário deve ser unico
    @Column(unique = true)
    private String username;
    private String name;
    private String password;

    //Cria um timestamp ao gerar a tabela de usuarios
    @CreationTimestamp
    private LocalDateTime createdAt;
}

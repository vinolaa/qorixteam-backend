package com.github.ovinola.qorixteam.config;

import com.github.ovinola.qorixteam.model.AppUser;
import com.github.ovinola.qorixteam.model.ChatRoom;
import com.github.ovinola.qorixteam.repository.AppUserRepository;
import com.github.ovinola.qorixteam.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DevDataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final ChatRoomRepository roomRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0 && roomRepository.count() == 0) {
            log.info("Inicializando dados de teste no banco...");
            AppUser testUser = AppUser.builder()
                    .username("vinicius_dev")
                    .email("vinicius@teste.com")
                    .build();
            userRepository.save(testUser);

            ChatRoom testRoom = ChatRoom.builder()
                    .name("Sala Geral (Dev)")
                    .isPrivate(false)
                    .build();
            roomRepository.save(testRoom);

            log.info("=====================================================");
            log.info("DADOS DE TESTE CRIADOS COM SUCESSO!");
            log.info("Copie estes UUIDs para usar no arquivo HTML de teste:");
            log.info("-> USER_ID: {}", testUser.getId());
            log.info("-> ROOM_ID: {}", testRoom.getId());
            log.info("=====================================================");
        }
    }

}

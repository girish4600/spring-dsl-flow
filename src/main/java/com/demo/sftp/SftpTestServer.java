package com.demo.sftp;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuthNoneFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("local")
@Slf4j
public class SftpTestServer {

    private SshServer sshServer;
    private static final int SFTP_PORT = 2222;
    private boolean running;

    @PostConstruct
    public void init() throws Exception {
        if (running) {
            throw new RuntimeException("Already started " + hashCode());
        } else {
            sshServer = SshServer.setUpDefaultServer();
            sshServer.setPort(SFTP_PORT);
            sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
//            sshServer.setPasswordAuthenticator((username, password, session) ->
//                            username.equals("demo")   && password.equals("password"));
            sshServer.setUserAuthFactories(Arrays.asList(UserAuthNoneFactory.INSTANCE));
            sshServer.setSubsystemFactories(List.of(new SftpSubsystemFactory.Builder().build()));
//            sshServer.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get("local-sftp")));
            log.info("Starting: {} (on port {})", hashCode(), SFTP_PORT);
            sshServer.start();
            log.info("Started: {} (on port {})", hashCode(), SFTP_PORT);
            log.info("sshServer.getPort() {}", sshServer.getPort());
        }
        running = true;
    }

    @PreDestroy
    public void stop() throws Exception {

        if (sshServer != null) {
            sshServer.stop();
        }
    }
}
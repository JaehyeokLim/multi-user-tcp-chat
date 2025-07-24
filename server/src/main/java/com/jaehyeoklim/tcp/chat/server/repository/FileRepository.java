package main.java.com.jaehyeoklim.tcp.chat.server.repository;

import main.java.com.jaehyeoklim.tcp.chat.server.domain.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.*;
import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.*;

/**
 * 사용자 정보를 파일로 저장하고 불러오는 역할을 하는 간단한 파일 기반 저장소.
 * <p>저장 포맷은 CSV 기반이며, 각 줄마다 하나의 사용자 데이터를 기록함.</p>
 */
public class FileRepository {

    private static final String DIRECTORY_PATH = "./temp";
    private static final String FILE_PATH = "./temp/users.dat";
    private static final String DELIMITER = ",";

    /**
     * 사용자 정보를 파일에 한 줄씩 추가 저장한다.
     * <p>BufferedWriter는 한 줄 단위 쓰기에 적합하고, append 모드(true)로 동작함.</p>
     *
     * @param user 저장할 사용자 정보
     */
    public synchronized void add(User user) {
        createDirectoryIfNotExists();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH, UTF_8, true))) {
            bufferedWriter.write(
                    user.getId() + DELIMITER +
                        user.getLoginId() + DELIMITER +
                        user.getPassword() + DELIMITER +
                        user.getName()
            );
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 사용자 데이터 파일이 저장될 디렉토리가 없으면 생성한다.
     * <p>{@code Files.createDirectories()}는 중간 경로까지 전부 생성해주며, 이미 존재해도 예외를 발생시키지 않는다.</p>
     */
    private synchronized void createDirectoryIfNotExists() {
        try {
            Files.createDirectories(Path.of(DIRECTORY_PATH));
            log("Ensured directory exists: " + DIRECTORY_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + DIRECTORY_PATH, e);
        }
    }

    public synchronized User findByLoginId(String loginId) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH, UTF_8))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] userData = line.split(DELIMITER);
                if (userData.length != 4) continue;

                if (userData[1].equals(loginId)) {
                    return new User(
                            UUID.fromString(userData[0]),
                            userData[1],
                            userData[2],
                            userData[3]
                    );
                }
            }

        } catch (FileNotFoundException e) {
            log("File not found: " + FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * 모든 사용자 데이터를 파일에서 읽어 반환한다.
     * <p>파일이 없을 경우 빈 리스트를 반환하며, 내부적으로 불변 리스트 {@code List.of()}를 사용함.</p>
     *
     * @return 사용자 리스트
     */
    public synchronized List<User> findAllUser() {
        List<User> users = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH, UTF_8))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] userData = line.split(DELIMITER);
                users.add(new User(
                        UUID.fromString(userData[0]),
                        userData[1],
                        userData[2],
                        userData[3]
                ));
            }

            return users;

        } catch (FileNotFoundException e) {
            log("FileNotFoundException: " + e.getMessage());
            return List.of(); // 불변 리스트 반환
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
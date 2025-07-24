package main.java.com.jaehyeoklim.tcp.chat.server.command;

import main.java.com.jaehyeoklim.tcp.chat.server.session.Session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 클라이언트로부터 들어온 명령어를 처리할 수 있게, 명령어와 메서드를 연결해주는 디스패처.
 *
 * <p>@Mapping 애노테이션이 붙은 메서드를 찾아 명령어 맵에 등록하고,
 * 이후 들어온 명령어 문자열에 따라 해당 메서드를 호출해준다.</p>
 */
public class CommandDispatcher {

    private final Map<String, CommandMethod> commandMap;

    /**
     * 컨트롤러들을 받아 각 메서드에 붙은 명령어(@Mapping)를 맵에 등록함.
     *
     * @param controllers 명령어 처리용 메서드를 가진 객체들
     */
    public CommandDispatcher(List<Object> controllers) {
        this.commandMap = new HashMap<>();
        initializeCommandMap(controllers);
    }

    // 각 컨트롤러에서 @Mapping 붙은 메서드를 찾아 commandMap에 등록
    private void initializeCommandMap(List<Object> controllers) {
        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    String command = method.getAnnotation(Mapping.class).value();

                    if (commandMap.containsKey(command)) {
                        throw new IllegalStateException("중복된 명령어 등록됨: " + command);
                    }

                    commandMap.put(command, new CommandMethod(controller, method));
                }
            }
        }
    }

    /**
     * 클라이언트가 보낸 메시지를 명령어로 해석하고, 등록된 메서드를 실행한다.
     *
     * @param rawMessage 사용자가 입력한 전체 메시지 (예: "/exit", "/message hello")
     * @param session    해당 클라이언트의 세션 정보
     */
    public void dispatch(String rawMessage, Session session) {
        if (!rawMessage.startsWith("/")) {
            System.err.println("Invalid command format: " + rawMessage);
            return;
        }

        String[] parts = rawMessage.split(" ", 2);
        String command = parts[0];
        String body = parts.length > 1 ? parts[1] : "";

        CommandMethod commandMethod = commandMap.get(command);
        if (commandMethod == null) {
            session.send("Unknown command: " + command);
            return;
        }

        commandMethod.invoke(body, session);
    }

    /**
     * 명령어에 매핑된 메서드와 그 컨트롤러 인스턴스를 보관하는 내부 자료구조.
     *
     * <p>record 타입으로 만들어 불변 객체로 관리되며,
     * invoke() 메서드를 통해 실제 명령 처리 메서드를 실행할 수 있다.</p>
     */
    private record CommandMethod(Object controller, Method method) {

        public void invoke(String message, Session session) {
            try {
                method.invoke(controller, message, session);
            } catch (IllegalAccessException | InvocationTargetException e) {
                session.send("An error occurred while processing the command: " + e.getMessage());
            }
        }
    }
}